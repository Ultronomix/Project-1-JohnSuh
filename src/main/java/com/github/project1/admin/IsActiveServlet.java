package com.github.project1.admin;

import java.io.IOException;
import java.time.LocalDateTime;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.project1.common.ErrorResponse;
import com.github.project1.common.exceptions.AuthenticationException;
import com.github.project1.common.exceptions.DataSourceException;
import com.github.project1.common.exceptions.InvalidRequestException;
import com.github.project1.users.UserResponse;

import static com.github.project1.common.SecurityUtils.isAdmin;

public class IsActiveServlet extends HttpServlet {

    private static Logger logger = LogManager.getLogger(IsActiveServlet.class);

    private final IsActiveService isActiveService;
    private final ObjectMapper jsonMapper;

    public IsActiveServlet(IsActiveService isActiveService, ObjectMapper jsonMapper) {
        this.isActiveService = isActiveService;
        this.jsonMapper = jsonMapper;
    }
    
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        resp.setContentType("application/json");
        logger.info("Attempting to alter a user at {}", LocalDateTime.now());

        HttpSession userSession = req.getSession(false);

        if (userSession == null) {
            logger.warn("User who is not logged in, attempted to access information at {}", LocalDateTime.now());
            
            resp.setStatus(401);
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(401, "Requester is not authenticated with the system, please log in.")));
            return;
        }

        
        UserResponse requester = (UserResponse) userSession.getAttribute("authUser");

        if (!isAdmin(requester)) {
            logger.warn("Requester with invalid permissions attempted to update a user at {}", LocalDateTime.now());

            resp.setStatus(403);
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(403, "Requester not permitted to communicate with this endpoint.")));
            return;
        }

        try {

            IsActiveRequest requestPayload = jsonMapper.readValue(req.getInputStream(), IsActiveRequest.class);
            isActiveService.updateIsActive(requestPayload);
            resp.setStatus(204);

        } catch (InvalidRequestException | JsonMappingException e) {
            resp.setStatus(400);// * bad request
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(400, e.getMessage())));
            logger.warn("Unable to persist updated user at {}, error message: {}", LocalDateTime.now(), e.getMessage());

        } catch (AuthenticationException e) {
            resp.setStatus(409); // * conflict; indicate that provided resource could not be saved
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(409, e.getMessage())));
            logger.warn("Unable to persist updated user at {}, error message: {}", LocalDateTime.now(), e.getMessage());

        } catch (DataSourceException e) {
            e.printStackTrace();
            resp.setStatus(500); // * internal error
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(500, e.getMessage())));
            logger.warn("Unable to persist updated user at {}, error message: {}", LocalDateTime.now(), e.getMessage());

        }

    }

}

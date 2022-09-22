package com.github.project1.financeMan;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.project1.common.ErrorResponse;
import com.github.project1.common.exceptions.AuthenticationException;
import com.github.project1.common.exceptions.DataSourceException;
import com.github.project1.common.exceptions.InvalidRequestException;
import com.github.project1.common.exceptions.ResourceNotFoundException;
import com.github.project1.reimburs.ReimbResponse;
import com.github.project1.users.UserResponse;

import static com.github.project1.common.SecurityUtils.isFinanceMan;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class StatusServlet extends HttpServlet {

    private static Logger logger = LogManager.getLogger(StatusServlet.class);

    private final StatusService statusService;
    private final ObjectMapper jsonMapper;

    public StatusServlet(StatusService statusService, ObjectMapper jsonMapper) {
        this.statusService = statusService;
        this.jsonMapper = jsonMapper;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        HttpSession reimbSession = req.getSession(false);

        if(reimbSession == null) {
            logger.warn("User who is not logged in, attempted to access information at {}", LocalDateTime.now());

            resp.setStatus(401);
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(401, "Requester not authenticated with server, log in")));
            return;
        }

        String idToSearchFor = req.getParameter("statusId");

        UserResponse requester = (UserResponse) reimbSession.getAttribute("authUser");

        if (!isFinanceMan(requester)) {
            logger.warn("Requester with invalid permissions attempted to view information at {}, {}", LocalDateTime.now(), requester.getUsername());

            resp.setStatus(403);
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(403, "Requester not permitted to communicate with this endpoint.")));
            return;
        }

        try {
            logger.info("Iterating through list of reimbursements by id at {}", LocalDateTime.now());
            
            if (idToSearchFor == null) {
                List<ReimbResponse> allReimbs = statusService.getAllReimb();
                resp.getWriter().write(jsonMapper.writeValueAsString(allReimbs));
            } else {
                ReimbResponse foundReimb = statusService.getReimbByStatus(idToSearchFor);
                resp.getWriter().write(jsonMapper.writeValueAsString(foundReimb));
            }
        } catch (InvalidRequestException | JsonMappingException e) {

            resp.setStatus(400); // BAD REQUEST;
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(400, e.getMessage())));
            logger.warn("Unable to locate reimbursement at {}, error message: {}", LocalDateTime.now(), e.getMessage());

        } catch (ResourceNotFoundException e) {

            resp.setStatus(404); // NOT FOUND; the sought resource could not be located
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(404, e.getMessage())));
            logger.warn("Unable to locate reimbursement at {}, error message: {}", LocalDateTime.now(), e.getMessage());

        } catch (DataSourceException e) {

            resp.setStatus(500); // INTERNAL SERVER ERROR; general error indicating a problem with the server
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(500, e.getMessage())));
           logger.warn("Unable to locate reimbursement at {}, error message: {}", LocalDateTime.now(), e.getMessage());

        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        resp.setContentType("application/json");

        HttpSession reimbSession = req.getSession(false);

        if(reimbSession == null) {
            resp.setStatus(401);
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(401, "Requester not authenticated with server, log in")));
            return;
        }

        UserResponse requester = (UserResponse) reimbSession.getAttribute("authUser");

        if (!isFinanceMan(requester)) {
            resp.setStatus(403);
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(403, "Requester not permitted to communicate with this endpoint.")));
            return;
        }

        try {

            UpdateStatusRequest requestPayload = jsonMapper.readValue(req.getInputStream(), UpdateStatusRequest.class);
            requestPayload.setResolverId(requester.getUserId());
            statusService.updateReimbAdmin(requestPayload);
            resp.setStatus(204);
            
        } catch (InvalidRequestException | JsonMappingException e) {
            resp.setStatus(400);// * bad request
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(400, e.getMessage())));
        } catch (AuthenticationException e) {
            resp.setStatus(409); // * conflict; indicate that provided resource could not be saved
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(409, e.getMessage())));
        } catch (DataSourceException e) {
            e.printStackTrace();
            resp.setStatus(500); // * internal error
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(500, e.getMessage())));
        }
        
    }
    
}

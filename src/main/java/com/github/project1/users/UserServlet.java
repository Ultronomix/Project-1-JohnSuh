package com.github.project1.users;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.project1.common.ErrorResponse;
import com.github.project1.common.ResourceCreationResponse;
import com.github.project1.common.exceptions.*;
import com.github.project1.common.SecurityUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

import static com.github.project1.common.SecurityUtils.isAdmin;
import static com.github.project1.common.SecurityUtils.requesterOwned;

public class UserServlet extends HttpServlet {

    private final UserService userService;

    // TODO inject a shared reference to a configured ObjectMapper
    public UserServlet(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper jsonMapper = new ObjectMapper();
        resp.setContentType("application/json");

        // Access the HTTP session on the request (if it exists; otherwise it will be null)
        HttpSession userSession = req.getSession(false);

        if (userSession == null) {
            
            resp.setStatus(401);
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(401, "Requester is not authenticated with the system, please log in.")));
            return;
        }

        String idToSearchFor = req.getParameter("user_id");

        UserResponse requester = (UserResponse) userSession.getAttribute("authUser");

        if (!isAdmin(requester) && !requesterOwned(requester, idToSearchFor)) { // Role who is applicable to view sensitive info
            // TODO encapsulate error response creation into its own utility method
            resp.setStatus(403); // BAD REQUEST;
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(403, "Requester is not permitted to communicate with this endpoint.")));
        }

        try {

            if (idToSearchFor == null) {
                List<UserResponse> allUsers = userService.getAllUsers();
                
                resp.getWriter().write(jsonMapper.writeValueAsString(allUsers));
            } else {
                UserResponse foundUser = userService.getUserById(idToSearchFor);
                resp.getWriter().write(jsonMapper.writeValueAsString(foundUser));
            }

        } catch (InvalidRequestException | JsonMappingException e) {

            resp.setStatus(400); // BAD REQUEST;
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(400, e.getMessage())));

        } catch (ResourceNotFoundException e) {

            resp.setStatus(404); // NOT FOUND; the sought resource could not be located
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(404, e.getMessage())));

        } catch (DataSourceException e) {

            resp.setStatus(500); // INTERNAL SERVER ERROR; general error indicating a problem with the server
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(500, e.getMessage())));

        }


    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper jsonMapper = new ObjectMapper();
        resp.setContentType("application/json");
        try {

            NewUserRequest requestBody = jsonMapper.readValue(req.getInputStream(), NewUserRequest.class);
            ResourceCreationResponse responseBody = userService.register(requestBody);
            resp.getWriter().write(jsonMapper.writeValueAsString(responseBody));

        } catch (InvalidRequestException | JsonMappingException e) {

            // TODO encapsulate error response creation into its own utility method
            resp.setStatus(400); // BAD REQUEST;
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(400, e.getMessage())));

        } catch (ResourcePersistenceException e) {

            resp.setStatus(409); // CONFLICT; indicates that the provided resource could not be saved without conflicting with other data
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(409, e.getMessage())));

        } catch (DataSourceException e) {

            resp.setStatus(500); // INTERNAL SERVER ERROR; general error indicating a problem with the server
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(500, e.getMessage())));

        }

    }
}

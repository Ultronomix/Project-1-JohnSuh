package com.github.project1.reimburs;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.project1.common.ErrorResponse;
import com.github.project1.common.ResourceCreationResponse;
import com.github.project1.common.exceptions.AuthenticationException;
import com.github.project1.common.exceptions.DataSourceException;
import com.github.project1.common.exceptions.InvalidRequestException;
import com.github.project1.common.exceptions.ResourceNotFoundException;
import com.github.project1.common.exceptions.ResourcePersistenceException;
import com.github.project1.users.UserResponse;

import static com.github.project1.common.SecurityUtils.isFinanceMan;
import static com.github.project1.common.SecurityUtils.requesterOwned;

public class ReimbServlet extends HttpServlet {

    private final ReimbService reimbService;
    private final ObjectMapper jsonMapper;

    public ReimbServlet(ReimbService reimbService, ObjectMapper jsonMapper) {
        this.reimbService = reimbService;
        this.jsonMapper = jsonMapper;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        HttpSession reimbSession = req.getSession(false);

        if(reimbSession == null) {
            resp.setStatus(401);
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(401, "Requester not authenticated with server, log in")));
            return;
        }

        String idToSearchFor = req.getParameter("author_id");

        UserResponse requester = (UserResponse) reimbSession.getAttribute("authUser");

        if (!isFinanceMan(requester) && !requesterOwned(requester, idToSearchFor)) {
            resp.setStatus(403);
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(403, "Requester not permitted to communicate with this endpoint.")));
            return;
        }

        try {
            if (idToSearchFor == null) {
                List<ReimbResponse> allReimbs = reimbService.getAllReimb();
                resp.getWriter().write(jsonMapper.writeValueAsString(allReimbs));
            } else {
                ReimbResponse foundReimb = reimbService.getReimbById(idToSearchFor);
                resp.getWriter().write(jsonMapper.writeValueAsString(foundReimb));
            }
        } catch (InvalidRequestException | JsonMappingException e) {

            resp.setStatus(400); // BAD REQUEST;
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(400, e.getMessage())));
            //logger.warn("Unable to locate at {}, error message: {}", LocalDateTime.now(), e.getMessage());

        } catch (ResourceNotFoundException e) {

            resp.setStatus(404); // NOT FOUND; the sought resource could not be located
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(404, e.getMessage())));
            //logger.warn("Unable to locate at {}, error message: {}", LocalDateTime.now(), e.getMessage());

        } catch (DataSourceException e) {

            resp.setStatus(500); // INTERNAL SERVER ERROR; general error indicating a problem with the server
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(500, e.getMessage())));
           // logger.warn("Unable to locate at {}, error message: {}", LocalDateTime.now(), e.getMessage());

        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        HttpSession reimbSession = req.getSession(false);

        if(reimbSession == null) {
            resp.setStatus(401);
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(401, "Requestor not authenticated with server, log in")));
            return;
        }

        UserResponse requester = (UserResponse) reimbSession.getAttribute("authUser");

        if (!requester.getUserId().equals("employee")) {
            resp.setStatus(403);
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(403, "Requester is not permitted to communicate with this endpoint.")));
            return;
        }

        try {
            NewReimbursementRequest requestBody = jsonMapper.readValue(req.getInputStream(), NewReimbursementRequest.class);
            requestBody.setAuthorId(requester.getUserId());
            ResourceCreationResponse responseBody = reimbService.create(requestBody);
            resp.getWriter().write(jsonMapper.writeValueAsString(responseBody));

        } catch (InvalidRequestException | JsonMappingException e) {

            resp.setStatus(400); // BAD REQUEST;
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(400, e.getMessage())));
            //logger.warn("Unable to persist at {}, error message: {}", LocalDateTime.now(), e.getMessage());

        } catch (ResourcePersistenceException e) {

            resp.setStatus(409); // CONFLICT; indicates that the provided resource could not be saved without conflicting with other data
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(409, e.getMessage())));
            //logger.warn("Unable to persist at {}, error message: {}", LocalDateTime.now(), e.getMessage());

        } catch (DataSourceException e) {

            resp.setStatus(500); // INTERNAL SERVER ERROR; general error indicating a problem with the server
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(500, e.getMessage())));
           // logger.warn("Unable to persist at {}, error message: {}", LocalDateTime.now(), e.getMessage());

        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        HttpSession reimbSession = req.getSession(false);

        if(reimbSession == null) {
            resp.setStatus(401);
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(401, "Requestor not authenticated with server, log in")));
            return;
        }

        UserResponse requester = (UserResponse) reimbSession.getAttribute("authUser");
        

        try {
            UpdateReimbRequest requestPayload = jsonMapper.readValue(req.getInputStream(), UpdateReimbRequest.class);

            if (!requester.getUserId().equals(requestPayload.getAuthorId()) && !status.getStatusId().equals("PENDING")) {
                resp.setStatus(403);
                resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(403, "Requester not permitted to communicate with this endpoint.")));
                return;
            }

            reimbService.updateReimb(requestPayload);
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

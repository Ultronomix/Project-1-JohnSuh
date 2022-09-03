package com.github.project1.users;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

public class UserServlet extends HttpServlet {

    private final UserDAO userDAO;

    public UserServlet(UserDAO userDAO) {
        this.userDAO = userDAO;
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper jsonMapper = new ObjectMapper();
        resp.setContentType("application/jason");
        List<User> allUsers = userDAO.getAllUsers();
        resp.getWriter().write(jsonMapper.writeValueAsString(allUsers));
    }
    

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().write("Post to /users works");
    }
}

package com.github.project1;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

import com.github.project1.users.UserDAO;
import com.github.project1.users.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.project1.auth.AuthService;
import com.github.project1.users.UserServlet;
import com.github.project1.auth.AuthServlet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class App {

    private static Logger logger = LogManager.getLogger(App.class);
    public static void main(String[] args) throws LifecycleException { 

        logger.info("Starting Application");

        String docBase = System.getProperty("java.io.tmpdir");
        Tomcat webServer = new Tomcat();

        webServer.setBaseDir(docBase);
        webServer.setPort(5000); 
        webServer.getConnector(); 

        UserDAO userDAO = new UserDAO();
        AuthService verifyService = new AuthService(userDAO);
        UserService userService = new UserService(userDAO);
        ObjectMapper jsonMapper = new ObjectMapper();
        UserServlet userServlet = new UserServlet(userService, jsonMapper);
        AuthServlet verifyServlet = new AuthServlet(verifyService, jsonMapper);

        // Web server context and servlet configurations
        final String rootContext = "/p1";
        webServer.addContext(rootContext, docBase);
        webServer.addServlet(rootContext, "UserServlet", userServlet).addMapping("/users");
        webServer.addServlet(rootContext, "AuthServlet", verifyServlet).addMapping("/auth");


        webServer.start();
        logger.info("Web application successfully started");
        webServer.getServer().await();

    }
}

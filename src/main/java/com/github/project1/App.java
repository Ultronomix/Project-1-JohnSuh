package com.github.project1;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

import com.github.project1.users.UserDAO;
import com.github.project1.users.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.project1.auth.AuthService;
import com.github.project1.users.UserServlet;
import com.github.project1.auth.AuthServlet;
import com.github.project1.reimburs.ReimbDAO;
import com.github.project1.reimburs.ReimbService;
import com.github.project1.reimburs.ReimbServlet;
import com.github.project1.status.StatusService;
import com.github.project1.status.StatusServlet;

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
        ReimbDAO reimbDAO = new ReimbDAO();

        AuthService verifyService = new AuthService(userDAO);
        UserService userService = new UserService(userDAO);
        ReimbService reimbService = new ReimbService(reimbDAO);
        StatusService statusService = new StatusService(reimbDAO);

        ObjectMapper jsonMapper = new ObjectMapper();
        
        UserServlet userServlet = new UserServlet(userService, jsonMapper);
        AuthServlet verifyServlet = new AuthServlet(verifyService, jsonMapper);
        ReimbServlet reimbServlet = new ReimbServlet(reimbService, jsonMapper);
        StatusServlet statusServlet = new StatusServlet(statusService, jsonMapper);

        // Web server context and servlet configurations
        final String rootContext = "/p1";
        webServer.addContext(rootContext, docBase);
        webServer.addServlet(rootContext, "UserServlet", userServlet).addMapping("/users");
        webServer.addServlet(rootContext, "AuthServlet", verifyServlet).addMapping("/auth");
        webServer.addServlet(rootContext, "ReimbServlet", reimbServlet).addMapping("/reimburse");
        webServer.addServlet(rootContext, "StatusServlet", statusServlet).addMapping("/status");

        webServer.start();
        logger.info("Web application successfully started");
        webServer.getServer().await();

    }
}

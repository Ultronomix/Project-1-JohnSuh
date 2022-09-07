package com.github.project1;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

import com.github.project1.DAO.UserDAO;
import com.github.project1.services.UserService;
import com.github.project1.services.VerifyService;
import com.github.project1.servlets.UserServlet;
import com.github.project1.servlets.VerifyServlet;




public class App {
    public static void main(String[] args) throws LifecycleException { 

        String docBase = System.getProperty("java.io.tmpdir");
        Tomcat webServer = new Tomcat();

        // Web server base configurations
        webServer.setBaseDir(docBase);
        webServer.setPort(5000); // defaults to 8080, but we can set it to whatever port we want (as long as its open)
        webServer.getConnector(); // formality, required in order for the server to receive requests

        // App component instantiation
        UserDAO userDAO = new UserDAO();
        VerifyService verifyService = new VerifyService(userDAO);
        UserService userService = new UserService(userDAO);
        UserServlet userServlet = new UserServlet(userService);
        VerifyServlet verifyServlet = new VerifyServlet(verifyService);

        // Web server context and servlet configurations
        final String rootContext = "/p1";
        webServer.addContext(rootContext, docBase);
        webServer.addServlet(rootContext, "UserServlet", userServlet).addMapping("/users");
        webServer.addServlet(rootContext, "VerifyServlet", verifyServlet).addMapping("/verify");

        // Starting and awaiting web requests
        webServer.start();
        webServer.getServer().await();

    

    }
}

package com.github.project1;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

import com.github.project1.users.UserDAO;
import com.github.project1.users.UserService;
import com.github.project1.auth.AuthService;
import com.github.project1.users.UserServlet;
import com.github.project1.auth.AuthServlet;




public class App {
    public static void main(String[] args) throws LifecycleException { 

        String docBase = System.getProperty("java.io.tmpdir");
        Tomcat webServer = new Tomcat();

        webServer.setBaseDir(docBase);
        webServer.setPort(5000); 
        webServer.getConnector(); 

        UserDAO userDAO = new UserDAO();
        AuthService verifyService = new AuthService(userDAO);
        UserService userService = new UserService(userDAO);
        UserServlet userServlet = new UserServlet(userService);
        AuthServlet verifyServlet = new AuthServlet(verifyService);

        // Web server context and servlet configurations
        final String rootContext = "/p1";
        webServer.addContext(rootContext, docBase);
        webServer.addServlet(rootContext, "UserServlet", userServlet).addMapping("/users");
        webServer.addServlet(rootContext, "AuthServlet", verifyServlet).addMapping("/auth");


        webServer.start();
        webServer.getServer().await();

    

    }
}

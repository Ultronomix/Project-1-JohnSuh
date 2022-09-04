package com.github.project1;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

import com.github.project1.auth.AuthServlet;
import com.github.project1.users.UserDAO;
import com.github.project1.users.UserServlet;

public class App {
    public static void main(String[] args) throws LifecycleException { 


        String docBase = System.getProperty("java.io.tmpdir");
        Tomcat webServer = new Tomcat();
        
        webServer.setBaseDir(docBase);
        webServer.setPort(5000);
        webServer.getConnector();

        //dependency injection            
        UserDAO userDAO = new UserDAO();
        UserServlet userServlet = new UserServlet(userDAO);
        AuthServlet authServlet = new AuthServlet(userDAO);


        final String rootContext = "/p0";
        webServer.addContext(rootContext, docBase);
        webServer.addServlet(rootContext, "UserServlet", userServlet).addMapping("/users");
        webServer.addServlet(rootContext, "AuthServlet", authServlet).addMapping("/auth");


        webServer.start();
        webServer.getServer().await();
        System.out.println("web application successfully started.");
    }
}

package com.github.project1.common.util;

import com.github.project1.users.UserDAO;

public class AppContext {

    private static boolean appRunning;


    public void startApp() {
        while(appRunning) {
            try {
                UserDAO userDAO = new UserDAO();
                userDAO.getAllUsers();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
}

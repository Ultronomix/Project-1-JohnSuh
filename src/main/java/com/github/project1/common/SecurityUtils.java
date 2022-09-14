package com.github.project1.common;

import com.github.project1.users.UserResponse;

public class SecurityUtils {

    public static boolean isAdmin(UserResponse subject) {
        return subject.getRole().equals("admin");

    }

    public static boolean isFinanceMan(UserResponse subject) {
        return subject.getRole().equals("finance_manager");
    }

    // Only to be used with GET user requests
    public static boolean requesterOwned(UserResponse subject, String resourceId) {
        return subject.getUserId().equals(resourceId);
    }
    
}

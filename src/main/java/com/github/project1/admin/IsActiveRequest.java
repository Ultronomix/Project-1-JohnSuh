package com.github.project1.admin;

public class IsActiveRequest {

    private String userId;
    private String isActive;

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "IsActiveRequest [isActive=" + isActive + ", userId=" + userId + "]";
    }

    

}
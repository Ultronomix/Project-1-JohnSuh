package com.github.project1.DTO;

import com.github.project1.POJO.User;

public class UserResponse {

    private String userId;
    private String username;
    private String email;
    private String password;
    private String givenName;
    private String surname;
    private String role;

    public UserResponse(User subject) {
        this.userId = subject.getUserId();
        this.username = subject.getUsername();
        this.email = subject.getEmail();
        this.password = subject.getPassword();
        this.givenName = subject.getGivenName();
        this.surname = subject.getSurname();
        this.role = subject.getRole().getRole();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "UserResponse [email=" + email + ", givenName=" + givenName + ", password=" + password + ", role=" + role
                + ", surname=" + surname + ", userId=" + userId + ", username=" + username + "]";
    }

    
    
}

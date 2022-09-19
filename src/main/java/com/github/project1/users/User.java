package com.github.project1.users;

// POJO = Plain Ol' Java Objects
public class User {

    private String userId;
    private String username;
    private String email;
    private String password;
    private String givenName;
    private String surname;
    private String isActive;
    private Role role;

    public User() {
        super();
    }

    public User(String userId, String username, String email, String password, String givenName, String surname, String isActive, Role role) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.password = password;
        this.givenName = givenName;
        this.surname = surname;
        this.isActive = isActive;
        this.role = role;
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

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    

    @Override
    public String toString() {
        return "User [email=" + email + ", givenName=" + givenName + ", isActive=" + isActive + ", password=" + password
                + ", role=" + role + ", surname=" + surname + ", userId=" + userId + ", username=" + username + "]";
    }

    

}

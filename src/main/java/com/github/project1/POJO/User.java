package com.github.project1.POJO;

import java.util.Objects;

public class User {

    private String userId;
    private String username;
    private String password;
    private String email;
    private String givenName;
    private String surname;
    private Role role;

    @Override
    public int hashCode() {
        return Objects.hash(userId, username, password, email, givenName, surname, role);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userId, user.userId) && Objects.equals(username, user.username) && Objects.equals(password, user.password) && (Objects.equals(givenName, user.givenName) && (Objects.equals(surname, user.surname)) && Objects.equals(role, user.role));
            
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User [email=" + email + ", givenName=" + givenName + ", password=" + password + ", role=" + role
                + ", surname=" + surname + ", userId=" + userId + ", username=" + username + "]";
    }

}

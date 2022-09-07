package com.github.project1.DTO;

import java.util.UUID;

import com.github.project1.POJO.User;
import com.github.project1.common.Request;

public class NewUserRequest implements Request<User> {

    private String username;
    private String email;
    private String password;
    private String givenName;
    private String surname;

    

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

    



    



    @Override
    public String toString() {
        return "NewUserRequest [email=" + email + ", givenName=" + givenName + ", password=" + password + ", surname="
                + surname + ", username=" + username + "]";
    }



    @Override
    public User extractEntity() {
        User extractedEntity = new User();
        extractedEntity.setUserId(UUID.randomUUID().toString());
        extractedEntity.setUsername(this.username);
        extractedEntity.setEmail(this.email);
        extractedEntity.setPassword(this.password);
        extractedEntity.setGivenName(this.givenName);
        extractedEntity.setSurname(this.surname);
        return extractedEntity;
    }
    
}

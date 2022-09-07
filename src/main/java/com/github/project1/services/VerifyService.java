package com.github.project1.services;

import com.github.project1.DAO.UserDAO;
import com.github.project1.DTO.Credentials;
import com.github.project1.DTO.UserResponse;
import com.github.project1.common.exceptions.AuthenticationException;
import com.github.project1.common.exceptions.InvalidRequestException;

public class VerifyService {

    private final UserDAO userDAO;

    public VerifyService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public UserResponse verify(Credentials credentials) {

        if (credentials == null) {
            throw new InvalidRequestException("The provided credentials object was found to be null.");
        }

        if (credentials.getUsername().length() < 4) {
            throw new InvalidRequestException("The provided username was not the correct length (must be at least 4 characters).");
        }

        if (credentials.getPassword().length() < 8) {
            throw new InvalidRequestException("The provided password was not the correct length (must be at least 8 characters).");
        }

        return userDAO.findUserByUsernameAndPassword(credentials.getUsername(), credentials.getPassword())
                      .map(UserResponse::new)
                      .orElseThrow(AuthenticationException::new);

    }
    
}

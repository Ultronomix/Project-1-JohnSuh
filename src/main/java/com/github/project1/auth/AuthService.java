package com.github.project1.auth;

import java.time.LocalDateTime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.project1.common.exceptions.AuthenticationException;
import com.github.project1.common.exceptions.InvalidRequestException;
import com.github.project1.users.UserDAO;
import com.github.project1.users.UserResponse;

public class AuthService {

    private static Logger logger = LogManager.getLogger(AuthService.class);

    private final UserDAO userDAO;


    public AuthService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public UserResponse authenticate(Credentials credentials) {

        if (credentials == null) {
            logger.warn("Credentials given are null at {}", LocalDateTime.now());
            throw new InvalidRequestException("The provided credentials object was found to be null.");
            
        }

        if (credentials.getUsername().length() < 4) {
            logger.warn("Incorrect username length at {}", LocalDateTime.now());
            throw new InvalidRequestException("The provided username was not the correct length (must be at least 4 characters).");
        }

        if (credentials.getPassword().length() < 8) {
            logger.warn("Incorrect password length at {}", LocalDateTime.now());
            throw new InvalidRequestException("The provided password was not the correct length (must be at least 8 characters).");
        }

        logger.info("Successfully logged in at {} for user: {}", LocalDateTime.now(), credentials.getUsername());
        return userDAO.findUserByUsernameAndPassword(credentials.getUsername(), credentials.getPassword())
                      .map(UserResponse::new)
                      .orElseThrow(AuthenticationException::new);

    }
}

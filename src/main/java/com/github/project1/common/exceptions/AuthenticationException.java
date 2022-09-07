package com.github.project1.common.exceptions;

public class AuthenticationException extends RuntimeException {
    public AuthenticationException() {
        super("Could not find a user account with the provided credentials!");
    }
}

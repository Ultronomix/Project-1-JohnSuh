package com.github.project1.auth;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import com.github.project1.users.UserDAO;
import com.github.project1.users.UserResponse;

public class AuthServiceTest {

    AuthService sut;

    @Before
    public void setup() {
        sut = new AuthService(new UserDAO());
    }

    @Test
    public void test_authenticateReturnsSuccessfully_givenValidAndKnownCredentials() {

        //Arrange
        Credentials credentialStub = new Credentials("valid", "credentials");

        //Act
        UserResponse actualResult = sut.authenticate(credentialStub);
        
        //Assertion
        assertNotNull(actualResult);
    }
    
}

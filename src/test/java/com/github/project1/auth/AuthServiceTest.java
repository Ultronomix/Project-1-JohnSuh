package com.github.project1.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.github.project1.common.exceptions.AuthenticationException;
import com.github.project1.common.exceptions.InvalidRequestException;
import com.github.project1.users.Role;
import com.github.project1.users.User;
import com.github.project1.users.UserDAO;
import com.github.project1.users.UserResponse;

public class AuthServiceTest {

    AuthService sut;
    UserDAO mockUserDAO;

    @BeforeEach
    public void setup() {
        mockUserDAO = Mockito.mock(UserDAO.class);
        sut = new AuthService(mockUserDAO);
    }

    @AfterEach
    public void cleanUp() {
        Mockito.reset(mockUserDAO); // helps to ensure that when/then on this mock are reset/invalidated
    }

    @Test
    public void test_authenticateReturnsSuccessfully_givenValidAndKnownCredentials() {

        //Arrange
        Credentials credentialStub = new Credentials("valid", "credentials");
        User userStub = new User("some-uuid", "valid", "valid123@revature.net", "credentials", "Val", "Id", "t", new Role("some-role-id", "employee"));
        when(mockUserDAO.findUserByUsernameAndPassword(anyString(), anyString())).thenReturn(Optional.of(userStub));
        UserResponse expectedResult = new UserResponse(userStub);
                
        //Act
        UserResponse actualResult = sut.authenticate(credentialStub);
        
        //Assertion
        assertNotNull(actualResult);
        assertEquals(expectedResult, actualResult);  // The objects compared need a .equals method in DAO
        verify(mockUserDAO, times(1)).findUserByUsernameAndPassword(anyString(), anyString());
    }

    @Test
    public void test_authenticate_throwsInvalidRequestException_givenTooShortPassword() {
        // Arrange
        Credentials credentialStub = new Credentials("invalid", "creds");

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.authenticate(credentialStub);
        });

        verify(mockUserDAO, times(0)).findUserByUsernameAndPassword(anyString(), anyString());

    }

    @Test
    public void test_authenticate_throwsInvalidRequestException_givenTooShortUsername() {
        // Arrange
        Credentials credentialStub = new Credentials("v", "credentials");

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.authenticate(credentialStub);
        });

        verify(mockUserDAO, times(0)).findUserByUsernameAndPassword(anyString(), anyString());

    }

    @Test
    public void test_authenticate_throwsInvalidRequestException_givenNullCredentials() {
        // Arrange
        Credentials credentialStub = null;

        // Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.authenticate(credentialStub);
        });

        verify(mockUserDAO, times(0)).findUserByUsernameAndPassword(anyString(), anyString());

    }

    @Test
    public void test_authenticate_throwsAuthenticationException_givenInvalidCredentials() {

        //Arrange
        Credentials credentialStub = new Credentials("unknown", "credentials");
        when(mockUserDAO.findUserByUsernameAndPassword(anyString(), anyString())).thenReturn(Optional.empty());
                
        //Act
        assertThrows(AuthenticationException.class, () -> {
            sut.authenticate(credentialStub);
        });
        
        //Assertion
        verify(mockUserDAO, times(1)).findUserByUsernameAndPassword(anyString(), anyString());
    }
    
}
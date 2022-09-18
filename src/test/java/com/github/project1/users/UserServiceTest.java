package com.github.project1.users;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.github.project1.auth.Credentials;
import com.github.project1.common.ResourceCreationResponse;
import com.github.project1.common.exceptions.InvalidRequestException;
import com.github.project1.common.exceptions.ResourceNotFoundException;
import com.github.project1.users.Role;
import com.github.project1.users.User;
import com.github.project1.users.UserDAO;
import com.github.project1.users.UserService;

public class UserServiceTest {

    UserService sut;
    UserDAO mockUserDAO;

    @BeforeEach
    public void setup() {
        mockUserDAO = Mockito.mock(UserDAO.class);
        sut = new UserService(mockUserDAO);
    }

    @AfterEach
    public void cleanUp() {
        Mockito.reset(mockUserDAO);
    }

    /* TODO:
     * updateUser
     * register
     */

    @Test
    public void test_getAllUsers_givenAListOfUsers() {

        //Arrange
        List<User> mockList = new ArrayList<>();
        User userStub = new User("some-uuid", "valid", "valid123@revature.net", "credentials", "Val", "Id", new Role("some-role-id", "employee"));
        User userStub2 = new User("some-OtherUuid", "valide", "valide123@revature.net", "credentials", "Valide", "Cred", new Role("some-other-role", "employee"));
        mockList.add(userStub);
        mockList.add(userStub2);
        when(mockUserDAO.getAllUsers()).thenReturn(mockList);
        List<UserResponse> expectedResult = mockUserDAO.getAllUsers().stream().map(UserResponse::new).collect(Collectors.toList());
       
        //Act
        List<UserResponse> actualResult = sut.getAllUsers();

        //Assertion
        assertNotNull(actualResult);
        assertEquals(expectedResult, actualResult);  // The objects compared need a .equals method in DAO
        verify(mockUserDAO, times(2)).getAllUsers();

    }

    @Test
    public void test_getUserByIdSuccessfully_givenValidId() {

        //Arrange
        User userStub = new User("some-uuid", "valid", "valid123@revature.net", "credentials", "Val", "Id", new Role("some-role-id", "employee"));
        when(mockUserDAO.findUserById(anyString())).thenReturn(Optional.of(userStub));
        UserResponse expectedResult = new UserResponse(userStub);

        //Act
        UserResponse actualResult = sut.getUserById("some-uuid");

        //Assertion
        assertNotNull(actualResult);
        assertEquals(expectedResult, actualResult);
        verify(mockUserDAO, times(1)).findUserById(anyString());
    }

    @Test
    public void test_getUserById_throwsInvalidRequestException_givenNullId() {

        //Arrange
        String idStub = null;
        
        //Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.getUserById(idStub);
        });

        verify(mockUserDAO, times(0)).findUserById(anyString());        

    }

    @Test
    public void test_getUserById_throwsInvalidRequestException_givenUknownUserId() {

        //Arrange
        String unknownUserId = "unknown-uuid";
        when(mockUserDAO.findUserById(anyString())).thenReturn(Optional.empty());

        //Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            sut.getUserById(unknownUserId);
        });

        verify(mockUserDAO, times(1)).findUserById(anyString());

    }

    @Test
    public void test_getUserById_throwsInvalidRequestException_givenInvalidUUIDString() {
        //Arrange
        User userStub = new User("some-uuid", "valid", "valid123@revature.net", "credentials", "Val", "Id", new Role("some-role-id", "employee"));
        String badUserId = "";
        //Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.getUserById(badUserId);
        });

        verify(mockUserDAO, times(0)).findUserByUsernameAndPassword(anyString(), anyString());
    }

    // @Test
    // public void test_updateUser_GivenValidUserToUpdate() {
        
    //     //Arrange
    //     User userStub = new User("some-uuid", "valid", "valid123@revature.net", "credentials", "Val", "Id", new Role("some-role-id", "employee"));
    //     UpdateUserRequest userStubUpdate = new UpdateUserRequest("some-uuid", "valid", "valid123@revature.net", "credentials", "Val", "Id");
    //     doReturn(Optional.of(userStub)).when(mockUserDAO).updateUser(any(User.class));
    //     UserResponse expectedResult = new UserResponse(userStub);

    //     //Act
    //     actualResult = sut.updateUser(userStubUpdate);

    //     //Assert
    //     assertNotNull(actualResult);
    //     assertEquals(expectedResult, actualResult);
    //     verify(mockUserDAO, times(1)).updateUser(any(User.class));


    // }
    
}

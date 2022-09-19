package com.github.project1.users;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
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

import com.github.project1.common.exceptions.InvalidRequestException;
import com.github.project1.common.exceptions.ResourceNotFoundException;
import com.github.project1.common.exceptions.ResourcePersistenceException;


public class UserServiceTest {

    UserService sut;
    UserDAO mockUserDAO;
    UpdateUserRequest mockUpdateUserRequest;
    NewUserRequest mockNewUserRequest;
    User mockUser;

    @BeforeEach
    public void setup() {
        mockUserDAO = Mockito.mock(UserDAO.class);
        mockUpdateUserRequest = Mockito.mock(UpdateUserRequest.class);
        mockNewUserRequest = Mockito.mock(NewUserRequest.class);
        mockUser = Mockito.mock(User.class);
        sut = new UserService(mockUserDAO);
    }

    @AfterEach
    public void cleanUp() {
        Mockito.reset(mockUserDAO);
    }

    @Test
    public void test_getAllUsers_givenAListOfUsers() {

        //Arrange
        List<User> mockList = new ArrayList<>();
        User userStub = new User("some-uuid", "valid", "valid123@revature.net", "credentials", "Val", "Id", "t", new Role("some-role-id", "employee"));
        User userStub2 = new User("some-OtherUuid", "valide", "valide123@revature.net", "credentials", "Valide", "Cred", "t", new Role("some-other-role", "employee"));
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
        User userStub = new User("some-uuid", "valid", "valid123@revature.net", "credentials", "Val", "Id", "t", new Role("some-role-id", "employee"));
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
        String badUserId = "";
        //Act & Assert
        assertThrows(InvalidRequestException.class, () -> {
            sut.getUserById(badUserId);
        });

        verify(mockUserDAO, times(0)).findUserById(anyString());
    }

    @Test
    public void test_updateUser_GivenValidUserToUpdate() {
        
        //Arrange
        User userStub = new User("some-uuid", "valid", "valid123@revature.net", "credentials", "Val", "Id", "t", new Role("some-role-id", "employee"));
        User userStubUpdated = new User("some-uuid", "valid", "valid123@revature.net", "credentials", "New", "Identity", "t", new Role("some-role-id", "employee"));
        when(mockUserDAO.findUserById(anyString())).thenReturn(Optional.of(userStub));

        doAnswer((i) -> {

			assertTrue("New".equals(i.getArgument(0)));
			return null;
		}).when(mockUpdateUserRequest).setGivenName(anyString());

        doAnswer((i) -> {
		
			assertTrue("Identity".equals(i.getArgument(0)));
			return null;
		}).when(mockUpdateUserRequest).setSurname(anyString());

        doAnswer((i) -> {
			
			assertTrue("some-uuid".equals(i.getArgument(0)));
			return null;
		}).when(mockUpdateUserRequest).setUserId(anyString());
 
        when(mockUpdateUserRequest.getGivenName()).thenReturn("New");
        when(mockUpdateUserRequest.getSurname()).thenReturn("Identity");
        when(mockUpdateUserRequest.getUserId()).thenReturn("some-uuid");

        UserResponse expectedResult = new UserResponse(userStubUpdated);

        //Act
        UserResponse actualResult = sut.updateUser(mockUpdateUserRequest);
       
        //Assert
        assertNotNull(actualResult);
        assertEquals(expectedResult, actualResult);
        verify(mockUserDAO, times(1)).updateUser(any(User.class));

    }

    @Test
    public void test_updateUser_GivenInvalidUserId() {

        String unknownUserId = "unknown-uuid";
        when(mockUserDAO.findUserById(anyString())).thenReturn(Optional.empty());

        //Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            sut.getUserById(unknownUserId);
        });

        verify(mockUserDAO, times(1)).findUserById(anyString());

    }

    // @Test
    // public void test_registerNewUser_givenValidInformation() {

    //     // Arrange
    //     String newUserIdStub = "some-uuid";
    //         // MockUserRequest
    //     doAnswer((i) -> {

	// 		assertTrue("Val".equals(i.getArgument(0)));
	// 		return null;
	// 	}).when(mockNewUserRequest).setGivenName(anyString());

    //     doAnswer((i) -> {

	// 		assertTrue("Id".equals(i.getArgument(0)));
	// 		return null;
	// 	}).when(mockNewUserRequest).setSurname(anyString());

    //     doAnswer((i) -> {

	// 		assertTrue("valid123@revature.net".equals(i.getArgument(0)));
	// 		return null;
	// 	}).when(mockNewUserRequest).setEmail(anyString());

    //     doAnswer((i) -> {

	// 		assertTrue("valid".equals(i.getArgument(0)));
	// 		return null;
	// 	}).when(mockNewUserRequest).setUsername(anyString());

    //     doAnswer((i) -> {

	// 		assertTrue("credentials".equals(i.getArgument(0)));
	// 		return null;
	// 	}).when(mockNewUserRequest).setPassword(anyString());

    //     when(mockNewUserRequest.getGivenName()).thenReturn("Val");
    //     when(mockNewUserRequest.getSurname()).thenReturn("Id");
    //     when(mockNewUserRequest.getEmail()).thenReturn("valid123@revature.net");
    //     when(mockNewUserRequest.getUsername()).thenReturn("valid");
    //     when(mockNewUserRequest.getPassword()).thenReturn("credentials");

    //         //MockUser
    //     doAnswer((i) -> {

	// 		assertTrue("Val".equals(i.getArgument(0)));
	// 		return null;
	// 	}).when(mockUser).setGivenName(anyString());

    //     doAnswer((i) -> {

	// 		assertTrue("Id".equals(i.getArgument(0)));
	// 		return null;
	// 	}).when(mockUser).setSurname(anyString());

    //     doAnswer((i) -> {

	// 		assertTrue("valid123@revature.net".equals(i.getArgument(0)));
	// 		return null;
	// 	}).when(mockUser).setEmail(anyString());

    //     doAnswer((i) -> {

	// 		assertTrue("valid".equals(i.getArgument(0)));
	// 		return null;
	// 	}).when(mockUser).setUsername(anyString());

    //     doAnswer((i) -> {

	// 		assertTrue("credentials".equals(i.getArgument(0)));
	// 		return null;
	// 	}).when(mockUser).setPassword(anyString());

    //     doAnswer((i) -> {

	// 		assertTrue("some-uuid".equals(i.getArgument(0)));
	// 		return null;
	// 	}).when(mockUser).setUserId(anyString());

    //     when(mockUser.getGivenName()).thenReturn("Val");
    //     when(mockUser.getSurname()).thenReturn("Id");
    //     when(mockUser.getEmail()).thenReturn("valid123@revature.net");
    //     when(mockUser.getUsername()).thenReturn("valid");
    //     when(mockUser.getPassword()).thenReturn("credentials");
    //     when(mockUser.getUserId()).thenReturn("some-uuid");

    //     when(mockNewUserRequest.extractEntity()).thenReturn(mockUser);

    //     when(mockUserDAO.save(any(User.class))).thenReturn(newUserIdStub);

    //     ResourceCreationResponse expectedResult = new ResourceCreationResponse("some-uuid");

    //     // Act

    //     ResourceCreationResponse actualResult = sut.register(mockNewUserRequest);

    //     //Assert
    //     assertNotNull(actualResult);
    //     assertEquals(expectedResult, actualResult);
    //     verify(mockUserDAO, times(1)).save(any(User.class));
    // }

    @Test
    public void test_registerNewUser_givenNull() {

        when(mockNewUserRequest.getGivenName()).thenReturn(null);
        when(mockNewUserRequest.getSurname()).thenReturn(null);
        when(mockNewUserRequest.getEmail()).thenReturn(null);
        when(mockNewUserRequest.getUsername()).thenReturn(null);
        when(mockNewUserRequest.getPassword()).thenReturn(null);

        assertThrows(InvalidRequestException.class, () -> {
            sut.register(mockNewUserRequest);
        });

        verify(mockUserDAO, times(0)).findUserByUsernameAndPassword(anyString(), anyString());

    }

    @Test
    public void test_registerNewUser_givenNullGivenName_surname() {

        doAnswer((i) -> {

			assertTrue("valid123@revature.net".equals(i.getArgument(0)));
			return null;
		}).when(mockNewUserRequest).setEmail(anyString());

        doAnswer((i) -> {

			assertTrue("valid".equals(i.getArgument(0)));
			return null;
		}).when(mockNewUserRequest).setUsername(anyString());

        doAnswer((i) -> {

			assertTrue("credentials".equals(i.getArgument(0)));
			return null;
		}).when(mockNewUserRequest).setPassword(anyString());

        when(mockNewUserRequest.getGivenName()).thenReturn(null);
        when(mockNewUserRequest.getSurname()).thenReturn(null);
        when(mockNewUserRequest.getEmail()).thenReturn("valid123@revature.net");
        when(mockNewUserRequest.getUsername()).thenReturn("valid");
        when(mockNewUserRequest.getPassword()).thenReturn("credentials");

        assertThrows(InvalidRequestException.class, () -> {
            sut.register(mockNewUserRequest);
        });

        verify(mockUserDAO, times(0)).findUserByUsernameAndPassword(anyString(), anyString());

    }
    
    @Test
    public void test_registerNewUser_givenNullEmail() {
        
        doAnswer((i) -> {

			assertTrue("Val".equals(i.getArgument(0)));
			return null;
		}).when(mockNewUserRequest).setGivenName(anyString());

        doAnswer((i) -> {

			assertTrue("Id".equals(i.getArgument(0)));
			return null;
		}).when(mockNewUserRequest).setSurname(anyString());

        doAnswer((i) -> {

			assertTrue("valid".equals(i.getArgument(0)));
			return null;
		}).when(mockNewUserRequest).setUsername(anyString());

        doAnswer((i) -> {

			assertTrue("credentials".equals(i.getArgument(0)));
			return null;
		}).when(mockNewUserRequest).setPassword(anyString());

        when(mockNewUserRequest.getGivenName()).thenReturn("Val");
        when(mockNewUserRequest.getSurname()).thenReturn("Id");
        when(mockNewUserRequest.getEmail()).thenReturn(null);
        when(mockNewUserRequest.getUsername()).thenReturn("valid");
        when(mockNewUserRequest.getPassword()).thenReturn("credentials");

        assertThrows(InvalidRequestException.class, () -> {
            sut.register(mockNewUserRequest);
        });

        verify(mockUserDAO, times(0)).findUserByUsernameAndPassword(anyString(), anyString());

    }

    @Test
    public void test_registerNewUser_givenNullUsername() {
        
        doAnswer((i) -> {

			assertTrue("Val".equals(i.getArgument(0)));
			return null;
		}).when(mockNewUserRequest).setGivenName(anyString());

        doAnswer((i) -> {

			assertTrue("Id".equals(i.getArgument(0)));
			return null;
		}).when(mockNewUserRequest).setSurname(anyString());

        doAnswer((i) -> {

			assertTrue("valid123@revature.net".equals(i.getArgument(0)));
			return null;
		}).when(mockNewUserRequest).setEmail(anyString());

        doAnswer((i) -> {

			assertTrue("credentials".equals(i.getArgument(0)));
			return null;
		}).when(mockNewUserRequest).setPassword(anyString());

        when(mockNewUserRequest.getGivenName()).thenReturn("Val");
        when(mockNewUserRequest.getSurname()).thenReturn("Id");
        when(mockNewUserRequest.getEmail()).thenReturn("valid123@revature.net");
        when(mockNewUserRequest.getUsername()).thenReturn(null);
        when(mockNewUserRequest.getPassword()).thenReturn("credentials");

        assertThrows(InvalidRequestException.class, () -> {
            sut.register(mockNewUserRequest);
        });

        verify(mockUserDAO, times(0)).findUserByUsernameAndPassword(anyString(), anyString());

    }

    @Test
    public void test_registerNewUser_givenNullPassword() {
        
        doAnswer((i) -> {

			assertTrue("Val".equals(i.getArgument(0)));
			return null;
		}).when(mockNewUserRequest).setGivenName(anyString());

        doAnswer((i) -> {

			assertTrue("Id".equals(i.getArgument(0)));
			return null;
		}).when(mockNewUserRequest).setSurname(anyString());

        doAnswer((i) -> {

			assertTrue("valid123@revature.net".equals(i.getArgument(0)));
			return null;
		}).when(mockNewUserRequest).setEmail(anyString());

        doAnswer((i) -> {

			assertTrue("valid".equals(i.getArgument(0)));
			return null;
		}).when(mockNewUserRequest).setUsername(anyString());

        when(mockNewUserRequest.getGivenName()).thenReturn("Val");
        when(mockNewUserRequest.getSurname()).thenReturn("Id");
        when(mockNewUserRequest.getEmail()).thenReturn("valid123@revature.net");
        when(mockNewUserRequest.getUsername()).thenReturn("valid");
        when(mockNewUserRequest.getPassword()).thenReturn(null);

        assertThrows(InvalidRequestException.class, () -> {
            sut.register(mockNewUserRequest);
        });

        verify(mockUserDAO, times(0)).findUserByUsernameAndPassword(anyString(), anyString());

    }

    @Test
    public void test_registerNewUser_EmailTaken() {

        doAnswer((i) -> {

			assertTrue("Val".equals(i.getArgument(0)));
			return null;
		}).when(mockNewUserRequest).setGivenName(anyString());

        doAnswer((i) -> {

			assertTrue("Id".equals(i.getArgument(0)));
			return null;
		}).when(mockNewUserRequest).setSurname(anyString());

        doAnswer((i) -> {

			assertTrue("valid123@revature.net".equals(i.getArgument(0)));
			return null;
		}).when(mockNewUserRequest).setEmail(anyString());

        doAnswer((i) -> {

			assertTrue("valid".equals(i.getArgument(0)));
			return null;
		}).when(mockNewUserRequest).setUsername(anyString());

        doAnswer((i) -> {

			assertTrue("credentials".equals(i.getArgument(0)));
			return null;
		}).when(mockNewUserRequest).setPassword(anyString());

        when(mockNewUserRequest.getGivenName()).thenReturn("Val");
        when(mockNewUserRequest.getSurname()).thenReturn("Id");
        when(mockNewUserRequest.getEmail()).thenReturn("valid123@revature.net");
        when(mockNewUserRequest.getUsername()).thenReturn("valid");
        when(mockNewUserRequest.getPassword()).thenReturn("credentials");

        when(mockUserDAO.isEmailTaken(anyString())).thenReturn(true);

        assertThrows(ResourcePersistenceException.class, () -> {
            sut.register(mockNewUserRequest);
        });

    }

    @Test
    public void test_registerNewUser_UsernameTaken() {

        doAnswer((i) -> {

			assertTrue("Val".equals(i.getArgument(0)));
			return null;
		}).when(mockNewUserRequest).setGivenName(anyString());

        doAnswer((i) -> {

			assertTrue("Id".equals(i.getArgument(0)));
			return null;
		}).when(mockNewUserRequest).setSurname(anyString());

        doAnswer((i) -> {

			assertTrue("valid123@revature.net".equals(i.getArgument(0)));
			return null;
		}).when(mockNewUserRequest).setEmail(anyString());

        doAnswer((i) -> {

			assertTrue("valid".equals(i.getArgument(0)));
			return null;
		}).when(mockNewUserRequest).setUsername(anyString());

        doAnswer((i) -> {

			assertTrue("credentials".equals(i.getArgument(0)));
			return null;
		}).when(mockNewUserRequest).setPassword(anyString());

        when(mockNewUserRequest.getGivenName()).thenReturn("Val");
        when(mockNewUserRequest.getSurname()).thenReturn("Id");
        when(mockNewUserRequest.getEmail()).thenReturn("valid123@revature.net");
        when(mockNewUserRequest.getUsername()).thenReturn("valid");
        when(mockNewUserRequest.getPassword()).thenReturn("credentials");

        when(mockUserDAO.isUsernameTaken(anyString())).thenReturn(true);

        assertThrows(ResourcePersistenceException.class, () -> {
            sut.register(mockNewUserRequest);
        });

    }
    
}

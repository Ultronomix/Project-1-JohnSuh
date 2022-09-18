package com.github.project1.users;

import com.github.project1.common.ResourceCreationResponse;
import com.github.project1.common.exceptions.InvalidRequestException;
import com.github.project1.common.exceptions.ResourceNotFoundException;
import com.github.project1.common.exceptions.ResourcePersistenceException;

import java.util.List;
import java.util.stream.Collectors;

public class UserService {

    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public List<UserResponse> getAllUsers() {

        return userDAO.getAllUsers()
                      .stream()
                      .map(UserResponse::new)
                      .collect(Collectors.toList());

    }

    public UserResponse getUserById(String userId) {

        if (userId == null || userId.length() <= 0) {
            throw new InvalidRequestException("A non-empty id must be provided!");
        }

        try {

            return userDAO.findUserById(userId)
                          .map(UserResponse::new)
                          .orElseThrow(ResourceNotFoundException::new);

        } catch (IllegalArgumentException e) {
            throw new InvalidRequestException("An invalid UUID string was provided.");
        }

    }

    public void updateUser(UpdateUserRequest updateUserRequest) {

        System.out.println(updateUserRequest);
 
        User userToUpdate = userDAO.findUserById(updateUserRequest.getUserId())
                                    .orElseThrow(ResourceNotFoundException::new);

        
        if (updateUserRequest.getGivenName() != null) {
            userToUpdate.setGivenName(updateUserRequest.getGivenName());
        }

        if (updateUserRequest.getSurname() != null) {
            userToUpdate.setSurname(updateUserRequest.getSurname());
        }

        if (updateUserRequest.getUsername() != null) {
            // you also need to make sure that the new username is not already taken
            userToUpdate.setUsername(updateUserRequest.getUsername());
        }

        if (updateUserRequest.getEmail() != null) {
            // you also need to make sure that the new email is not already taken
            userToUpdate.setEmail(updateUserRequest.getEmail());
        }

        if (updateUserRequest.getPassword() != null) {
            userToUpdate.setPassword(updateUserRequest.getPassword());
        }

        userDAO.updateUser(userToUpdate);
       
    }

    public ResourceCreationResponse register(NewUserRequest newUser) {

        if (newUser == null) {
            throw new InvalidRequestException("Provided request payload was null.");
        }

        if (newUser.getGivenName() == null || newUser.getGivenName().length() <= 0 ||
            newUser.getSurname() == null || newUser.getSurname().length() <= 0)
        {
            throw new InvalidRequestException("A non-empty given name and surname must be provided");
        }

        if (newUser.getEmail() == null || newUser.getEmail().length() <= 0) {
            throw new InvalidRequestException("A non-empty email must be provided.");
        }

        if (newUser.getUsername() == null || newUser.getUsername().length() < 4) {
            throw new InvalidRequestException("A username with at least 4 characters must be provided.");
        }

        if (newUser.getPassword() == null || newUser.getPassword().length() < 8) {
            throw new InvalidRequestException("A password with at least 8 characters must be provided.");
        }

        if (userDAO.isEmailTaken(newUser.getEmail())) {
            throw new ResourcePersistenceException("Resource not persisted! The provided email is already taken.");
        }

        if (userDAO.isUsernameTaken(newUser.getUsername())) {
            throw new ResourcePersistenceException("Resource not persisted! The provided username is already taken.");
        }

        User userToPersist = newUser.extractEntity();
        String newUserId = userDAO.save(userToPersist);
        return new ResourceCreationResponse(newUserId);

    }
}

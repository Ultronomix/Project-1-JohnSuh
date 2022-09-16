package com.github.project1.isActive;

import java.util.List;
import java.util.stream.Collectors;

import com.github.project1.common.exceptions.InvalidRequestException;
import com.github.project1.common.exceptions.ResourceNotFoundException;
import com.github.project1.users.UpdateUserRequest;
import com.github.project1.users.User;
import com.github.project1.users.UserDAO;
import com.github.project1.users.UserResponse;

public class IsActiveService {

    private final UserDAO userDAO;

    public IsActiveService(UserDAO userDAO) {
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

    public void updateIsActive(IsActiveRequest updateIsActive) {

        System.out.println(updateIsActive);

        User userToUpdate = userDAO.findUserById(updateIsActive.getUserId())
                                    .orElseThrow(ResourceNotFoundException::new);

        if (updateIsActive.getIsActive() != null) {
            userToUpdate.setIsActive(updateIsActive.getIsActive());
        }

        userDAO.updateUser(userToUpdate);

    }


}

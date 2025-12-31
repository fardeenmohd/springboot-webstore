package com.users.service;

import com.users.payload.UserDTO;
import com.users.payload.UserResponse;

public interface UserService {
    UserDTO createUser(UserDTO userDTO);

    UserResponse getAllUsers(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    UserDTO getUserById(Long userId);

    UserDTO deleteUser(Long userId);

    UserDTO updateUser(UserDTO userDTO);

}
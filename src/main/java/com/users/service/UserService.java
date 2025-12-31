package com.users.service;

import com.users.payload.UserDTO;

import java.util.List;

public interface UserService {
    UserDTO createUser(UserDTO userDTO);

    List<UserDTO> getAllUsers(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    UserDTO getUserById(Long userId);

    UserDTO deleteUser(Long userId);

    UserDTO updateUser(UserDTO userDTO);

}
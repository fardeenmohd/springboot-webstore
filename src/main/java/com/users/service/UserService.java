package com.users.service;

import com.users.model.User;

import java.util.List;

public interface UserService {
    User createUser(User user);

    List<User> getAllUsers();

    User getUserById(Long userId);

    String deleteUser(Long userId);
}
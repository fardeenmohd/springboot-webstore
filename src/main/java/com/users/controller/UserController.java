package com.users.controller;

import com.users.config.AppConstants;
import com.users.payload.UserDTO;
import com.users.payload.UserResponse;
import com.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/admin/user/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
        UserDTO fetchedUserDTO = userService.getUserById(id);

        return ResponseEntity.ok(fetchedUserDTO);
    }

    @DeleteMapping("/admin/user/{id}")
    public ResponseEntity<UserDTO> deleteUser(@PathVariable Long id) {
        UserDTO deletedUserDTO = userService.deleteUser(id);
        return ResponseEntity.ok(deletedUserDTO);
    }

    @GetMapping("/admin/user")
    public ResponseEntity<UserResponse> getAllUsers(
            @RequestParam(name = "pageNumber", required = false, defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(name = "pageSize", required = false, defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
            @RequestParam(name = "sortBy", required = false, defaultValue = AppConstants.SORT_BY) String sortBy,
            @RequestParam(name = "sortOrder", required = false, defaultValue = AppConstants.SORT_ORDER) String sortOrder) {

        UserResponse usersDTO = userService.getAllUsers(pageNumber, pageSize, sortBy, sortOrder);

        return ResponseEntity.ok(usersDTO);
    }

    @PostMapping("/admin/user")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        UserDTO createdUserDTO = userService.createUser(userDTO);
        return new ResponseEntity<>(createdUserDTO, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/user")
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO userDTO) {
        UserDTO updatedUserDTO = userService.updateUser(userDTO);
        return ResponseEntity.ok(updatedUserDTO);
    }
}
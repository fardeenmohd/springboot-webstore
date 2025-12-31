package com.users.controller;

import com.users.config.AppConstants;
import com.users.payload.UserDTO;
import com.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

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
    public ResponseEntity<List<UserDTO>> getAllUsers(
            @RequestParam(name = "pageNumber", required = false, defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(name = "pageSize", required = false, defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
            @RequestParam(name = "sortBy", required = false, defaultValue = AppConstants.SORT_BY) String sortBy,
            @RequestParam(name = "sortOrder", required = false, defaultValue = AppConstants.SORT_ORDER) String sortOrder) {

        List<UserDTO> usersDTO = userService.getAllUsers(pageNumber, pageSize, sortBy, sortOrder);

        return ResponseEntity.ok(usersDTO);
    }

    @PostMapping("/admin/user")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        UserDTO createdUserDTO = userService.createUser(userDTO);
        return new ResponseEntity<>(createdUserDTO, HttpStatus.CREATED);
    }

    @PutMapping("/admin/user")
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO userDTO) {
        UserDTO updatedUserDTO = userService.updateUser(userDTO);
        return ResponseEntity.ok(updatedUserDTO);
    }
}
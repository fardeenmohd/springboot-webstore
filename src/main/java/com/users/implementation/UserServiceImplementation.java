package com.users.implementation;

import com.users.exceptions.ApiException;
import com.users.model.User;
import com.users.payload.UserDTO;
import com.users.payload.UserResponse;
import com.users.repositories.UserRepository;
import com.users.service.UserService;
import com.webstore.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImplementation implements UserService {

    private final UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);
        if (userRepository.findByEmail(userDTO.getEmail()) != null) {
            throw new ApiException("User already exists");
        }
        userRepository.save(user);

        return userDTO;
    }

    @Override
    public UserResponse getAllUsers(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<User> userPages = userRepository.findAll(pageDetails);
        List<User> users = userPages.getContent();
        List<UserDTO> userDTOS = users.stream().map((user) -> modelMapper.map(user, UserDTO.class)).toList();

        UserResponse userResponse = new UserResponse();
        userResponse.setContent(userDTOS);

        return userResponse;
    }

    @Override
    public UserDTO getUserById(Long userId) {
        User fetchedUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));
        UserDTO fetchedUserDTO = modelMapper.map(fetchedUser, UserDTO.class);

        return fetchedUserDTO;
    }

    @Override
    public UserDTO deleteUser(Long userId) {
        User deletedUser = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));
        userRepository.deleteById(userId);

        UserDTO deletedUserDTO = modelMapper.map(deletedUser, UserDTO.class);

        return deletedUserDTO;
    }

    @Override
    public UserDTO updateUser(UserDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);
        if (userRepository.findByEmail(userDTO.getEmail()) != null) {
            throw new ApiException("User already exists");
        }
        userRepository.findById(user.getId()).orElseThrow(() -> new ResourceNotFoundException("User", "userId", user.getId()));
        userRepository.save(user);

        return userDTO;
    }
}
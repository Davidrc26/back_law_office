package com.example.back_law_office.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import com.example.back_law_office.dtos.CreateUserDTO;
import com.example.back_law_office.dtos.UserDTO;
import com.example.back_law_office.models.User;
import com.example.back_law_office.repositories.UserRepository;
import com.example.back_law_office.models.Role;
import com.example.back_law_office.repositories.RolesRepository;
import java.util.Set;
import java.util.HashSet;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RolesRepository rolesRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Método para crear un nuevo usuario
    public UserDTO createUser(CreateUserDTO createUserDTO) {
        User user = new User();
        user.setUsername(createUserDTO.getUsername());
        user.setPassword(passwordEncoder.encode(createUserDTO.getPassword()));
        user.setEmail(createUserDTO.getEmail());
        user.setPhone(createUserDTO.getPhone());
        if (createUserDTO.getRoleIds() != null && !createUserDTO.getRoleIds().isEmpty()) {
            Set<Role> roles = new HashSet<>(rolesRepository.findAllById(createUserDTO.getRoleIds()));
            user.setRoles(roles);
        } else {
            user.setRoles(new HashSet<>());
        }
        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserDTO.class);
    }

    public User createUserFromGoogle(String username, String email, Set<Role> roles) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode("123456")); // Contraseña dummy, no se usará
        user.setRoles(roles);
        return userRepository.save(user);
    }

    // Método para obtener todos los usuarios
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }

    // Método para obtener un usuario por ID
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return modelMapper.map(user, UserDTO.class);
    }

    // Método para actualizar un usuario existente
    public UserDTO updateUser(Long id, CreateUserDTO userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        user.setUsername(userDetails.getUsername());
        if (userDetails.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        }
        user.setEmail(userDetails.getEmail());
        user.setPhone(userDetails.getPhone());
        if (userDetails.getRoleIds() != null && !userDetails.getRoleIds().isEmpty()) {
            Set<Role> roles = new HashSet<>(rolesRepository.findAllById(userDetails.getRoleIds()));
            user.setRoles(roles);
        } else {
            user.setRoles(new HashSet<>());
        }
        User updatedUser = userRepository.save(user);
        return modelMapper.map(updatedUser, UserDTO.class);

    }
    // Método para eliminar un usuario por ID

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        userRepository.deleteById(id);
    }

    
}

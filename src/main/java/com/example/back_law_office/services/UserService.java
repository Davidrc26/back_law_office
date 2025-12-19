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

    /**
     * Crea un nuevo usuario.
     * @param createUserDTO DTO con los datos del usuario a crear.
     * @return El usuario creado.
     */
    public UserDTO createUser(CreateUserDTO createUserDTO) {
        User user = new User();
        user.setPassword(passwordEncoder.encode(createUserDTO.getPassword()));
        user.setEmail(createUserDTO.getEmail());
        user.setFirstName(createUserDTO.getFirstName());
        user.setLastName(createUserDTO.getLastName());
        user.setSecondName(createUserDTO.getSecondName());
        user.setSecondLastName(createUserDTO.getSecondLastName());
        user.setDocumentNumber(createUserDTO.getDocumentNumber());
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

    /**
     * Obtiene todos los usuarios.
     * @return Una lista de usuarios.
     */
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Obtiene un usuario por su ID.
     * @param id ID del usuario a buscar.
     * @return El usuario encontrado.
     * @throws ResponseStatusException si el usuario no se encuentra.
     */
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return modelMapper.map(user, UserDTO.class);
    }

    /**
     * Actualiza un usuario existente.
     * @param id ID del usuario a actualizar.
     * @param userDetails DTO con los nuevos datos del usuario.
     * @return El usuario actualizado.
     */
    public UserDTO updateUser(Long id, CreateUserDTO userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        if (userDetails.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        }
        user.setEmail(userDetails.getEmail());
        user.setFirstName(userDetails.getFirstName());
        user.setLastName(userDetails.getLastName());
        user.setSecondName(userDetails.getSecondName());
        user.setSecondLastName(userDetails.getSecondLastName());
        user.setDocumentNumber(userDetails.getDocumentNumber());
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
    
    /**
     * Elimina un usuario por su ID.
     * @param id ID del usuario a eliminar.
     * @throws ResponseStatusException si el usuario no se encuentra.
     */
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        userRepository.deleteById(id);
    }

    
}

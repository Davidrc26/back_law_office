package com.example.back_law_office.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import com.example.back_law_office.dtos.*;
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

    /**
     * Obtiene todos los usuarios.
     * @return Una lista de usuarios.
     */
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::convertToUserDTO)
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
        return convertToUserDTO(user);
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

    /**
     * Convierte un User a UserDTO incluyendo los perfiles específicos
     */
    private UserDTO convertToUserDTO(User user) {
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        
        // Mapear perfiles específicos si existen
        if (user.getStudentProfile() != null) {
            userDTO.setStudentProfile(modelMapper.map(user.getStudentProfile(), StudentProfileDTO.class));
        }
        if (user.getProfessorProfile() != null) {
            userDTO.setProfessorProfile(modelMapper.map(user.getProfessorProfile(), ProfessorProfileDTO.class));
        }
        if (user.getAdministratorProfile() != null) {
            userDTO.setAdministratorProfile(modelMapper.map(user.getAdministratorProfile(), AdministratorProfileDTO.class));
        }
        if (user.getAssistantProfile() != null) {
            userDTO.setAssistantProfile(modelMapper.map(user.getAssistantProfile(), AssistantProfileDTO.class));
        }
        
        return userDTO;
    }
    
}

package com.example.back_law_office.services;

import com.example.back_law_office.dtos.*;
import com.example.back_law_office.models.*;
import com.example.back_law_office.repositories.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.Set;

@Service
public class StudentService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentProfileRepository studentProfileRepository;

    @Autowired
    private RolesRepository rolesRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public UserDTO createStudent(CreateStudentDTO dto) {
        // Crear usuario base
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());

        // Asignar roles
        if (dto.getRoleIds() != null && !dto.getRoleIds().isEmpty()) {
            Set<Role> roles = new HashSet<>(rolesRepository.findAllById(dto.getRoleIds()));
            user.setRoles(roles);
        }

        // Guardar usuario primero
        User savedUser = userRepository.save(user);

        // Crear perfil de estudiante
        StudentProfile profile = new StudentProfile();
        profile.setUser(savedUser);
        profile.setStudentCode(dto.getStudentCode());
        profile.setSemester(dto.getSemester());
        profile.setMajor(dto.getMajor());
        profile.setEnrollmentDate(dto.getEnrollmentDate());
        profile.setUniversity(dto.getUniversity());
        profile.setAcademicStatus(dto.getAcademicStatus());

        studentProfileRepository.save(profile);
        savedUser.setStudentProfile(profile);

        // Convertir a DTO
        return convertToUserDTO(savedUser);
    }

    @Transactional
    public UserDTO updateStudent(Long userId, CreateStudentDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        // Actualizar datos de usuario
        user.setUsername(dto.getUsername());
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());

        // Actualizar roles
        if (dto.getRoleIds() != null && !dto.getRoleIds().isEmpty()) {
            Set<Role> roles = new HashSet<>(rolesRepository.findAllById(dto.getRoleIds()));
            user.setRoles(roles);
        }

        // Actualizar o crear perfil
        StudentProfile profile = user.getStudentProfile();
        if (profile == null) {
            profile = new StudentProfile();
            profile.setUser(user);
        }
        profile.setStudentCode(dto.getStudentCode());
        profile.setSemester(dto.getSemester());
        profile.setMajor(dto.getMajor());
        profile.setEnrollmentDate(dto.getEnrollmentDate());
        profile.setUniversity(dto.getUniversity());
        profile.setAcademicStatus(dto.getAcademicStatus());

        studentProfileRepository.save(profile);
        user.setStudentProfile(profile);
        User updatedUser = userRepository.save(user);

        return convertToUserDTO(updatedUser);
    }

    public UserDTO getStudentById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        
        if (user.getStudentProfile() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student profile not found for this user");
        }
        
        return convertToUserDTO(user);
    }

    private UserDTO convertToUserDTO(User user) {
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        if (user.getStudentProfile() != null) {
            userDTO.setStudentProfile(modelMapper.map(user.getStudentProfile(), StudentProfileDTO.class));
        }
        return userDTO;
    }
}

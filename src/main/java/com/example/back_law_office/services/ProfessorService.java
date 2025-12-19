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
public class ProfessorService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfessorProfileRepository professorProfileRepository;

    @Autowired
    private RolesRepository rolesRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public UserDTO createProfessor(CreateProfessorDTO dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());

        if (dto.getRoleIds() != null && !dto.getRoleIds().isEmpty()) {
            Set<Role> roles = new HashSet<>(rolesRepository.findAllById(dto.getRoleIds()));
            user.setRoles(roles);
        }

        User savedUser = userRepository.save(user);

        ProfessorProfile profile = new ProfessorProfile();
        profile.setUser(savedUser);
        profile.setDepartment(dto.getDepartment());
        profile.setSpecialization(dto.getSpecialization());
        profile.setOfficeNumber(dto.getOfficeNumber());
        profile.setHireDate(dto.getHireDate());
        profile.setTitle(dto.getTitle());
        profile.setResearchArea(dto.getResearchArea());
        profile.setEmploymentType(dto.getEmploymentType());

        professorProfileRepository.save(profile);
        savedUser.setProfessorProfile(profile);

        return convertToUserDTO(savedUser);
    }

    @Transactional
    public UserDTO updateProfessor(Long userId, CreateProfessorDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        user.setUsername(dto.getUsername());
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());

        if (dto.getRoleIds() != null && !dto.getRoleIds().isEmpty()) {
            Set<Role> roles = new HashSet<>(rolesRepository.findAllById(dto.getRoleIds()));
            user.setRoles(roles);
        }

        ProfessorProfile profile = user.getProfessorProfile();
        if (profile == null) {
            profile = new ProfessorProfile();
            profile.setUser(user);
        }
        profile.setDepartment(dto.getDepartment());
        profile.setSpecialization(dto.getSpecialization());
        profile.setOfficeNumber(dto.getOfficeNumber());
        profile.setHireDate(dto.getHireDate());
        profile.setTitle(dto.getTitle());
        profile.setResearchArea(dto.getResearchArea());
        profile.setEmploymentType(dto.getEmploymentType());

        professorProfileRepository.save(profile);
        user.setProfessorProfile(profile);
        User updatedUser = userRepository.save(user);

        return convertToUserDTO(updatedUser);
    }

    public UserDTO getProfessorById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        
        if (user.getProfessorProfile() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Professor profile not found for this user");
        }
        
        return convertToUserDTO(user);
    }

    private UserDTO convertToUserDTO(User user) {
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        if (user.getProfessorProfile() != null) {
            userDTO.setProfessorProfile(modelMapper.map(user.getProfessorProfile(), ProfessorProfileDTO.class));
        }
        return userDTO;
    }
}

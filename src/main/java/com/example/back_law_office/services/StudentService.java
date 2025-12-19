package com.example.back_law_office.services;

import com.example.back_law_office.dtos.*;
import com.example.back_law_office.models.*;
import com.example.back_law_office.repositories.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;


@Service
public class StudentService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentProfileRepository studentProfileRepository;


    @Autowired
    private ModelMapper modelMapper;


    public UserDTO createStudent(CreateStudentDTO dto, User user) {
        StudentProfile profile = new StudentProfile();
        profile.setUser(user);
        profile.setStudentCode(dto.getStudentCode());
        profile.setSemester(dto.getSemester());
        profile.setEnrollmentDate(dto.getEnrollmentDate());
        profile.setUniversity(dto.getUniversity());
        studentProfileRepository.save(profile);
        return convertToUserDTO(profile.getUser());
    }

    @Transactional
    public UserDTO updateStudent(Long userId, CreateStudentDTO dto) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        StudentProfile profile = user.getStudentProfile();
        if (profile == null) {
            profile = new StudentProfile();
            profile.setUser(user);
        }
        profile.setStudentCode(dto.getStudentCode());
        profile.setSemester(dto.getSemester());
        profile.setEnrollmentDate(dto.getEnrollmentDate());
        profile.setUniversity(dto.getUniversity());

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

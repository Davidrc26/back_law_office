package com.example.back_law_office.controllers;

import com.example.back_law_office.dtos.CreateStudentDTO;
import com.example.back_law_office.dtos.UserDTO;
import com.example.back_law_office.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping("/create")
    public ResponseEntity<UserDTO> createStudent(@RequestBody CreateStudentDTO dto) {
        UserDTO userDTO = studentService.createStudent(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getStudent(@PathVariable Long userId) {
        UserDTO userDTO = studentService.getStudentById(userId);
        return ResponseEntity.ok(userDTO);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserDTO> updateStudent(@PathVariable Long userId, @RequestBody CreateStudentDTO dto) {
        UserDTO userDTO = studentService.updateStudent(userId, dto);
        return ResponseEntity.ok(userDTO);
    }
}

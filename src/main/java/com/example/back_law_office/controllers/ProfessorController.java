package com.example.back_law_office.controllers;

import com.example.back_law_office.dtos.CreateProfessorDTO;
import com.example.back_law_office.dtos.UserDTO;
import com.example.back_law_office.services.ProfessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/professors")
public class ProfessorController {

    @Autowired
    private ProfessorService professorService;

    @PostMapping("/create")
    public ResponseEntity<UserDTO> createProfessor(@RequestBody CreateProfessorDTO dto) {
        UserDTO userDTO = professorService.createProfessor(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getProfessor(@PathVariable Long userId) {
        UserDTO userDTO = professorService.getProfessorById(userId);
        return ResponseEntity.ok(userDTO);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserDTO> updateProfessor(@PathVariable Long userId, @RequestBody CreateProfessorDTO dto) {
        UserDTO userDTO = professorService.updateProfessor(userId, dto);
        return ResponseEntity.ok(userDTO);
    }
}

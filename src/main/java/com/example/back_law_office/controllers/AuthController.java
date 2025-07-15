package com.example.back_law_office.controllers;

import com.example.back_law_office.security.JwtUtil;

import org.springframework.transaction.annotation.Transactional;

import com.example.back_law_office.repositories.UserRepository;
import com.example.back_law_office.models.Role;
import com.example.back_law_office.models.User;
import com.example.back_law_office.dtos.LoginRequestDTO;
import com.example.back_law_office.dtos.JwtResponseDTO;

import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AuthController {


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly = true)
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401).body("Usuario o contraseña incorrectos");
        }
        Set<Role> safeRoles = user.getRoles() == null ? Set.of() : Set.copyOf(user.getRoles());
        String roles = safeRoles.stream()
                .map(r -> r.getName())
                .collect(Collectors.joining(","));
        String token = jwtUtil.generateToken(user.getUsername(), roles);
        return ResponseEntity.ok(new JwtResponseDTO(token));
    }
}
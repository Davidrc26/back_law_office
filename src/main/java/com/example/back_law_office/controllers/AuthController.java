package com.example.back_law_office.controllers;

import com.example.back_law_office.security.JwtUtil;
import com.example.back_law_office.security.GoogleTokenVerifier;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.example.back_law_office.models.Role;
import com.example.back_law_office.repositories.UserRepository;
import com.example.back_law_office.models.User;
import com.example.back_law_office.dtos.LoginRequestDTO;
import com.example.back_law_office.dtos.UserDTO;
import com.example.back_law_office.dtos.JwtResponseDTO;
import com.example.back_law_office.services.UserService;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.http.HttpStatus;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private GoogleTokenVerifier googleTokenVerifier;


    @Value("${google.clientId:}")
    private String googleClientId;

    @Transactional(readOnly = true)
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401).body("Usuario o contraseña incorrectos");
        }
        Set<Role> safeRoles = user.getRoles() == null ? Set.of() : Set.copyOf(user.getRoles());
        String roles = safeRoles.stream()
                .map(r -> r.getName())
                .collect(Collectors.joining(","));
        String token = jwtUtil.generateToken(user.getEmail(), roles);
        return ResponseEntity.ok(new JwtResponseDTO(modelMapper.map(user, UserDTO.class), token));
    }

    @Transactional
    @PostMapping("/google")
    public ResponseEntity<?> loginWithGoogle(@RequestBody GoogleLoginRequest googleLoginRequest) {
        String idToken = googleLoginRequest.getToken();
        GoogleIdToken.Payload payload;
        try {
            payload = googleTokenVerifier.verify(idToken);
        } catch (Exception e) {
            System.out.println("Error al verificar el token de Google: " + e.getMessage());
            return ResponseEntity.status(401).body("Token de Google inválido");
        }
        if (payload == null) {
            return ResponseEntity.status(401).body("Token de Google inválido");
        }

        String email = payload.getEmail();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        Set<Role> safeRoles = user.getRoles() == null ? Set.of() : new java.util.HashSet<>(user.getRoles());
        String roles = safeRoles.stream().map(Role::getName).collect(Collectors.joining(","));
        String token = jwtUtil.generateToken(user.getEmail(), roles);
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        return ResponseEntity.ok(new JwtResponseDTO(userDTO, token));
    }

    public static class GoogleLoginRequest {
        private String token;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
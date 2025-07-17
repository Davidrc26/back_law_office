package com.example.back_law_office.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtResponseDTO {
    private UserDTO user;
    private String token;
} 
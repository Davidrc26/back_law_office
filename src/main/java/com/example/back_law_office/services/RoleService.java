package com.example.back_law_office.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.back_law_office.models.Role;
import com.example.back_law_office.repositories.RoleRepository;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;
    /**
     * Obtiene todos los procedimientos.
     * @return Una lista de procedimientos.
     */
    public List<Role> getAllRoles() {
        List<Role> roles = roleRepository.findAll();
        return roles.stream().filter(role -> !role.getSpanishName().contains("admin"))
                .collect(Collectors.toList());
    }
    
}
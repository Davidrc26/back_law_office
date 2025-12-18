package com.example.back_law_office.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.back_law_office.models.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    
}

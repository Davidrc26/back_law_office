package com.example.back_law_office.models;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Set;

@Entity
@Data
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;
    
    @Column(name = "spanish_name", nullable = false)
    private String spanishName;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users;
} 
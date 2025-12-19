package com.example.back_law_office.dtos;

import lombok.Data;
import java.util.Set;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String email;

    private Set<RoleDTO> roles;

    private String phone;
    
    // Perfiles específicos (solo uno estará presente según el rol)
    private StudentProfileDTO studentProfile;
    private ProfessorProfileDTO professorProfile;
    private AdministratorProfileDTO administratorProfile;
    private AssistantProfileDTO assistantProfile;
}
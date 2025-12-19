package com.example.back_law_office.dtos;

import lombok.Data;
import java.time.LocalDate;

@Data
public class AdministratorProfileDTO {
    private Long id;
    private String adminLevel;
    private String department;
    private LocalDate hireDate;
    private String position;
    private String responsibilities;
}

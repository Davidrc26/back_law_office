package com.example.back_law_office.dtos;

import lombok.Data;
import java.time.LocalDate;

@Data
public class ProfessorProfileDTO {
    private Long id;
    private String department;
    private String specialization;
    private String officeNumber;
    private LocalDate hireDate;
    private String title;
    private String researchArea;
    private String employmentType;
}

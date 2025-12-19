package com.example.back_law_office.dtos;

import lombok.Data;
import java.time.LocalDate;

@Data
public class StudentProfileDTO {
    private Long id;
    private String studentCode;
    private Integer semester;
    private LocalDate enrollmentDate;
    private String university;
}

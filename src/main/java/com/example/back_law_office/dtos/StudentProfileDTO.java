package com.example.back_law_office.dtos;

import lombok.Data;
import java.time.LocalDate;

@Data
public class StudentProfileDTO {
    private Long id;
    private String studentCode;
    private Integer semester;
    private String major;
    private LocalDate enrollmentDate;
    private String university;
    private String academicStatus;
}

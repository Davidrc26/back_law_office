package com.example.back_law_office.dtos;

import lombok.Data;
import java.time.LocalDate;

@Data
public class AssistantProfileDTO {
    private Long id;
    private String assignedDepartment;
    private String shift;
    private LocalDate hireDate;
    private Long supervisorId;
    private String workArea;
}

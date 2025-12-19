package com.example.back_law_office.dtos;

import lombok.Data;
import java.util.Set;
@Data
public class CreateUserDTO {
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String secondName;
    private String secondLastName;
    private String documentNumber;
    private Long documentTypeId;
    private Set<Long> roleIds;
    private String phone;
}

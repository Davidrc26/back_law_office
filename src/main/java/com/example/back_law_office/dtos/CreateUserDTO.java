package com.example.back_law_office.dtos;

import lombok.Data;
import java.util.Set;
@Data
public class CreateUserDTO {
    private String username;
    private String password;
    private String email;
    private Set<Long> roleIds;
    private String phone;
}

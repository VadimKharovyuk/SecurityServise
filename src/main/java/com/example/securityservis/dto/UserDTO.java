package com.example.securityservis.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String role;
    private String password; // Добавьте это поле
}

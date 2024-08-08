package com.example.securityservis.dto;

import com.example.securityservis.model.User;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String role;
    private String password;
    private boolean blocked;



}

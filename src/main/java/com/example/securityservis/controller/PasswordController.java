package com.example.securityservis.controller;

import com.example.securityservis.repository.EmailFeignClient;
import com.example.securityservis.service.PasswordGenerator;
import com.example.securityservis.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/password")
public class PasswordController {

    @Autowired
    private EmailFeignClient emailFeignClient;

    @Autowired
    private PasswordGenerator passwordGenerator;

    @Autowired
    private UserService userService;

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String email) {
        // Генерация нового пароля
        String newPassword = passwordGenerator.generateNewPassword(12); // длиной 12 символов

        // Обновление пароля в базе данных
        userService.updatePassword(email, newPassword);

        // Отправка нового пароля на электронную почту
        emailFeignClient.sendEmailPassword(email, newPassword);

        return ResponseEntity.ok("New password sent");
    }
}

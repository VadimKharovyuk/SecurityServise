package com.example.securityservis.controller;
import com.example.securityservis.repository.EmailFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/password")
public class PasswordController {
    @Autowired
    private EmailFeignClient emailFeignClient;

    @PostMapping("/password")
    public ResponseEntity<String> sendEmailPassword(@RequestParam String emailRequest) {
        emailFeignClient.sendEmailPassword(emailRequest);
        return ResponseEntity.ok("New password sent to email");
    }


}

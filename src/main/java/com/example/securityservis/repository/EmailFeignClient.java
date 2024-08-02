package com.example.securityservis.repository;

import com.example.securityservis.dto.EmailRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "EmailService")
public interface EmailFeignClient {

    @PostMapping("/api/email/send")
    String sendEmail(@RequestBody EmailRequest emailRequest);


    @PostMapping("/api/email/password")
    String sendEmailPassword(@RequestParam String emailRequest, @RequestParam String newPassword);



}


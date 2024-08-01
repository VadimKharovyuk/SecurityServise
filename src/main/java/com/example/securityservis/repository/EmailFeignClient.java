package com.example.securityservis.repository;

import com.example.securityservis.dto.EmailRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "EmailService")
public interface EmailFeignClient {

    @PostMapping("/api/email/send")
    String sendEmail(@RequestBody EmailRequest emailRequest);
}


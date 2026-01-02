package edu.exam_online.exam_online_system.repository;

import edu.exam_online.exam_online_system.dto.email.EmailRequest;
import edu.exam_online.exam_online_system.dto.email.EmailResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "exam-online-system", url = "https://api.brevo.com")
public interface SendEmailClient {
    @PostMapping(value = "/v3/smtp/email", produces = MediaType.APPLICATION_JSON_VALUE)
    EmailResponse sendEmail(@RequestHeader("api-key") String apiKey, @RequestBody EmailRequest body);
}
package edu.exam_online.exam_online_system.service.auth;

public interface EmailService {
    void sendVerificationEmail(String to, String code);
}

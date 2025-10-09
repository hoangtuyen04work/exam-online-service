package edu.exam_online.exam_online_system.service;

public interface EmailService {
    void sendVerificationEmail(String to, String code);
}

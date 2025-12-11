package edu.exam_online.exam_online_system.service.auth;

import edu.exam_online.exam_online_system.dto.email.EmailResponse;
import edu.exam_online.exam_online_system.dto.email.SendEmailRequest;
import edu.exam_online.exam_online_system.exception.AppException;

public interface EmailService {
    EmailResponse sendEmail(SendEmailRequest sendEmailRequest) throws AppException;

    void sendVerificationEmail(String to, String code);
}

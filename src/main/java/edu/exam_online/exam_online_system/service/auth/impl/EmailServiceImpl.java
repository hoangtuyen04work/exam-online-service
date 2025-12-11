package edu.exam_online.exam_online_system.service.auth.impl;

import edu.exam_online.exam_online_system.dto.email.EmailRequest;
import edu.exam_online.exam_online_system.dto.email.EmailResponse;
import edu.exam_online.exam_online_system.dto.email.Recipient;
import edu.exam_online.exam_online_system.dto.email.SendEmailRequest;
import edu.exam_online.exam_online_system.dto.email.Sender;
import edu.exam_online.exam_online_system.exception.AppException;
import edu.exam_online.exam_online_system.exception.ErrorCode;
import edu.exam_online.exam_online_system.repository.SendEmailClient;
import edu.exam_online.exam_online_system.service.auth.EmailService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class EmailServiceImpl implements EmailService {

    @NonFinal
    @Value("${api.key}")
    String apiKey;
    @NonFinal
    @Value(("${email.name}"))
    String emailSender;


    SendEmailClient sendEmailClient;

    @Override
    public EmailResponse sendEmail(SendEmailRequest sendEmailRequest) throws AppException {
        Sender sender = new Sender();
        sender.setEmail(emailSender);
        sender.setName("Exam Online System");
        EmailRequest emailRequest = EmailRequest.builder()
                .to(sendEmailRequest.getTo())
                .sender(sender)
                .subject(sendEmailRequest.getSubject())
                .htmlContent(sendEmailRequest.getHtmlContent())
                .build();
        try{
            return sendEmailClient.sendEmail(apiKey, emailRequest);
        }
        catch(Exception e){
            log.error("Send email error: {}", e.getMessage());
            throw new AppException(ErrorCode.SEND_EMAIL_ERROR_CODE);
        }
    }

    @Override
    public void sendVerificationEmail(String to, String code) {
        String htmlContent = """
            <div style="font-family: Arial, sans-serif; padding: 20px; background-color: #f7f7f7;">
                <div style="max-width: 500px; margin: auto; background: white; padding: 20px; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.1);">
                    <h2 style="color: #333; text-align: center;">Xác nhận tài khoản</h2>
                    <p>Chào bạn,</p>
                    <p>Đây là mã xác nhận để kích hoạt tài khoản của bạn trong hệ thống <b>Exam Online System</b>.</p>

                    <div style="text-align: center; margin: 30px 0;">
                        <div style="font-size: 26px; font-weight: bold; letter-spacing: 6px; padding: 15px; background: #eef4ff; display: inline-block; border-radius: 8px; border: 1px solid #c9d7ff;">
                            %s
                        </div>
                    </div>

                    <p>Mã xác nhận có hiệu lực trong <b>5 phút</b>. Vui lòng không chia sẻ mã cho bất kỳ ai.</p>

                    <p>Nếu bạn không yêu cầu đăng ký tài khoản, vui lòng bỏ qua email này.</p>

                    <hr style="margin-top: 30px;">

                    <p style="font-size: 12px; color: #777; text-align: center;">
                        Đây là email tự động, vui lòng không trả lời.  
                        <br>Exam Online System © 2025
                    </p>
                </div>
            </div>
            """.formatted(code);

        String subject = "Xác nhận tài khoản - Exam Online System";

        Recipient recipient = Recipient.builder()
                .name("Exam Online System")
                .email(to)
                .build();


        SendEmailRequest sendEmailRequest = SendEmailRequest.builder()
                .htmlContent(htmlContent)
                .subject(subject)
                .to(List.of(recipient))
                .build();
        sendEmail(sendEmailRequest);
    }
}

package edu.exam_online.exam_online_system.service.auth.impl;

import edu.exam_online.exam_online_system.exception.AppException;
import edu.exam_online.exam_online_system.exception.ErrorCode;
import edu.exam_online.exam_online_system.service.auth.EmailService;
import jakarta.mail.internet.InternetAddress;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class EmailServiceImpl implements EmailService {
    JavaMailSender mailSender;

    @Override
    public void sendVerificationEmail(String to, String code) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");

            // Đổi tên hiển thị của email
            helper.setFrom(new InternetAddress("noreply@examsystem.com", "Exam Online System"));

            helper.setTo(to);
            helper.setSubject("Xác nhận tài khoản - Exam Online System");

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

            helper.setText(htmlContent, true); // HTML = true

            mailSender.send(message);

        } catch (Exception e) {
            throw new AppException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

}

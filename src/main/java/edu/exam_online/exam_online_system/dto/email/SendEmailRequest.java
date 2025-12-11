package edu.exam_online.exam_online_system.dto.email;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SendEmailRequest {
    List<Recipient> to;
    String subject;
    String htmlContent;
}
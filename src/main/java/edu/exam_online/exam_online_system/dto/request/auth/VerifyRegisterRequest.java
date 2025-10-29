package edu.exam_online.exam_online_system.dto.request.auth;

import lombok.Data;

@Data
public class VerifyRegisterRequest {
    private String code;
    private Long userId;
}

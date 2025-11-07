package edu.exam_online.exam_online_system.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode{
    // ===== USER =====
    REFRESH_TOKEN_FALSE(401, "Refresh token failed", HttpStatus.UNAUTHORIZED),
    REFRESH_TOKEN_INVALID(401, "Refresh token invalid", HttpStatus.UNAUTHORIZED),
    USER_EXISTED(409, "User already exists", HttpStatus.CONFLICT),
    USER_NOT_EXISTED(400, "User not found", HttpStatus.NOT_FOUND),
    WRONG_PASSWORD_OR_USERID(401, "Wrong password or user ID", HttpStatus.UNAUTHORIZED),
    NOT_AUTHENTICATION(401, "Not authenticated", HttpStatus.UNAUTHORIZED),
    NOT_AUTHORIZATION(403, "Access denied", HttpStatus.FORBIDDEN),
    WRONG_VERIFICATION_CODE(400, "Wrong verification code", HttpStatus.BAD_REQUEST),
    VERIFICATION_CODE_EXPIRED(410, "Verification code expired", HttpStatus.GONE),
    ACCOUNT_IS_INACTIVE(403, "Account is inactive", HttpStatus.FORBIDDEN),
    ACCOUNT_IS_LOCKED(403, "Account is locked", HttpStatus.FORBIDDEN),
    CHANGE_PASSWORD_FAILED(400, "Change password failed", HttpStatus.BAD_REQUEST),

    // ===== ROLE =====
    ROLE_NOT_FOUND(404, "Role not found", HttpStatus.NOT_FOUND),

    // ===== SYSTEM =====
    INTERNAL_SERVER_ERROR(500, "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR),
    SERVICE_UNAVAILABLE(503, "Service temporarily unavailable", HttpStatus.SERVICE_UNAVAILABLE),

    // ===== GENERAL =====
    CONFLICT(409, "Conflict occurred", HttpStatus.CONFLICT),
    RESOURCE_ALREADY_EXISTS(409, "Resource already exists", HttpStatus.CONFLICT),
    RESOURCE_NOT_FOUND(404, "Requested resource not found", HttpStatus.NOT_FOUND),

    // ====== EXAM ========
    EXAM_NOT_FOUND(400, "Exam not found", HttpStatus.BAD_REQUEST),
    EXAM_SESSION_NOT_FOUND(400, "Exam session not found", HttpStatus.BAD_REQUEST),
    CAN_NOT_DO_EXAM(400, "Can not do exam", HttpStatus.BAD_REQUEST),
    EXAM_NOT_YET_START(400, "Exam not yet start", HttpStatus.BAD_REQUEST),
    EXAM_CLOSED(400, "Exam closed", HttpStatus.BAD_REQUEST),
    CANNOT_JOIN_EXAM_SESSION(400, "Can not join exam session", HttpStatus.BAD_REQUEST),
    EXAM_SESSION_STUDENT_NOT_FOUND(400, "Exam session student not found", HttpStatus.BAD_REQUEST),
    BANK_QUESTION_NOT_FOUND(400, "Bank question not found", HttpStatus.NOT_FOUND),
    QUESTION_NOT_FOUND(400, "Question not found", HttpStatus.NOT_FOUND);

    @Getter
    int status;
    String message;
    HttpStatus httpStatus;

    ErrorCode(int status, String message,HttpStatus httpStatus){
        this.status = status;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}

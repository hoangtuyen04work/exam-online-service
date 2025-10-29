package edu.exam_online.exam_online_system.exception;

import edu.exam_online.exam_online_system.commons.BaseResponse;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.text.ParseException;

@ControllerAdvice
@Hidden
@Slf4j
public class GlobalException {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<BaseResponse<Object>> handleAppException(AppException e) {
        ErrorCode errorCode = e.getErrorCode();
        log.error("Runtime Exception: {}", e.getMessage());
        return ResponseEntity
                .status(errorCode.getStatus()) // ✅ Set HTTP status
                .body(BaseResponse.error(errorCode.getStatus(), errorCode.getMessage()));
    }

    @ExceptionHandler(ParseException.class)
    public ResponseEntity<BaseResponse<Object>> handleParseException(ParseException e) {
        ErrorCode errorCode = ErrorCode.NOT_AUTHENTICATION;
        log.error("Runtime Exception: {}", e.getMessage());
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(BaseResponse.error(errorCode.getStatus(), errorCode.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<BaseResponse<Object>> handleRuntimeException(RuntimeException e) {
        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        log.error("Runtime Exception: {}", e.getMessage());
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(BaseResponse.error(errorCode.getStatus(), errorCode.getMessage()));
    }
}

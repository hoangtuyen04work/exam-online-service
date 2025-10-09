package edu.exam_online.exam_online_system.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppException extends RuntimeException {
    private final ErrorCode errorCode;

    public AppException(ErrorCode error) {
        super(error.getMessage()); // message có thể hiện ở logs
        this.errorCode = error;
    }

}

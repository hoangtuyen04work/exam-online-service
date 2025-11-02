package edu.exam_online.exam_online_system.commons.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExamSessionStudentStateEnum {
    NOT_OPEN,
    OPENING,
    JOINED,
    CLOSED
}

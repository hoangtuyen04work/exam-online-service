package edu.exam_online.exam_online_system.commons.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StudentEventEnum {
    ENTER,
    LEAVE,
    FOCUS_LOST,
    SUBMIT
}

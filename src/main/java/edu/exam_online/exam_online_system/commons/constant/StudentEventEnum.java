package edu.exam_online.exam_online_system.commons.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StudentEventEnum {
    WAITING, // Sinh viên đang đợi vào phòng thi
    ENTER, // Sinh viên vào phòng thi
    LEAVE, // Sinh viên rời phòng thi
    FOCUS_LOST, // Sinh viên mất focus
    FOCUS_REGAINED, // Sinh viên lấy lại focus
    TAB_SWITCH, // Sinh viên chuyển tab
    SUBMIT, // Sinh viên nộp bài
    DISCONNECTED, // Sinh viên mất kết nối đột ngột
    RECONNECTED // Sinh viên kết nối lại
}

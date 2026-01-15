package edu.exam_online.exam_online_system.commons.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExamStudentStatusEnum {
    WAITING, // Đang đợi vào phòng thi
    IN_PROGRESS, // Đang làm bài
    COMPLETED, // Đã hoàn thành
    FOCUS_LOST, // Đang mất tập trung
    DISCONNECTED, // Đã mất kết nối/thoát đột ngột
    LEFT // Đã rời phòng thi
}
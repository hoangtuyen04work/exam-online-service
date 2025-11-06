package edu.exam_online.exam_online_system.dto.response.exam.teacher;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ImportResultResponse {
    private int successCount;
    private List<String> errors;
}

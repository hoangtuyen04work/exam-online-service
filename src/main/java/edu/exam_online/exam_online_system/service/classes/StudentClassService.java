package edu.exam_online.exam_online_system.service.classes;

import edu.exam_online.exam_online_system.dto.response.classes.StudentClassDetailResponse;
import edu.exam_online.exam_online_system.dto.response.classes.StudentClassResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StudentClassService {
    
    Page<StudentClassResponse> getMyClasses(Pageable pageable);
    
    StudentClassDetailResponse getClassDetail(Long classId);
}

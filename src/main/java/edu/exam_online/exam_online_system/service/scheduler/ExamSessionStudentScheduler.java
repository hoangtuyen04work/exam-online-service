package edu.exam_online.exam_online_system.service.scheduler;

import edu.exam_online.exam_online_system.service.exam.ExamSessionStudentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class ExamSessionStudentScheduler {

    ExamSessionStudentService examSessionStudentService;

    @Scheduled(cron = "0 0/1 * * * *")
    public void autoSaveExamSessionStudent() {
        log.info("autoSaveExamSessionStudent");
        examSessionStudentService.autoSaveExamSessionStudent();
    }
}

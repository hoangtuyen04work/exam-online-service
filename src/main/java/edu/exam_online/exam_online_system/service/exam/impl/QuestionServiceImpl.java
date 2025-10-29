package edu.exam_online.exam_online_system.service.exam.impl;

import edu.exam_online.exam_online_system.service.exam.QuestionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class QuestionServiceImpl implements QuestionService {
}

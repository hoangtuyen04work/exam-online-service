package edu.exam_online.exam_online_system.service.exam.impl;

import edu.exam_online.exam_online_system.mapper.AnswerMapper;
import edu.exam_online.exam_online_system.repository.exam.AnswerRepository;
import edu.exam_online.exam_online_system.service.exam.AnswerService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class AnswerServiceImpl implements AnswerService {

    AnswerRepository answerRepository;

    AnswerMapper answerMapper;

}

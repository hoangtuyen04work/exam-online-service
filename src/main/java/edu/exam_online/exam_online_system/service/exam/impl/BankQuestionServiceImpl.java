package edu.exam_online.exam_online_system.service.exam.impl;

import edu.exam_online.exam_online_system.dto.request.exam.BankQuestionCreationRequest;
import edu.exam_online.exam_online_system.dto.request.exam.BankQuestionSearchParam;
import edu.exam_online.exam_online_system.dto.request.exam.QuestionCreationRequest;
import edu.exam_online.exam_online_system.dto.response.exam.teacher.BankQuestionDetailResponse;
import edu.exam_online.exam_online_system.dto.response.exam.teacher.BankQuestionResponse;
import edu.exam_online.exam_online_system.entity.auth.User;
import edu.exam_online.exam_online_system.entity.exam.Answer;
import edu.exam_online.exam_online_system.entity.exam.BankQuestion;
import edu.exam_online.exam_online_system.entity.exam.Question;
import edu.exam_online.exam_online_system.exception.AppException;
import edu.exam_online.exam_online_system.exception.ErrorCode;
import edu.exam_online.exam_online_system.mapper.AnswerMapper;
import edu.exam_online.exam_online_system.mapper.BankQuestionMapper;
import edu.exam_online.exam_online_system.mapper.QuestionMapper;
import edu.exam_online.exam_online_system.repository.auth.UserRepository;
import edu.exam_online.exam_online_system.repository.exam.BankQuestionRepository;
import edu.exam_online.exam_online_system.service.exam.BankQuestionService;
import edu.exam_online.exam_online_system.utils.SecurityUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class BankQuestionServiceImpl implements BankQuestionService {

    BankQuestionRepository bankQuestionRepository;
    UserRepository userRepository;

    BankQuestionMapper bankQuestionMapper;
    QuestionMapper questionMapper;
    AnswerMapper answerMapper;

    @Override
    public void deleteById(Long bankQuestionId){
        bankQuestionRepository.deleteById(bankQuestionId);
    }

    @Override
    public BankQuestionDetailResponse getById(Long bankQuestionId){
        Long userId = SecurityUtils.getUserId();
        BankQuestion bankQuestion = bankQuestionRepository.findByTeacherIdAndId(userId, bankQuestionId)
                .orElseThrow(() -> new AppException(ErrorCode.BANK_QUESTION_NOT_FOUND));

        return bankQuestionMapper.toDetailResponse(bankQuestion, questionMapper.toResponse(bankQuestion.getQuestions()));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BankQuestionResponse> searchBankQuestion(BankQuestionSearchParam param, Pageable pageable){
        Long userId = SecurityUtils.getUserId();

        Page<BankQuestion> bankQuestions = bankQuestionRepository.search(userId, param.getName(), pageable);
        return bankQuestions.map(bankQuestionMapper::toResponse);
    }

    @Override
    @Transactional
    public void createBankQuestion(BankQuestionCreationRequest request){
        BankQuestion bankQuestion = bankQuestionMapper.toEntity(request);
        Long userId = SecurityUtils.getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        List<Question> question = request.getQuestions().stream().map(questionRequest ->{
            Question questionEntity = createQuestion(questionRequest);
            questionEntity.setBankQuestion(bankQuestion);
            return questionEntity;
        }).toList();
        bankQuestion.setQuestions(question);
        bankQuestion.setTeacher(user);

        bankQuestionRepository.save(bankQuestion);
        log.info("createBankQuestion:{}",bankQuestion);
    }

    private Question createQuestion(QuestionCreationRequest request){
        Question question = questionMapper.toEntity(request);

        List<Answer> answers = request.getAnswers().stream()
                .map(answerRequest -> answerMapper.toEntity(answerRequest, question) )
                .toList();
        question.setAnswers(answers);
        return question;
    }
}

package edu.exam_online.exam_online_system.service.exam.impl;

import edu.exam_online.exam_online_system.dto.request.exam.AnswerUpdateRequest;
import edu.exam_online.exam_online_system.dto.request.exam.ExamBankQuestionCreationRequest;
import edu.exam_online.exam_online_system.dto.request.exam.ExamCreationRequest;
import edu.exam_online.exam_online_system.dto.request.exam.ExamUpdateQuestionsRequest;
import edu.exam_online.exam_online_system.dto.request.exam.QuestionCreationRequest;
import edu.exam_online.exam_online_system.dto.request.exam.QuestionUpdateRequest;
import edu.exam_online.exam_online_system.dto.response.exam.teacher.ExamDetailResponse;
import edu.exam_online.exam_online_system.dto.response.exam.teacher.ExamResponse;
import edu.exam_online.exam_online_system.dto.request.exam.ExamUpdateRequest;
import edu.exam_online.exam_online_system.dto.response.exam.teacher.QuestionResponse;
import edu.exam_online.exam_online_system.entity.auth.User;
import edu.exam_online.exam_online_system.entity.exam.Answer;
import edu.exam_online.exam_online_system.entity.exam.BankQuestion;
import edu.exam_online.exam_online_system.entity.exam.Exam;
import edu.exam_online.exam_online_system.entity.exam.Question;
import edu.exam_online.exam_online_system.entity.exam.QuestionExam;
import edu.exam_online.exam_online_system.exception.AppException;
import edu.exam_online.exam_online_system.exception.ErrorCode;
import edu.exam_online.exam_online_system.mapper.AnswerMapper;
import edu.exam_online.exam_online_system.mapper.ExamMapper;
import edu.exam_online.exam_online_system.mapper.QuestionExamMapper;
import edu.exam_online.exam_online_system.mapper.QuestionMapper;
import edu.exam_online.exam_online_system.repository.auth.UserRepository;
import edu.exam_online.exam_online_system.repository.exam.AnswerRepository;
import edu.exam_online.exam_online_system.repository.exam.BankQuestionRepository;
import edu.exam_online.exam_online_system.repository.exam.ExamRepository;
import edu.exam_online.exam_online_system.repository.exam.QuestionRepository;
import edu.exam_online.exam_online_system.service.exam.ExamService;
import edu.exam_online.exam_online_system.utils.SecurityUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE,  makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class ExamServiceImpl implements ExamService {

    ExamRepository examRepository;
    UserRepository userRepository;
    QuestionRepository questionRepository;
    BankQuestionRepository bankQuestionRepository;
    AnswerRepository answerRepository;

    ExamMapper examMapper;
    QuestionMapper questionMapper;
    AnswerMapper answerMapper;
    QuestionExamMapper questionExamMapper;

    @Override
    @Transactional
    public void createExamFromBankQuestion(ExamBankQuestionCreationRequest request){
        Long userId = SecurityUtils.getUserId();
        BankQuestion bankQuestion = bankQuestionRepository.findByIdAndTeacherId(request.getBankQuestionId(), userId)
                .orElseThrow(() -> new AppException(ErrorCode.BANK_QUESTION_NOT_FOUND));

        List<Question> questions = bankQuestion.getQuestions();
        Collections.shuffle(questions);

        List<Question> questionShuffles = questions.stream().limit(request.getNumber()).toList();
        Exam exam = examMapper.toEntity(request);
        List<QuestionExam> questionExams =  questionShuffles.stream()
                .map(question -> questionExamMapper.toEntity(exam, question)).toList();
        exam.setQuestionExams(questionExams);
        examRepository.save(exam);
    }

    @Override
    @Transactional
    public void deleteById(Long examId){
        Long userId = SecurityUtils.getUserId();
        examRepository.deleteByIdAndTeacherId(examId, userId);
    }

    @Override
    @Transactional
    public void updateExam(Long examId, ExamUpdateQuestionsRequest request) {

        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new AppException(ErrorCode.EXAM_NOT_FOUND));
        examMapper.updateEntity(exam, request);

        List<Long> questionIdUpdates = request.getQuestions().stream()
                .map(QuestionUpdateRequest::getQuestionId)
                .filter(Objects::nonNull)
                .toList();
        Map<Long, QuestionUpdateRequest> questionIdToQuestionUpdate = request.getQuestions().stream()
                .filter(question -> Objects.nonNull(question.getQuestionId()))
                        .collect(Collectors.toMap(QuestionUpdateRequest::getQuestionId, question -> question));

        exam.getQuestionExams().removeIf(question ->
                !questionIdUpdates.contains(question.getQuestion().getId())
        );

        List<Question> questions = new ArrayList<>();
        List<Answer> answers = new ArrayList<>();

        List<QuestionExam> questionExams = exam.getQuestionExams().stream()
                .peek(questionExam -> {
                    Question question = questionExam.getQuestion();
                    QuestionUpdateRequest questionUpdateRequest = questionIdToQuestionUpdate.get(question.getId());
                    updateAnswers(answers, question, questionUpdateRequest);
                    questionMapper.updateEntity(question, questionUpdateRequest);
                    questions.add(question);
                }).toList();
        exam.getQuestionExams().addAll(questionExams);

        List<QuestionUpdateRequest> questionUpdateRequests = request.getQuestions().stream()
                .filter(question -> Objects.isNull(question.getQuestionId()))
                .toList();
        List<QuestionExam> newQuestionExams = questionUpdateRequests.stream()
                .map(
                        questionRequest -> {
                            Question question = createQuestion(questionRequest);
                            questions.add(question);
                            return createQuestionExam(questionRequest, question, exam);
                        }
                )
                .toList();
        exam.getQuestionExams().addAll(newQuestionExams);

        questionRepository.saveAll(questions);
        Long userId = SecurityUtils.getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() ->  new AppException(ErrorCode.USER_NOT_EXISTED));
        user.getExams().add(exam);

        exam.setTeacher(user);
        examRepository.save(exam);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ExamResponse> getAllExam(Pageable pageable){
        Long teacherId = SecurityUtils.getUserId();

        Page<Exam> exams = examRepository.findByTeacherId(teacherId, pageable);

        return exams.map(examMapper::toResponse);
    }

    @Override
    @Transactional
    public void updateExamQuestions(Long examId, ExamUpdateQuestionsRequest request) {
        Long teacherId = SecurityUtils.getUserId();

        Exam exam = validateAndGetExam(examId, teacherId);

        for (QuestionUpdateRequest qReq : request.getQuestions()) {
            QuestionExam questionExam = findOrCreateQuestionExam(exam, qReq, teacherId);
            updateQuestionContent(questionExam.getQuestion(), qReq, teacherId);
        }

        examRepository.save(exam);
    }

    @Override
    @Transactional(readOnly = true)
    public ExamDetailResponse getExamDetail(Long examId) {
        Long teacherId = SecurityUtils.getUserId();

        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new AppException(ErrorCode.EXAM_NOT_FOUND));
        exam.getQuestionExams().forEach(questionExam -> questionExam.getQuestion().getAnswers().size());

        if (!exam.getTeacher().getId().equals(teacherId)) {
            throw new AppException(ErrorCode.NOT_AUTHORIZATION);
        }

        List<QuestionResponse> questions = exam.getQuestionExams()
                .stream()
                .map(questionMapper::toQuestionResponse)
                .toList();

        return examMapper.toExamDetailResponse(exam, questions);
    }

    @Override
    @Transactional
    public ExamDetailResponse updateExam(Long examId, ExamUpdateRequest request) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new AppException(ErrorCode.EXAM_NOT_FOUND));

        Long teacherId = SecurityUtils.getUserId();
        if (!exam.getTeacher().getId().equals(teacherId)) {
            throw new AppException(ErrorCode.NOT_AUTHORIZATION);
        }

        examMapper.updateExamFromRequest(request, exam); // MapStruct lo phần set dữ liệu
        examRepository.save(exam);

        return examMapper.toExamDetailResponse(exam);
    }

    @Override
    @Transactional(readOnly = true)
    public ExamResponse getExamById(Long id) {
        Exam exam = examRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.EXAM_NOT_FOUND));

        log.info("Fetched exam successfully: {}", exam.getId());
        return examMapper.toResponse(exam);
    }

    @Override
    @Transactional
    public ExamResponse createExam(ExamCreationRequest request){
        Exam exam = examMapper.toEntity(request);
        List<QuestionCreationRequest> questionRequests = request.getQuestions();
        List<QuestionExam> questionExams = questionRequests.stream()
                .map(
                        questionRequest -> {
                            Question question = createQuestion(questionRequest);
                            return createQuestionExam(questionRequest, question, exam);
                        }
                )
                .toList();
        exam.setQuestionExams(questionExams);

        Long userId = SecurityUtils.getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() ->  new AppException(ErrorCode.USER_NOT_EXISTED));
        user.getExams().add(exam);

        exam.setTeacher(user);
        Exam savedExam = examRepository.save(exam);

        log.info("create exam success, exam id: {}", savedExam.getId());
        return examMapper.toResponse(savedExam);
    }

    private void updateAnswers(Question question, List<AnswerUpdateRequest> answerRequests, Long teacherId) {
        question.getAnswers().clear();

        List<Answer> answers = answerRequests.stream()
                .map(req -> Answer.builder()
                        .content(req.getContent())
                        .isCorrect(req.isCorrect())
                        .createdBy(teacherId)
                        .question(question) // ✅ rất quan trọng
                        .build())
                .toList();

        question.getAnswers().addAll(answers);
    }


    private void updateQuestionContent(Question question, QuestionUpdateRequest qReq, Long teacherId) {
        questionMapper.updateEntity(question, qReq);

        updateAnswers(question, qReq.getAnswers(), teacherId);
    }

    private QuestionExam findOrCreateQuestionExam(Exam exam, QuestionUpdateRequest qReq, Long teacherId) {
        // Tìm câu hỏi trong exam
        QuestionExam existing = exam.getQuestionExams()
                .stream()
                .filter(q -> q.getQuestion().getId().equals(qReq.getQuestionId()))
                .findFirst()
                .orElse(null);

        if (existing != null) {
            existing.setPoint(qReq.getPoint());
            existing.setOrderColumn(qReq.getOrderColumn());
            return existing;
        }

        // Nếu chưa có → tạo mới
        Question newQuestion = new Question();

        QuestionExam newQE = questionExamMapper.toEntity(exam, qReq, newQuestion);

        exam.getQuestionExams().add(newQE);
        return newQE;
    }

    private Exam validateAndGetExam(Long examId, Long teacherId) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new AppException(ErrorCode.EXAM_NOT_FOUND));

        if (!exam.getTeacher().getId().equals(teacherId)) {
            throw new AppException(ErrorCode.NOT_AUTHORIZATION);
        }

        return exam;
    }

    private QuestionExam createQuestionExam(QuestionCreationRequest request, Question question, Exam exam){
        QuestionExam questionExam = questionExamMapper.toEntity(request);
        questionExam.setQuestion(question);
        questionExam.setExam(exam);

        question.getQuestionExams().add(questionExam);
        return questionExam;
    }

    private Question createQuestion(QuestionCreationRequest request){
        Question question = questionMapper.toEntity(request);

        List<Answer> answers = request.getAnswers().stream()
                .map(answerRequest -> answerMapper.toEntity(answerRequest, question) )
                .toList();
        question.setAnswers(answers);
        return question;
    }

    private void validateUpdateExam(Long examId){
        Long teacherId = SecurityUtils.getUserId();
        examRepository.findByIdAndTeacherId(examId, teacherId).orElseThrow(() -> new AppException(ErrorCode.NOT_AUTHENTICATION));
    }

    private QuestionExam createQuestionExam(QuestionUpdateRequest request, Question question, Exam exam){
        QuestionExam questionExam = questionExamMapper.toEntity(request);
        questionExam.setQuestion(question);
        questionExam.setExam(exam);

        question.getQuestionExams().add(questionExam);
        return questionExam;
    }

    private Question createQuestion(QuestionUpdateRequest request){
        Question question = questionMapper.toEntity(request);

        List<Answer> answers = request.getAnswers().stream()
                .map(answerRequest -> answerMapper.toEntity(answerRequest, question) )
                .toList();
        question.setAnswers(answers);
        return question;
    }

    private void updateAnswers(List<Answer> answers, Question question, QuestionUpdateRequest qReq) {
        List<Long> answerIdUpdates = qReq.getAnswers().stream()
                .map(AnswerUpdateRequest::getAnswerId)
                .filter(Objects::nonNull)
                .toList();
        Map<Long, AnswerUpdateRequest> answerIdToAnswerUpdate = qReq.getAnswers().stream()
                .filter(answer -> Objects.nonNull(answer.getAnswerId()))
                        .collect(Collectors.toMap(AnswerUpdateRequest::getAnswerId, answer -> answer));

        question.getAnswers().removeIf(answer ->
                !answerIdUpdates.contains(answer.getId())
        );

        List<Answer> answerUpdates = question.getAnswers().stream()
                .peek(answer -> answerMapper.updateEntity(answer, answerIdToAnswerUpdate.get(answer.getId())))
                .toList();
        answers.addAll(answerUpdates);

        List<Answer> newAnswers = qReq.getAnswers().stream()
                .filter(ans -> Objects.isNull(ans.getAnswerId()))
                .map(ans -> {
                    Answer answer = answerMapper.toEntity(ans, question);
                    answer.setQuestion(question);
                    return answer;
                })
                .toList();
        question.getAnswers().addAll(newAnswers);

        answers.addAll(newAnswers);
    }
}

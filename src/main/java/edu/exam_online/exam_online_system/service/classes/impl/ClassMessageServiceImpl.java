package edu.exam_online.exam_online_system.service.classes.impl;

import edu.exam_online.exam_online_system.commons.constant.RoleEnum;
import edu.exam_online.exam_online_system.dto.request.classes.ChatMessageRequest;
import edu.exam_online.exam_online_system.dto.response.classes.ChatMessageResponse;
import edu.exam_online.exam_online_system.entity.auth.User;
import edu.exam_online.exam_online_system.entity.classes.Class;
import edu.exam_online.exam_online_system.entity.classes.ClassMessage;
import edu.exam_online.exam_online_system.exception.AppException;
import edu.exam_online.exam_online_system.exception.ErrorCode;
import edu.exam_online.exam_online_system.repository.auth.UserRepository;
import edu.exam_online.exam_online_system.repository.classes.ClassMessageRepository;
import edu.exam_online.exam_online_system.repository.classes.ClassRepository;
import edu.exam_online.exam_online_system.repository.classes.ClassStudentRepository;
import edu.exam_online.exam_online_system.service.classes.ClassMessageService;
import edu.exam_online.exam_online_system.utils.SecurityUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class ClassMessageServiceImpl implements ClassMessageService {

    ClassMessageRepository classMessageRepository;
    ClassRepository classRepository;
    ClassStudentRepository classStudentRepository;
    UserRepository userRepository;

    @Override
    @Transactional
    public ChatMessageResponse sendMessage(ChatMessageRequest request) {
        Long userId = SecurityUtils.getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Class classEntity = classRepository.findById(request.getClassId())
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));

        // Check if user is teacher or student in this class
        boolean isTeacher = classEntity.getTeacher().getId().equals(userId);
        boolean isStudent = false;

        if (!isTeacher) {
            isStudent = classStudentRepository.existsByClassEntityIdAndStudentId(request.getClassId(), userId);
            if (!isStudent) {
                throw new AppException(ErrorCode.NOT_AUTHORIZATION);
            }

            // Check if student chat is allowed
            if (!classEntity.getAllowStudentChat()) {
                throw new AppException(ErrorCode.NOT_AUTHORIZATION);
            }
        }

        // Create and save message
        ClassMessage message = ClassMessage.builder()
                .classEntity(classEntity)
                .sender(user)
                .content(request.getContent())
                .build();

        message = classMessageRepository.save(message);

        log.info("Message sent: classId={}, userId={}, messageId={}", request.getClassId(), userId, message.getId());

        return mapToResponse(message);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ChatMessageResponse> getMessages(Long classId, Pageable pageable) {
        Long userId = SecurityUtils.getUserId();

        Class classEntity = classRepository.findById(classId)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));

        // Check if user is teacher or student in this class
        boolean isTeacher = classEntity.getTeacher().getId().equals(userId);
        boolean isStudent = classStudentRepository.existsByClassEntityIdAndStudentId(classId, userId);

        if (!isTeacher && !isStudent) {
            throw new AppException(ErrorCode.NOT_AUTHORIZATION);
        }

        Page<ClassMessage> messages = classMessageRepository.findByClassId(classId, pageable);
        return messages.map(this::mapToResponse);
    }

    @Override
    @Transactional
    public void updateChatSetting(Long classId, Boolean allowStudentChat) {
        Long userId = SecurityUtils.getUserId();

        Class classEntity = classRepository.findById(classId)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));

        // Only teacher can update chat settings
        if (!classEntity.getTeacher().getId().equals(userId)) {
            throw new AppException(ErrorCode.NOT_AUTHORIZATION);
        }

        classEntity.setAllowStudentChat(allowStudentChat);
        classRepository.save(classEntity);

        log.info("Chat setting updated: classId={}, allowStudentChat={}", classId, allowStudentChat);
    }

    @Override
    @Transactional(readOnly = true)
    public Boolean getChatSetting(Long classId) {
        Long userId = SecurityUtils.getUserId();

        Class classEntity = classRepository.findById(classId)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));

        // Check if user has access to this class
        boolean isTeacher = classEntity.getTeacher().getId().equals(userId);
        boolean isStudent = classStudentRepository.existsByClassEntityIdAndStudentId(classId, userId);

        if (!isTeacher && !isStudent) {
            throw new AppException(ErrorCode.NOT_AUTHORIZATION);
        }

        return classEntity.getAllowStudentChat();
    }

    private ChatMessageResponse mapToResponse(ClassMessage message) {
        User sender = message.getSender();
        String senderRole = sender.getRole().getName().equals(RoleEnum.TEACHER.name())  ? "TEACHER" : "STUDENT";

        return ChatMessageResponse.builder()
                .id(message.getId())
                .classId(message.getClassEntity().getId())
                .senderId(sender.getId())
                .senderName(sender.getUsername())
                .senderRole(senderRole)
                .content(message.getContent())
                .createdAt(message.getCreatedAt())
                .build();
    }
}

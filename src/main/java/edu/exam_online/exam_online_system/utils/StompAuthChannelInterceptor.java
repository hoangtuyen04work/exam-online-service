package edu.exam_online.exam_online_system.utils;

import edu.exam_online.exam_online_system.exception.AppException;
import edu.exam_online.exam_online_system.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor; // Import class này
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class StompAuthChannelInterceptor implements ChannelInterceptor {

    TokenUtils tokenUtils;


    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        // StompHeaderAccessor.wrap(message) có thể ném NPE nếu message không phải STOMP
        // Sử dụng cách an toàn hơn:
        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        // Chỉ xử lý khi accessor tồn tại và là lệnh CONNECT
        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {

            List<String> authHeaders = accessor.getNativeHeader("Authorization");
            log.info("[WS] Connecting... Token header: {}", authHeaders);

            if (authHeaders == null || authHeaders.isEmpty()) {
                log.error("[WS] Authentication failed: No Authorization header.");
                // BẮT BUỘC: Ném lỗi để từ chối kết nối
                throw new AppException(ErrorCode.NOT_AUTHENTICATION);
            }

            String token = authHeaders.get(0);
            token = token.replace("Bearer ", "");

            if (!tokenUtils.validateToken(token)) {
                log.error("[WS] Authentication failed: Invalid token.");
                // BẮT BUỘC: Ném lỗi để từ chối kết nối
                throw new AppException(ErrorCode.NOT_AUTHENTICATION);
            }

            Long userId = tokenUtils.getUserId(token);
            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(userId, null, Collections.emptyList());

            // ⭐ LƯU VÀO SESSION KHI CONNECT THÀNH CÔNG
            accessor.setUser(auth);
            log.info("[WS] Connection established. Principal set = {}", auth.getName());
        }

        // Không cần check SUBSCRIBE ở đây.
        // Principal sẽ tự động được truyền đi từ session.

        return message;
    }
}
//
//package edu.exam_online.exam_online_system.utils;
//
//import edu.exam_online.exam_online_system.exception.AppException;
//import edu.exam_online.exam_online_system.exception.ErrorCode;
//import lombok.AccessLevel;
//import lombok.RequiredArgsConstructor;
//import lombok.experimental.FieldDefaults;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.messaging.Message;
//import org.springframework.messaging.MessageChannel;
//import org.springframework.messaging.simp.stomp.StompCommand;
//import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
//import org.springframework.messaging.support.ChannelInterceptor;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.stereotype.Component;
//
//import java.util.Collections;
//import java.util.List;
//
//@Component
//@RequiredArgsConstructor
//@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
//@Slf4j
//public class StompAuthChannelInterceptor implements ChannelInterceptor {
//
//    TokenUtils tokenUtils;
//
//    @Override
//    public Message<?> preSend(Message<?> message, MessageChannel channel) {
//
//        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
//
//        StompCommand command = accessor.getCommand();
//
//        if (command == null)
//            return message;
//
//        if (StompCommand.CONNECT.equals(command) || StompCommand.SUBSCRIBE.equals(command)) {
//
//            List<String> authHeaders = accessor.getNativeHeader("Authorization");
//            log.info("[WS] incoming token header: {}", authHeaders);
//
//            if (authHeaders == null || authHeaders.isEmpty()) {
//                return message; // ❗ KHÔNG THROW
//            }
//
//            String token = authHeaders.get(0);
//            token = token.replace("Bearer ", "");
//
//            if (!tokenUtils.validateToken(token)) {
//                throw new AppException(ErrorCode.NOT_AUTHENTICATION);
//            }
//
//            Long userId = tokenUtils.getUserId(token);
//            UsernamePasswordAuthenticationToken auth =
//                    new UsernamePasswordAuthenticationToken(userId, null, Collections.emptyList());
//
//            accessor.setUser(auth);  // ⭐ LƯU VÀO SESSION
//
//            log.info("[WS] Principal set = {}", auth.getName());
//        }
//
//        return message;
//    }
//
//
//
//}

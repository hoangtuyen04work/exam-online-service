package edu.exam_online.exam_online_system.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.exam_online.exam_online_system.commons.BaseResponse;
import edu.exam_online.exam_online_system.exception.ErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

//custom jwt authentication entry point to resolve unauthentication access
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ErrorCode errorCode = ErrorCode.NOT_AUTHENTICATION;
        response.setStatus(errorCode.getStatus());
        response.getContentType();
        BaseResponse<?> apiResponse = BaseResponse.error(errorCode.getStatus(), errorCode.getMessage());
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
        response.flushBuffer();
    }
}

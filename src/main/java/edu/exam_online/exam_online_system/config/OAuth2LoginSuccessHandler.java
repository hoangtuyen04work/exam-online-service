package edu.exam_online.exam_online_system.config;

import edu.exam_online.exam_online_system.entity.auth.Role;
import edu.exam_online.exam_online_system.entity.auth.Token;
import edu.exam_online.exam_online_system.entity.auth.User;
import edu.exam_online.exam_online_system.exception.AppException;
import edu.exam_online.exam_online_system.exception.ErrorCode;
import edu.exam_online.exam_online_system.mapper.UserMapper;
import edu.exam_online.exam_online_system.repository.auth.RoleRepository;
import edu.exam_online.exam_online_system.repository.auth.UserRepository;
import edu.exam_online.exam_online_system.service.auth.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler
        implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TokenService tokenService;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {

        HttpSession session = request.getSession(false);

        String role = null;
        if (session != null) {
            role = (String) session.getAttribute("OAUTH2_ROLE");
            session.removeAttribute("OAUTH2_ROLE"); // dọn cho sạch
        }

        if (role == null) {
            throw new RuntimeException("Role is missing in OAuth2 login");
        }

        OAuth2AuthenticationToken oauthToken =
                (OAuth2AuthenticationToken) authentication;

        String jwt = createOrUpdateUser(oauthToken, request, Long.parseLong(role));

        response.sendRedirect(
            "http://localhost:3000/oauth2/success?token=" + jwt
        );
    }

    @Transactional
    public String createOrUpdateUser(
            OAuth2AuthenticationToken oauthToken,
            HttpServletRequest request,
            Long roleId
    ) {
        OAuth2User oauthUser = oauthToken.getPrincipal();
        Map<String, Object> attr = oauthUser.getAttributes();

        String email = extractEmail(attr);
        String name = (String) attr.get("name");

        User user = userRepository
                .findByEmailAndRoleId(email, roleId)
                .orElseGet(() -> createNewUser(email, name, roleId));

        Token token = tokenService.saveToken(user);
        return token.getToken();
    }

    private String extractEmail(Map<String, Object> attr) {
        String email = (String) attr.get("email");
        if (email == null) {
            email = (String) attr.get("preferred_username");
        }
        return email;
    }

    private User createNewUser(String email, String name, Long roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        User user = userMapper.toEntity(email, name, role);
        return userRepository.save(user);
    }

}

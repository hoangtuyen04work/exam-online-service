package edu.exam_online.exam_online_system.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

@Component
public class CustomOAuth2AuthorizationRequestResolver
        implements OAuth2AuthorizationRequestResolver {

    private final OAuth2AuthorizationRequestResolver defaultResolver;

    public CustomOAuth2AuthorizationRequestResolver(
            ClientRegistrationRepository repo
    ) {
        this.defaultResolver =
            new DefaultOAuth2AuthorizationRequestResolver(
                repo, "/oauth2/authorization"
            );
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
        saveRoleToSession(request);
        return defaultResolver.resolve(request);
    }

    @Override
    public OAuth2AuthorizationRequest resolve(
            HttpServletRequest request,
            String clientRegistrationId
    ) {
        saveRoleToSession(request);
        return defaultResolver.resolve(request, clientRegistrationId);
    }

    private void saveRoleToSession(HttpServletRequest request) {
        String role = request.getParameter("role");
        if (role != null) {
            request.getSession(true)
                   .setAttribute("OAUTH2_ROLE", role);
        }
    }
}

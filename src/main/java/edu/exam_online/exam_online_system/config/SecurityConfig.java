package edu.exam_online.exam_online_system.config;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SecurityConfig {
    CustomJwtDecoder customJwtDecoder;
    JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    OAuth2LoginSuccessHandler successHandler;
    CustomOAuth2AuthorizationRequestResolver customOAuth2AuthorizationRequestResolver;
//    CustomAuthenticationProvider customAuthenticationProvider;

    public SecurityConfig(CustomJwtDecoder customJwtDecoder, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint, OAuth2LoginSuccessHandler successHandler, CustomOAuth2AuthorizationRequestResolver customOAuth2AuthorizationRequestResolver) {
        this.customJwtDecoder = customJwtDecoder;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
//        this.customAuthenticationProvider = customAuthenticationProvider;
        this.successHandler = successHandler;
        this.customOAuth2AuthorizationRequestResolver = customOAuth2AuthorizationRequestResolver;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(withDefaults())
                .authorizeHttpRequests(request -> request
                        // Swagger public
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/swagger-resources/**",
                                "/webjars/**",
                                "/actuator",
                                "/actuator/**",
                                "/api/auth/",
                                "/api/auth/login",
                                "/api/auth/me",
                                "/api/auth/forgot-password",
                                "/api/auth/reset-password",
                                "/api/auth/resend-code",
                                "/api/auth/refresh-token",
                                "/api/auth/verify-email",
                                "/api/auth/register",
                                "/api/roles",
                                "/oauth2/**"
                        ).permitAll()
                        .requestMatchers("/ws/**").permitAll()

                        .requestMatchers("/public/**").permitAll()
                        .anyRequest().authenticated()
                )
//                .authenticationProvider(customAuthenticationProvider)
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwtConfigurer -> jwtConfigurer
                                .decoder(customJwtDecoder)
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                        )
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                )
                .oauth2Login(oauth2 -> oauth2
                        .authorizationEndpoint(auth -> auth
                                .authorizationRequestResolver(customOAuth2AuthorizationRequestResolver)
                        )
                        .successHandler(successHandler)
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint) // ✅ thêm dòng này
                )
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);

        return httpSecurity.build();
    }

    @Bean
    public AuthenticationSuccessHandler oAuth2SuccessHandler() {
        return (request, response, authentication) -> {
            response.sendRedirect("http://localhost:3000/oauth2/success");
        };
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new CustomJwtAuthenticationConverter());
        return jwtAuthenticationConverter;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
}
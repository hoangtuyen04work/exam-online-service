package edu.exam_online.exam_online_system.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class WebConfig {

    @Value("${origin}")
    private String ORIGIN;
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("http://localhost:5173");
        corsConfiguration.addAllowedOrigin("http://localhost:5176");
        corsConfiguration.addAllowedOrigin(ORIGIN);
        corsConfiguration.addAllowedOrigin("http://192.120.1.102:3000");
        corsConfiguration.addAllowedOrigin("http://192.120.2.103:3000");
        corsConfiguration.addAllowedOrigin("http://192.120.3.104:3000");
        corsConfiguration.addAllowedOrigin("http://192.120.5.105:3000");

        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return new CorsFilter(source);
    }
}

package edu.exam_online.exam_online_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
@EnableJpaAuditing
public class ExamOnlineSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExamOnlineSystemApplication.class, args);
	}

}

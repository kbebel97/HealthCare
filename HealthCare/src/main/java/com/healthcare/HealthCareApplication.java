package com.healthcare;

import org.springframework.boot.SpringApplication;
//(exclude = SecurityAutoConfiguration.class)
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.healthcare.repository.UserRepository;
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)

public class HealthCareApplication {

	public static void main(String[] args) {
		SpringApplication.run(HealthCareApplication.class, args);
	}

}

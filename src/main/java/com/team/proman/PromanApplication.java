package com.team.proman;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class PromanApplication {

	public static void main(String[] args) {
		SpringApplication.run(PromanApplication.class, args);
	}
}

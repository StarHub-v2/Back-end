package com.example.starhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class StarhubApplication {

	public static void main(String[] args) {
		SpringApplication.run(StarhubApplication.class, args);
	}

}

package com.getterz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class GetterzApplication {

	public static void main(String[] args) {
		SpringApplication.run(GetterzApplication.class, args);
	}

}

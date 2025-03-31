package com.qpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class QuickParkAssistAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuickParkAssistAppApplication.class, args);
	}

}

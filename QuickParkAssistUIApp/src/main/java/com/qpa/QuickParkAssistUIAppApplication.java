package com.qpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.qpa.*")
public class QuickParkAssistUIAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuickParkAssistUIAppApplication.class, args);

	}

}

package com.qpa;

import java.util.Scanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.controller")
public class QuickParkAssistUIAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuickParkAssistUIAppApplication.class, args);
		  
	}

}

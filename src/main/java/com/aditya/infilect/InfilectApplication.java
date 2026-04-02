package com.aditya.infilect;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class InfilectApplication {

	public static void main(String[] args) {
		SpringApplication.run(InfilectApplication.class, args);
	}

}

package com.ayahathout.borrower_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.ayahathout.borrower_service.clients")
@EnableScheduling
public class BorrowerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BorrowerServiceApplication.class, args);
	}

}
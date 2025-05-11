package com.nvc.motel_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableFeignClients
@EnableScheduling
public class MotelServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MotelServiceApplication.class, args);
	}

}

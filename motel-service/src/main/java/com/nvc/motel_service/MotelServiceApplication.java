package com.nvc.motel_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MotelServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MotelServiceApplication.class, args);
	}

}

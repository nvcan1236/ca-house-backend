package com.nvc.ca_house;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class CaHouseApplicationTests {

	@Test
	void contextLoads() {
		String password = "123456";
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		System.out.println(passwordEncoder.encode(password));
		System.out.println(passwordEncoder.encode(password));
	}

}

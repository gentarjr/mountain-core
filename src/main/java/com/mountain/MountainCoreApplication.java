package com.mountain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MountainCoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(MountainCoreApplication.class, args);
	}

}

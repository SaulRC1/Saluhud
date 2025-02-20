package com.uhu.saluhud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.uhu.saluhuddatabaseutils")
public class SaluhudApplication {

	public static void main(String[] args) {
		SpringApplication.run(SaluhudApplication.class, args);
	}

}

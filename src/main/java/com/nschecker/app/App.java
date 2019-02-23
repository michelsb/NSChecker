package com.nschecker.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication(scanBasePackages = { "com.nschecker.controllers", "com.nschecker.modules"})
//@ComponentScan("com.nschecker.controllers")
@Configuration
public class App {

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}
	
}

package com.calindra.tech.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;


@SpringBootApplication
@PropertySource("classpath:/application.properties")
public class BackendTechApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendTechApplication.class, args);
	}

}

package com.tytuspawlak.cinema.api;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "com.tytuspawlak.cinema")
@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Cinema management API", description = "Application for managing a cinema", version = "0.0.1"))
public class CinemaApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CinemaApiApplication.class, args);
	}
}

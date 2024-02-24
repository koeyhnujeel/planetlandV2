package com.planetlandV2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class PlanetlandV2Application {

	public static void main(String[] args) {
		SpringApplication.run(PlanetlandV2Application.class, args);
	}
}

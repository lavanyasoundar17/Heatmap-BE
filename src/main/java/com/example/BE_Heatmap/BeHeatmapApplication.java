package com.example.BE_Heatmap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;


@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.example.BE_Heatmap.repository")
public class BeHeatmapApplication {

	public static void main(String[] args) {
		SpringApplication.run(BeHeatmapApplication.class, args);
	}

}

package com.example.BE_Heatmap;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BeHeatmapApplicationTests {

	@Test
	void contextLoads() {
		System.out.println("GOOGLE_APPLICATION_CREDENTIALS: " + System.getenv("GOOGLE_APPLICATION_CREDENTIALS"));
		System.out.println("FIREBASE_STORAGE_BUCKET_NAME: " + System.getenv("FIREBASE_STORAGE_BUCKET_NAME"));
	}

}

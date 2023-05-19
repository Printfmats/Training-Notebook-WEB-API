package com.example.demo;

import com.example.demo.jwt.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(RsaKeyProperties.class)
public class TrainingNotebookApplication {
	public static void main(String[] args) {
		SpringApplication.run(TrainingNotebookApplication.class, args);
	}

}

package com.app.quantitymeasurementapp;

import com.app.quantitymeasurementapp.config.AppProperties;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
@OpenAPIDefinition(info = @Info(title = "Quantity Measurement API", version = "2.0.0", description = "UC18 — Google OAuth2 + JWT + User Management"))
public class QuantityMeasurementAppApplication {
	public static void main(String[] args) {
		SpringApplication.run(QuantityMeasurementAppApplication.class, args);
	}
}
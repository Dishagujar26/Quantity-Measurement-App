package com.app.quantitymeasurementapp.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Binds application.properties values prefixed with "app" into a strongly-typed
 * configuration bean.
 */
@Data
@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {

	private Jwt jwt = new Jwt();
	private String baseUrl = "http://localhost:8080";

	@Data
	public static class Jwt {
		private String secret;
		private long expirationMs = 86400000; // 24 hours
	}
}
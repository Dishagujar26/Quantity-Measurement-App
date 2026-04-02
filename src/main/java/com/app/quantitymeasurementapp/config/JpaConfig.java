package com.app.quantitymeasurementapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class JpaConfig {
	// Separating @EnableJpaAuditing from the main application class
	// prevents @WebMvcTest from failing with "JPA metamodel must not be empty".
	// @WebMvcTest excludes @Configuration classes in other packages by default,
	// but picks up @SpringBootApplication — so auditing must live separately.
}
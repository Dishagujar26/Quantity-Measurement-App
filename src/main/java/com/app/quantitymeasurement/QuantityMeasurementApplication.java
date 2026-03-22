package com.app.quantitymeasurement;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
    info = @Info(
        title       = "Quantity Measurement API",
        version     = "17.0",
        description = "REST API for quantity measurement operations — comparison, conversion, " +
                      "addition, subtraction, and division across Length, Weight, Volume, " +
                      "and Temperature units."
    )
)
public class QuantityMeasurementApplication {

    /**
     * Bootstraps the Spring Boot application.
     *
     * @param args command-line arguments passed to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(QuantityMeasurementApplication.class, args);
    }
}

package com.qma.quantity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Represents a single quantity with a value, unit, and measurement type")
public class QuantityDTO {

    @NotNull(message = "Value must not be null")
    @JsonProperty("value")
    private Double value;

    @NotEmpty(message = "Unit must not be empty")
    @JsonProperty("unit")
    private String unit;

    @NotEmpty(message = "Measurement type must not be empty")
    @Pattern(
        regexp = "LengthUnit|WeightUnit|VolumeUnit|TemperatureUnit|LENGTH|WEIGHT|VOLUME|TEMPERATURE",
        message = "Measurement type must be one of: LengthUnit, WeightUnit, VolumeUnit, TemperatureUnit, LENGTH, WEIGHT, VOLUME, TEMPERATURE"
    )
    @JsonProperty("measurementType")
    private String measurementType;

    public QuantityDTO(double value, String unit, String measurementType) {
        this.value = value;
        this.unit = unit;
        this.measurementType = measurementType;
    }

    public String getNormalisedMeasurementType() {
        if (measurementType == null) return null;

        return switch (measurementType.trim().toUpperCase()) {
            case "LENGTHUNIT", "LENGTH" -> "LENGTH";
            case "WEIGHTUNIT", "WEIGHT" -> "WEIGHT";
            case "VOLUMEUNIT", "VOLUME" -> "VOLUME";
            case "TEMPERATUREUNIT", "TEMPERATURE" -> "TEMPERATURE";
            default -> measurementType.trim().toUpperCase();
        };
    }
}
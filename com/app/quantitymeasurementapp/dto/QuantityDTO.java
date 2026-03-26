package com.app.quantitymeasurementapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO representing a single measurement quantity for API input/output.
 *
 * Validation annotations ensure incoming JSON is well-formed before
 * the service layer processes it.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Represents a single quantity with a value, unit, and measurement type")
public class QuantityDTO {

    @NotNull(message = "Value must not be null")
    @Schema(description = "Numeric measurement value", example = "1.0")
    @JsonProperty("value")
    private Double value;

    @NotEmpty(message = "Unit must not be empty")
    @Schema(description = "Unit of measurement", example = "FEET")
    @JsonProperty("unit")
    private String unit;

    @NotEmpty(message = "Measurement type must not be empty")
    @Pattern(
        regexp = "LengthUnit|WeightUnit|VolumeUnit|TemperatureUnit|LENGTH|WEIGHT|VOLUME|TEMPERATURE",
        message = "Unit must be valid for the specified measurement type"
    )
    @Schema(
        description = "Measurement category",
        example = "LengthUnit",
        allowableValues = {"LengthUnit", "WeightUnit", "VolumeUnit", "TemperatureUnit"}
    )
    @JsonProperty("measurementType")
    private String measurementType;

    // ── Convenience constructor matching UC16 usage (double not Double) ───
    public QuantityDTO(double value, String unit, String measurementType) {
        this.value           = value;
        this.unit            = unit;
        this.measurementType = measurementType;
    }

    /**
     * Normalise measurementType to the internal UC16 convention (LENGTH, WEIGHT …).
     * Accepts both "LengthUnit" (API style) and "LENGTH" (legacy style).
     */
    public String getNormalisedMeasurementType() {
        if (measurementType == null) return null;
        return switch (measurementType.toUpperCase()) {
            case "LENGTHUNIT", "LENGTH" -> "LENGTH";
            case "WEIGHTUNIT", "WEIGHT" -> "WEIGHT";
            case "VOLUMEUNIT", "VOLUME" -> "VOLUME";
            case "TEMPERATUREUNIT", "TEMPERATURE" -> "TEMPERATURE";
            default -> measurementType.toUpperCase();
        };
    }
}

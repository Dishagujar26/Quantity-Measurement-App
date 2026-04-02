package com.app.quantitymeasurementapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request body wrapper for binary quantity operations
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Input wrapper containing two quantities for a measurement operation")
public class QuantityInputDTO {

    @Valid
    @NotNull(message = "thisQuantityDTO must not be null")
    @JsonProperty("thisQuantityDTO")
    @Schema(
        description = "First quantity (the 'this' operand)",
        example = "{\"value\":1.0,\"unit\":\"FEET\",\"measurementType\":\"LengthUnit\"}"
    )
    private QuantityDTO thisQuantityDTO;

    @Valid
    @NotNull(message = "thatQuantityDTO must not be null")
    @JsonProperty("thatQuantityDTO")
    @Schema(
        description = "Second quantity (the 'that' operand)",
        example = "{\"value\":12.0,\"unit\":\"INCHES\",\"measurementType\":\"LengthUnit\"}"
    )
    private QuantityDTO thatQuantityDTO;
}
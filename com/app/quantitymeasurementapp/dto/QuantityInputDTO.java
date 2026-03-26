package com.app.quantitymeasurementapp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request body wrapper for binary quantity operations (add, subtract, divide, compare, convert).
 *
 * <pre>
 * {
 *   "thisQuantityDTO": { "value": 1.0, "unit": "FEET",   "measurementType": "LengthUnit" },
 *   "thatQuantityDTO": { "value": 12.0, "unit": "INCHES", "measurementType": "LengthUnit" }
 * }
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Input wrapper containing two quantities for a measurement operation")
public class QuantityInputDTO {

    @Valid
    @NotNull(message = "thisQuantityDTO must not be null")
    @Schema(description = "First quantity (the 'this' operand)")
    private QuantityDTO thisQuantityDTO;

    @Valid
    @NotNull(message = "thatQuantityDTO must not be null")
    @Schema(description = "Second quantity (the 'that' operand)")
    private QuantityDTO thatQuantityDTO;
}

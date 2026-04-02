package com.app.quantitymeasurementapp.controller;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.quantitymeasurementapp.dto.QuantityInputDTO;
import com.app.quantitymeasurementapp.dto.QuantityMeasurementDTO;
import com.app.quantitymeasurementapp.service.IQuantityMeasurementService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/quantities")
@Tag(name = "Quantity Measurements", description = "REST API for quantity measurement operations")
public class QuantityMeasurementController {

    private static final Logger logger = LoggerFactory.getLogger(QuantityMeasurementController.class);

    @Autowired
    private IQuantityMeasurementService service;

    // ============================================================
    // GET /api/v1/quantities
    // API Indexd
    // ============================================================

    @Operation(summary = "API Index", description = "Returns all available endpoints and valid unit values.")
    @GetMapping
    public ResponseEntity<Map<String, Object>> apiIndex() {
        Map<String, Object> index = new LinkedHashMap<>();
        index.put("application", "Quantity Measurement App — UC17");
        index.put("version", "1.0.0");
        index.put("status", "UP");
        index.put("timestamp", LocalDateTime.now().toString());

        Map<String, String> operations = new LinkedHashMap<>();
        operations.put("POST /api/v1/quantities/add", "Add two compatible quantities");
        operations.put("POST /api/v1/quantities/subtract", "Subtract second from first");
        operations.put("POST /api/v1/quantities/divide", "Divide first by second");
        operations.put("POST /api/v1/quantities/compare", "Compare two quantities for equality");
        operations.put("POST /api/v1/quantities/convert", "Convert to target unit");
        index.put("operations", operations);

        Map<String, String> history = new LinkedHashMap<>();
        history.put("GET /api/v1/quantities/history/operation/{op}", "History by operation type");
        history.put("GET /api/v1/quantities/history/type/{type}", "History by measurement type");
        history.put("GET /api/v1/quantities/history/errored", "All error records");
        history.put("GET /api/v1/quantities/count/{operation}", "Count of successful operations");
        index.put("history", history);

        Map<String, String> measurementTypes = new LinkedHashMap<>();
        measurementTypes.put("LengthUnit", "FEET, INCHES, YARD, CENTIMETER");
        measurementTypes.put("WeightUnit", "KILOGRAM, GRAM, POUND, TONNE");
        measurementTypes.put("VolumeUnit", "LITRE, MILLILITRE, GALLON, CUBIC_FEET");
        measurementTypes.put("TemperatureUnit", "CELSIUS, FAHRENHEIT, KELVIN");
        index.put("measurementTypes", measurementTypes);

        Map<String, String> links = new LinkedHashMap<>();
        links.put("swagger-ui", "http://localhost:8080/swagger-ui.html");
        links.put("h2-console", "http://localhost:8080/h2-console");
        links.put("health", "http://localhost:8080/actuator/health");
        links.put("api-docs", "http://localhost:8080/api-docs");
        index.put("links", links);

        return ResponseEntity.ok(index);
    }

    // ============================================================
    // POST OPERATIONS
    // ============================================================

    @Operation(summary = "Add two quantities", description = "Adds two compatible quantities. Result is returned in the unit of the first operand.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Addition successful",
                    content = @Content(schema = @Schema(implementation = QuantityMeasurementDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input or incompatible units",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"status\":400,\"error\":\"Quantity Measurement Error\",\"message\":\"Cannot perform arithmetic between different measurement categories\"}"))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/add")
    public ResponseEntity<QuantityMeasurementDTO> performAdd(@Valid @RequestBody QuantityInputDTO input) {
        logger.info("POST /api/v1/quantities/add thisUnit={} thatUnit={}",
                input.getThisQuantityDTO().getUnit(),
                input.getThatQuantityDTO().getUnit());

        return ResponseEntity.ok(
                service.add(input.getThisQuantityDTO(), input.getThatQuantityDTO())
        );
    }

    @Operation(summary = "Subtract two quantities", description = "Subtracts second quantity from first quantity.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Subtraction successful"),
            @ApiResponse(responseCode = "400", description = "Invalid input or incompatible units")
    })
    @PostMapping("/subtract")
    public ResponseEntity<QuantityMeasurementDTO> performSubtract(@Valid @RequestBody QuantityInputDTO input) {
        logger.info("POST /api/v1/quantities/subtract");

        return ResponseEntity.ok(
                service.subtract(input.getThisQuantityDTO(), input.getThatQuantityDTO())
        );
    }

    @Operation(summary = "Divide two quantities", description = "Divides first quantity by second quantity and returns a ratio.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Division successful"),
            @ApiResponse(responseCode = "400", description = "Incompatible units"),
            @ApiResponse(responseCode = "500", description = "Divide by zero")
    })
    @PostMapping("/divide")
    public ResponseEntity<QuantityMeasurementDTO> performDivide(@Valid @RequestBody QuantityInputDTO input) {
        logger.info("POST /api/v1/quantities/divide");

        return ResponseEntity.ok(
                service.divide(input.getThisQuantityDTO(), input.getThatQuantityDTO())
        );
    }

    @Operation(summary = "Compare two quantities", description = "Compares two quantities for equality after conversion to a common base unit.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Comparison successful"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping("/compare")
    public ResponseEntity<QuantityMeasurementDTO> performCompare(@Valid @RequestBody QuantityInputDTO input) {
        logger.info("POST /api/v1/quantities/compare");

        return ResponseEntity.ok(
                service.compare(input.getThisQuantityDTO(), input.getThatQuantityDTO())
        );
    }

    @Operation(summary = "Convert a quantity to target unit", description = "Converts thisQuantityDTO into the target unit provided in thatQuantityDTO.unit. Keep thatQuantityDTO.value = 0.0 from frontend.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Conversion successful"),
            @ApiResponse(responseCode = "400", description = "Invalid unit or measurement type")
    })
    @PostMapping("/convert")
    public ResponseEntity<QuantityMeasurementDTO> performConvert(@Valid @RequestBody QuantityInputDTO input) {
        logger.info("POST /api/v1/quantities/convert from={} to={}",
                input.getThisQuantityDTO().getUnit(),
                input.getThatQuantityDTO().getUnit());

        return ResponseEntity.ok(
                service.convert(input.getThisQuantityDTO(), input.getThatQuantityDTO().getUnit())
        );
    }

    // ============================================================
    // GET HISTORY / ANALYTICS
    // ============================================================

    @Operation(summary = "Get history by operation type", description = "Returns all records for the given operation.")
    @GetMapping("/history/operation/{operation}")
    public ResponseEntity<List<QuantityMeasurementDTO>> getHistoryByOperation(
            @Parameter(description = "Operation type", example = "add")
            @PathVariable String operation) {

        logger.info("GET /api/v1/quantities/history/operation/{}", operation);
        return ResponseEntity.ok(service.getHistoryByOperation(operation));
    }

    @Operation(summary = "Get history by measurement type", description = "Returns all records for the given measurement type.")
    @GetMapping("/history/type/{measurementType}")
    public ResponseEntity<List<QuantityMeasurementDTO>> getHistoryByMeasurementType(
            @Parameter(description = "Measurement type", example = "LengthUnit")
            @PathVariable String measurementType) {

        logger.info("GET /api/v1/quantities/history/type/{}", measurementType);
        return ResponseEntity.ok(service.getHistoryByMeasurementType(measurementType));
    }

    @Operation(summary = "Count successful operations by type", description = "Returns count of successful non-error operations.")
    @GetMapping("/count/{operation}")
    public ResponseEntity<Long> getOperationCount(
            @Parameter(description = "Operation type", example = "compare")
            @PathVariable String operation) {

        logger.info("GET /api/v1/quantities/count/{}", operation);
        return ResponseEntity.ok(service.getOperationCount(operation));
    }

    @Operation(summary = "Get all errored operations", description = "Returns all records where an error occurred.")
    @GetMapping("/history/errored")
    public ResponseEntity<List<QuantityMeasurementDTO>> getErrorHistory() {
        logger.info("GET /api/v1/quantities/history/errored");
        return ResponseEntity.ok(service.getErrorHistory());
    }
}
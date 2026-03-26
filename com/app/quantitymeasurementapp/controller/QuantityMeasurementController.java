package com.app.quantitymeasurementapp.controller;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/quantities")
@Tag(name = "Quantity Measurements", description = "REST API for quantity measurement operations")
public class QuantityMeasurementController {

	private static final Logger logger = LoggerFactory.getLogger(QuantityMeasurementController.class);

	@Autowired
	private IQuantityMeasurementService service;

	// ═════════════════════════════════════════════════════════════════════
	// GET / — API Index
	// ═════════════════════════════════════════════════════════════════════

	@Operation(summary = "API Index", description = "Returns all available endpoints and valid unit values.")
	@GetMapping
	public ResponseEntity<Map<String, Object>> apiIndex() {
		Map<String, Object> index = new LinkedHashMap<>();
		index.put("application", "Quantity Measurement App — UC17");
		index.put("version", "1.0.0");
		index.put("status", "UP");
		index.put("timestamp", LocalDateTime.now().toString());

		Map<String, String> operations = new LinkedHashMap<>();
		operations.put("POST /add", "Add two compatible quantities");
		operations.put("POST /subtract", "Subtract second from first");
		operations.put("POST /divide", "Divide first by second (returns ratio)");
		operations.put("POST /compare", "Compare two quantities for equality");
		operations.put("POST /convert", "Convert to target unit");
		index.put("operations", operations);

		Map<String, String> history = new LinkedHashMap<>();
		history.put("GET /history/operation/{op}", "History by operation type");
		history.put("GET /history/type/{type}", "History by measurement type");
		history.put("GET /history/errored", "All error records");
		history.put("GET /count/{operation}", "Count of successful operations");
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

	// ═════════════════════════════════════════════════════════════════════
	// POST — Operations
	// ═════════════════════════════════════════════════════════════════════

	@Operation(summary = "Add two quantities", description = "Adds two compatible quantities. Result in unit of first operand.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Addition successful", content = @Content(schema = @Schema(implementation = QuantityMeasurementDTO.class))),
			@ApiResponse(responseCode = "400", description = "Invalid input or incompatible units", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"status\":400,\"error\":\"Quantity Measurement Error\",\"message\":\"Cannot perform arithmetic between different measurement categories\"}"))),
			@ApiResponse(responseCode = "500", description = "Internal server error") })
	@PostMapping("/add")
	public ResponseEntity<QuantityMeasurementDTO> performAdd(@Valid @RequestBody QuantityInputDTO input) {
		logger.info("POST /add  thisUnit={} thatUnit={}", input.getThisQuantityDTO().getUnit(),
				input.getThatQuantityDTO().getUnit());
		return ResponseEntity.ok(service.add(input.getThisQuantityDTO(), input.getThatQuantityDTO()));
	}

	@Operation(summary = "Subtract two quantities", description = "Subtracts second from first. Result in unit of first operand.")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Subtraction successful"),
			@ApiResponse(responseCode = "400", description = "Invalid input or incompatible units") })
	@PostMapping("/subtract")
	public ResponseEntity<QuantityMeasurementDTO> performSubtract(@Valid @RequestBody QuantityInputDTO input) {
		logger.info("POST /subtract");
		return ResponseEntity.ok(service.subtract(input.getThisQuantityDTO(), input.getThatQuantityDTO()));
	}

	@Operation(summary = "Divide two quantities", description = "Divides first by second. Returns dimensionless ratio as resultValue.")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Division successful"),
			@ApiResponse(responseCode = "400", description = "Incompatible units"),
			@ApiResponse(responseCode = "500", description = "Divide by zero") })
	@PostMapping("/divide")
	public ResponseEntity<QuantityMeasurementDTO> performDivide(@Valid @RequestBody QuantityInputDTO input) {
		logger.info("POST /divide");
		return ResponseEntity.ok(service.divide(input.getThisQuantityDTO(), input.getThatQuantityDTO()));
	}

	@Operation(summary = "Compare two quantities", description = "Compares for equality after converting to common base unit. "
			+ "resultString will be 'true' or 'false'.")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Comparison performed"),
			@ApiResponse(responseCode = "400", description = "Invalid input") })
	@PostMapping("/compare")
	public ResponseEntity<QuantityMeasurementDTO> performCompare(@Valid @RequestBody QuantityInputDTO input) {
		logger.info("POST /compare");
		return ResponseEntity.ok(service.compare(input.getThisQuantityDTO(), input.getThatQuantityDTO()));
	}

	@Operation(summary = "Convert a quantity to a target unit", description = "Converts thisQuantityDTO to the unit in thatQuantityDTO.unit. "
			+ "Set thatQuantityDTO.value = 0.0.")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Conversion successful"),
			@ApiResponse(responseCode = "400", description = "Invalid unit or measurement type") })
	@PostMapping("/convert")
	public ResponseEntity<QuantityMeasurementDTO> performConvert(@Valid @RequestBody QuantityInputDTO input) {
		logger.info("POST /convert  from={} to={}", input.getThisQuantityDTO().getUnit(),
				input.getThatQuantityDTO().getUnit());
		return ResponseEntity.ok(service.convert(input.getThisQuantityDTO(), input.getThatQuantityDTO().getUnit()));
	}

	// ═════════════════════════════════════════════════════════════════════
	// GET — History / Analytics
	// ═════════════════════════════════════════════════════════════════════

	@Operation(summary = "Get history by operation type", description = "Returns all records for the given operation (add, subtract, divide, compare, convert).")
	@GetMapping("/history/operation/{operation}")
	public ResponseEntity<List<QuantityMeasurementDTO>> getHistoryByOperation(
			@Parameter(description = "Operation type e.g. add, compare", example = "add") @PathVariable String operation) {
		logger.info("GET /history/operation/{}", operation);
		return ResponseEntity.ok(service.getHistoryByOperation(operation));
	}

	@Operation(summary = "Get history by measurement type", description = "Returns all records for the given type (LengthUnit, WeightUnit, VolumeUnit, TemperatureUnit).")
	@GetMapping("/history/type/{measurementType}")
	public ResponseEntity<List<QuantityMeasurementDTO>> getHistoryByMeasurementType(
			@Parameter(description = "Measurement type e.g. LengthUnit", example = "LengthUnit") @PathVariable String measurementType) {
		logger.info("GET /history/type/{}", measurementType);
		return ResponseEntity.ok(service.getHistoryByMeasurementType(measurementType));
	}

	@Operation(summary = "Count successful operations by type", description = "Returns count of non-error operations for the given type.")
	@GetMapping("/count/{operation}")
	public ResponseEntity<Long> getOperationCount(
			@Parameter(description = "Operation type e.g. compare", example = "compare") @PathVariable String operation) {
		logger.info("GET /count/{}", operation);
		return ResponseEntity.ok(service.getOperationCount(operation));
	}

	@Operation(summary = "Get all errored operations", description = "Returns all records where an error occurred during processing.")
	@GetMapping("/history/errored")
	public ResponseEntity<List<QuantityMeasurementDTO>> getErrorHistory() {
		logger.info("GET /history/errored");
		return ResponseEntity.ok(service.getErrorHistory());
	}
}
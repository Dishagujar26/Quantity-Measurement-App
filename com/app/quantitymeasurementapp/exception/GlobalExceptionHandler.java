package com.app.quantitymeasurementapp.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

/**
 * Centralised exception handling for all REST controllers. Provides consistent,
 * structured error responses for every exception type.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	private static final String QME_ERROR = "Quantity Measurement Error";

	// ── 1. Bean Validation failures (@Valid / @Validated) ─────────────────
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex,
			HttpServletRequest request) {

		String messages = ex.getBindingResult().getFieldErrors().stream().map(FieldError::getDefaultMessage)
				.collect(Collectors.joining("; "));

		logger.warn("Validation error on {}: {}", request.getRequestURI(), messages);

		ErrorResponse error = new ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), QME_ERROR,
				messages, request.getRequestURI());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}

	// ── 2. Domain / business rule violations ──────────────────────────────
	@ExceptionHandler(QuantityMeasurementException.class)
	public ResponseEntity<ErrorResponse> handleQuantityException(QuantityMeasurementException ex,
			HttpServletRequest request) {

		logger.warn("Quantity measurement error on {}: {}", request.getRequestURI(), ex.getMessage());

		ErrorResponse error = new ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), QME_ERROR,
				ex.getMessage(), request.getRequestURI());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}

	// ── 3. Illegal arguments (bad enum names, null units, etc.) ───────────
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex,
			HttpServletRequest request) {

		logger.warn("Illegal argument on {}: {}", request.getRequestURI(), ex.getMessage());

		ErrorResponse error = new ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), QME_ERROR,
				ex.getMessage(), request.getRequestURI());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}

	// ── 4. Unsupported operations (Temperature arithmetic) ────────────────
	@ExceptionHandler(UnsupportedOperationException.class)
	public ResponseEntity<ErrorResponse> handleUnsupportedOperation(UnsupportedOperationException ex,
			HttpServletRequest request) {

		logger.warn("Unsupported operation on {}: {}", request.getRequestURI(), ex.getMessage());

		ErrorResponse error = new ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), QME_ERROR,
				ex.getMessage(), request.getRequestURI());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}

	// ── 5. Arithmetic errors (divide by zero) ─────────────────────────────
	@ExceptionHandler(ArithmeticException.class)
	public ResponseEntity<ErrorResponse> handleArithmeticException(ArithmeticException ex, HttpServletRequest request) {

		logger.error("Arithmetic error on {}: {}", request.getRequestURI(), ex.getMessage());

		ErrorResponse error = new ErrorResponse(LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(),
				"Internal Server Error", ex.getMessage(), request.getRequestURI());

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
	}

	// ── 6. Catch-all for any other exception ──────────────────────────────
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, HttpServletRequest request) {

		logger.error("Unexpected error on {}: {}", request.getRequestURI(), ex.getMessage(), ex);

		ErrorResponse error = new ErrorResponse(LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(),
				"Internal Server Error", ex.getMessage(), request.getRequestURI());

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
	}
}

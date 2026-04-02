package com.app.quantitymeasurementapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * JPA entity mapped to the quantity_measurement_entity table. Lombok generates
 * all boilerplate; Spring Data auditing handles timestamps.
 */
@Entity
@Table(name = "quantity_measurement_entity", indexes = { @Index(name = "idx_operation", columnList = "operation"),
		@Index(name = "idx_mtype", columnList = "this_measurement_type"),
		@Index(name = "idx_is_error", columnList = "is_error"),
		@Index(name = "idx_created_at", columnList = "created_at") })
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuantityMeasurementEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// ── First operand ─────────────────────────────────────────────────────
	@Column(name = "this_value", nullable = false)
	private double thisValue;

	@Column(name = "this_unit", nullable = false, length = 50)
	private String thisUnit;

	@Column(name = "this_measurement_type", nullable = false, length = 50)
	private String thisMeasurementType;

	// ── Second operand (nullable for unary ops like convert) ──────────────
	@Column(name = "that_value")
	private Double thatValue;

	@Column(name = "that_unit", length = 50)
	private String thatUnit;

	@Column(name = "that_measurement_type", length = 50)
	private String thatMeasurementType;

	// ── Operation ─────────────────────────────────────────────────────────
	@Column(name = "operation", nullable = false, length = 20)
	private String operation;

	// ── Result ────────────────────────────────────────────────────────────
	@Column(name = "result_value")
	private Double resultValue;

	@Column(name = "result_unit", length = 50)
	private String resultUnit;

	@Column(name = "result_measurement_type", length = 50)
	private String resultMeasurementType;

	@Column(name = "result_string", length = 255)
	private String resultString;

	// ── Error tracking ────────────────────────────────────────────────────
	@Column(name = "is_error")
	private boolean error;

	@Column(name = "error_message", length = 500)
	private String errorMessage;

	// ── Audit timestamps ──────────────────────────────────────────────────
	@CreatedDate
	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	@LastModifiedDate
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	/**
	 * Convenience constructor (without id and audit fields) used by service layer.
	 */
	public QuantityMeasurementEntity(double thisValue, String thisUnit, String thisMeasurementType, Double thatValue,
			String thatUnit, String thatMeasurementType, String operation, Double resultValue, String resultUnit,
			String resultMeasurementType, String resultString, boolean error, String errorMessage) {

		this.thisValue = thisValue;
		this.thisUnit = thisUnit;
		this.thisMeasurementType = thisMeasurementType;
		this.thatValue = thatValue;
		this.thatUnit = thatUnit;
		this.thatMeasurementType = thatMeasurementType;
		this.operation = operation;
		this.resultValue = resultValue;
		this.resultUnit = resultUnit;
		this.resultMeasurementType = resultMeasurementType;
		this.resultString = resultString;
		this.error = error;
		this.errorMessage = errorMessage;
	}
}

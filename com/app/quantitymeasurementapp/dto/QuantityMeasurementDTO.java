package com.app.quantitymeasurementapp.dto;

import com.app.quantitymeasurementapp.model.QuantityMeasurementEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Data Transfer Object for QuantityMeasurementEntity.
 *
 * Used to transfer operation results between the service layer and REST
 * controllers. Contains static factory methods for clean Entity ↔ DTO
 * conversion.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response object capturing a full quantity measurement operation and its result")
public class QuantityMeasurementDTO {

	// ── First operand ─────────────────────────────────────────────────────
	@Schema(example = "1.0")
	private double thisValue;

	@Schema(example = "FEET")
	private String thisUnit;

	@Schema(example = "LengthUnit")
	private String thisMeasurementType;

	// ── Second operand ────────────────────────────────────────────────────
	@Schema(example = "12.0")
	private Double thatValue;

	@Schema(example = "INCHES")
	private String thatUnit;

	@Schema(example = "LengthUnit")
	private String thatMeasurementType;

	// ── Operation ─────────────────────────────────────────────────────────
	@Schema(example = "add")
	private String operation;

	// ── Result ────────────────────────────────────────────────────────────
	@Schema(example = "null — populated for boolean results like compare")
	private String resultString;

	@Schema(example = "2.0")
	private double resultValue;

	@Schema(example = "FEET")
	private String resultUnit;

	@Schema(example = "LengthUnit")
	private String resultMeasurementType;

	// ── Error ─────────────────────────────────────────────────────────────
	@Schema(example = "null")
	private String errorMessage;

	@Schema(example = "false")
	private boolean error;

	// ════════════════════════════════════════════════════════════════════
	// Static factory methods
	// ════════════════════════════════════════════════════════════════════

	/**
	 * Convert a persisted entity to a response DTO.
	 */
	public static QuantityMeasurementDTO fromEntity(QuantityMeasurementEntity entity) {
		QuantityMeasurementDTO dto = new QuantityMeasurementDTO();
		dto.thisValue = entity.getThisValue();
		dto.thisUnit = entity.getThisUnit();
		dto.thisMeasurementType = entity.getThisMeasurementType();
		dto.thatValue = entity.getThatValue();
		dto.thatUnit = entity.getThatUnit();
		dto.thatMeasurementType = entity.getThatMeasurementType();
		dto.operation = entity.getOperation();
		dto.resultString = entity.getResultString();
		dto.resultValue = entity.getResultValue() != null ? entity.getResultValue() : 0.0;
		dto.resultUnit = entity.getResultUnit();
		dto.resultMeasurementType = entity.getResultMeasurementType();
		dto.errorMessage = entity.getErrorMessage();
		dto.error = entity.isError();
		return dto;
	}

	/**
	 * Convert this DTO back to a JPA entity (for saving).
	 */
	public QuantityMeasurementEntity toEntity() {
		return new QuantityMeasurementEntity(thisValue, thisUnit, thisMeasurementType, thatValue, thatUnit,
				thatMeasurementType, operation, resultValue, resultUnit, resultMeasurementType, resultString, error,
				errorMessage);
	}

	/**
	 * Convert a list of entities to a list of DTOs using Stream API.
	 */
	public static List<QuantityMeasurementDTO> fromEntityList(List<QuantityMeasurementEntity> entities) {
		return entities.stream().map(QuantityMeasurementDTO::fromEntity).collect(Collectors.toList());
	}

	/**
	 * Convert a list of DTOs to a list of entities using Stream API.
	 */
	public static List<QuantityMeasurementEntity> toEntityList(List<QuantityMeasurementDTO> dtos) {
		return dtos.stream().map(QuantityMeasurementDTO::toEntity).collect(Collectors.toList());
	}
}

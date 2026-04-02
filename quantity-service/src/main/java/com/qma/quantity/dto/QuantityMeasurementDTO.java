package com.qma.quantity.dto;

import com.qma.quantity.model.QuantityMeasurementEntity;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuantityMeasurementDTO {

    private Long id;

    private Double thisValue;
    private String thisUnit;
    private String thisMeasurementType;

    private Double thatValue;
    private String thatUnit;
    private String thatMeasurementType;

    private String operation;

    private Double resultValue;
    private String resultUnit;
    private String resultMeasurementType;
    private String resultString;

    private String errorMessage;
    private boolean error;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static QuantityMeasurementDTO fromEntity(QuantityMeasurementEntity entity) {
        return QuantityMeasurementDTO.builder()
                .id(entity.getId())
                .thisValue(entity.getThisValue())
                .thisUnit(entity.getThisUnit())
                .thisMeasurementType(entity.getThisMeasurementType())
                .thatValue(entity.getThatValue())
                .thatUnit(entity.getThatUnit())
                .thatMeasurementType(entity.getThatMeasurementType())
                .operation(entity.getOperation())
                .resultValue(entity.getResultValue())
                .resultUnit(entity.getResultUnit())
                .resultMeasurementType(entity.getResultMeasurementType())
                .resultString(entity.getResultString())
                .error(entity.isError())
                .errorMessage(entity.getErrorMessage())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static List<QuantityMeasurementDTO> fromEntityList(List<QuantityMeasurementEntity> entities) {
        return entities.stream().map(QuantityMeasurementDTO::fromEntity).toList();
    }
}
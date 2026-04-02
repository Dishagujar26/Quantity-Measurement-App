package com.qma.history.dto;

import com.qma.history.model.QuantityHistoryEntity;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuantityHistoryDTO {

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

    private boolean error;
    private String errorMessage;

    private LocalDateTime createdAt;

    public static QuantityHistoryDTO fromEntity(QuantityHistoryEntity entity) {
        return QuantityHistoryDTO.builder()
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
                .build();
    }

    public static List<QuantityHistoryDTO> fromEntityList(List<QuantityHistoryEntity> entities) {
        return entities.stream().map(QuantityHistoryDTO::fromEntity).toList();
    }
}
package com.qma.history.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "quantity_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuantityHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
}
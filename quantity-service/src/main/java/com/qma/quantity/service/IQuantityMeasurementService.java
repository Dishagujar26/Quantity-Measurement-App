package com.qma.quantity.service;

import com.qma.quantity.dto.QuantityDTO;
import com.qma.quantity.dto.QuantityMeasurementDTO;

import java.util.List;

public interface IQuantityMeasurementService {
    QuantityMeasurementDTO add(QuantityDTO q1, QuantityDTO q2);
    QuantityMeasurementDTO subtract(QuantityDTO q1, QuantityDTO q2);
    QuantityMeasurementDTO divide(QuantityDTO q1, QuantityDTO q2);
    QuantityMeasurementDTO compare(QuantityDTO q1, QuantityDTO q2);
    QuantityMeasurementDTO convert(QuantityDTO q, String targetUnit);

    List<QuantityMeasurementDTO> getHistoryByOperation(String operation);
    List<QuantityMeasurementDTO> getHistoryByMeasurementType(String measurementType);
    long getOperationCount(String operation);
    List<QuantityMeasurementDTO> getErrorHistory();
}
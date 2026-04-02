package com.qma.history.service;

import com.qma.history.dto.QuantityHistoryDTO;
import com.qma.history.repository.QuantityHistoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistoryService {

    private final QuantityHistoryRepository repository;

    public HistoryService(QuantityHistoryRepository repository) {
        this.repository = repository;
    }

    public List<QuantityHistoryDTO> getHistoryByOperation(String operation) {
        return QuantityHistoryDTO.fromEntityList(repository.findByOperation(operation.toLowerCase()));
    }

    public List<QuantityHistoryDTO> getHistoryByMeasurementType(String measurementType) {
        return QuantityHistoryDTO.fromEntityList(repository.findByThisMeasurementType(measurementType));
    }

    public long getOperationCount(String operation) {
        return repository.countByOperationAndErrorFalse(operation.toLowerCase());
    }

    public List<QuantityHistoryDTO> getErrorHistory() {
        return QuantityHistoryDTO.fromEntityList(repository.findByErrorTrue());
    }
}
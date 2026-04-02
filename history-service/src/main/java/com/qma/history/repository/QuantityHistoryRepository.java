package com.qma.history.repository;

import com.qma.history.model.QuantityHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuantityHistoryRepository extends JpaRepository<QuantityHistoryEntity, Long> {
    List<QuantityHistoryEntity> findByOperation(String operation);
    List<QuantityHistoryEntity> findByThisMeasurementType(String measurementType);
    List<QuantityHistoryEntity> findByErrorTrue();
    long countByOperationAndErrorFalse(String operation);
}
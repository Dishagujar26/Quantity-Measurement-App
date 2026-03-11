package com.quantitymeasurementapp.repository;

import com.quantitymeasurementapp.entity.QuantityMeasurementEntity;

public interface IQuantityMeasurementRepository {

	void save(String key, QuantityMeasurementEntity entity);

	QuantityMeasurementEntity find(String key);
}
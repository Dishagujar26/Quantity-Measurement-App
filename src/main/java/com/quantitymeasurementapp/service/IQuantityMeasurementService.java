package com.quantitymeasurementapp.service;

import com.quantitymeasurementapp.dto.QuantityDTO;

public interface IQuantityMeasurementService {

	QuantityDTO add(QuantityDTO q1, QuantityDTO q2);

	QuantityDTO subtract(QuantityDTO q1, QuantityDTO q2);

	double divide(QuantityDTO q1, QuantityDTO q2);

	QuantityDTO convert(QuantityDTO q, String targetUnit);

	boolean compare(QuantityDTO q1, QuantityDTO q2);
}
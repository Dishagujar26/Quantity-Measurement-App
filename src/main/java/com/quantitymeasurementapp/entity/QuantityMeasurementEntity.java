package com.quantitymeasurementapp.entity;

public class QuantityMeasurementEntity {

	private double value;
	private String unit;

	public QuantityMeasurementEntity(double value, String unit) {
		this.value = value;
		this.unit = unit;
	}

	public double getValue() {
		return value;
	}

	public String getUnit() {
		return unit;
	}
}
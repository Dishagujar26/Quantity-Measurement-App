package com.QuantityMeasurementApp;

public enum LengthUnit {

	FEET(12.0), INCHES(1.0), YARDS(36.0), CENTIMETERS(0.393701);

	private final double toInchesFactor;

	LengthUnit(double toInchesFactor) {
		this.toInchesFactor = toInchesFactor;
	}

	public double toInches(double value) {
		return value * toInchesFactor;
	}

}
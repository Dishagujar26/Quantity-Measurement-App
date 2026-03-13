package com.quantitymeasurementapp.entity;

public class QuantityModel {
	private double value;
	private String unit;
	private String measurementType;

	public QuantityModel(double value, String unit, String measurementType) {
		this.value = value;
		this.unit = unit;
		this.measurementType = measurementType;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getMeasurementType() {
		return measurementType;
	}

	public void setMeasurementType(String measurementType) {
		this.measurementType = measurementType;
	}

	@Override
	public String toString() {
		return "QuantityModel{" + "value=" + value + ", unit='" + unit + '\'' + ", measurementType='" + measurementType
				+ '\'' + '}';
	}
}

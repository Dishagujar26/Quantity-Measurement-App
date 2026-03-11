package com.quantitymeasurementapp.dto;
public class QuantityDTO {

	private double value;
	private String unit;
	private String measurementType;

	public QuantityDTO(double value, String unit, String measurementType) {
		this.value = value;
		this.unit = unit;
		this.measurementType = measurementType;
	}

	public double getValue() {
		return value;
	}

	public String getUnit() {
		return unit;
	}

	public String getMeasurementType() {
		return measurementType;
	}

	@Override
	public String toString() {
		return "QuantityDTO{value=" + value + ", unit='" + unit + "', type='" + measurementType + "'}";
	}
}
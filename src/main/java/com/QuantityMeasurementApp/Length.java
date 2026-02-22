package com.QuantityMeasurementApp;

public class Length {
	private final double value;
	private final LengthUnit unit;

	public Length(double value, LengthUnit unit) {
		if (unit == null) {
			throw new IllegalArgumentException("Unit cannot be null");
		}
		this.value = value;
		this.unit = unit;
	}

	private double convertToBaseUnit() {
		return this.value * unit.getConversionFactor();
	}

	@Override
	public boolean equals(Object obj) {

		if (this == obj)
			return true;

		if (obj == null || getClass() != obj.getClass())
			return false;

		Length that = (Length) obj;

		return Double.compare(this.convertToBaseUnit(), that.convertToBaseUnit()) == 0;
	}

}
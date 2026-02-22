package com.QuantityMeasurementApp;

import java.util.Objects;

public class Length {
	private final double value;
	private final LengthUnit unit;
	
	public Length(double value, LengthUnit unit) {
		if (!Double.isFinite(value)) {
			throw new IllegalArgumentException("Value must be a finite number");
		}
		if (unit == null) {
			throw new IllegalArgumentException("Unit must not be null");
		}
		this.value = value;
		this.unit = unit;
	}
	
	public double getValue() {
		return value;
	}
	public LengthUnit getUnit() {
		return unit;
	}

	private double convertToBaseUnit() {
		double valueInInches = value * unit.getConversionFactor();
		return Math.round(valueInInches * 100.0) / 100.0;
	}

	private boolean compare(Length thatLength) {
		return Double.compare(this.convertToBaseUnit(), thatLength.convertToBaseUnit()) == 0;
	}

	public Length convertTo(LengthUnit targetUnit) {
		if (targetUnit == null) {
			throw new IllegalArgumentException("Target unit must not be null");
		}

		double valueInInches = this.convertToBaseUnit();
		double convertedValue = valueInInches / targetUnit.getConversionFactor();
		double roundedValue = Math.round(convertedValue * 100.0) / 100.0;

		return new Length(roundedValue, targetUnit);
	}

	public static double convert(double value, LengthUnit source, LengthUnit target) {

		if (!Double.isFinite(value)) {
			throw new IllegalArgumentException("Value must be a finite number");
		}

		if (source == null || target == null) {
			throw new IllegalArgumentException("Source and target units must not be null");
		}

		double valueInInches = value * source.getConversionFactor();
		double result = valueInInches / target.getConversionFactor();

		return Math.round(result * 100.0) / 100.0;
	}

	@Override
	public boolean equals(Object o) {

		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		

		Length other = (Length) o;
		return compare(other);
	}

	@Override
	public int hashCode() {
		return Objects.hash(convertToBaseUnit());
	}

	@Override
	public String toString() {
		return String.format("%.2f %s", value, unit);
	}


	public static void main(String[] args) {

        Length length1 = new Length(1.0, LengthUnit.FEET);
        Length length2 = new Length(12.0,LengthUnit.INCHES);
        System.out.println("Are lengths equal? " + length1.equals(length2)); 

        Length length3 = new Length(1.0, LengthUnit.YARDS);
        Length length4 = new Length(36.0, LengthUnit.INCHES);
        System.out.println("Are lengths equal? " + length3.equals(length4)); 
        
        Length length5 = new Length(100.0, LengthUnit.CENTIMETERS);
        Length length6 = new Length(39.3701, LengthUnit.INCHES);
        System.out.println("Are lengths equal? " + length5.equals(length6)); 
    }
}
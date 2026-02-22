package com.QuantityMeasurementApp;

public class QuantityMeasurementApp {

	public static boolean demonstrateLengthEquality(Length l1, Length l2) {
		return l1.equals(l2);
	}

	public static boolean demonstrateLengthComparison(double value1, LengthUnit unit1, double value2,
			LengthUnit unit2) {

		Length l1 = new Length(value1, unit1);
		Length l2 = new Length(value2, unit2);

		boolean result = l1.equals(l2);

		System.out.println("lengths are equal : " + result);
		return result;
	}

	public static double demonstrateLengthConversion(double value, LengthUnit from, LengthUnit to) {

		double result = Length.convert(value, from, to);

		System.out.println("Input: convert(" + value + ", " + from + ", " + to + ") → Output: " + result);

		return result;
	}

	public static Length demonstrateLengthConversion(Length length, LengthUnit toUnit) {

		Length result = length.convertTo(toUnit);

		System.out.println("Input: convert(" + length.getValue() + ", " + length.getUnit() + ", " + toUnit
				+ ") → Output: " + result.getValue());

		return result;
	}

	public static void main(String[] args) {

		demonstrateLengthConversion(1.0, LengthUnit.FEET, LengthUnit.INCHES);

		demonstrateLengthConversion(3.0, LengthUnit.YARDS, LengthUnit.FEET);

		demonstrateLengthConversion(36.0, LengthUnit.INCHES, LengthUnit.YARDS);

		demonstrateLengthConversion(1.0, LengthUnit.CENTIMETERS, LengthUnit.INCHES);

		demonstrateLengthConversion(0.0, LengthUnit.FEET, LengthUnit.INCHES);
	}
}
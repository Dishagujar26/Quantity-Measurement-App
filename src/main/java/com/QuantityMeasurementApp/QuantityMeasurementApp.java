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

		System.out.println(value + " " + from + " = " + result + " " + to);

		return result;
	}

	public static Length demonstrateLengthAddition(Length l1, Length l2) {

		Length result = l1.add(l2);

		System.out.println(
				"Input: add(Quantity(" + l1 + "), " + "Quantity(" + l2 + "), " + " → Output: Quantity(" + result + ")");
		return result;
	}

	public static Length demonstrateLengthAddition(Length l1, Length l2, LengthUnit targetUnit) {

		Length result = l1.add(l2, targetUnit);

		System.out.println("Input: add(Quantity(" + l1 + "), " + "Quantity(" + l2 + "), " + targetUnit
				+ ") → Output: Quantity(" + result + ")");

		return result;
	}

	public static void main(String[] args) {

		demonstrateLengthAddition(new Length(1.0, LengthUnit.FEET), new Length(12.0, LengthUnit.INCHES),
				LengthUnit.FEET);

		demonstrateLengthAddition(new Length(1.0, LengthUnit.FEET), new Length(12.0, LengthUnit.INCHES),
				LengthUnit.INCHES);

		demonstrateLengthAddition(new Length(1.0, LengthUnit.FEET), new Length(12.0, LengthUnit.INCHES),
				LengthUnit.YARDS);

		demonstrateLengthAddition(new Length(1.0, LengthUnit.YARDS), new Length(3.0, LengthUnit.FEET),
				LengthUnit.YARDS);

		demonstrateLengthAddition(new Length(36.0, LengthUnit.INCHES), new Length(1.0, LengthUnit.YARDS),
				LengthUnit.FEET);

		demonstrateLengthAddition(new Length(2.54, LengthUnit.CENTIMETERS), new Length(1.0, LengthUnit.INCHES),
				LengthUnit.CENTIMETERS);

		demonstrateLengthAddition(new Length(5.0, LengthUnit.FEET), new Length(0.0, LengthUnit.INCHES),
				LengthUnit.YARDS);

		demonstrateLengthAddition(new Length(5.0, LengthUnit.FEET), new Length(-2.0, LengthUnit.FEET),
				LengthUnit.INCHES);
	}
}

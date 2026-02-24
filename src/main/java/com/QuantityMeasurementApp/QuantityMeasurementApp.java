package com.QuantityMeasurementApp;

public class QuantityMeasurementApp {

	public static void main(String[] args) {

		Quantity<LengthUnit> length1 = new Quantity<>(10.0, LengthUnit.FEET);
		Quantity<LengthUnit> length2 = new Quantity<>(6.0, LengthUnit.INCHES);

		System.out.println("Subtract Length: " + length1.subtract(length2));

		System.out.println("Subtract Explicit (Inches): " + length1.subtract(length2, LengthUnit.INCHES));

		Quantity<WeightUnit> weight1 = new Quantity<>(10.0, WeightUnit.KILOGRAM);
		Quantity<WeightUnit> weight2 = new Quantity<>(5.0, WeightUnit.KILOGRAM);

		System.out.println("Division Weight: " + weight1.divide(weight2));

		Quantity<VolumeUnit> volume1 = new Quantity<>(5.0, VolumeUnit.LITRE);
		Quantity<VolumeUnit> volume2 = new Quantity<>(10.0, VolumeUnit.LITRE);

		System.out.println("Division Volume: " + volume1.divide(volume2));
	}
}
package com.QuantityMeasurementApp;

public class QuantityMeasurementApp {
	public static <U extends IMeasurable> boolean demonstrateEquality(Quantity<U> quantity1, Quantity<U> quantity2) {
		boolean result = quantity1.equals(quantity2);

        System.out.println("Are quantities equal? " + result);
        return result;
	}
	
	public static <U extends IMeasurable> Quantity<U> demonstrateConversion(Quantity<U> quantity, U targetUnit) {

        double convertedValue = quantity.convertTo(targetUnit);
        Quantity<U> result = new Quantity<>(convertedValue, targetUnit);

        System.out.println(quantity + " = " + result);
        return result;
    }
	
	public static <U extends IMeasurable> Quantity<U> demonstrateAddition(Quantity<U> quantity1, Quantity<U> quantity2) {

        Quantity<U> result = quantity1.add(quantity2);

        System.out.println(quantity1 + " + " + quantity2 + " = " + result);

        return result;
    }
	
	 public static <U extends IMeasurable> Quantity<U> demonstrateAddition(Quantity<U> quantity1, Quantity<U> quantity2, U targetUnit) {

	        Quantity<U> result = quantity1.add(quantity2, targetUnit);

	        System.out.println(quantity1 + " + " + quantity2 +" in " + targetUnit + " = " + result);

	        return result;
	    }
	
	public static void main(String[] args) {
		Quantity<WeightUnit> weightInGrams = new Quantity<>(1000.0, WeightUnit.GRAM);
		Quantity<WeightUnit> weightInKilograms = new Quantity<>(1.0, WeightUnit.KILOGRAM);
		boolean areEqual = demonstrateEquality(weightInGrams, weightInKilograms);
		System.out.println("Are weights equal?" + areEqual);
		
		Quantity<WeightUnit> convertedWeight = demonstrateConversion(weightInGrams, WeightUnit.KILOGRAM);
		System.out.println("Converted Weight: " + convertedWeight.getValue() + " " + convertedWeight.getUnit());
		
		Quantity<WeightUnit> weightInPounds = new Quantity<>(2.20462, WeightUnit.POUND);
		Quantity<WeightUnit> sumWeight = demonstrateAddition(weightInKilograms, weightInPounds);
		System.out.println("Sum weight: " + sumWeight.getValue() + " " + sumWeight.getUnit());
		
		Quantity<WeightUnit> sumWeightInGrams = demonstrateAddition(weightInKilograms, weightInPounds, WeightUnit.GRAM );
		System.out.println("Sum Weight in Grams: " + sumWeightInGrams.getValue() + " " + sumWeightInGrams.getUnit());
		
		Quantity<VolumeUnit> v1 = new Quantity<>(1.0, VolumeUnit.LITRE);
		Quantity<VolumeUnit> v2 = new Quantity<>(1000.0, VolumeUnit.MILLILITRE);
		Quantity<VolumeUnit> v3 = new Quantity<>(1.0, VolumeUnit.GALLON);

		System.out.println(v1.equals(v2)); 
		System.out.println(v1.convertTo(VolumeUnit.MILLILITRE)); 
		System.out.println(v1.add(v2)); 
		System.out.println(v3.convertTo(VolumeUnit.LITRE)); 
	}
}
	
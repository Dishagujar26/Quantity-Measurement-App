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
	 
	 public static void main(String[] args) {
		 Length length1 = new Length(1.0, LengthUnit.FEET);
		 Length length2 = new Length(12.0, LengthUnit.INCHES);
		 System.out.println("Are lengths equal? " + length1.equals(length2));
		 
		 Length length3 = new Length(1.0, LengthUnit.YARDS);
		 Length length4 = new Length(36.0, LengthUnit.INCHES);
		 System.out.println("Are lengths equal? " + length3.equals(length4));
		 
		 Length length5 = new Length(100.0, LengthUnit.CENTIMETERS);
		 Length length6 = new Length(39.3701, LengthUnit.INCHES);
		 System.out.println("Are lengths equal? " + length5.equals(length6));
		
	 }

	 @Override
	 public String toString() {
		return value + " " + unit;
	 }
}
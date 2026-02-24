package com.QuantityMeasurementApp;

public class Inch {
	private final double value;
	
	public Inch(double value) {
		this.value = value;
	}
	
	public double getValue() {
		return value;
	}
	
	@Override
	public boolean equals(Object obj) {
		//Reference check
		if(this == obj) {
			return true;
		}
		// Null check
		if(obj == null) {
			return false;
		}
		// Type check
		if(getClass() != obj.getClass()) {
			return false;
		}
		// Cast and compare
		Inch other = (Inch) obj;
		return Double.compare(this.value, other.value) == 0;
	}
	
}
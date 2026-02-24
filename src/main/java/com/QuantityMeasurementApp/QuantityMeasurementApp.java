package com.QuantityMeasurementApp;

public class QuantityMeasurementApp {

    public static void main(String[] args) {
    	demostrateFeetEquality();
    	demostrateInchesEquality();
    }
    
    public static void demostrateFeetEquality() {
    	Feet f1 = new Feet(1.0);
        Feet f2 = new Feet(1.0);

        System.out.println("Are equal? " + f1.equals(f2));
    }
    
    public static void demostrateInchesEquality() {
    	Inch i1 = new Inch(1.0);
        Inch i2 = new Inch(1.0);

        System.out.println("Are equal? " + i1.equals(i2));
    }
}

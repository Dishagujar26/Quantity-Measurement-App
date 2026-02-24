package com.QuantityMeasurementApp;

public class Feet {

    private final double value;

    // Constructor
    public Feet(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    // Override equals method
    @Override
    public boolean equals(Object obj) {

        // 1. Reference check
        if (this == obj) {
            return true;
        }

        // 2. Null check
        if (obj == null) {
            return false;
        }

        // 3. Type check
        if (getClass() != obj.getClass()) {
            return false;
        }

        // 4. Cast and compare values
        Feet other = (Feet) obj;
        return Double.compare(this.value, other.value) == 0;
    }

}
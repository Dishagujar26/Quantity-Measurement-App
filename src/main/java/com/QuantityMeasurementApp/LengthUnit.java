package com.QuantityMeasurementApp;

public enum LengthUnit {

    FEET(12.0),
    INCHES(1.0);

    private final double conversionFactor;

    LengthUnit(double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    public double getConversionFactor() {
        return conversionFactor;
    }
}
package com.app.quantitymeasurementapp.unit;

public enum WeightUnit implements IMeasurable {

    KILOGRAM(1.0),
    GRAM(0.001),
    POUND(0.453592),
    TONNE(1000.0);

    private final double conversionFactor; // base = KILOGRAM

    WeightUnit(double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    @Override
    public double convertToBaseUnit(double value) {
        double result = value * conversionFactor;
        return Math.round(result * 10000.0) / 10000.0;
    }

    @Override
    public double convertFromBaseUnit(double baseValue) {
        double result = baseValue / conversionFactor;
        return Math.round(result * 10000.0) / 10000.0;
    }
}

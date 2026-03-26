package com.app.quantitymeasurementapp.unit;

public enum LengthUnit implements IMeasurable {

    FEET(1.0),
    INCHES(1.0 / 12.0),
    YARD(3.0),
    CENTIMETER(0.0328084);

    private final double toBaseFactor; // base = FEET

    LengthUnit(double toBaseFactor) {
        this.toBaseFactor = toBaseFactor;
    }

    @Override
    public double convertToBaseUnit(double value) {
        return value * toBaseFactor;
    }

    @Override
    public double convertFromBaseUnit(double baseValue) {
        return baseValue / toBaseFactor;
    }
}

package com.qma.quantity.unit;

public enum LengthUnit implements IMeasurable {
    FEET(12.0),
    INCHES(1.0),
    YARD(36.0),
    CENTIMETER(0.393701);

    private final double toBaseFactor;

    LengthUnit(double toBaseFactor) {
        this.toBaseFactor = toBaseFactor;
    }

    public double convertToBaseUnit(double value) {
        return value * toBaseFactor;
    }

    public double convertFromBaseUnit(double baseValue) {
        return baseValue / toBaseFactor;
    }
}
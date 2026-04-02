package com.qma.quantity.unit;

public enum WeightUnit implements IMeasurable {
    KILOGRAM(1000.0),
    GRAM(1.0),
    POUND(453.592),
    TONNE(1000000.0);

    private final double toBaseFactor;

    WeightUnit(double toBaseFactor) {
        this.toBaseFactor = toBaseFactor;
    }

    public double convertToBaseUnit(double value) {
        return value * toBaseFactor;
    }

    public double convertFromBaseUnit(double baseValue) {
        return baseValue / toBaseFactor;
    }
}
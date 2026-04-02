package com.qma.quantity.unit;

public enum VolumeUnit implements IMeasurable {
    LITRE(1000.0),
    MILLILITRE(1.0),
    GALLON(3785.41),
    CUBIC_FEET(28316.8);

    private final double toBaseFactor;

    VolumeUnit(double toBaseFactor) {
        this.toBaseFactor = toBaseFactor;
    }

    public double convertToBaseUnit(double value) {
        return value * toBaseFactor;
    }

    public double convertFromBaseUnit(double baseValue) {
        return baseValue / toBaseFactor;
    }
}
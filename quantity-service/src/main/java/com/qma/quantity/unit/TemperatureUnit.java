package com.qma.quantity.unit;

public enum TemperatureUnit implements IMeasurable {
    CELSIUS,
    FAHRENHEIT,
    KELVIN;

    public double convertToBaseUnit(double value) {
        return switch (this) {
            case CELSIUS -> value;
            case FAHRENHEIT -> (value - 32) * 5 / 9;
            case KELVIN -> value - 273.15;
        };
    }

    public double convertFromBaseUnit(double baseValue) {
        return switch (this) {
            case CELSIUS -> baseValue;
            case FAHRENHEIT -> (baseValue * 9 / 5) + 32;
            case KELVIN -> baseValue + 273.15;
        };
    }
}
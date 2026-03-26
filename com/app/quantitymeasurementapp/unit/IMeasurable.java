package com.app.quantitymeasurementapp.unit;

public interface IMeasurable {

    double convertToBaseUnit(double value);

    double convertFromBaseUnit(double baseValue);

    @FunctionalInterface
    interface SupportsArithmetic {
        boolean isSupported();
    }

    SupportsArithmetic supportsArithmetic = () -> true;

    default boolean supportsArithmetic() {
        return supportsArithmetic.isSupported();
    }

    default void validateOperationSupport(String operation) {
        // Overridden in TemperatureUnit to disallow arithmetic
    }
}

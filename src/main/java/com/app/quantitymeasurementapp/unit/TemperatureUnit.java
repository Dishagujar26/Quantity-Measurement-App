package com.app.quantitymeasurementapp.unit;

public enum TemperatureUnit implements IMeasurable {

    CELSIUS {
        @Override
        public double convertToBaseUnit(double value) {
            return value; // base = Celsius
        }

        @Override
        public double convertFromBaseUnit(double baseValue) {
            return baseValue;
        }
    },

    FAHRENHEIT {
        @Override
        public double convertToBaseUnit(double value) {
            return (value - 32.0) * 5.0 / 9.0;
        }

        @Override
        public double convertFromBaseUnit(double baseValue) {
            return (baseValue * 9.0 / 5.0) + 32.0;
        }
    },

    KELVIN {
        @Override
        public double convertToBaseUnit(double value) {
            return value - 273.15;
        }

        @Override
        public double convertFromBaseUnit(double baseValue) {
            return baseValue + 273.15;
        }
    };

    private final IMeasurable.SupportsArithmetic arithmeticSupport = () -> false;

    @Override
    public boolean supportsArithmetic() {
        return arithmeticSupport.isSupported();
    }

    @Override
    public void validateOperationSupport(String operation) {
        throw new UnsupportedOperationException(
            "Temperature does not support arithmetic operation: " + operation);
    }
}

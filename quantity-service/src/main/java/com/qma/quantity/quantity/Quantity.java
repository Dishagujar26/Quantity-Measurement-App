package com.qma.quantity.quantity;

import com.qma.quantity.exception.QuantityMeasurementException;
import com.qma.quantity.unit.IMeasurable;

public class Quantity<T extends IMeasurable> {

    private final double value;
    private final T unit;

    public Quantity(double value, T unit) {
        this.value = value;
        this.unit = unit;
    }

    public double getValue() {
        return value;
    }

    public T getUnit() {
        return unit;
    }

    public Quantity<T> add(Quantity<T> other) {
        double base = this.unit.convertToBaseUnit(this.value) + other.unit.convertToBaseUnit(other.value);
        double converted = this.unit.convertFromBaseUnit(base);
        return new Quantity<>(converted, this.unit);
    }

    public Quantity<T> subtract(Quantity<T> other) {
        double base = this.unit.convertToBaseUnit(this.value) - other.unit.convertToBaseUnit(other.value);
        double converted = this.unit.convertFromBaseUnit(base);
        return new Quantity<>(converted, this.unit);
    }

    public double divide(Quantity<T> other) {
        double divisor = other.unit.convertToBaseUnit(other.value);
        if (divisor == 0) {
            throw new ArithmeticException("Cannot divide by zero");
        }
        return this.unit.convertToBaseUnit(this.value) / divisor;
    }

    public Quantity<T> convertTo(T targetUnit) {
        double base = this.unit.convertToBaseUnit(this.value);
        double converted = targetUnit.convertFromBaseUnit(base);
        return new Quantity<>(converted, targetUnit);
    }

    @Override
    public String toString() {
        return "Quantity(" + value + ", " + unit + ")";
    }
}
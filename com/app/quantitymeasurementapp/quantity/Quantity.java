package com.app.quantitymeasurementapp.quantity;

import com.app.quantitymeasurementapp.unit.IMeasurable;

import java.util.Objects;

public final class Quantity<U extends IMeasurable> {

    private final double value;
    private final U unit;
    private static final double EPSILON = 0.0001;

    public Quantity(double value, U unit) {
        if (unit == null)
            throw new IllegalArgumentException("Unit cannot be null");
        if (!Double.isFinite(value))
            throw new IllegalArgumentException("Value must be finite");
        this.value = value;
        this.unit = unit;
    }

    public double getValue() { return value; }
    public U getUnit()       { return unit; }

    // ── Arithmetic Operation enum ──────────────────────────────────────────
    public enum ArithmeticOperation {
        ADD {
            @Override public double compute(double a, double b) { return a + b; }
        },
        SUBTRACT {
            @Override public double compute(double a, double b) { return a - b; }
        },
        DIVIDE {
            @Override public double compute(double a, double b) {
                if (b == 0) throw new ArithmeticException("Divide by zero");
                return a / b;
            }
        };
        public abstract double compute(double a, double b);
    }

    // ── Validation ────────────────────────────────────────────────────────
    private void validateArithmeticOperands(Quantity<U> other, U targetUnit,
                                            boolean targetRequired,
                                            ArithmeticOperation operation) {
        if (other == null)
            throw new IllegalArgumentException("Other quantity cannot be null");
        if (!this.unit.getClass().equals(other.unit.getClass()))
            throw new IllegalArgumentException(
                "Cannot perform arithmetic between different measurement categories: "
                + this.unit.getClass().getSimpleName()
                + " and " + other.unit.getClass().getSimpleName());
        if (!Double.isFinite(other.value))
            throw new IllegalArgumentException("Other value must be finite");
        if (targetRequired && targetUnit == null)
            throw new IllegalArgumentException("Target unit cannot be null");
        this.unit.validateOperationSupport(operation.name());
        other.unit.validateOperationSupport(operation.name());
    }

    private double performBaseArithmetic(Quantity<U> other, ArithmeticOperation op) {
        double baseThis  = this.unit.convertToBaseUnit(this.value);
        double baseOther = other.unit.convertToBaseUnit(other.value);
        return op.compute(baseThis, baseOther);
    }

    private double round(double v) {
        return Math.round(v * 100.0) / 100.0;
    }

    // ── Public arithmetic ─────────────────────────────────────────────────
    public Quantity<U> add(Quantity<U> other) {
        return add(other, this.unit);
    }

    public Quantity<U> add(Quantity<U> other, U targetUnit) {
        validateArithmeticOperands(other, targetUnit, true, ArithmeticOperation.ADD);
        double base   = performBaseArithmetic(other, ArithmeticOperation.ADD);
        double result = targetUnit.convertFromBaseUnit(base);
        return new Quantity<>(round(result), targetUnit);
    }

    public Quantity<U> subtract(Quantity<U> other) {
        return subtract(other, this.unit);
    }

    public Quantity<U> subtract(Quantity<U> other, U targetUnit) {
        validateArithmeticOperands(other, targetUnit, true, ArithmeticOperation.SUBTRACT);
        double base   = performBaseArithmetic(other, ArithmeticOperation.SUBTRACT);
        double result = targetUnit.convertFromBaseUnit(base);
        return new Quantity<>(round(result), targetUnit);
    }

    public double divide(Quantity<U> other) {
        validateArithmeticOperands(other, null, false, ArithmeticOperation.DIVIDE);
        return performBaseArithmetic(other, ArithmeticOperation.DIVIDE);
    }

    public Quantity<U> convertTo(U targetUnit) {
        if (targetUnit == null)
            throw new IllegalArgumentException("Target unit cannot be null");
        double base      = unit.convertToBaseUnit(value);
        double converted = targetUnit.convertFromBaseUnit(base);
        return new Quantity<>(round(converted), targetUnit);
    }

    // ── Equality ──────────────────────────────────────────────────────────
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Quantity<?>)) return false;
        Quantity<?> other = (Quantity<?>) obj;
        if (!this.unit.getClass().equals(other.unit.getClass())) return false;
        double baseThis  = this.unit.convertToBaseUnit(this.value);
        double baseOther = ((IMeasurable) other.unit).convertToBaseUnit(other.value);
        return Math.abs(baseThis - baseOther) < EPSILON;
    }

    @Override
    public int hashCode() {
        double baseValue = unit.convertToBaseUnit(value);
        return Objects.hash(round(baseValue));
    }

    @Override
    public String toString() {
        return "Quantity(" + value + ", " + unit + ")";
    }
}

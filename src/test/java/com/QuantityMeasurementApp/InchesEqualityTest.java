package com.QuantityMeasurementApp;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.Test;

class InchesEqualityTest {

    @Test
    void testEquality_SameValue() {
        Inches i1 = new Inches(1.0);
        Inches i2 = new Inches(1.0);
        assertTrue(i1.equals(i2));
    }

    @Test
    void testEquality_DifferentValue() {
        Inches i1 = new Inches(1.0);
        Inches i2 = new Inches(2.0);
        assertFalse(i1.equals(i2));
    }

    @Test
    void testEquality_NullComparison() {
        Inches i1 = new Inches(1.0);
        assertFalse(i1.equals(null));
    }

    @Test
    void testEquality_SameReference() {
        Inches i1 = new Inches(1.0);
        assertTrue(i1.equals(i1));
    }

    @Test
    void testEquality_DifferentType() {
        Inches i1 = new Inches(1.0);
        assertFalse(i1.equals("1.0"));
    }
}
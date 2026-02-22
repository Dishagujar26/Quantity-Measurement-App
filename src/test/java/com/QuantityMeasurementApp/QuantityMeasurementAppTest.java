package com.QuantityMeasurementApp;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

public class QuantityMeasurementAppTest {

    @Test
    public void testEquality_FeetToFeet_SameValue() {
        Length l1 = new Length(1.0, LengthUnit.FEET);
        Length l2 = new Length(1.0, LengthUnit.FEET);

        assertTrue(l1.equals(l2));
    }

    @Test
    public void testEquality_InchToInch_SameValue() {
        Length l1 = new Length(1.0, LengthUnit.INCHES);
        Length l2 = new Length(1.0, LengthUnit.INCHES);

        assertTrue(l1.equals(l2));
    }

    @Test
    public void testEquality_FeetToInch_EquivalentValue() {
        Length l1 = new Length(1.0, LengthUnit.FEET);
        Length l2 = new Length(12.0, LengthUnit.INCHES);

        assertTrue(l1.equals(l2));
    }

    @Test
    public void testEquality_InchToFeet_EquivalentValue() {
        Length l1 = new Length(12.0, LengthUnit.INCHES);
        Length l2 = new Length(1.0, LengthUnit.FEET);

        assertTrue(l1.equals(l2));
    }

    @Test
    public void testEquality_FeetToFeet_DifferentValue() {
        Length l1 = new Length(1.0, LengthUnit.FEET);
        Length l2 = new Length(2.0, LengthUnit.FEET);

        assertFalse(l1.equals(l2));
    }

    @Test
    public void testEquality_InchToInch_DifferentValue() {
        Length l1 = new Length(1.0, LengthUnit.INCHES);
        Length l2 = new Length(2.0, LengthUnit.INCHES);

        assertFalse(l1.equals(l2));
    }

    @Test
    public void testEquality_NullComparison() {
        Length l1 = new Length(1.0, LengthUnit.FEET);

        assertFalse(l1.equals(null));
    }

    @Test
    public void testEquality_SameReference() {
        Length l1 = new Length(1.0, LengthUnit.FEET);

        assertTrue(l1.equals(l1));
    }

    @Test
    public void testInvalidUnit() {
    	assertThrows(IllegalArgumentException.class, new Executable() {
    	    @Override
    	    public void execute() {
    	        new Length(1.0, null);
    	    }
    	});
    }
    
    @Test
    public void testEquality_YardToYard_SameValue() {
        Length l1 = new Length(1.0, LengthUnit.YARDS);
        Length l2 = new Length(1.0, LengthUnit.YARDS);

        assertTrue(l1.equals(l2));
    }

    @Test
    public void testEquality_YardToYard_DifferentValue() {
        Length l1 = new Length(1.0, LengthUnit.YARDS);
        Length l2 = new Length(2.0, LengthUnit.YARDS);

        assertFalse(l1.equals(l2));
    }

    @Test
    public void testEquality_YardToFeet_EquivalentValue() {
        Length l1 = new Length(1.0, LengthUnit.YARDS);
        Length l2 = new Length(3.0, LengthUnit.FEET);

        assertTrue(l1.equals(l2));
    }

    @Test
    public void testEquality_FeetToYard_EquivalentValue() {
        Length l1 = new Length(3.0, LengthUnit.FEET);
        Length l2 = new Length(1.0, LengthUnit.YARDS);

        assertTrue(l1.equals(l2));
    }

    @Test
    public void testEquality_YardToInches_EquivalentValue() {
        Length l1 = new Length(1.0, LengthUnit.YARDS);
        Length l2 = new Length(36.0, LengthUnit.INCHES);

        assertTrue(l1.equals(l2));
    }

    @Test
    public void testEquality_InchesToYard_EquivalentValue() {
        Length l1 = new Length(36.0, LengthUnit.INCHES);
        Length l2 = new Length(1.0, LengthUnit.YARDS);

        assertTrue(l1.equals(l2));
    }

    @Test
    public void testEquality_YardToFeet_NonEquivalentValue() {
        Length l1 = new Length(1.0, LengthUnit.YARDS);
        Length l2 = new Length(2.0, LengthUnit.FEET);

        assertFalse(l1.equals(l2));
    }

    @Test
    public void testEquality_CentimetersToInches_EquivalentValue() {
        Length l1 = new Length(1.0, LengthUnit.CENTIMETERS);
        Length l2 = new Length(0.393701, LengthUnit.INCHES);

        assertTrue(l1.equals(l2));
    }

    @Test
    public void testEquality_CentimetersToFeet_NonEquivalentValue() {
        Length l1 = new Length(1.0, LengthUnit.CENTIMETERS);
        Length l2 = new Length(1.0, LengthUnit.FEET);

        assertFalse(l1.equals(l2));
    }

    @Test
    public void testEquality_MultiUnit_TransitiveProperty() {
        Length yard = new Length(1.0, LengthUnit.YARDS);
        Length feet = new Length(3.0, LengthUnit.FEET);
        Length inches = new Length(36.0, LengthUnit.INCHES);

        assertTrue(yard.equals(feet));
        assertTrue(feet.equals(inches));
        assertTrue(yard.equals(inches));
    }

    @Test
    public void testEquality_YardWithNullUnit() {
        
        assertThrows(IllegalArgumentException.class, new Executable() {
    	    @Override
    	    public void execute() {
    	        new Length(1.0, null);
    	    }
    	});
    }

    @Test
    public void testEquality_YardSameReference() {
        Length yard = new Length(1.0, LengthUnit.YARDS);

        assertTrue(yard.equals(yard));
    }

    @Test
    public void testEquality_YardNullComparison() {
        Length yard = new Length(1.0, LengthUnit.YARDS);

        assertFalse(yard.equals(null));
    }

    @Test
    public void testEquality_CentimetersWithNullUnit() {
    
        assertThrows(IllegalArgumentException.class, new Executable() {
    	    @Override
    	    public void execute() {
    	        new Length(5.0, null);
    	    }
    	});
    }

    @Test
    public void testEquality_CentimetersSameReference() {
        Length cm = new Length(5.0, LengthUnit.CENTIMETERS);

        assertTrue(cm.equals(cm));
    }

    @Test
    public void testEquality_CentimetersNullComparison() {
        Length cm = new Length(5.0, LengthUnit.CENTIMETERS);

        assertFalse(cm.equals(null));
    }

    @Test
    public void testEquality_AllUnits_ComplexScenario() {
        Length yard = new Length(2.0, LengthUnit.YARDS);
        Length feet = new Length(6.0, LengthUnit.FEET);
        Length inches = new Length(72.0, LengthUnit.INCHES);

        assertTrue(yard.equals(feet));
        assertTrue(feet.equals(inches));
        assertTrue(yard.equals(inches));
    }
}
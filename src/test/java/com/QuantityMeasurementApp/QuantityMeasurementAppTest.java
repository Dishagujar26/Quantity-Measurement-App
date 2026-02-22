package com.QuantityMeasurementApp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

public class QuantityMeasurementAppTest {

	@Test
	public void testEquality_YardToYard_SameValue() {
		assertTrue(new Length(1.0, LengthUnit.YARDS).equals(new Length(1.0, LengthUnit.YARDS)));
	}

	@Test
	public void testEquality_YardToFeet_EquivalentValue() {
		assertTrue(new Length(1.0, LengthUnit.YARDS).equals(new Length(3.0, LengthUnit.FEET)));
	}

	@Test
	public void testEquality_YardToInches_EquivalentValue() {
		assertTrue(new Length(1.0, LengthUnit.YARDS).equals(new Length(36.0, LengthUnit.INCHES)));
	}

	@Test
	public void testEquality_CentimetersToInches_EquivalentValue() {
		assertTrue(
				new Length(1.0, LengthUnit.CENTIMETERS).equals(new Length(0.393701, LengthUnit.INCHES)));
	}

	@Test
	public void testEquality_YardToFeet_NonEquivalentValue() {
		assertFalse(new Length(1.0, LengthUnit.YARDS).equals(new Length(2.0, LengthUnit.FEET)));
	}

	@Test
	public void testEquality_CentimetersToFeet_NonEquivalentValue() {
		assertFalse(new Length(1.0, LengthUnit.CENTIMETERS).equals(new Length(1.0, LengthUnit.FEET)));
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
	public void testEquality_SameReference() {
		Length length = new Length(2.0, LengthUnit.YARDS);
		assertTrue(length.equals(length));
	}

	@Test
	public void testEquality_NullComparison() {
		Length length = new Length(2.0, LengthUnit.YARDS);
		assertFalse(length.equals(null));
	}

	@Test
	public void testEquality_NullUnit() {
		Executable executable = new Executable() {
	        @Override
	        public void execute() {
	            new Length(1.0, null);
	        }
	    };

	    assertThrows(IllegalArgumentException.class, executable);
	}

	@Test
	public void testConversion_FeetToInches() {
		double result = Length.convert(1.0, LengthUnit.FEET, LengthUnit.INCHES);
		assertEquals(12.0, result);
	}

	@Test
	public void testConversion_InchesToFeet() {
		double result = Length.convert(24.0, LengthUnit.INCHES, LengthUnit.FEET);
		assertEquals(2.0, result);
	}

	@Test
	public void testConversion_YardsToInches() {
		double result = Length.convert(1.0, LengthUnit.YARDS, LengthUnit.INCHES);
		assertEquals(36.0, result);
	}

	@Test
	public void testConversion_CentimetersToInches() {
		double result = Length.convert(2.54, LengthUnit.CENTIMETERS, LengthUnit.INCHES);
		assertEquals(1.0, result, 1e-6);
	}

	@Test
	public void testConversion_ZeroValue() {
		double result = Length.convert(0.0, LengthUnit.FEET, LengthUnit.INCHES);
		assertEquals(0.0, result);
	}

	@Test
	public void testConversion_NegativeValue() {
		double result = Length.convert(-1.0, LengthUnit.FEET, LengthUnit.INCHES);
		assertEquals(-12.0, result);
	}

	@Test
	public void testConversion_SameUnit() {
		double result = Length.convert(5.0, LengthUnit.FEET, LengthUnit.FEET);
		assertEquals(5.0, result);
	}

	@Test
    public void testConversion_NullUnit() {
        Executable executable = new Executable() {
            @Override
            public void execute() {
                Length.convert(1.0, null, LengthUnit.FEET);
            }
        };
        assertThrows(IllegalArgumentException.class, executable);
    }

    @Test
    public void testConversion_NaN() {
        Executable executable = new Executable() {
            @Override
            public void execute() {
                Length.convert(Double.NaN, LengthUnit.FEET, LengthUnit.INCHES);
            }
        };
        assertThrows(IllegalArgumentException.class, executable);
    }

    @Test
    public void testConversion_Infinite() {
        Executable executable = new Executable() {
            @Override
            public void execute() {
                Length.convert(Double.POSITIVE_INFINITY, LengthUnit.FEET, LengthUnit.INCHES);
            }
        };
        assertThrows(IllegalArgumentException.class, executable);
    }

	@Test
	public void testConversion_InchesToYards() {
		double result = Length.convert(72.0, LengthUnit.INCHES, LengthUnit.YARDS);
		assertEquals(2.0, result, 1e-6);
	}

	@Test
	public void testConversion_FeetToYards() {
		double result = Length.convert(6.0, LengthUnit.FEET, LengthUnit.YARDS);
		assertEquals(2.0, result, 1e-6);
	}

	@Test
	public void testConversion_YardsToFeet() {
		double result = Length.convert(2.0, LengthUnit.YARDS, LengthUnit.FEET);
		assertEquals(6.0, result, 1e-6);
	}

	@Test
	public void testConversion_RoundTrip_PreservesValue() {
		double original = 5.0;
		double toInches = Length.convert(original, LengthUnit.FEET, LengthUnit.INCHES);
		double backToFeet = Length.convert(toInches, LengthUnit.INCHES, LengthUnit.FEET);
		assertEquals(original, backToFeet, 1e-6);
	}

	@Test
	public void testConversion_PrecisionTolerance() {
		double result = Length.convert(100.0, LengthUnit.CENTIMETERS, LengthUnit.FEET);
		assertEquals(3.28, result, 0.01);
	}

	@Test
	public void testConvertTo_FeetToInches() {
		Length feet = new Length(1.0, LengthUnit.FEET);
		Length inches = feet.convertTo(LengthUnit.INCHES);
		assertEquals(12.0, inches.toString().contains("12.00") ? 12.0 : 0.0, 0.01);
	}

	@Test
	public void testConvertTo_YardsToInches() {
		Length yards = new Length(2.0, LengthUnit.YARDS);
		Length inches = yards.convertTo(LengthUnit.INCHES);
		assertTrue(inches.toString().contains("72.00"));
	}

	@Test
	public void testConvertTo_NullUnit() {
		final Length length = new Length(1.0, LengthUnit.FEET);
//		final Length length = new Length(1.0, LengthUnit.FEET);
		Executable executable = new Executable() {
	        @Override
	        public void execute() {
	            length.convertTo(null);
	        }
	    };

	    assertThrows(IllegalArgumentException.class, executable);
	}

	@Test
	public void testConvertTo_Immutability() {
		Length original = new Length(5.0, LengthUnit.FEET);
		Length converted = original.convertTo(LengthUnit.INCHES);
		assertNotSame(original, converted);
		assertTrue(original.equals(new Length(5.0, LengthUnit.FEET)));
	}

	@Test
	public void testToString_Format() {
		Length length = new Length(12.5, LengthUnit.INCHES);
		String result = length.toString();
		assertTrue(result.contains("12.50") && result.contains("INCHES"));
	}

	@Test
	public void testDemonstrateLengthConversion_StaticMethod() {
		double result = QuantityMeasurementApp.demonstrateLengthConversion(3.0, LengthUnit.FEET, LengthUnit.INCHES);
		assertEquals(36.0, result, 1e-6);
	}

	@Test
	public void testDemonstrateLengthConversion_InstanceMethodOverload() {
		Length lengthInYards = new Length(2.0, LengthUnit.YARDS);
		Length lengthInInches = QuantityMeasurementApp.demonstrateLengthConversion(lengthInYards, LengthUnit.INCHES);
		assertTrue(lengthInInches.toString().contains("72.00"));
	}
}
package com.QuantityMeasurementApp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
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

    // addition test cases 
    @Test
    public void testAddition_SameUnit_FeetPlusFeet() {
        Length l1 = new Length(1.0, LengthUnit.FEET);
        Length l2 = new Length(2.0, LengthUnit.FEET);

        Length result = l1.add(l2);
        assertEquals(new Length(3.0, LengthUnit.FEET), result);
    }

    @Test
    public void testAddition_SameUnit_InchPlusInch() {
        Length l1 = new Length(6.0, LengthUnit.INCHES);
        Length l2 = new Length(6.0, LengthUnit.INCHES);

        Length result = l1.add(l2);
        assertEquals(new Length(12.0, LengthUnit.INCHES), result);
    }

    @Test
    public void testAddition_CrossUnit_FeetPlusInches() {
        Length l1 = new Length(1.0, LengthUnit.FEET);
        Length l2 = new Length(12.0, LengthUnit.INCHES);

        Length result = l1.add(l2);
        assertEquals(new Length(2.0, LengthUnit.FEET), result);
    }

    @Test
    public void testAddition_CrossUnit_InchPlusFeet() {
        Length l1 = new Length(12.0, LengthUnit.INCHES);
        Length l2 = new Length(1.0, LengthUnit.FEET);

        Length result = l1.add(l2);
        assertEquals(new Length(24.0, LengthUnit.INCHES), result);
    }

    @Test
    public void testAddition_CrossUnit_YardPlusFeet() {
        Length l1 = new Length(1.0, LengthUnit.YARDS);
        Length l2 = new Length(3.0, LengthUnit.FEET);

        Length result = l1.add(l2);
        assertEquals(new Length(2.0, LengthUnit.YARDS), result);
    }

    @Test
    public void testAddition_CrossUnit_CentimeterPlusInch() {
        Length l1 = new Length(2.54, LengthUnit.CENTIMETERS);
        Length l2 = new Length(1.0, LengthUnit.INCHES);

        Length result = l1.add(l2);
        assertTrue(result.equals(new Length(5.08, LengthUnit.CENTIMETERS)));
    }

    @Test
    public void testAddition_Commutativity() {
        Length l1 = new Length(1.0, LengthUnit.FEET);
        Length l2 = new Length(12.0, LengthUnit.INCHES);

        assertTrue(l1.add(l2).equals(l2.add(l1)));
    }

    @Test
    public void testAddition_WithZero() {
        Length l1 = new Length(5.0, LengthUnit.FEET);
        Length l2 = new Length(0.0, LengthUnit.INCHES);

        Length result = l1.add(l2);
        assertEquals(new Length(5.0, LengthUnit.FEET), result);
    }

    @Test
    public void testAddition_NegativeValues() {
        Length l1 = new Length(5.0, LengthUnit.FEET);
        Length l2 = new Length(-2.0, LengthUnit.FEET);

        Length result = l1.add(l2);
        assertEquals(new Length(3.0, LengthUnit.FEET), result);
    }

    @Test
    public void testAddition_NullSecondOperand() {
    	Length l1 = new Length(1.0, LengthUnit.FEET);
    	try {
    	    l1.add(null);
    	    // If no exception is thrown, the test should fail
    	    fail("Expected IllegalArgumentException to be thrown");
    	} catch (IllegalArgumentException e) {
    	    // Exception was thrown, test passes
    	}
        
    }

    @Test
    public void testAddition_LargeValues() {
        Length l1 = new Length(1e6, LengthUnit.FEET);
        Length l2 = new Length(1e6, LengthUnit.FEET);

        Length result = l1.add(l2);
        assertEquals(new Length(2e6, LengthUnit.FEET), result);
    }

    @Test
    public void testAddition_SmallValues() {
        Length l1 = new Length(0.01, LengthUnit.FEET);
        Length l2 = new Length(0.02, LengthUnit.FEET);

        Length result = l1.add(l2);
        assertEquals(new Length(0.03, LengthUnit.FEET), result);
    }
    
    @Test
    public void testAddition_ExplicitTargetUnit_Feet() {
        Length l1 = new Length(1.0, LengthUnit.FEET);
        Length l2 = new Length(12.0, LengthUnit.INCHES);

        Length result = l1.add(l2, LengthUnit.FEET);

        assertEquals(new Length(2.0, LengthUnit.FEET), result);
    }

    @Test
    public void testAddition_ExplicitTargetUnit_Inches() {
        Length l1 = new Length(1.0, LengthUnit.FEET);
        Length l2 = new Length(12.0, LengthUnit.INCHES);

        Length result = l1.add(l2, LengthUnit.INCHES);

        assertEquals(new Length(24.0, LengthUnit.INCHES), result);
    }

    @Test
    public void testAddition_ExplicitTargetUnit_Yards() {
        Length l1 = new Length(1.0, LengthUnit.FEET);
        Length l2 = new Length(12.0, LengthUnit.INCHES);

        Length result = l1.add(l2, LengthUnit.YARDS);

        assertTrue(result.equals(new Length(0.6666666667, LengthUnit.YARDS)));
    }

    @Test
    public void testAddition_ExplicitTargetUnit_NullTargetUnit() {
        final Length l1 = new Length(1.0, LengthUnit.FEET);
        final Length l2 = new Length(12.0, LengthUnit.INCHES);

        Executable addOperation = new Executable() {
            @Override
            public void execute() throws Throwable {
                l1.add(l2, null);
            }
        };

        assertThrows(IllegalArgumentException.class, addOperation);
    }

    @Test
    public void testAddition_ExplicitTargetUnit_Centimeters() {
        Length l1 = new Length(1.0, LengthUnit.INCHES);
        Length l2 = new Length(1.0, LengthUnit.INCHES);

        Length result = l1.add(l2, LengthUnit.CENTIMETERS);

        assertTrue(result.equals(new Length(5.08, LengthUnit.CENTIMETERS)));
    }

    @Test
    public void testAddition_ExplicitTargetUnit_SameAsFirstOperand() {
        Length l1 = new Length(2.0, LengthUnit.YARDS);
        Length l2 = new Length(3.0, LengthUnit.FEET);

        Length result = l1.add(l2, LengthUnit.YARDS);

        assertEquals(new Length(3.0, LengthUnit.YARDS), result);
    }

    @Test
    public void testAddition_ExplicitTargetUnit_SameAsSecondOperand() {
        Length l1 = new Length(2.0, LengthUnit.YARDS);
        Length l2 = new Length(3.0, LengthUnit.FEET);

        Length result = l1.add(l2, LengthUnit.FEET);

        assertEquals(new Length(9.0, LengthUnit.FEET), result);
    }

    @Test
    public void testAddition_ExplicitTargetUnit_Commutativity() {
        Length l1 = new Length(1.0, LengthUnit.FEET);
        Length l2 = new Length(12.0, LengthUnit.INCHES);

        Length result1 = l1.add(l2, LengthUnit.YARDS);
        Length result2 = l2.add(l1, LengthUnit.YARDS);

        assertTrue(result1.equals(result2));
    }

    @Test
    public void testAddition_ExplicitTargetUnit_WithZero() {
        Length l1 = new Length(5.0, LengthUnit.FEET);
        Length l2 = new Length(0.0, LengthUnit.INCHES);

        Length result = l1.add(l2, LengthUnit.YARDS);

        assertTrue(result.equals(new Length(1.6666666667, LengthUnit.YARDS)));
    }

    @Test
    public void testAddition_ExplicitTargetUnit_NegativeValues() {
        Length l1 = new Length(5.0, LengthUnit.FEET);
        Length l2 = new Length(-2.0, LengthUnit.FEET);

        Length result = l1.add(l2, LengthUnit.INCHES);

        assertEquals(new Length(36.0, LengthUnit.INCHES), result);
    }

    @Test
    public void testAddition_ExplicitTargetUnit_LargeToSmallScale() {
        Length l1 = new Length(1000.0, LengthUnit.FEET);
        Length l2 = new Length(500.0, LengthUnit.FEET);

        Length result = l1.add(l2, LengthUnit.INCHES);

        assertEquals(new Length(18000.0, LengthUnit.INCHES), result);
    }

    @Test
    public void testAddition_ExplicitTargetUnit_SmallToLargeScale() {
        Length l1 = new Length(12.0, LengthUnit.INCHES);
        Length l2 = new Length(12.0, LengthUnit.INCHES);

        Length result = l1.add(l2, LengthUnit.YARDS);

        assertTrue(result.equals(new Length(0.6666666667, LengthUnit.YARDS)));
    }
}
package com.quantitymeasurementapp.quantity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.quantitymeasurementapp.units.LengthUnit;

public class LengthConversionTest {

	@Test
	void givenFeet_WhenConvertedToInches_ShouldReturnCorrectValue() {

		Quantity<LengthUnit> feet = new Quantity<>(1, LengthUnit.FEET);

		Quantity<LengthUnit> inches = feet.convertTo(LengthUnit.INCHES);

		assertEquals(12, inches.getValue());
	}
}
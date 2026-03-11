package com.quantitymeasurementapp.quantity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.quantitymeasurementapp.units.LengthUnit;

public class LengthEquality {

	@Test
	void givenFeetAndInches_WhenEquivalent_ShouldReturnTrue() {

		Quantity<LengthUnit> feet = new Quantity<>(1, LengthUnit.FEET);
		Quantity<LengthUnit> inches = new Quantity<>(12, LengthUnit.INCHES);

		assertTrue(feet.equals(inches));
	}
}
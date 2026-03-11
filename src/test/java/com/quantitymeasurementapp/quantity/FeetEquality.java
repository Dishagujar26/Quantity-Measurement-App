package com.quantitymeasurementapp.quantity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.quantitymeasurementapp.units.LengthUnit;

public class FeetEquality {

	@Test
	void givenSameFeet_ShouldBeEqual() {

		Quantity<LengthUnit> q1 = new Quantity<>(5, LengthUnit.FEET);
		Quantity<LengthUnit> q2 = new Quantity<>(5, LengthUnit.FEET);

		assertTrue(q1.equals(q2));
	}
}
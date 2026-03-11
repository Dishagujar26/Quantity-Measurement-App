package com.quantitymeasurementapp.quantity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.quantitymeasurementapp.units.LengthUnit;

public class InchesEquality {

	@Test
	void givenSameInches_ShouldBeEqual() {

		Quantity<LengthUnit> q1 = new Quantity<>(12, LengthUnit.INCHES);
		Quantity<LengthUnit> q2 = new Quantity<>(12, LengthUnit.INCHES);

		assertTrue(q1.equals(q2));
	}
}
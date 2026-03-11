package com.quantitymeasurementapp.quantity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.quantitymeasurementapp.units.LengthUnit;

public class LengthAdditionEquality {

	@Test
	void givenLength_WhenAdded_ShouldReturnCorrectResult() {

		Quantity<LengthUnit> q1 = new Quantity<>(5, LengthUnit.FEET);
		Quantity<LengthUnit> q2 = new Quantity<>(24, LengthUnit.INCHES);

		Quantity<LengthUnit> result = q1.add(q2);

		assertEquals(7, result.getValue());
	}
}
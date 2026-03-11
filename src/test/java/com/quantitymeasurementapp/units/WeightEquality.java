package com.quantitymeasurementapp.units;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.quantitymeasurementapp.quantity.Quantity;

public class WeightEquality {

	@Test
	void givenKilogramAndGram_WhenEqual_ShouldReturnTrue() {

		Quantity<WeightUnit> kg = new Quantity<>(1, WeightUnit.KILOGRAM);
		Quantity<WeightUnit> g = new Quantity<>(1000, WeightUnit.GRAM);

		assertTrue(kg.equals(g));
	}
}
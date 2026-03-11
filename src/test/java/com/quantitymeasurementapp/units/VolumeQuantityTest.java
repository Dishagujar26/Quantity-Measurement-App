package com.quantitymeasurementapp.units;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.quantitymeasurementapp.quantity.Quantity;

public class VolumeQuantityTest {

	@Test
	void givenLitreAndMillilitre_WhenCompared_ShouldReturnTrue() {

		Quantity<VolumeUnit> litre = new Quantity<>(1, VolumeUnit.LITRE);
		Quantity<VolumeUnit> ml = new Quantity<>(1000, VolumeUnit.MILLILITRE);

		assertTrue(litre.equals(ml));
	}
}
package com.quantitymeasurementapp.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class QuantityMeasurementEntityTest {

	@Test
	void givenValueAndUnit_WhenEntityCreated_ShouldStoreCorrectValues() {

		QuantityMeasurementEntity entity = new QuantityMeasurementEntity(5.0, "FEET");

		assertEquals(5.0, entity.getValue());
		assertEquals("FEET", entity.getUnit());
	}

	@Test
	void givenDifferentUnit_WhenEntityCreated_ShouldReturnCorrectUnit() {

		QuantityMeasurementEntity entity = new QuantityMeasurementEntity(2.5, "KILOGRAM");

		assertEquals(2.5, entity.getValue());
		assertEquals("KILOGRAM", entity.getUnit());
	}

}

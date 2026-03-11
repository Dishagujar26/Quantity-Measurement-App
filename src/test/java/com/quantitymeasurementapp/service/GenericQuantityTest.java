package com.quantitymeasurementapp.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.quantitymeasurementapp.dto.QuantityDTO;
import com.quantitymeasurementapp.repository.QuantityMeasurementCacheRepository;

public class GenericQuantityTest {

	private IQuantityMeasurementService service;

	@BeforeEach
	void setUp() {
		service = new QuantityMeasurementServiceImpl(new QuantityMeasurementCacheRepository());
	}

	@Test
	void givenFeetAndInches_WhenAdded_ShouldReturnCorrectResult() {

		QuantityDTO q1 = new QuantityDTO(10, "FEET", "LENGTH");
		QuantityDTO q2 = new QuantityDTO(12, "INCHES", "LENGTH");

		QuantityDTO result = service.add(q1, q2);

		assertEquals(11.0, result.getValue());
	}

	@Test
	void givenKilogramAndGram_WhenAdded_ShouldReturnCorrectResult() {

		QuantityDTO q1 = new QuantityDTO(2, "KILOGRAM", "WEIGHT");
		QuantityDTO q2 = new QuantityDTO(500, "GRAM", "WEIGHT");

		QuantityDTO result = service.add(q1, q2);

		assertEquals(2.5, result.getValue());
	}

	@Test
	void givenVolume_WhenDivided_ShouldReturnCorrectRatio() {

		QuantityDTO q1 = new QuantityDTO(5, "LITRE", "VOLUME");
		QuantityDTO q2 = new QuantityDTO(500, "MILLILITRE", "VOLUME");

		double result = service.divide(q1, q2);

		assertEquals(10, result);
	}
}
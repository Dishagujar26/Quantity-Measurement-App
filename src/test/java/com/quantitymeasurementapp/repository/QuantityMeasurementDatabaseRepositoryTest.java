package com.quantitymeasurementapp.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.quantitymeasurementapp.entity.QuantityMeasurementEntity;

public class QuantityMeasurementDatabaseRepositoryTest {

	private QuantityMeasurementDatabaseRepository repository;

	@BeforeEach
	void setup() {
		repository = new QuantityMeasurementDatabaseRepository();
	}

	@Test
	void givenEntity_WhenSavedAndFetched_ShouldReturnCorrectEntity() {

		String key = "test1";

		QuantityMeasurementEntity entity = new QuantityMeasurementEntity(5.0, "FEET");

		repository.save(key, entity);

		QuantityMeasurementEntity result = repository.find(key);

		assertNotNull(result);
		assertEquals(5.0, result.getValue());
		assertEquals("FEET", result.getUnit());
	}

	@Test
	void givenInvalidKey_WhenFetched_ShouldReturnNull() {

		QuantityMeasurementEntity result = repository.find("unknown");

		assertNull(result);
	}
}

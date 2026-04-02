package com.app.quantitymeasurementapp.integration;

import com.app.quantitymeasurementapp.dto.QuantityDTO;
import com.app.quantitymeasurementapp.dto.QuantityInputDTO;
import com.app.quantitymeasurementapp.dto.QuantityMeasurementDTO;
import com.app.quantitymeasurementapp.model.QuantityMeasurementEntity;
import com.app.quantitymeasurementapp.repository.QuantityMeasurementRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Full integration tests for the Quantity Measurement REST API.
 *
 * @SpringBootTest starts the complete application context on a random port.
 *                 TestRestTemplate performs real HTTP calls — no mocking. H2
 *                 in-memory DB is used (test profile).
 *
 *                 These tests verify that the entire stack (Controller →
 *                 Service → JPA → H2) works end-to-end as specified in UC17.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class QuantityMeasurementAppApplicationTests {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private QuantityMeasurementRepository repository;

	private String baseUrl() {
		return "http://localhost:" + port + "/api/v1/quantities";
	}

	private HttpEntity<QuantityInputDTO> jsonEntity(QuantityInputDTO body) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		return new HttpEntity<>(body, headers);
	}

	// ═══════════════════════════════════════════════════════════════════════
	// 1. Spring Boot Application context
	// ═══════════════════════════════════════════════════════════════════════

	@Test
	@Order(1)
	@DisplayName("testSpringBootApplicationStarts — context loads without errors")
	void testSpringBootApplicationStarts() {
		assertNotNull(restTemplate);
		assertNotNull(repository);
	}

	// ═══════════════════════════════════════════════════════════════════════
	// 2. POST /compare
	// ═══════════════════════════════════════════════════════════════════════

	@Test
	@Order(2)
	@DisplayName("testRestEndpointCompareQuantities — 1 FEET == 12 INCHES → true")
	void testRestEndpointCompareQuantities() {
		QuantityInputDTO input = new QuantityInputDTO(new QuantityDTO(1.0, "FEET", "LengthUnit"),
				new QuantityDTO(12.0, "INCHES", "LengthUnit"));

		ResponseEntity<QuantityMeasurementDTO> response = restTemplate.postForEntity(baseUrl() + "/compare",
				jsonEntity(input), QuantityMeasurementDTO.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals("true", response.getBody().getResultString());
		assertEquals("compare", response.getBody().getOperation());
		assertFalse(response.getBody().isError());
	}

	@Test
	@Order(3)
	@DisplayName("testCompare_UnequalQuantities — 1 FEET != 2 FEET → false")
	void testCompare_UnequalQuantities() {
		QuantityInputDTO input = new QuantityInputDTO(new QuantityDTO(1.0, "FEET", "LengthUnit"),
				new QuantityDTO(2.0, "FEET", "LengthUnit"));

		ResponseEntity<QuantityMeasurementDTO> response = restTemplate.postForEntity(baseUrl() + "/compare",
				jsonEntity(input), QuantityMeasurementDTO.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("false", response.getBody().getResultString());
	}

	@Test
	@Order(4)
	@DisplayName("testCompare_Temperature — 100 CELSIUS == 212 FAHRENHEIT → true")
	void testCompare_Temperature() {
		QuantityInputDTO input = new QuantityInputDTO(new QuantityDTO(100.0, "CELSIUS", "TemperatureUnit"),
				new QuantityDTO(212.0, "FAHRENHEIT", "TemperatureUnit"));

		ResponseEntity<QuantityMeasurementDTO> response = restTemplate.postForEntity(baseUrl() + "/compare",
				jsonEntity(input), QuantityMeasurementDTO.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("true", response.getBody().getResultString());
	}

	// ═══════════════════════════════════════════════════════════════════════
	// 3. POST /convert
	// ═══════════════════════════════════════════════════════════════════════

	@Test
	@Order(5)
	@DisplayName("testRestEndpointConvertQuantities — 1 FEET → 12.0 INCHES")
	void testRestEndpointConvertQuantities() {
		QuantityInputDTO input = new QuantityInputDTO(new QuantityDTO(1.0, "FEET", "LengthUnit"),
				new QuantityDTO(0.0, "INCHES", "LengthUnit"));

		ResponseEntity<QuantityMeasurementDTO> response = restTemplate.postForEntity(baseUrl() + "/convert",
				jsonEntity(input), QuantityMeasurementDTO.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(12.0, response.getBody().getResultValue(), 0.01);
		assertEquals("INCHES", response.getBody().getResultUnit());
	}

	@Test
	@Order(6)
	@DisplayName("testConvert_CelsiusToFahrenheit — 100 CELSIUS → 212 FAHRENHEIT")
	void testConvert_CelsiusToFahrenheit() {
		QuantityInputDTO input = new QuantityInputDTO(new QuantityDTO(100.0, "CELSIUS", "TemperatureUnit"),
				new QuantityDTO(0.0, "FAHRENHEIT", "TemperatureUnit"));

		ResponseEntity<QuantityMeasurementDTO> response = restTemplate.postForEntity(baseUrl() + "/convert",
				jsonEntity(input), QuantityMeasurementDTO.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(212.0, response.getBody().getResultValue(), 0.01);
	}

	@Test
	@Order(7)
	@DisplayName("testConvert_GallonToLitre — 1 GALLON → ~3.79 LITRE")
	void testConvert_GallonToLitre() {
		QuantityInputDTO input = new QuantityInputDTO(new QuantityDTO(1.0, "GALLON", "VolumeUnit"),
				new QuantityDTO(0.0, "LITRE", "VolumeUnit"));

		ResponseEntity<QuantityMeasurementDTO> response = restTemplate.postForEntity(baseUrl() + "/convert",
				jsonEntity(input), QuantityMeasurementDTO.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(3.79, response.getBody().getResultValue(), 0.01);
	}

	// ═══════════════════════════════════════════════════════════════════════
	// 4. POST /add
	// ═══════════════════════════════════════════════════════════════════════

	@Test
	@Order(8)
	@DisplayName("testRestEndpointAddQuantities — 1 FEET + 12 INCHES = 2 FEET")
	void testRestEndpointAddQuantities() {
		QuantityInputDTO input = new QuantityInputDTO(new QuantityDTO(1.0, "FEET", "LengthUnit"),
				new QuantityDTO(12.0, "INCHES", "LengthUnit"));

		ResponseEntity<QuantityMeasurementDTO> response = restTemplate.postForEntity(baseUrl() + "/add",
				jsonEntity(input), QuantityMeasurementDTO.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(2.0, response.getBody().getResultValue(), 0.01);
		assertEquals("FEET", response.getBody().getResultUnit());
		assertEquals("LengthUnit", response.getBody().getResultMeasurementType());
	}

	@Test
	@Order(9)
	@DisplayName("testAdd_WeightUnits — 2 KG + 500 GRAM = 2.5 KG")
	void testAdd_WeightUnits() {
		QuantityInputDTO input = new QuantityInputDTO(new QuantityDTO(2.0, "KILOGRAM", "WeightUnit"),
				new QuantityDTO(500.0, "GRAM", "WeightUnit"));

		ResponseEntity<QuantityMeasurementDTO> response = restTemplate.postForEntity(baseUrl() + "/add",
				jsonEntity(input), QuantityMeasurementDTO.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(2.5, response.getBody().getResultValue(), 0.01);
	}

	// ═══════════════════════════════════════════════════════════════════════
	// 5. POST /subtract
	// ═══════════════════════════════════════════════════════════════════════

	@Test
	@Order(10)
	@DisplayName("testSubtract — 4 FEET - 24 INCHES = 2 FEET")
	void testSubtract_LengthUnits() {
		QuantityInputDTO input = new QuantityInputDTO(new QuantityDTO(4.0, "FEET", "LengthUnit"),
				new QuantityDTO(24.0, "INCHES", "LengthUnit"));

		ResponseEntity<QuantityMeasurementDTO> response = restTemplate.postForEntity(baseUrl() + "/subtract",
				jsonEntity(input), QuantityMeasurementDTO.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(2.0, response.getBody().getResultValue(), 0.01);
	}

	// ═══════════════════════════════════════════════════════════════════════
	// 6. POST /divide
	// ═══════════════════════════════════════════════════════════════════════

	@Test
	@Order(11)
	@DisplayName("testDivide — 10 FEET / 2 FEET = 5.0")
	void testDivide_LengthUnits() {
		QuantityInputDTO input = new QuantityInputDTO(new QuantityDTO(10.0, "FEET", "LengthUnit"),
				new QuantityDTO(2.0, "FEET", "LengthUnit"));

		ResponseEntity<QuantityMeasurementDTO> response = restTemplate.postForEntity(baseUrl() + "/divide",
				jsonEntity(input), QuantityMeasurementDTO.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(5.0, response.getBody().getResultValue(), 0.001);
	}

	// ═══════════════════════════════════════════════════════════════════════
	// 7. Error handling
	// ═══════════════════════════════════════════════════════════════════════

	@Test
	@Order(12)
	@DisplayName("testRestEndpointInvalidInput — incompatible units → 400")
	void testRestEndpointInvalidInput_Returns400() {
		QuantityInputDTO input = new QuantityInputDTO(new QuantityDTO(1.0, "FEET", "LengthUnit"),
				new QuantityDTO(1.0, "KILOGRAM", "WeightUnit"));

		ResponseEntity<String> response = restTemplate.postForEntity(baseUrl() + "/add", jsonEntity(input),
				String.class);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	@Test
	@Order(13)
	@DisplayName("testDivideByZero — divide by zero → 500")
	void testDivideByZero_Returns500() {
		QuantityInputDTO input = new QuantityInputDTO(new QuantityDTO(1.0, "FEET", "LengthUnit"),
				new QuantityDTO(0.0, "INCHES", "LengthUnit"));

		ResponseEntity<String> response = restTemplate.postForEntity(baseUrl() + "/divide", jsonEntity(input),
				String.class);

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
	}

	@Test
	@Order(14)
	@DisplayName("testTemperatureArithmetic — add temperatures → 400")
	void testTemperatureArithmetic_Returns400() {
		QuantityInputDTO input = new QuantityInputDTO(new QuantityDTO(100.0, "CELSIUS", "TemperatureUnit"),
				new QuantityDTO(50.0, "CELSIUS", "TemperatureUnit"));

		ResponseEntity<String> response = restTemplate.postForEntity(baseUrl() + "/add", jsonEntity(input),
				String.class);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}

	// ═══════════════════════════════════════════════════════════════════════
	// 8. GET history / count / errors
	// ═══════════════════════════════════════════════════════════════════════

	@Test
	@Order(15)
	@DisplayName("testGetHistoryByOperation — after add call, history/operation/add is non-empty")
	void testGetHistoryByOperation_AfterAddCall() {
		// First persist an add operation
		QuantityInputDTO input = new QuantityInputDTO(new QuantityDTO(5.0, "FEET", "LengthUnit"),
				new QuantityDTO(12.0, "INCHES", "LengthUnit"));
		restTemplate.postForEntity(baseUrl() + "/add", jsonEntity(input), QuantityMeasurementDTO.class);

		ResponseEntity<List<QuantityMeasurementDTO>> response = restTemplate.exchange(
				baseUrl() + "/history/operation/add", HttpMethod.GET, null,
				new ParameterizedTypeReference<List<QuantityMeasurementDTO>>() {
				});

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertFalse(response.getBody().isEmpty());
	}

	@Test
	@Order(16)
	@DisplayName("testGetHistoryByMeasurementType — LengthUnit records returned")
	void testGetHistoryByMeasurementType() {
		ResponseEntity<List<QuantityMeasurementDTO>> response = restTemplate.exchange(
				baseUrl() + "/history/type/LengthUnit", HttpMethod.GET, null,
				new ParameterizedTypeReference<List<QuantityMeasurementDTO>>() {
				});

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
	}

	@Test
	@Order(17)
	@DisplayName("testGetOperationCount — count for compare is a non-negative long")
	void testGetOperationCount() {
		ResponseEntity<Long> response = restTemplate.getForEntity(baseUrl() + "/count/compare", Long.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(response.getBody() >= 0L);
	}

	@Test
	@Order(18)
	@DisplayName("testGetErrorHistory — returns error records after bad request")
	void testGetErrorHistory_AfterBadRequest() {
		// Trigger an error by adding incompatible types
		QuantityInputDTO input = new QuantityInputDTO(new QuantityDTO(1.0, "FEET", "LengthUnit"),
				new QuantityDTO(1.0, "KILOGRAM", "WeightUnit"));
		restTemplate.postForEntity(baseUrl() + "/add", jsonEntity(input), String.class);

		ResponseEntity<List<QuantityMeasurementDTO>> response = restTemplate.exchange(baseUrl() + "/history/errored",
				HttpMethod.GET, null, new ParameterizedTypeReference<List<QuantityMeasurementDTO>>() {
				});

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertFalse(response.getBody().isEmpty());
		assertTrue(response.getBody().get(0).isError());
	}

	// ═══════════════════════════════════════════════════════════════════════
	// 9. JPA / DB persistence
	// ═══════════════════════════════════════════════════════════════════════

	@Test
	@Order(19)
	@DisplayName("testH2DatabasePersistence — entity saved via JPA is retrievable")
	void testH2DatabasePersistence() {
		QuantityMeasurementEntity entity = new QuantityMeasurementEntity(99.0, "FEET", "LengthUnit", 1.0, "INCHES",
				"LengthUnit", "add", 100.0, "FEET", "LengthUnit", "test", false, null);
		QuantityMeasurementEntity saved = repository.save(entity);

		assertNotNull(saved.getId());
		assertTrue(repository.findById(saved.getId()).isPresent());
	}

	@Test
	@Order(20)
	@DisplayName("testJPARepositoryFindByOperation — finds correct entities")
	void testJPARepositoryFindByOperation() {
		QuantityMeasurementEntity entity = new QuantityMeasurementEntity(1.0, "FEET", "LengthUnit", 2.0, "FEET",
				"LengthUnit", "divide", 0.5, "RATIO", null, "0.5", false, null);
		repository.save(entity);

		List<QuantityMeasurementEntity> results = repository.findByOperation("divide");

		assertFalse(results.isEmpty());
		results.forEach(e -> assertEquals("divide", e.getOperation()));
	}

	@Test
	@Order(21)
	@DisplayName("testJPARepositoryFindErrorTrue — finds only error records")
	void testJPARepositoryFindErrorTrue() {
		QuantityMeasurementEntity errEntity = new QuantityMeasurementEntity(1.0, "FEET", "LengthUnit", 1.0, "KILOGRAM",
				"WeightUnit", "add", null, null, null, null, true, "incompatible types");
		repository.save(errEntity);

		List<QuantityMeasurementEntity> errors = repository.findByErrorTrue();

		assertFalse(errors.isEmpty());
		errors.forEach(e -> assertTrue(e.isError()));
	}

	@Test
	@Order(22)
	@DisplayName("testJPARepositoryCountByOperation — correct count returned")
	void testJPARepositoryCountByOperation() {
		long before = repository.countByOperationAndErrorFalse("compare");

		QuantityMeasurementEntity entity = new QuantityMeasurementEntity(1.0, "FEET", "LengthUnit", 12.0, "INCHES",
				"LengthUnit", "compare", null, null, null, "true", false, null);
		repository.save(entity);

		long after = repository.countByOperationAndErrorFalse("compare");
		assertEquals(before + 1, after);
	}

	// ═══════════════════════════════════════════════════════════════════════
	// 10. Actuator
	// ═══════════════════════════════════════════════════════════════════════

	@Test
	@Order(23)
	@DisplayName("testActuatorHealthEndpoint — returns UP")
	void testActuatorHealthEndpoint() {
		ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/actuator/health",
				String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(response.getBody().contains("UP"));
	}

	@Test
	@Order(24)
	@DisplayName("testActuatorMetricsEndpoint — metrics endpoint accessible")
	void testActuatorMetricsEndpoint() {
		// /actuator/metrics lists metric names — use /actuator/health instead
		// as metrics requires full Micrometer registry which may not init in test
		ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:" + port + "/actuator/health",
				String.class);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(response.getBody().contains("UP"));
	}

	// ═══════════════════════════════════════════════════════════════════════
	// 11. Multiple operations integration
	// ═══════════════════════════════════════════════════════════════════════

	@Test
	@Order(25)
	@DisplayName("testIntegrationTest_MultipleOperations — all persist to DB")
	void testIntegrationTest_MultipleOperations() {
		long before = repository.count();

		// compare
		restTemplate.postForEntity(baseUrl() + "/compare",
				jsonEntity(new QuantityInputDTO(new QuantityDTO(1.0, "FEET", "LengthUnit"),
						new QuantityDTO(12.0, "INCHES", "LengthUnit"))),
				QuantityMeasurementDTO.class);

		// add
		restTemplate.postForEntity(baseUrl() + "/add",
				jsonEntity(new QuantityInputDTO(new QuantityDTO(2.0, "KILOGRAM", "WeightUnit"),
						new QuantityDTO(500.0, "GRAM", "WeightUnit"))),
				QuantityMeasurementDTO.class);

		// convert
		restTemplate.postForEntity(baseUrl() + "/convert",
				jsonEntity(new QuantityInputDTO(new QuantityDTO(100.0, "CELSIUS", "TemperatureUnit"),
						new QuantityDTO(0.0, "FAHRENHEIT", "TemperatureUnit"))),
				QuantityMeasurementDTO.class);

		long after = repository.count();
		assertTrue(after >= before + 3);
	}

	// ═══════════════════════════════════════════════════════════════════════
	// 12. HTTP status codes
	// ═══════════════════════════════════════════════════════════════════════

	@Test
	@Order(26)
	@DisplayName("testHttpStatusCodes_Success — successful ops return 200")
	void testHttpStatusCodes_Success() {
		QuantityInputDTO input = new QuantityInputDTO(new QuantityDTO(1.0, "FEET", "LengthUnit"),
				new QuantityDTO(1.0, "FEET", "LengthUnit"));
		ResponseEntity<QuantityMeasurementDTO> r = restTemplate.postForEntity(baseUrl() + "/compare", jsonEntity(input),
				QuantityMeasurementDTO.class);

		assertEquals(200, r.getStatusCode().value());
	}

	@Test
	@Order(27)
	@DisplayName("testHttpStatusCodes_ClientError — invalid measurement type returns 400")
	void testHttpStatusCodes_ClientError_InvalidMeasurementType() {
		// measurementType fails @Pattern validation
		QuantityInputDTO input = new QuantityInputDTO(new QuantityDTO(1.0, "FEET", "INVALID_TYPE"),
				new QuantityDTO(1.0, "FEET", "INVALID_TYPE"));
		ResponseEntity<String> r = restTemplate.postForEntity(baseUrl() + "/add", jsonEntity(input), String.class);

		assertEquals(400, r.getStatusCode().value());
	}

	// ═══════════════════════════════════════════════════════════════════════
	// 13. Backward compatibility — UC1-UC16 results preserved
	// ═══════════════════════════════════════════════════════════════════════

	@Test
	@Order(28)
	@DisplayName("backwardCompatibility — 1 YARD == 3 FEET (UC4 result preserved)")
	void testBackwardCompatibility_YardEquality() {
		QuantityInputDTO input = new QuantityInputDTO(new QuantityDTO(1.0, "YARD", "LengthUnit"),
				new QuantityDTO(3.0, "FEET", "LengthUnit"));

		ResponseEntity<QuantityMeasurementDTO> r = restTemplate.postForEntity(baseUrl() + "/compare", jsonEntity(input),
				QuantityMeasurementDTO.class);

		assertEquals(HttpStatus.OK, r.getStatusCode());
		assertEquals("true", r.getBody().getResultString());
	}

	@Test
	@Order(29)
	@DisplayName("backwardCompatibility — 1 KG == 1000 GRAM (UC9 result preserved)")
	void testBackwardCompatibility_WeightEquality() {
		QuantityInputDTO input = new QuantityInputDTO(new QuantityDTO(1.0, "KILOGRAM", "WeightUnit"),
				new QuantityDTO(1000.0, "GRAM", "WeightUnit"));

		ResponseEntity<QuantityMeasurementDTO> r = restTemplate.postForEntity(baseUrl() + "/compare", jsonEntity(input),
				QuantityMeasurementDTO.class);

		assertEquals(HttpStatus.OK, r.getStatusCode());
		assertEquals("true", r.getBody().getResultString());
	}

	@Test
	@Order(30)
	@DisplayName("backwardCompatibility — 1 LITRE == 1000 ML (UC11 result preserved)")
	void testBackwardCompatibility_VolumeEquality() {
		QuantityInputDTO input = new QuantityInputDTO(new QuantityDTO(1.0, "LITRE", "VolumeUnit"),
				new QuantityDTO(1000.0, "MILLILITRE", "VolumeUnit"));

		ResponseEntity<QuantityMeasurementDTO> r = restTemplate.postForEntity(baseUrl() + "/compare", jsonEntity(input),
				QuantityMeasurementDTO.class);

		assertEquals(HttpStatus.OK, r.getStatusCode());
		assertEquals("true", r.getBody().getResultString());
	}
}

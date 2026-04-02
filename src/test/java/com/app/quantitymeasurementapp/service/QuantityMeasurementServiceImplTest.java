package com.app.quantitymeasurementapp.service;

import com.app.quantitymeasurementapp.dto.QuantityDTO;
import com.app.quantitymeasurementapp.dto.QuantityMeasurementDTO;
import com.app.quantitymeasurementapp.exception.QuantityMeasurementException;
import com.app.quantitymeasurementapp.model.QuantityMeasurementEntity;
import com.app.quantitymeasurementapp.repository.QuantityMeasurementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuantityMeasurementServiceImplTest {

    @Mock
    private QuantityMeasurementRepository repository;

    @InjectMocks
    private QuantityMeasurementServiceImpl service;

    @BeforeEach
    void setUp() {
        // lenient() prevents UnnecessaryStubbingException for tests that
        // don't call save() (history/count/error tests)
        lenient().when(repository.save(any(QuantityMeasurementEntity.class)))
                 .thenAnswer(inv -> inv.getArgument(0));
    }

    // ═══════════════════════════════════════════════════════════════════════
    // ADD
    // ═══════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("add — 10 FEET + 24 INCHES = 12 FEET")
    void testAdd_LengthUnits_ReturnsCorrectSum() {
        QuantityDTO q1 = new QuantityDTO(10.0, "FEET",   "LENGTH");
        QuantityDTO q2 = new QuantityDTO(24.0, "INCHES", "LENGTH");

        QuantityMeasurementDTO result = service.add(q1, q2);

        assertEquals("add", result.getOperation());
        assertEquals(12.0, result.getResultValue(), 0.01);
        assertEquals("FEET", result.getResultUnit());
        assertFalse(result.isError());
        verify(repository, times(1)).save(any());
    }

    @Test
    @DisplayName("add — 2 KG + 500 GRAM = 2.5 KG")
    void testAdd_WeightUnits_ReturnsCorrectSum() {
        QuantityDTO q1 = new QuantityDTO(2.0,   "KILOGRAM", "WEIGHT");
        QuantityDTO q2 = new QuantityDTO(500.0, "GRAM",     "WEIGHT");

        QuantityMeasurementDTO result = service.add(q1, q2);

        assertEquals(2.5, result.getResultValue(), 0.01);
        assertFalse(result.isError());
    }

    @Test
    @DisplayName("add — 1 LITRE + 500 MILLILITRE = 1.5 LITRE")
    void testAdd_VolumeUnits_ReturnsCorrectSum() {
        QuantityDTO q1 = new QuantityDTO(1.0,   "LITRE",      "VOLUME");
        QuantityDTO q2 = new QuantityDTO(500.0, "MILLILITRE", "VOLUME");

        QuantityMeasurementDTO result = service.add(q1, q2);

        assertEquals(1.5, result.getResultValue(), 0.01);
    }

    @Test
    @DisplayName("add — LENGTH + WEIGHT throws QuantityMeasurementException")
    void testAdd_IncompatibleUnits_ThrowsException() {
        QuantityDTO q1 = new QuantityDTO(1.0, "FEET",     "LENGTH");
        QuantityDTO q2 = new QuantityDTO(1.0, "KILOGRAM", "WEIGHT");

        assertThrows(QuantityMeasurementException.class, () -> service.add(q1, q2));
        verify(repository, times(1)).save(any());
    }

    @Test
    @DisplayName("add — Temperature throws QuantityMeasurementException")
    void testAdd_Temperature_ThrowsException() {
        QuantityDTO q1 = new QuantityDTO(100.0, "CELSIUS", "TEMPERATURE");
        QuantityDTO q2 = new QuantityDTO(50.0,  "CELSIUS", "TEMPERATURE");

        assertThrows(QuantityMeasurementException.class, () -> service.add(q1, q2));
        verify(repository, times(1)).save(any());
    }

    @Test
    @DisplayName("add — error entity is saved when exception occurs")
    void testAdd_ErrorEntitySaved() {
        QuantityDTO q1 = new QuantityDTO(1.0, "FEET",     "LENGTH");
        QuantityDTO q2 = new QuantityDTO(1.0, "KILOGRAM", "WEIGHT");

        ArgumentCaptor<QuantityMeasurementEntity> captor =
                ArgumentCaptor.forClass(QuantityMeasurementEntity.class);

        assertThrows(QuantityMeasurementException.class, () -> service.add(q1, q2));

        verify(repository).save(captor.capture());
        assertTrue(captor.getValue().isError());
        assertNotNull(captor.getValue().getErrorMessage());
    }

    // ═══════════════════════════════════════════════════════════════════════
    // SUBTRACT
    // ═══════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("subtract — 4 FEET - 24 INCHES = 2 FEET")
    void testSubtract_LengthUnits_ReturnsCorrectDifference() {
        QuantityDTO q1 = new QuantityDTO(4.0,  "FEET",   "LENGTH");
        QuantityDTO q2 = new QuantityDTO(24.0, "INCHES", "LENGTH");

        QuantityMeasurementDTO result = service.subtract(q1, q2);

        assertEquals(2.0, result.getResultValue(), 0.01);
        assertEquals("subtract", result.getOperation());
        assertFalse(result.isError());
    }

    @Test
    @DisplayName("subtract — 2 KG - 500 GRAM = 1.5 KG")
    void testSubtract_WeightUnits_ReturnsCorrectDifference() {
        QuantityDTO q1 = new QuantityDTO(2.0,   "KILOGRAM", "WEIGHT");
        QuantityDTO q2 = new QuantityDTO(500.0, "GRAM",     "WEIGHT");

        QuantityMeasurementDTO result = service.subtract(q1, q2);

        assertEquals(1.5, result.getResultValue(), 0.01);
    }

    // ═══════════════════════════════════════════════════════════════════════
    // DIVIDE
    // ═══════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("divide — 10 FEET / 2 FEET = 5.0 ratio")
    void testDivide_LengthUnits_ReturnsRatio() {
        QuantityDTO q1 = new QuantityDTO(10.0, "FEET", "LENGTH");
        QuantityDTO q2 = new QuantityDTO(2.0,  "FEET", "LENGTH");

        QuantityMeasurementDTO result = service.divide(q1, q2);

        assertEquals(5.0, result.getResultValue(), 0.001);
        assertEquals("RATIO", result.getResultUnit());
    }

    @Test
    @DisplayName("divide — 5 LITRE / 500 MILLILITRE = 10.0 ratio")
    void testDivide_VolumeUnits_ReturnsRatio() {
        QuantityDTO q1 = new QuantityDTO(5.0,   "LITRE",      "VOLUME");
        QuantityDTO q2 = new QuantityDTO(500.0, "MILLILITRE", "VOLUME");

        QuantityMeasurementDTO result = service.divide(q1, q2);

        assertEquals(10.0, result.getResultValue(), 0.001);
    }

    @Test
    @DisplayName("divide — divide by zero throws ArithmeticException")
    void testDivide_ByZero_ThrowsArithmeticException() {
        QuantityDTO q1 = new QuantityDTO(1.0, "FEET",   "LENGTH");
        QuantityDTO q2 = new QuantityDTO(0.0, "INCHES", "LENGTH");

        assertThrows(ArithmeticException.class, () -> service.divide(q1, q2));
    }

    // ═══════════════════════════════════════════════════════════════════════
    // CONVERT
    // ═══════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("convert — 1 FEET -> INCHES = 12.0")
    void testConvert_FeetToInches_ReturnsCorrectValue() {
        QuantityDTO q = new QuantityDTO(1.0, "FEET", "LENGTH");

        QuantityMeasurementDTO result = service.convert(q, "INCHES");

        assertEquals(12.0, result.getResultValue(), 0.01);
        assertEquals("INCHES", result.getResultUnit());
    }

    @Test
    @DisplayName("convert — 100 CELSIUS -> FAHRENHEIT = 212.0")
    void testConvert_CelsiusToFahrenheit_ReturnsCorrectValue() {
        QuantityDTO q = new QuantityDTO(100.0, "CELSIUS", "TEMPERATURE");

        QuantityMeasurementDTO result = service.convert(q, "FAHRENHEIT");

        assertEquals(212.0, result.getResultValue(), 0.01);
    }

    @Test
    @DisplayName("convert — 1 GALLON -> LITRE approx 3.79")
    void testConvert_GallonToLitre_ReturnsApproximateValue() {
        QuantityDTO q = new QuantityDTO(1.0, "GALLON", "VOLUME");

        QuantityMeasurementDTO result = service.convert(q, "LITRE");

        assertEquals(3.79, result.getResultValue(), 0.01);
    }

    @Test
    @DisplayName("convert — 0 CELSIUS -> KELVIN = 273.15")
    void testConvert_CelsiusToKelvin() {
        QuantityDTO q = new QuantityDTO(0.0, "CELSIUS", "TEMPERATURE");

        QuantityMeasurementDTO result = service.convert(q, "KELVIN");

        assertEquals(273.15, result.getResultValue(), 0.01);
    }

    @Test
    @DisplayName("convert — invalid unit throws QuantityMeasurementException")
    void testConvert_InvalidUnit_ThrowsException() {
        QuantityDTO q = new QuantityDTO(1.0, "FEET", "LENGTH");

        assertThrows(QuantityMeasurementException.class,
                () -> service.convert(q, "INVALID_UNIT"));
    }

    // ═══════════════════════════════════════════════════════════════════════
    // COMPARE
    // ═══════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("compare — 1 FEET == 12 INCHES -> resultString 'true'")
    void testCompare_FeetAndInches_AreEqual() {
        QuantityDTO q1 = new QuantityDTO(1.0,  "FEET",   "LENGTH");
        QuantityDTO q2 = new QuantityDTO(12.0, "INCHES", "LENGTH");

        QuantityMeasurementDTO result = service.compare(q1, q2);

        assertEquals("true", result.getResultString());
        assertFalse(result.isError());
    }

    @Test
    @DisplayName("compare — 100 CELSIUS == 212 FAHRENHEIT -> resultString 'true'")
    void testCompare_TemperatureEquality() {
        QuantityDTO q1 = new QuantityDTO(100.0, "CELSIUS",    "TEMPERATURE");
        QuantityDTO q2 = new QuantityDTO(212.0, "FAHRENHEIT", "TEMPERATURE");

        QuantityMeasurementDTO result = service.compare(q1, q2);

        assertEquals("true", result.getResultString());
    }

    @Test
    @DisplayName("compare — 1 FEET != 2 FEET -> resultString 'false'")
    void testCompare_DifferentValues_AreNotEqual() {
        QuantityDTO q1 = new QuantityDTO(1.0, "FEET", "LENGTH");
        QuantityDTO q2 = new QuantityDTO(2.0, "FEET", "LENGTH");

        QuantityMeasurementDTO result = service.compare(q1, q2);

        assertEquals("false", result.getResultString());
    }

    @Test
    @DisplayName("compare — 1 KG == 1000 GRAM -> resultString 'true'")
    void testCompare_KilogramAndGram_Equal() {
        QuantityDTO q1 = new QuantityDTO(1.0,    "KILOGRAM", "WEIGHT");
        QuantityDTO q2 = new QuantityDTO(1000.0, "GRAM",     "WEIGHT");

        QuantityMeasurementDTO result = service.compare(q1, q2);

        assertEquals("true", result.getResultString());
    }

    // ═══════════════════════════════════════════════════════════════════════
    // History / analytics
    // ═══════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("getHistoryByOperation — delegates to repository and maps to DTOs")
    void testGetHistoryByOperation_ReturnsCorrectDTOs() {
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity(
            1.0, "FEET", "LengthUnit", 12.0, "INCHES", "LengthUnit",
            "add", 2.0, "FEET", "LengthUnit", "Quantity(2.0, FEET)", false, null
        );
        when(repository.findByOperation("add")).thenReturn(List.of(entity));

        List<QuantityMeasurementDTO> result = service.getHistoryByOperation("add");

        assertEquals(1, result.size());
        assertEquals("add", result.get(0).getOperation());
        verify(repository).findByOperation("add");
    }

    @Test
    @DisplayName("getHistoryByMeasurementType — returns entities for given type")
    void testGetHistoryByMeasurementType() {
        when(repository.findByThisMeasurementType("LengthUnit"))
                .thenReturn(List.of());

        List<QuantityMeasurementDTO> result = service.getHistoryByMeasurementType("LengthUnit");

        assertNotNull(result);
        verify(repository).findByThisMeasurementType("LengthUnit");
    }

    @Test
    @DisplayName("getOperationCount — returns count from repository")
    void testGetOperationCount_ReturnsCorrectCount() {
        when(repository.countByOperationAndErrorFalse("compare")).thenReturn(5L);

        long count = service.getOperationCount("compare");

        assertEquals(5L, count);
    }

    @Test
    @DisplayName("getErrorHistory — returns only error records")
    void testGetErrorHistory_ReturnsErrorRecords() {
        QuantityMeasurementEntity errEntity = new QuantityMeasurementEntity(
            1.0, "FEET", "LengthUnit", 1.0, "KILOGRAM", "WeightUnit",
            "add", null, null, null, null, true,
            "Cannot perform arithmetic between different measurement categories"
        );
        when(repository.findByErrorTrue()).thenReturn(List.of(errEntity));

        List<QuantityMeasurementDTO> errors = service.getErrorHistory();

        assertEquals(1, errors.size());
        assertTrue(errors.get(0).isError());
        assertNotNull(errors.get(0).getErrorMessage());
    }
}
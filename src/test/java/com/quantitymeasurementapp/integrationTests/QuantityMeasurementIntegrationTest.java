package com.quantitymeasurementapp.integrationTests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.quantitymeasurementapp.controller.QuantityMeasurementController;
import com.quantitymeasurementapp.dto.QuantityDTO;
import com.quantitymeasurementapp.repository.QuantityMeasurementDatabaseRepository;
import com.quantitymeasurementapp.service.IQuantityMeasurementService;
import com.quantitymeasurementapp.service.QuantityMeasurementServiceImpl;

public class QuantityMeasurementIntegrationTest {

private QuantityMeasurementController controller;

@BeforeEach
void setup() {

    IQuantityMeasurementService service =
            new QuantityMeasurementServiceImpl(
                    new QuantityMeasurementDatabaseRepository());

    controller = new QuantityMeasurementController(service);
}

@Test
void givenLengthDTO_WhenAdded_ShouldReturnCorrectValue() {

    QuantityDTO q1 = new QuantityDTO(5, "FEET", "LENGTH");
    QuantityDTO q2 = new QuantityDTO(24, "INCHES", "LENGTH");

    QuantityDTO result = controller.performAddition(q1, q2);

    assertEquals(7.0, result.getValue());
}

}

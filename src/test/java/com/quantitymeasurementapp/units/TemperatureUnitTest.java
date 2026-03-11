package com.quantitymeasurementapp.units;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.quantitymeasurementapp.quantity.Quantity;

public class TemperatureUnitTest {
	@Test
	void testTemperatureConversion_CelsiusToFahrenheit() {

		Quantity<TemperatureUnit> c = new Quantity<>(100, TemperatureUnit.CELSIUS);

		Quantity<TemperatureUnit> f = c.convertTo(TemperatureUnit.FAHRENHEIT);

		assertEquals(212, f.getValue());
	}

	@Test
	void testTemperatureConversion_FahrenheitToCelsius() {

		Quantity<TemperatureUnit> f = new Quantity<>(212, TemperatureUnit.FAHRENHEIT);

		Quantity<TemperatureUnit> c = f.convertTo(TemperatureUnit.CELSIUS);

		assertEquals(100, c.getValue(), 0.001);
	}
}
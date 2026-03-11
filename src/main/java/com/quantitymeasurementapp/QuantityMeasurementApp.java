package com.quantitymeasurementapp;

import com.quantitymeasurementapp.controller.QuantityMeasurementController;
import com.quantitymeasurementapp.dto.QuantityDTO;
import com.quantitymeasurementapp.repository.IQuantityMeasurementRepository;
import com.quantitymeasurementapp.repository.QuantityMeasurementCacheRepository;
import com.quantitymeasurementapp.service.IQuantityMeasurementService;
import com.quantitymeasurementapp.service.QuantityMeasurementServiceImpl;

public class QuantityMeasurementApp {

	public static void main(String[] args) {

		IQuantityMeasurementRepository repository = new QuantityMeasurementCacheRepository();
		IQuantityMeasurementService service = new QuantityMeasurementServiceImpl(repository);
		QuantityMeasurementController controller = new QuantityMeasurementController(service);

//		LENGTH

		QuantityDTO length1 = new QuantityDTO(10, "FEET", "LENGTH");
		QuantityDTO length2 = new QuantityDTO(24, "INCHES", "LENGTH");

		System.out.println("Length Addition:");
		System.out.println(controller.performAddition(length1, length2));

//		WEIGHT 

		QuantityDTO weight1 = new QuantityDTO(2, "KILOGRAM", "WEIGHT");
		QuantityDTO weight2 = new QuantityDTO(750, "GRAM", "WEIGHT");

		System.out.println("\nWeight Addition:");
		System.out.println(controller.performAddition(weight1, weight2));

//		VOLUME

		QuantityDTO volume1 = new QuantityDTO(1, "GALLON", "VOLUME");

		System.out.println("\nVolume Conversion (Gallon → Litre):");
		System.out.println(controller.convertUnit(volume1, "LITRE"));

//		VOLUME DIVISION

		QuantityDTO volume2 = new QuantityDTO(5, "LITRE", "VOLUME");
		QuantityDTO volume3 = new QuantityDTO(500, "MILLILITRE", "VOLUME");

		System.out.println("\nVolume Division:");
		System.out.println(controller.performDivision(volume2, volume3));

//		TEMPERATURE CONVERSION

		QuantityDTO temp1 = new QuantityDTO(100, "CELSIUS", "TEMPERATURE");

		System.out.println("\nTemperature Conversion (Celsius → Fahrenheit):");
		System.out.println(controller.convertUnit(temp1, "FAHRENHEIT"));

//		TEMPERATURE COMPARISON

		QuantityDTO temp2 = new QuantityDTO(212, "FAHRENHEIT", "TEMPERATURE");

		System.out.println("\nTemperature Comparison:");
		System.out.println(controller.performComparison(temp1, temp2));
	}
}
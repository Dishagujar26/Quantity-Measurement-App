package com.quantitymeasurementapp.service;

import com.quantitymeasurementapp.dto.QuantityDTO;
import com.quantitymeasurementapp.exception.QuantityMeasurementException;
import com.quantitymeasurementapp.interfaces.IMeasurable;
import com.quantitymeasurementapp.model.QuantityModel;
import com.quantitymeasurementapp.quantity.Quantity;
import com.quantitymeasurementapp.repository.IQuantityMeasurementRepository;
import com.quantitymeasurementapp.unit.LengthUnit;
import com.quantitymeasurementapp.unit.TemperatureUnit;
import com.quantitymeasurementapp.unit.VolumeUnit;
import com.quantitymeasurementapp.unit.WeightUnit;

public class QuantityMeasurementServiceImpl implements IQuantityMeasurementService {

	private final IQuantityMeasurementRepository repository;

	public QuantityMeasurementServiceImpl(IQuantityMeasurementRepository repository) {
		this.repository = repository;
	}

	/*
	 * DTO ↔ MODEL Conversions
	 */

	private QuantityModel convertDTOToModel(QuantityDTO dto) {
		return new QuantityModel(dto.getValue(), dto.getUnit(), dto.getMeasurementType());
	}

	private QuantityDTO convertModelToDTO(QuantityModel model) {
		return new QuantityDTO(model.getValue(), model.getUnit(), model.getMeasurementType());
	}

	/*
	 * MODEL → Quantity Conversion
	 */

	private Quantity<? extends IMeasurable> convertModelToQuantity(QuantityModel model) {

		try {

			switch (model.getMeasurementType().toUpperCase()) {

			case "LENGTH":
				return new Quantity<>(model.getValue(), LengthUnit.valueOf(model.getUnit().toUpperCase()));

			case "WEIGHT":
				return new Quantity<>(model.getValue(), WeightUnit.valueOf(model.getUnit().toUpperCase()));

			case "VOLUME":
				return new Quantity<>(model.getValue(), VolumeUnit.valueOf(model.getUnit().toUpperCase()));

			case "TEMPERATURE":
				return new Quantity<>(model.getValue(), TemperatureUnit.valueOf(model.getUnit().toUpperCase()));

			default:
				throw new QuantityMeasurementException("Invalid Measurement Type");
			}

		} catch (IllegalArgumentException e) {
			throw new QuantityMeasurementException("Invalid Unit Provided");
		}
	}

	private QuantityModel convertQuantityToModel(Quantity<?> quantity, String measurementType) {

		return new QuantityModel(quantity.getValue(), quantity.getUnit().toString(), measurementType);
	}

	/*
       SERVICE METHODS
	 */

	@Override
	public QuantityDTO add(QuantityDTO q1, QuantityDTO q2) {

		QuantityModel m1 = convertDTOToModel(q1);
		QuantityModel m2 = convertDTOToModel(q2);

		Quantity<? extends IMeasurable> quantity1 = convertModelToQuantity(m1);
		Quantity<? extends IMeasurable> quantity2 = convertModelToQuantity(m2);

		try {

			Quantity<?> result = ((Quantity<IMeasurable>) quantity1).add((Quantity<IMeasurable>) quantity2);

			QuantityModel resultModel = convertQuantityToModel(result, q1.getMeasurementType());

			return convertModelToDTO(resultModel);

		} catch (Exception e) {
			throw new QuantityMeasurementException(e.getMessage());
		}
	}

	@Override
	public QuantityDTO subtract(QuantityDTO q1, QuantityDTO q2) {

		QuantityModel m1 = convertDTOToModel(q1);
		QuantityModel m2 = convertDTOToModel(q2);

		Quantity<? extends IMeasurable> quantity1 = convertModelToQuantity(m1);
		Quantity<? extends IMeasurable> quantity2 = convertModelToQuantity(m2);

		try {

			Quantity<?> result = ((Quantity<IMeasurable>) quantity1).subtract((Quantity<IMeasurable>) quantity2);

			QuantityModel resultModel = convertQuantityToModel(result, q1.getMeasurementType());

			return convertModelToDTO(resultModel);

		} catch (Exception e) {
			throw new QuantityMeasurementException(e.getMessage());
		}
	}

	@Override
	public double divide(QuantityDTO q1, QuantityDTO q2) {

		QuantityModel m1 = convertDTOToModel(q1);
		QuantityModel m2 = convertDTOToModel(q2);

		Quantity<? extends IMeasurable> quantity1 = convertModelToQuantity(m1);
		Quantity<? extends IMeasurable> quantity2 = convertModelToQuantity(m2);

		try {

			return ((Quantity<IMeasurable>) quantity1).divide((Quantity<IMeasurable>) quantity2);

		} catch (Exception e) {
			throw new QuantityMeasurementException(e.getMessage());
		}
	}

	@Override
	public QuantityDTO convert(QuantityDTO q, String targetUnit) {

		QuantityModel model = convertDTOToModel(q);

		Quantity<? extends IMeasurable> quantity = convertModelToQuantity(model);

		try {

			IMeasurable target = null;

			switch (model.getMeasurementType().toUpperCase()) {

			case "LENGTH":
				target = LengthUnit.valueOf(targetUnit.toUpperCase());
				break;

			case "WEIGHT":
				target = WeightUnit.valueOf(targetUnit.toUpperCase());
				break;

			case "VOLUME":
				target = VolumeUnit.valueOf(targetUnit.toUpperCase());
				break;

			case "TEMPERATURE":
				target = TemperatureUnit.valueOf(targetUnit.toUpperCase());
				break;
			}

			Quantity<?> converted = ((Quantity<IMeasurable>) quantity).convertTo(target);

			QuantityModel resultModel = convertQuantityToModel(converted, model.getMeasurementType());

			return convertModelToDTO(resultModel);

		} catch (Exception e) {
			throw new QuantityMeasurementException(e.getMessage());
		}
	}

	@Override
	public boolean compare(QuantityDTO q1, QuantityDTO q2) {

		QuantityModel m1 = convertDTOToModel(q1);
		QuantityModel m2 = convertDTOToModel(q2);

		Quantity<? extends IMeasurable> quantity1 = convertModelToQuantity(m1);
		Quantity<? extends IMeasurable> quantity2 = convertModelToQuantity(m2);

		try {

			return quantity1.equals(quantity2);

		} catch (Exception e) {
			throw new QuantityMeasurementException(e.getMessage());
		}
	}
}
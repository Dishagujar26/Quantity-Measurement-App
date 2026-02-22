package com.QuantityMeasurementApp;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class QuantityMeasurementAppTest {

	@Test
	public void testFeetEquality_SameValue() {
		Feet f1 = new Feet(1.0);
		Feet f2 = new Feet(1.0);

		assertTrue(f1.equals(f2), "1.0 ft should be equal to 1.0 ft");
	}

	@Test
	public void testFeetEquality_DifferentValue() {
		Feet f1 = new Feet(1.0);
		Feet f2 = new Feet(2.0);

		assertFalse(f1.equals(f2), "1.0 ft should not be equal to 2.0 ft");
	}

	@Test
	public void testFeetEquality_NullComparison() {
		Feet f1 = new Feet(1.0);

		assertFalse(f1.equals(null), "Feet object should not be equal to null");
	}

	@Test
	public void testFeetEquality_DifferentClass() {
		Feet f1 = new Feet(1.0);
		String other = "1.0";

		assertFalse(f1.equals(other), "Feet object should not be equal to different type");
	}

	@Test
	public void testFeetEquality_SameReference() {
		Feet f1 = new Feet(1.0);

		assertTrue(f1.equals(f1), "Object should be equal to itself");
	}

	@Test
	void testEquality_SameValue() {
		Inches i1 = new Inches(1.0);
		Inches i2 = new Inches(1.0);
		assertTrue(i1.equals(i2));
	}

	@Test
	void testEquality_DifferentValue() {
		Inches i1 = new Inches(1.0);
		Inches i2 = new Inches(2.0);
		assertFalse(i1.equals(i2));
	}

	@Test
	void testEquality_NullComparison() {
		Inches i1 = new Inches(1.0);
		assertFalse(i1.equals(null));
	}

	@Test
	void testEquality_SameReference() {
		Inches i1 = new Inches(1.0);
		assertTrue(i1.equals(i1));
	}

	@Test
	void testEquality_DifferentType() {
		Inches i1 = new Inches(1.0);
		assertFalse(i1.equals("1.0"));
	}
}
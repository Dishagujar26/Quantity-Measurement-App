package com.app.quantitymeasurementapp.model;

/**
 * Represents the type of operation performed on quantities.
 */
public enum OperationType {

	ADD("add"), SUBTRACT("subtract"), MULTIPLY("multiply"), DIVIDE("divide"), COMPARE("compare"), CONVERT("convert");

	private final String displayName;

	OperationType(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Case-insensitive lookup.
	 */
	public static OperationType fromString(String value) {
		for (OperationType type : values()) {
			if (type.name().equalsIgnoreCase(value) || type.displayName.equalsIgnoreCase(value)) {
				return type;
			}
		}
		throw new IllegalArgumentException("Unknown operation type: " + value);
	}
}

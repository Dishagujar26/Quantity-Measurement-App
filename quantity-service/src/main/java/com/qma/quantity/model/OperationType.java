package com.qma.quantity.model;

public enum OperationType {
    ADD("add"),
    SUBTRACT("subtract"),
    DIVIDE("divide"),
    COMPARE("compare"),
    CONVERT("convert");

    private final String displayName;

    OperationType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
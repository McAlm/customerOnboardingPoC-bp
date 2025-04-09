package com.camunda.demo.model;

public enum AccountType {

    CHECKING("Checking"),
    SAVINGS("Savings"),
    BUSINESS("Business"),
    STUDENT("Student"),
    RETIREMENT("Retirement");

    private final String displayName;

    AccountType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
    public static AccountType fromDisplayName(String displayName) {
        for (AccountType type : AccountType.values()) {
            if (type.getDisplayName().equalsIgnoreCase(displayName)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown account type: " + displayName);
    }
}

package edu.ptithcm.configs;

public enum Role {
    ADMIN("ADMIN"),
    STAFF("STAFF");

    private final String value;

    Role(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}

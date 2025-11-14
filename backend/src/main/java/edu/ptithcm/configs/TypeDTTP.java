package edu.ptithcm.configs;

public enum TypeDTTP {
    //authentication type
    LOGIN("LOGIN"),
    LOGOUT("LOGOUT"),
    FORCE_KICK("FORCE_KICK"),
    //employee type
    EMPLOYEE_GET_ALL("EMPLOYEE_GET_ALL"),
    EMPLOYEE_CREATE("EMPLOYEE_CREATE"),
    EMPLOYEE_GET_ACTIVE("EMPLOYEE_GET_ACTIVE"),
    EMPLOYEE_UPDATE("EMPLOYEE_UPDATE"),
    EMPLOYEE_DELETE("EMPLOYEE_DELETE"),
    EMPLOYEE_FILTER("EMPLOYEE_FILTER"),
    //product type
    PRODUCT_GET_ALL("PRODUCT_GET_ALL"),
    PRODUCT_CREATE("PRODUCT_CREATE"),
    PRODUCT_UPDATE("PRODUCT_UPDATE"),
    PRODUCT_DELETE("PRODUCT_DELETE"),

    //System
    PING("PING"),
    PING_RESPONSE("PING_RESPONSE");
    

    private final String value;

    TypeDTTP(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
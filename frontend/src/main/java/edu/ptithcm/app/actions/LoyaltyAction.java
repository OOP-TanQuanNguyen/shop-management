package edu.ptithcm.app.actions;

public enum LoyaltyAction {
    LOYALTY_GET_ALL,
    LOYALTY_GET_BY_CUSTOMER,
    LOYALTY_CREATE,
    LOYALTY_UPDATE,
    LOYALTY_DELETE,
    LOYALTY_MESSAGE,
    LOYALTY_ERROR;

    @Override
    public String toString() {
        return name();
    }
}

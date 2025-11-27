package edu.ptithcm.app.actions;

public enum InvoiceAction {

    // ----- LOAD LIST -----
    INVOICE_GET_ALL,
    INVOICE_GET_BY_CUSTOMER,
    INVOICE_GET_BY_BRANCH,
    INVOICE_GET_BY_EMPLOYEE,
    // ----- CRUD + BUSINESS -----
    INVOICE_CREATE,
    INVOICE_UPDATE,
    INVOICE_DELETE,
    INVOICE_CONFIRM,
    INVOICE_CANCEL,
    // ----- STATE / MESSAGE -----
    INVOICE_MESSAGE,
    INVOICE_ERROR,
    INVOICE_CLEAR_MESSAGE
}

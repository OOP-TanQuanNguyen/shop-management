package edu.ptithcm.models;

import java.io.Serializable;
import java.util.Objects;

public class InvoiceDetailId implements Serializable {
    private String product;
    private String invoice;

    public InvoiceDetailId() {}
    public InvoiceDetailId(String product, String invoice) {
        this.product = product;
        this.invoice = invoice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InvoiceDetailId)) return false;
        InvoiceDetailId that = (InvoiceDetailId) o;
        return Objects.equals(product, that.product)
            && Objects.equals(invoice, that.invoice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product, invoice);
    }
}

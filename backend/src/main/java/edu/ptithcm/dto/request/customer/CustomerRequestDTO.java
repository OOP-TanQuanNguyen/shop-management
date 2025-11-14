package edu.ptithcm.dto.request.customer;

import java.util.Map;

public class CustomerRequestDTO {

    private final String customerId;
    private final String name;
    private final String phone;

    public CustomerRequestDTO(Map<String, Object> data) {
        this.customerId = (String) data.get("customerId");
        this.name = (String) data.get("name");
        this.phone = (String) data.get("phone");
    }

    public String getCustomerId() { return customerId; }
    public String getName() { return name; }
    public String getPhone() { return phone; }

    // Validate
    public boolean validForCreate() {
        return name != null && !name.isBlank();
    }

    public boolean validForUpdate() {
        return customerId != null && !customerId.isBlank() && validForCreate();
    }
}

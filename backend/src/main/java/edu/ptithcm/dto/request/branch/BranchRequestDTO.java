package edu.ptithcm.dto.request.branch;

import edu.ptithcm.utils.RequestUtil;
import java.util.Map;

public class BranchRequestDTO {

    private final Integer branchId;
    private final String name;
    private final String phone;
    private final String address;

    public BranchRequestDTO(Map<String, Object> data) {
        this.branchId = RequestUtil.toInt(data.get("branchId"));
        this.name = RequestUtil.toStr(data.get("name"));
        this.phone = RequestUtil.toStr(data.get("phone"));
        this.address = RequestUtil.toStr(data.get("address"));
    }

    // Getters
    public Integer getBranchId() { return branchId; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }

    // Validate
    public boolean validForCreate() {
        return name != null && !name.isBlank();
    }

    public boolean validForUpdate() {
        return branchId != null && branchId > 0 && validForCreate();
    }
}

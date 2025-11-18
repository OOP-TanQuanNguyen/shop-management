package edu.ptithcm.dto.request.shift;

import edu.ptithcm.utils.RequestUtil;
import java.sql.Time;
import java.util.Map;

public class ShiftRequestDTO {
    private final Integer id;
    private final String name;
    private final Time startTime;
    private final Time endTime;

    public ShiftRequestDTO(Map<String, Object> data) {
        this.id = RequestUtil.toInt(data.get("id"));
        this.name = RequestUtil.toStr(data.get("name"));
        this.startTime = data.get("startTime") instanceof Time ? (Time)data.get("startTime") : null;
        this.endTime = data.get("endTime") instanceof Time ? (Time)data.get("endTime") : null;
    }

    public Integer getShiftId() { return id; }
    public String getName() { return name; }
    public Time getStartTime() { return startTime; }
    public Time getEndTime() { return endTime; }

    // Validate
    public boolean validForCreate() {
        return name != null && !name.isEmpty()
                && startTime != null
                && endTime != null;
    }

    public boolean validForUpdate() {
        return id != null && id > 0 && validForCreate();
    }
}

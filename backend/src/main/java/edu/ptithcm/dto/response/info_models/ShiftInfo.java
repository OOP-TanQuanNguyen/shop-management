package edu.ptithcm.dto.response.info_models;

import java.sql.Time;
import java.util.HashMap;
import java.util.Map;

public class ShiftInfo {
    private final Integer id;
    private final String name;
    private final Time startTime;
    private final Time endTime;

    private ShiftInfo(Builder b) {
        this.id = b.id;
        this.name = b.name;
        this.startTime = b.startTime;
        this.endTime = b.endTime;
    }

    public Integer getShiftId() { return id; }
    public String getName() { return name; }
    public Time getStartTime() { return startTime; }
    public Time getEndTime() { return endTime; }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("name", name);
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        return map;
    }

    public static class Builder {
        private Integer id;
        private String name;
        private Time startTime;
        private Time endTime;

        public Builder id(Integer id) { this.id = id; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder startTime(Time startTime) { this.startTime = startTime; return this; }
        public Builder endTime(Time endTime) { this.endTime = endTime; return this; }

        public ShiftInfo build() { return new ShiftInfo(this); }
    }
}

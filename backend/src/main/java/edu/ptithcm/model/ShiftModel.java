package edu.ptithcm.model;

import java.sql.Time;

public class ShiftModel {
    private Integer id;
    private String name;
    private Time startTime;
    private Time endTime;

    public ShiftModel() {}

    private ShiftModel(Builder b) {
        this.id = b.id;
        this.name = b.name;
        this.startTime = b.startTime;
        this.endTime = b.endTime;
    }

    public Integer getId() { return this.id; }
    public String getName() { return this.name; }
    public Time getStartTime() { return this.startTime; }
    public Time getEndTime() { return this.endTime; }

    public static class Builder {
        private Integer id;
        private String name;
        private Time startTime;
        private Time endTime;

        public Builder id(Integer id) { this.id = id; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder start(Time t) { this.startTime = t; return this; }
        public Builder end(Time t) { this.endTime = t; return this; }
        public ShiftModel build() { return new ShiftModel(this); }
    }
}

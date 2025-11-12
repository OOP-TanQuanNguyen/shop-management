package edu.ptithcm.models;

import jakarta.persistence.*;
import java.sql.Time;
import java.util.List;

@Entity
@Table(name = "shift")
public class ShiftModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shift_id")
    private Integer id;

    private String name;

    @Column(name = "start_time")
    private Time startTime;

    @Column(name = "end_time")
    private Time endTime;

    @OneToMany(mappedBy = "shift", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShiftAssignmentModel> assignments;

    // --- Constructors ---
    public ShiftModel() {}
    private ShiftModel(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.startTime = builder.startTime;
        this.endTime = builder.endTime;
        this.assignments = builder.assignments;
    }

    // --- Builder ---
    public static class Builder {
        private Integer id;
        private String name;
        private Time startTime;
        private Time endTime;
        private List<ShiftAssignmentModel> assignments;

        public Builder id(Integer id) { this.id = id; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder startTime(Time startTime) { this.startTime = startTime; return this; }
        public Builder endTime(Time endTime) { this.endTime = endTime; return this; }
        public Builder assignments(List<ShiftAssignmentModel> assignments) { this.assignments = assignments; return this; }
        public ShiftModel build() { return new ShiftModel(this); }
    }

    // --- Getters & Setters ---
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Time getStartTime() { return startTime; }
    public void setStartTime(Time startTime) { this.startTime = startTime; }
    public Time getEndTime() { return endTime; }
    public void setEndTime(Time endTime) { this.endTime = endTime; }
    public List<ShiftAssignmentModel> getAssignments() { return assignments; }
    public void setAssignments(List<ShiftAssignmentModel> assignments) { this.assignments = assignments; }

    @Override
    public String toString() {
        return "ShiftModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}

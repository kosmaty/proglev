package com.proglev.domain;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class ProgesteroneLevelMeasurement {
    private double progesteroneLevel;
    private LocalDate measurementDate;
    private String notes;

    public ProgesteroneLevelMeasurement() {
    }

    public ProgesteroneLevelMeasurement(double progesteroneLevel, LocalDate measurementDate, String notes) {
        this.progesteroneLevel = progesteroneLevel;
        this.measurementDate = measurementDate;
        this.notes = notes;
    }
}

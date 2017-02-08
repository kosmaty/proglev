package com.proglev.domain;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ProgesteroneLevelMeasurement {
    private double progesteroneLevel;
    private LocalDate measurementDate;
    private String notes;
}

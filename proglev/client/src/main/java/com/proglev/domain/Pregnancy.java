package com.proglev.domain;

import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Pregnancy {
    private Long id;
    private String patientFirstName;
    private String patientLastName;
    private LocalDate lastPeriodDate;
    private Set<ProgesteroneLevelMeasurement> progesteroneMeasurements = new HashSet<>();
}

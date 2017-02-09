package com.proglev.domain;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class Pregnancy {
    private Long id;
    private String patientFirstName;
    private String patientLastName;
    private LocalDate lastPeriodDate;
    private Set<ProgesteroneLevelMeasurement> progesteroneMeasurements = new HashSet<>();

    public Pregnancy() {
    }

    public Pregnancy(Long id, String patientFirstName, String patientLastName, LocalDate lastPeriodDate,
                     Set<ProgesteroneLevelMeasurement> progesteroneMeasurements) {
        this.id = id;
        this.patientFirstName = patientFirstName;
        this.patientLastName = patientLastName;
        this.lastPeriodDate = lastPeriodDate;
        this.progesteroneMeasurements = progesteroneMeasurements;
    }
}

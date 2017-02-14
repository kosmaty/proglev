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
    private String email;
    private Set<ProgesteroneLevelMeasurement> progesteroneMeasurements = new HashSet<>();

    public Pregnancy() {
    }

    public Pregnancy(Long id, String patientFirstName, String patientLastName, LocalDate lastPeriodDate, String email,
                     Set<ProgesteroneLevelMeasurement> progesteroneMeasurements) {
        this.id = id;
        this.patientFirstName = patientFirstName;
        this.patientLastName = patientLastName;
        this.lastPeriodDate = lastPeriodDate;
        this.email = email;
        this.progesteroneMeasurements = progesteroneMeasurements;
    }

    public void addProgesteroneMeasurement(ProgesteroneLevelMeasurement measurement){
        progesteroneMeasurements.add(measurement);
    }
}

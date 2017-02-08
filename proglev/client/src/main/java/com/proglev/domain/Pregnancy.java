package com.proglev.domain;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class Pregnancy {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private long id;

    private String patientFirstName;
    private String patientLastName;
    private LocalDate lastPeriodDate;

    @OneToMany(cascade = {CascadeType.ALL})
    private Set<ProgesteroneLevelMeasurement> progesteroneMeasurements = new HashSet<>();
}

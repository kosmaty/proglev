package com.proglev.domain;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
public class ProgesteroneLevelMeasurement {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    private double progesteroneLevel;
    private LocalDate measurementDate;
    private String notes;

    @ManyToOne
    private Pregnancy pregnancy;
}

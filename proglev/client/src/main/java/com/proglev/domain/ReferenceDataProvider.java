package com.proglev.domain;

import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;

@Component
public class ReferenceDataProvider {

    public static final int FIRST_VISIBLE_WEEK = 4;
    public static final int LAST_VISIBLE_WEEK = 40;
    private List<Double> plus2d;
    private List<Double> mean;
    private List<Double> minus2d;

    public ReferenceDataProvider() {
        mean = unmodifiableList(asList(
                21.6, 25.9, 26.2, 30.0, 33.4, 39.5, 47.8, 51.8, 55.6, 65.0,
                72.2, 81.6, 90.4, 104.5, 118.2, 134.8, 152.3, 159.4, 166.2
        ));

        plus2d = unmodifiableList(asList(
                36.76, 43.03, 43.24, 45.19, 56.22, 55.57, 64.86, 72.00, 75.03,
                88.22, 96.86, 105.08, 115.46, 129.08, 153.95, 168.86

        ));
        minus2d = unmodifiableList(asList(
                6.27, 9.30, 8.43, 14.05, 16.43, 23.14, 30.27, 32.43, 36.32,
                42.16, 45.84, 54.92, 64.86, 72.86, 77.19, 97.73, 111.14, 127.35, 139.03

        ));
    }

    public List<Double> getPlus2d() {
        return plus2d;
    }

    public List<Double> getMean() {
        return mean;
    }

    public List<Double> getMinus2d() {
        return minus2d;
    }
}

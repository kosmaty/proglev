package com.proglev.domain;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static java.util.Collections.unmodifiableList;

@Component
public class ReferenceDataProvider {

    public static final int FIRST_VISIBLE_WEEK = 4;
    public static final int LAST_VISIBLE_WEEK = 40;
    private List<Double> plus2d;
    private List<Double> mean;
    private List<Double> minus2d;

    public ReferenceDataProvider() {
        mean = Arrays.asList(
                21.6, 25.9, 26.2, 30.0, 33.4, 39.5, 47.8, 51.8, 55.6, 65.0,
                72.2, 81.6, 90.4, 104.5, 118.2, 134.8, 152.3, 159.4, 166.2
        );

        List<Double> plus2d = new ArrayList<>();
        List<Double> minus2d = new ArrayList<>();

        Random random = new Random();

        mean.forEach(value -> {
            double stdDev = 5 + random.nextDouble() * 5;
            plus2d.add(value + 2 * stdDev);
            minus2d.add(value - 2 * stdDev);
        });

        this.plus2d = unmodifiableList(plus2d);
        this.mean = unmodifiableList(mean);
        this.minus2d = unmodifiableList(minus2d);
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

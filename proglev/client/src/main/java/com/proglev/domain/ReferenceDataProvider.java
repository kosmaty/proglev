package com.proglev.domain;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.util.Collections.unmodifiableList;

@Component
public class ReferenceDataProvider {

    public static final int FIRST_VISIBLE_WEEK = 4;
    public  static final int LAST_VISIBLE_WEEK = 40;
    private List<Double> plus2d;
    private List<Double> mean;
    private List<Double> minus2d;

    public ReferenceDataProvider() {
        List<Double> plus2d = new ArrayList<>();
        List<Double> mean = new ArrayList<>();
        List<Double> minus2d = new ArrayList<>();

        Random random = new Random();

        for (int i = FIRST_VISIBLE_WEEK; i <= LAST_VISIBLE_WEEK; i++) {
            double stdDev = 5 + random.nextDouble() * 5;
            double value = 20 + (i * i / 10) + random.nextDouble() * 5;
            plus2d.add(value + 2 * stdDev);
            mean.add(value);
            minus2d.add(value - 2 * stdDev);
        }

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

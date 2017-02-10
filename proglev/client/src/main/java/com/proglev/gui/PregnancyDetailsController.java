package com.proglev.gui;

import com.proglev.domain.Pregnancy;
import com.proglev.domain.ProgesteroneLevelMeasurement;
import com.proglev.domain.ReferenceDataProvider;
import com.proglev.util.FxmlComponentLoader;
import com.proglev.util.FxmlController;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.Duration;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.List;

@FxmlController
public class PregnancyDetailsController {
    @FXML
    private LineChart<String, Double> progesteroneLevelChart;

    @Resource
    private FxmlComponentLoader loader;
    @Resource
    private ReferenceDataProvider referenceDataProvider;

    public Label nameLabel;
    private Pregnancy pregnancy;

    public Node createComponent(Pregnancy pregnancy) throws IOException {
        this.pregnancy = pregnancy;
        return loader.load(getClass().getResource("pregnancyDetails.fxml"));
    }

    @FXML
    public void initialize() {
        nameLabel.setText(pregnancy.getPatientFirstName() + " " + pregnancy.getPatientLastName());

        XYChart.Series<String, Double> mean = configureSeries("mean", referenceDataProvider.getMean());
        XYChart.Series<String, Double> plus2d = configureSeries("plus 2d", referenceDataProvider.getPlus2d());
        XYChart.Series<String, Double> minus2d = configureSeries("minus 2d", referenceDataProvider.getMinus2d());
        XYChart.Series<String, Double> measurements = configureMeasurementsSeries();

        progesteroneLevelChart.getData().addAll(minus2d, mean, plus2d, measurements);
    }

    private XYChart.Series<String, Double> configureMeasurementsSeries() {

        XYChart.Series<String, Double> measurements = new XYChart.Series<>();
        for (ProgesteroneLevelMeasurement measurement : pregnancy.getProgesteroneMeasurements()) {
            int week = calculateWeek(measurement);
            measurements.getData().add(new XYChart.Data<>(Integer.toString(week), measurement.getProgesteroneLevel()));
        }
        return measurements;
    }

    private int calculateWeek(ProgesteroneLevelMeasurement measurement) {
        return (int) ChronoUnit.WEEKS.between(pregnancy.getLastPeriodDate(), measurement.getMeasurementDate());
    }

    private XYChart.Series<String, Double> configureSeries(String seriesName, List<Double> data) {
        XYChart.Series<String, Double> minus2d = new XYChart.Series<>();
        minus2d.setName(seriesName);
        for (int i = 0; i < data.size(); i++) {
            String label = Integer.toString(i + ReferenceDataProvider.FIRST_VISIBLE_WEEK);
            minus2d.getData().add(new XYChart.Data<>(label, data.get(i)));
        }
        return minus2d;
    }
}

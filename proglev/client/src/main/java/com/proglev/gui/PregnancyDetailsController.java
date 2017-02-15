package com.proglev.gui;

import com.proglev.domain.Pregnancy;
import com.proglev.domain.PregnancyRepository;
import com.proglev.domain.ProgesteroneLevelMeasurement;
import com.proglev.domain.ReferenceDataProvider;
import com.proglev.util.FxmlComponentLoader;
import com.proglev.util.FxmlController;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

import static com.proglev.util.Unchecked.unchecked;
import static java.util.concurrent.CompletableFuture.runAsync;

@FxmlController
public class PregnancyDetailsController {
    public static final URL PREGNANCY_DETAILS_FXML = PregnancyDetailsController.class.getResource("pregnancyDetails.fxml");
    @FXML
    private Pane formContainer;
    @FXML
    private Button addMeasurementButton;
    @FXML
    private Button editDetailsButton;
    @FXML
    private LineChart<String, Double> progesteroneLevelChart;
    @FXML
    private Label nameLabel;

    @Resource
    private FxmlComponentLoader loader;
    @Resource
    private ReferenceDataProvider referenceDataProvider;
    @Resource
    private AddPregnancyController addPregnancyController;
    @Resource
    private EditMeasurementController editMeasurementController;
    @Resource
    private PregnancyRepository pregnancyRepository;

    @Resource(name = "i18n")
    private ResourceBundle i18n;
    @Resource(name = "pregnancyRepositoryExecutor")
    private Executor backgroundExecutor;
    @Resource(name = "fxExecutor")
    private Executor fxExecutor;

    private Pregnancy pregnancy;

    public Node createComponent(Pregnancy pregnancy) throws IOException {
        this.pregnancy = pregnancy;
        return loader.load(PREGNANCY_DETAILS_FXML);
    }

    @FXML
    public void initialize() {
        initHeader();

        XYChart.Series<String, Double> mean = configureSeries(i18n("mean"), referenceDataProvider.getMean());
        XYChart.Series<String, Double> plus2d = configureSeries(i18n("plus2sd"), referenceDataProvider.getPlus2d());
        XYChart.Series<String, Double> minus2d = configureSeries(i18n("minus2sd"), referenceDataProvider.getMinus2d());
        XYChart.Series<String, Double> measurements = configureMeasurementsSeries();

        progesteroneLevelChart.getData().addAll(minus2d, mean, plus2d, measurements);


    }

    private String i18n(String key) {
        return i18n.getString(key);
    }

    private void initHeader() {
        nameLabel.setText(pregnancy.getPatientFirstName() + " " + pregnancy.getPatientLastName());
        diableButtonsWhenEditFormIsVisible();
    }

    private void diableButtonsWhenEditFormIsVisible() {
        formContainer.getChildren().addListener((InvalidationListener) o -> {
            boolean disableButtons = formContainer.getChildren().size() > 1;
            editDetailsButton.setDisable(disableButtons);
            addMeasurementButton.setDisable(disableButtons);
        });
    }

    private XYChart.Series<String, Double> configureMeasurementsSeries() {

        XYChart.Series<String, Double> measurements = new XYChart.Series<>();
        for (ProgesteroneLevelMeasurement measurement : pregnancy.getProgesteroneMeasurements()) {
            int week = calculateWeek(measurement);
            XYChart.Data<String, Double> data = new XYChart.Data<>(Integer.toString(week), measurement.getProgesteroneLevel());
            Tooltip tooltip = tooltipFor(measurement);
            data.nodeProperty().addListener(new ChangeListener<Node>() {
                @Override
                public void changed(ObservableValue<? extends Node> observable, Node oldValue, Node newValue) {
                    if (newValue != null) {
                        Tooltip.install(newValue, tooltip);
                        data.nodeProperty().removeListener(this);
                    }
                }
            });


            measurements.getData().add(data);
        }
        return measurements;
    }

    private Tooltip tooltipFor(ProgesteroneLevelMeasurement measurement) {
        String toolTipText = MessageFormat.format(
                i18n("measurementPointTooltip"),
                measurement.getMeasurementDate(),
                measurement.getProgesteroneLevel(),
                measurement.getNotes());
        return new Tooltip(toolTipText);
    }

    private int calculateWeek(ProgesteroneLevelMeasurement measurement) {
        int week = (int) ChronoUnit.WEEKS.between(pregnancy.getLastPeriodDate(), measurement.getMeasurementDate());
        return roundToEven(week);
    }

    private int roundToEven(int week) {
        if (week % 2 == 0) {
            return week;
        } else {
            return week - 1;
        }
    }

    private XYChart.Series<String, Double> configureSeries(String seriesName, List<Double> data) {
        XYChart.Series<String, Double> series = new XYChart.Series<>();
        series.setName(seriesName);
        for (int i = 0; i < data.size(); i++) {
            String label = Integer.toString(i * 2 + ReferenceDataProvider.FIRST_VISIBLE_WEEK);
            series.getData().add(new XYChart.Data<>(label, data.get(i)));
        }
        return series;
    }

    @FXML
    public void editDetails(ActionEvent actionEvent) throws IOException {
        Node editForm = addPregnancyController.createComponent(this::onDetailsSaved, this::hideDetailsPane, pregnancy);
        formContainer.getChildren().add(1, editForm);
    }

    private void onDetailsSaved(Pregnancy pregnancy) {
        runAsync(unchecked(() -> pregnancyRepository.update(pregnancy)), backgroundExecutor)
                .thenRunAsync(() -> {
                    this.pregnancy = pregnancy;
                    initHeader();
                    hideDetailsPane();
                }, fxExecutor);

    }

    private void hideDetailsPane() {
        if (formContainer.getChildren().size() > 1) {
            formContainer.getChildren().remove(1, formContainer.getChildren().size());
        }
    }

    @FXML
    public void addMeasurement(ActionEvent actionEvent) throws IOException {
        Consumer<ProgesteroneLevelMeasurement> onSave = this::onMeasurementAdded;
        Node editForm = editMeasurementController.createComponent(onSave, this::hideDetailsPane);
        formContainer.getChildren().add(1, editForm);
    }

    private void onMeasurementAdded(ProgesteroneLevelMeasurement measurement) {
        this.pregnancy.addProgesteroneMeasurement(measurement);
        runAsync(unchecked(() -> pregnancyRepository.update(this.pregnancy)), backgroundExecutor)
                .thenRunAsync((() -> {
                    hideDetailsPane();
                    refreshMeasurmentSeries();
                }), fxExecutor);
    }

    private void refreshMeasurmentSeries() {
        XYChart.Series<String, Double> measurements = configureMeasurementsSeries();
        progesteroneLevelChart.getData().set(3, measurements);
    }
}

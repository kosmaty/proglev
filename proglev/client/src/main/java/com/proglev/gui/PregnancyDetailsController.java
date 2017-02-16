package com.proglev.gui;

import com.proglev.domain.Pregnancy;
import com.proglev.domain.PregnancyRepository;
import com.proglev.domain.ProgesteroneLevelMeasurement;
import com.proglev.domain.ReferenceDataProvider;
import com.proglev.util.FxmlComponentLoader;
import com.proglev.util.FxmlController;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

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
    public static final int EDIT_FORM_INDEX = 2;
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

    @FXML
    private VBox popup;
    @FXML
    private Label popupLabel;
    @FXML
    private Button popupButton;
    private EventHandler<MouseEvent> hidePopup = event -> popup.setVisible(false);

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
        initChart();
        initMeasurementPopup();
    }

    private void initChart() {
        XYChart.Series<String, Double> mean = configureSeries(i18n("mean"), referenceDataProvider.getMean());
        XYChart.Series<String, Double> plus2d = configureSeries(i18n("plus2sd"), referenceDataProvider.getPlus2d());
        XYChart.Series<String, Double> minus2d = configureSeries(i18n("minus2sd"), referenceDataProvider.getMinus2d());
        XYChart.Series<String, Double> measurements = configureMeasurementsSeries();

        progesteroneLevelChart.getData().addAll(minus2d, mean, plus2d, measurements);
    }

    private void initMeasurementPopup() {
        popup.setOnMousePressed(Event::consume);
        formContainer.sceneProperty().addListener(new ChangeListener<Scene>() {
            @Override
            public void changed(ObservableValue<? extends Scene> observable, Scene oldValue, Scene newValue) {
                if (newValue != null){
                    formContainer.getScene().removeEventHandler(MouseEvent.MOUSE_PRESSED, hidePopup);
                    formContainer.getScene().addEventHandler(MouseEvent.MOUSE_PRESSED, hidePopup);
                    formContainer.sceneProperty().removeListener(this);
                }
            }
        });
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
            boolean disableButtons = formContainer.getChildren().size() > EDIT_FORM_INDEX;
            editDetailsButton.setDisable(disableButtons);
            addMeasurementButton.setDisable(disableButtons);
        });
    }

    private XYChart.Series<String, Double> configureMeasurementsSeries() {
        XYChart.Series<String, Double> measurements = new XYChart.Series<>();
        for (ProgesteroneLevelMeasurement measurement : pregnancy.getProgesteroneMeasurements()) {
            int week = calculateWeek(measurement);
            XYChart.Data<String, Double> data = new XYChart.Data<>(Integer.toString(week), measurement.getProgesteroneLevel());
            data.nodeProperty().addListener(new ChangeListener<Node>() {
                @Override
                public void changed(ObservableValue<? extends Node> observable, Node oldValue, Node newValue) {
                    if (newValue != null) {
                        data.nodeProperty().removeListener(this);
                        installPopup(data, measurement);
                    }
                }
            });

            measurements.getData().add(data);
        }
        return measurements;
    }

    private void installPopup(XYChart.Data<String, Double> data, ProgesteroneLevelMeasurement measurement) {
        data.getNode().setOnMouseClicked(event -> {
            popupLabel.setText(measurementDetailsString(measurement));
            popupButton.setOnAction(e -> deleteMeasurement(measurement));

            popup.setManaged(false);
            popup.autosize();
            Point2D point2D = formContainer.screenToLocal(event.getScreenX(), event.getScreenY());
            popup.relocate(point2D.getX(), point2D.getY());
            popup.setVisible(true);
        });
    }

    private void deleteMeasurement(ProgesteroneLevelMeasurement measurement) {
        pregnancy.removeMeasurement(measurement);
            runAsync(unchecked(() -> pregnancyRepository.update(this.pregnancy)), backgroundExecutor)
                    .thenRunAsync((() -> {
                        popup.setVisible(false);
                        refreshMeasurmentSeries();
                    }), fxExecutor);
    }

    private String measurementDetailsString(ProgesteroneLevelMeasurement measurement) {
        return MessageFormat.format(
                    i18n("measurementPointTooltip"),
                    measurement.getMeasurementDate(),
                    measurement.getProgesteroneLevel(),
                    measurement.getNotes());
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
        formContainer.getChildren().add(EDIT_FORM_INDEX, editForm);
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
        if (formContainer.getChildren().size() > EDIT_FORM_INDEX) {
            formContainer.getChildren().remove(EDIT_FORM_INDEX, formContainer.getChildren().size());
        }
    }

    @FXML
    public void addMeasurement(ActionEvent actionEvent) throws IOException {
        Consumer<ProgesteroneLevelMeasurement> onSave = this::onMeasurementAdded;
        Node editForm = editMeasurementController.createComponent(onSave, this::hideDetailsPane);
        formContainer.getChildren().add(EDIT_FORM_INDEX, editForm);
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

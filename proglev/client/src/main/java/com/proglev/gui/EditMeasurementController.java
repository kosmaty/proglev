package com.proglev.gui;


import com.proglev.domain.Pregnancy;
import com.proglev.domain.ProgesteroneLevelMeasurement;
import com.proglev.util.FxmlComponentLoader;
import com.proglev.util.FxmlController;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import javafx.util.converter.DoubleStringConverter;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

@FxmlController
public class EditMeasurementController {
    @FXML
    private DatePicker measurementDateField;
    @FXML
    private Spinner<Double> progesteroneLevelField2;
    @FXML
    private TextField progesteroneLevelField;
    @FXML
    private TextArea notesFiels;
    @FXML
    private Button saveButton;

    @Resource
    private FxmlComponentLoader loader;

    private Consumer<ProgesteroneLevelMeasurement> onSave;
    private Runnable onCancel;

    @FXML
    public void initialize() {
        progesteroneLevelField.setTextFormatter(new TextFormatter<Double>(new DoubleStringConverter()));
        saveButton.disableProperty().bind(progesteroneLevelField.getTextFormatter().valueProperty().isNull()
                .or(measurementDateField.valueProperty().isNull()));
//        progesteroneLevelField.setValueFactory(
//                new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 1000.0, 100.0, 1.0));
    }

    @FXML
    public void save() {
        ProgesteroneLevelMeasurement newMeasurement = measurementWithFormData();
        onSave.accept(newMeasurement);
    }

    @FXML
    public void cancel() {
        onCancel.run();
    }

    private ProgesteroneLevelMeasurement measurementWithFormData() {
        return ProgesteroneLevelMeasurement.builder()
                .measurementDate(measurementDateField.getValue())
                .progesteroneLevel((Double) progesteroneLevelField.getTextFormatter().getValue())
                .notes(notesFiels.getText())
                .build();
    }

    public Node createComponent(Consumer<ProgesteroneLevelMeasurement> onSave, Runnable onCancel) throws IOException {
        this.onSave = onSave;
        this.onCancel = onCancel;
        return loader.load(getClass().getResource("editMeasurement.fxml"));
    }
}
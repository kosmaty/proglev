package com.proglev.gui;

import com.proglev.domain.Pregnancy;
import com.proglev.domain.ProgesteroneLevelMeasurement;
import com.proglev.util.FxmlComponentLoader;
import com.proglev.util.FxmlController;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

@FxmlController
public class AddPregnancyController {

    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField emailField;
    @FXML
    private DatePicker lastPeriodDateField;
    @FXML
    private Button saveButton;

    @Resource
    private FxmlComponentLoader loader;

    private Consumer<Pregnancy> onSave = p -> {
    };
    private Runnable onCancel = () -> {
    };

    private Pregnancy pregnancy;

    @FXML
    public void initialize() {
        saveButton.disableProperty().bind(firstNameField.textProperty().isEmpty()
                .or(lastNameField.textProperty().isEmpty())
                .or(lastPeriodDateField.valueProperty().isNull())
                .or(emailField.textProperty().isEmpty()));

        String pattern = "yyyy-MM-dd";
        StringConverter<LocalDate> converter = new StringConverter<LocalDate>() {

            DateTimeFormatter dateFormatter =
                    DateTimeFormatter.ofPattern(pattern);

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        };
        lastPeriodDateField.setConverter(converter);

        if (pregnancy != null) {
            initializeFormData();
        }
    }

    private void initializeFormData() {
        firstNameField.setText(pregnancy.getPatientFirstName());
        lastNameField.setText(pregnancy.getPatientLastName());
        lastPeriodDateField.setValue(pregnancy.getLastPeriodDate());
        emailField.setText(pregnancy.getEmail());
    }

    public void save() {
        Pregnancy newPregnancy = pregnancyWithFormData();
        onSave.accept(newPregnancy);
    }

    private Pregnancy pregnancyWithFormData() {
        return Pregnancy.builder()
                .id(currentIdOrNull())
                .patientFirstName(firstNameField.getText())
                .patientLastName(lastNameField.getText())
                .lastPeriodDate(lastPeriodDateField.getValue())
                .email(emailField.getText())
                .progesteroneMeasurements(currentMeasurementsOrEmptySet())
                .build();
    }

    private Set<ProgesteroneLevelMeasurement> currentMeasurementsOrEmptySet() {
        return Optional.ofNullable(pregnancy).map(p -> p.getProgesteroneMeasurements()).orElse(new HashSet<>());
    }

    private Long currentIdOrNull() {
        return Optional.ofNullable(pregnancy).map(p -> p.getId()).orElse(null);
    }

    public void cancel() {
        onCancel.run();
    }

    public Node createComponent(Consumer<Pregnancy> onSave, Runnable onCancel) throws IOException {
        return createComponent(onSave, onCancel, null);
    }

    public Node createComponent(Consumer<Pregnancy> onSave, Runnable onCancel, Pregnancy pregnancy) throws IOException {
        this.pregnancy = pregnancy;
        this.onSave = onSave;
        this.onCancel = onCancel;
        return loader.load(getClass().getResource("addPregnancy.fxml"));
    }
}

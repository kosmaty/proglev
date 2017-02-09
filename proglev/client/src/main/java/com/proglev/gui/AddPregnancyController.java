package com.proglev.gui;

import com.proglev.domain.Pregnancy;
import com.proglev.domain.PregnancyRepository;
import com.proglev.util.FxmlComponentLoader;
import com.proglev.util.FxmlController;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

import static com.proglev.util.Unchecked.unchecked;
import static java.util.concurrent.CompletableFuture.runAsync;

@FxmlController
public class AddPregnancyController {
    @FXML
    Button saveButton;
    @FXML
    TextField firstNameField;
    @FXML
    TextField lastNameField;
    @FXML
    DatePicker lastPeriodDateField;

    @Resource
    ProgLevController progLevController;

    @Resource
    private PregnancyRepository pregnancyRepository;
    @Resource
    private FxmlComponentLoader loader;

    @Resource(name = "pregnancyRepositoryExecutor")
    private ExecutorService executor;

    private Runnable onDone = () -> {};

    @FXML
    public void initialize() {
        saveButton.disableProperty().bind(firstNameField.textProperty().isEmpty()
                .or(lastNameField.textProperty().isEmpty())
                .or(lastPeriodDateField.valueProperty().isNull()));

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
        lastPeriodDateField.setPromptText("rrrr-mm-dd");
    }

    public void save() {
        Pregnancy newPregnancy = pregnancyWithFormData();
        runAsync(unchecked(() -> pregnancyRepository.create(newPregnancy)), executor)
                .thenRun(() -> Platform.runLater(onDone));
    }

    private Pregnancy pregnancyWithFormData() {
        return Pregnancy.builder()
                .patientFirstName(firstNameField.getText())
                .patientLastName(lastNameField.getText())
                .lastPeriodDate(lastPeriodDateField.getValue())
                .build();
    }

    public void cancel() {
        onDone.run();
    }

    public Node createComponent(Runnable onDone) throws IOException {
        this.onDone = onDone;
        return loader.load(getClass().getResource("addPregnancy.fxml"));
    }
}

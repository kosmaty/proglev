package com.proglev.gui;

import com.proglev.domain.Pregnancy;
import com.proglev.domain.PregnancyRepository;
import com.proglev.util.FxmlComponentLoader;
import com.proglev.util.FxmlController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

import static com.proglev.util.Unchecked.unchecked;
import static java.util.concurrent.CompletableFuture.runAsync;

@FxmlController
public class ProgLevController {
    @FXML
    BorderPane mainPanel;

    @Resource
    private PregnancyRepository pregnancyRepository;

    @Resource
    private FxmlComponentLoader loader;

    @Resource
    private AddPregnancyController addPregnancyController;
    @Resource
    private PregnancyDetailsController pregnancyDetailsController;
    @Resource(name = "pregnancyRepositoryExecutor")
    private ExecutorService executor;

    @FXML
    public void initialize() {
        showTable();
    }

    public void showTable() {
        Node table = null;
        try {
            table = loader.load(getClass().getResource("pregnanciesTable.fxml"));
            mainPanel.setCenter(table);
        } catch (IOException e) {
            e.printStackTrace();
            mainPanel.setCenter(new Label("Wystąpił błąd przy wyświetlaniu tabeli"));
        }
    }

    public void showAddPregnancy() {
        try {

            Node addPregnancyPane = addPregnancyController.createComponent(this::onPregnancyAdded, this::showTable);
            mainPanel.setCenter(addPregnancyPane);
        } catch (IOException e) {
            e.printStackTrace();
            mainPanel.setCenter(new Label("Wystąpił błąd przy wyświetlaniu formularza dodawania"));
        }
    }

    private void onPregnancyAdded(Pregnancy newPregnancy) {
        runAsync(unchecked(() -> pregnancyRepository.create(newPregnancy)), executor)
       .thenRun(() -> Platform.runLater(this::showTable));
    }

    public void showPregnancyDetails(Pregnancy pregnancy) {
        try {
            Node pregnancyDetailsPane = pregnancyDetailsController.createComponent(pregnancy);
            mainPanel.setCenter(pregnancyDetailsPane);
        } catch (IOException e) {
            e.printStackTrace();
            mainPanel.setCenter(new Label("Wystąpił błąd przy wyświetlaniu formularza dodawania"));
        }
    }
}

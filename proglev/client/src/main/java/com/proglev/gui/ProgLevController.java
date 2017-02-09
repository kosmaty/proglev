package com.proglev.gui;

import com.proglev.domain.Pregnancy;
import com.proglev.domain.PregnancyRepository;
import com.proglev.util.FxmlComponentLoader;
import com.proglev.util.FxmlController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import javax.annotation.Resource;
import java.io.IOException;

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
        Node addPregnancyPane = null;
        try {
            addPregnancyPane = addPregnancyController.createComponent(this::showTable);
            mainPanel.setCenter(addPregnancyPane);
        } catch (IOException e) {
            e.printStackTrace();
            mainPanel.setCenter(new Label("Wystąpił błąd przy wyświetlaniu formularza dodawania"));
        }
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

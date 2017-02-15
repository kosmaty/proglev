package com.proglev.gui;

import com.proglev.domain.Pregnancy;
import com.proglev.domain.PregnancyRepository;
import com.proglev.util.FxmlComponentLoader;
import com.proglev.util.FxmlController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executor;

import static com.proglev.gui.PregnanciesTableController.PREGNANCIES_LIST_FXML;
import static com.proglev.util.Unchecked.unchecked;
import static java.util.concurrent.CompletableFuture.runAsync;

@FxmlController
public class ProgLevController {
    public static final URL PROG_LEV_APPLICATION_FXML =
            ProgLevController.class.getResource("proglevApp.fxml");

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
    private Executor executor;
    @Resource(name = "i18n")
    private ResourceBundle i18n;


    @FXML
    public void initialize() {
        showTable();
    }

    public void showTable() {
        Node table = null;
        try {
            table = loader.load(PREGNANCIES_LIST_FXML);
            mainPanel.setCenter(table);
        } catch (IOException e) {
            e.printStackTrace();
            mainPanel.setCenter(new Label(i18n("pregnancyListLoadingError")));
        }
    }

    public void showAddPregnancy() {
        try {

            Node addPregnancyPane = addPregnancyController.createComponent(this::onPregnancyAdded, this::showTable);
            mainPanel.setCenter(addPregnancyPane);
        } catch (IOException e) {
            e.printStackTrace();
            mainPanel.setCenter(new Label(i18n("addPregnancyFormDispalyError")));
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
            mainPanel.setCenter(new Label(i18n("pregnancyDetailsDisplayError")));
        }
    }

    private String i18n(String key){
        return i18n.getString(key);
    }
}

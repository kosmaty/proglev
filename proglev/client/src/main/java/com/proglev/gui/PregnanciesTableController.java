package com.proglev.gui;


import com.proglev.domain.Pregnancy;
import com.proglev.domain.PregnancyRepository;
import com.proglev.util.FxmlController;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@FxmlController
public class PregnanciesTableController {
    @FXML
    private TableView<Pregnancy> pregancyTable;

    @Resource
    private PregnancyRepository pregnancyRepository;

    @Resource
    private ProgLevController progLevController;


    @FXML
    public void initialize() {
        pregancyTable.getItems().clear();
        pregancyTable.getItems().addAll(pregnancyRepository.getAll());

    }

    public void onMouseClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() > 1){
            Pregnancy pregnancy = pregancyTable.getSelectionModel().selectedItemProperty().get();
            progLevController.showPregnancyDetails(pregnancy);
        }
    }
}

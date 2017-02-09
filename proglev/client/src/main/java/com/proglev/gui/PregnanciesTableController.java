package com.proglev.gui;


import com.proglev.domain.PregnancyRepository;
import com.proglev.util.FxmlController;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@FxmlController
public class PregnanciesTableController {
    @FXML
    private TableView pregancyTable;

    @Resource
    private PregnancyRepository pregnancyRepository;


    @FXML
    public void initialize() {
        pregancyTable.getItems().clear();
        pregancyTable.getItems().addAll(pregnancyRepository.getAll());
    }
}

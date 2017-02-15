package com.proglev.gui;


import com.proglev.domain.PregnancyRepository;
import com.proglev.util.FxmlController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import javax.annotation.Resource;
import java.util.List;

import static com.proglev.util.Unchecked.unchecked;
import static java.util.stream.Collectors.toList;

@FxmlController
public class PregnanciesTableController {

    @FXML
    private TextField searchField;
    @FXML
    private VBox pregnancyList;

    @Resource
    private PregnancyRepository pregnancyRepository;
    @Resource
    private ProgLevController progLevController;

    @FXML
    public void initialize() {
        refreshList();
        searchField.textProperty().addListener((v, o, n) -> refreshList());
    }

    private void refreshList() {
        pregnancyList.getChildren().clear();
        String searchValue = searchField.textProperty().get();
        List<PregnancyListItem> listItems = pregnancyRepository.getAll().stream()
                .filter(p -> searchField.textProperty().isEmpty().get()
                        || p.getPatientLastName().toLowerCase().contains(searchValue.toLowerCase())
                        || p.getPatientFirstName().toLowerCase().contains(searchValue.toLowerCase())
                        || p.getEmail().toLowerCase().contains(searchValue.toLowerCase()))
                .map(p -> {
                    PregnancyListItem item = new PregnancyListItem(p);
                    item.onPregnancySelected(progLevController::showPregnancyDetails);
                    item.registerOnDeleteAction(pr -> {
                        unchecked(() -> pregnancyRepository.delete(pr.getId())).run(); // TODO anynchExec
                        refreshList();
                    });
                    return item;
                })
                .collect(toList());
        pregnancyList.getChildren().addAll(listItems);
    }
}

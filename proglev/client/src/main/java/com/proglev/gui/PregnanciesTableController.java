package com.proglev.gui;


import com.proglev.domain.Pregnancy;
import com.proglev.domain.PregnancyRepository;
import com.proglev.util.FxmlController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import java.util.List;

import static com.proglev.gui.Fonts.FONT_AWSOME_14;
import static java.util.stream.Collectors.toList;

@FxmlController
public class PregnanciesTableController {

    @FXML
    private Label searchIcon;
    @FXML
    private TextField searchField;
    @FXML
    private VBox pregnancyList;
    @FXML
    private TableView<Pregnancy> pregancyTable;

    @Resource
    private PregnancyRepository pregnancyRepository;

    @Resource
    private ProgLevController progLevController;

    @FXML
    public void initialize() {
        refreshList();
        searchField.textProperty().addListener((v, o, n) -> refreshList());
        searchIcon.setFont(FONT_AWSOME_14);
        searchIcon.setText("\uf002");
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
                    return item;
                })
                .collect(toList());
        pregnancyList.getChildren().addAll(listItems);
    }

    public void onMouseClicked(MouseEvent mouseEvent) {
//        if (mouseEvent.getClickCount() > 1){
//            Pregnancy pregnancy = pregancyTable.getSelectionModel().selectedItemProperty().get();
//            progLevController.showPregnancyDetails(pregnancy);
//        }
    }
}

package com.proglev.gui;


import com.proglev.domain.PregnancyRepository;
import com.proglev.util.FxmlComponentLoader;
import com.proglev.util.FxmlController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;

import static com.proglev.gui.PregnancyListItemController.PREGNANCY_LIST_ITEM_FXML;
import static com.proglev.util.Unchecked.unchecked;
import static java.util.stream.Collectors.toList;

@FxmlController
public class PregnanciesTableController {

    public static final URL PREGNANCIES_LIST_FXML =
            PregnanciesTableController.class.getResource("pregnanciesTable.fxml");

    @FXML
    private TextField searchField;
    @FXML
    private VBox pregnancyList;

    @Resource
    private PregnancyRepository pregnancyRepository;
    @Resource
    private ProgLevController progLevController;
    @Resource
    private FxmlComponentLoader loader;

    @FXML
    public void initialize() {
        refreshList();
        searchField.textProperty().addListener((v, o, n) -> refreshList());
    }

    private void refreshList() {
        pregnancyList.getChildren().clear();
        String searchValue = searchField.textProperty().get();
        List<Node> listItems = pregnancyRepository.getAll().stream()
                .filter(p -> searchField.textProperty().isEmpty().get()
                        || p.getPatientLastName().toLowerCase().contains(searchValue.toLowerCase())
                        || p.getPatientFirstName().toLowerCase().contains(searchValue.toLowerCase())
                        || p.getEmail().toLowerCase().contains(searchValue.toLowerCase()))
                .map(p -> {
                    try {
                        FXMLLoader fxmlLoader = this.loader.createLoader(PREGNANCY_LIST_ITEM_FXML);
                        fxmlLoader.load();
                        PregnancyListItemController controller = fxmlLoader.getController();
                        controller.setPregnancy(p);
                        controller.onPregnancySelected(progLevController::showPregnancyDetails);
                        controller.registerOnDeleteAction(pr -> {
                            unchecked(() -> pregnancyRepository.delete(pr.getId())).run(); // TODO anynchExec
                            refreshList();
                        });
                        return (Node) fxmlLoader.getRoot();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }).filter(Objects::nonNull)
                .collect(toList());
        pregnancyList.getChildren().addAll(listItems);
    }
}

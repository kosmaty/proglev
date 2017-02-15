package com.proglev.gui;

import com.proglev.domain.Pregnancy;
import com.proglev.util.FxmlController;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import javax.annotation.Resource;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

@FxmlController
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PregnancyListItemController {

    public static final URL PREGNANCY_LIST_ITEM_FXML =
            PregnancyListItemController.class.getResource("pregnancyListItem.fxml");

    @FXML
    private Label nameLabel;
    @FXML
    private Label emailLabel;

    private Pregnancy pregnancy;
    private Consumer<Pregnancy> deleteAction;

    public StringProperty nameProperty() {
        return nameLabel.textProperty();
    }

    public StringProperty emailProperty() {
        return emailLabel.textProperty();
    }

    public void onPregnancySelected(Consumer<Pregnancy> action) {
        nameLabel.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY)
                action.accept(pregnancy);
        });
    }

    @FXML
    private void initialize() {

    }

    @FXML
    public void onDelete(ActionEvent event) {
        deleteAction.accept(pregnancy);
    }

    public void registerOnDeleteAction(Consumer<Pregnancy> action) {
        this.deleteAction = action;
    }

    public void setPregnancy(Pregnancy pregnancy) {
        this.pregnancy = pregnancy;
        nameProperty().set(pregnancy.getPatientFirstName() + " " + pregnancy.getPatientLastName());
        emailProperty().set(pregnancy.getEmail());
    }
}

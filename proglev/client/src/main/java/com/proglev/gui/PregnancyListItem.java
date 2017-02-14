package com.proglev.gui;


import com.proglev.domain.Pregnancy;
import com.proglev.util.FxmlController;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.io.IOException;
import java.util.function.Consumer;

@FxmlController
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PregnancyListItem extends VBox{
    @FXML
    private Label nameLabel;
    @FXML
    private Label emailLabel;

    private Pregnancy pregnancy;

    public PregnancyListItem() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                "pregnancyListItem.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new IllegalStateException(exception);
        }
    }

    public PregnancyListItem(Pregnancy pregnancy){
        this();
        this.pregnancy = pregnancy;
        nameProperty().set(pregnancy.getPatientFirstName() + " " + pregnancy.getPatientLastName());
        emailProperty().set(pregnancy.getEmail());
    }
    
    public StringProperty nameProperty(){
        return nameLabel.textProperty();
    }

    public StringProperty emailProperty(){
        return emailLabel.textProperty();
    }

    public void onPregnancySelected(Consumer<Pregnancy> action){
        nameLabel.setOnMouseClicked(event -> action.accept(pregnancy));
    }
}

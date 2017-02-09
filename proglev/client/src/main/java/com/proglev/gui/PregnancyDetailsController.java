package com.proglev.gui;

import com.proglev.domain.Pregnancy;
import com.proglev.util.FxmlComponentLoader;
import com.proglev.util.FxmlController;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;

import javax.annotation.Resource;
import java.io.IOException;

@FxmlController
public class PregnancyDetailsController {
    @Resource
    private FxmlComponentLoader loader;

    public Label nameLabel;
    private Pregnancy pregnancy;

    public Node createComponent(Pregnancy pregnancy) throws IOException {
        this.pregnancy = pregnancy;
        return loader.load(getClass().getResource("pregnancyDetails.fxml"));
    }

    @FXML
    public void initialize(){
        nameLabel.setText(pregnancy.getPatientFirstName() + " " + pregnancy.getPatientLastName());
    }
}

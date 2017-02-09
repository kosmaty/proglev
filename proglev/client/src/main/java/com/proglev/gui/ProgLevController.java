package com.proglev.gui;

import com.proglev.domain.Pregnancy;
import com.proglev.domain.PregnancyRepository;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Collection;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ProgLevController {
    @FXML
    private TableView<Pregnancy> pregancyTable;

    @Resource
    private PregnancyRepository pregnancyRepository;

    @PostConstruct
    public void setup(){
        System.out.println("ProgLevController init");
    }

    @FXML
    public void initialize(){
        pregancyTable.getItems().clear();
        pregancyTable.getItems().addAll(pregnancyRepository.getAll());
    }

}

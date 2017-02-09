package com.proglev.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URL;

@Component
public class FxmlComponentLoader {
    @Resource
    private SpringContextControllerFactory controllerFactory;

    public FXMLLoader createLoader(URL fxmlLocation){
        FXMLLoader loader = new FXMLLoader(fxmlLocation);
        loader.setControllerFactory(controllerFactory);
        return loader;
    }

    public <T extends Node> T load(URL fxmlLocation) throws IOException {
        FXMLLoader loader = createLoader(fxmlLocation);
        return loader.load();
    }

}

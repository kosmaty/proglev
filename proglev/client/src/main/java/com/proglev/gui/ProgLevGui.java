package com.proglev.gui;

import com.proglev.ProglevApplication;
import com.proglev.util.FxmlComponentLoader;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.util.Locale;

import static com.proglev.gui.ProgLevController.PROG_LEV_APPLICATION_FXML;

public class ProgLevGui extends Application {

    private ConfigurableApplicationContext ctx;

    public static void main(String[] args) {
        Locale.setDefault(new Locale("pl"));
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Font.loadFont(getClass().getResource("/com/proglev/gui/fonts/fontawesome-webfont.ttf").toExternalForm(), 10);
        Platform.setImplicitExit(true);
        ctx = SpringApplication.run(ProglevApplication.class);
        ctx.registerShutdownHook();
        FxmlComponentLoader loader = ctx.getBean(FxmlComponentLoader.class);
        Parent root = loader.load(PROG_LEV_APPLICATION_FXML);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        ctx.close();
    }
}



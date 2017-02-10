package com.proglev.gui;/**
 * Created by kyko on 2/7/2017.
 */

import com.proglev.ProglevApplication;
import com.proglev.domain.Pregnancy;
import com.proglev.util.FxmlComponentLoader;
import com.proglev.util.SpringContextControllerFactory;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Locale;

public class ProgLevGui extends Application {

    private ConfigurableApplicationContext ctx;
    public static void main(String[] args) {
        Locale.setDefault(new Locale("pl"));
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Platform.setImplicitExit(true);
        ctx = SpringApplication.run(ProglevApplication.class);
        ctx.registerShutdownHook();
        FxmlComponentLoader loader = ctx.getBean(FxmlComponentLoader.class);
        Parent root = loader.load(getClass().getResource("proglevApp.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        ctx.close();
    }
}



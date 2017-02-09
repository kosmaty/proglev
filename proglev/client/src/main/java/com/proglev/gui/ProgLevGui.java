package com.proglev.gui;/**
 * Created by kyko on 2/7/2017.
 */

import com.proglev.ProglevApplication;
import com.proglev.domain.Pregnancy;
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

public class ProgLevGui extends Application {

    private ConfigurableApplicationContext ctx;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
//        BorderPane root = new BorderPane();
//        TableView<Pregnancy> t = new TableView<>();
//
//        Pregnancy p = Pregnancy.builder()
//                .id(10L)
//                .lastPeriodDate(LocalDate.of(2016, 8, 22))
//                .patientFirstName("a")
//                .patientLastName("b")
//                .build();
//        t.getItems().add(p);
//
//        root.setCenter(t);

        Platform.setImplicitExit(true);
        ctx = SpringApplication.run(ProglevApplication.class);
        ctx.registerShutdownHook();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("proglevApp.fxml"));
        loader.setControllerFactory(ctx.getBean(SpringContextControllerFactory.class));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        ctx.close();
    }
}



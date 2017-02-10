package com.proglev.util;

import javafx.application.Platform;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;

@Component("fxExecutor")
public class FxExecutor implements Executor{

    @Override
    public void execute(Runnable command) {
        if (Platform.isFxApplicationThread()){
            command.run();
        }else {
            Platform.runLater(command);
        }
    }
}

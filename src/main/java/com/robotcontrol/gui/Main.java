package com.robotcontrol.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/MainPane.fxml"));
        primaryStage.setTitle("SCARA arm control");
        Scene scene = new Scene(root, 1000, 600);
        primaryStage.setScene(scene);

        primaryStage.getIcons().add(new Image(("images/icon.png")));
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                com.sun.javafx.application.PlatformImpl.tkExit();
                Platform.exit();
            }

        });
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}

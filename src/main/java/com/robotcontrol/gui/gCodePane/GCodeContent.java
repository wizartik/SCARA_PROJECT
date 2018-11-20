package com.robotcontrol.gui.gCodePane;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class GCodeContent extends AnchorPane {

    public GCodeContent(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("GcodePane.fxml"));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

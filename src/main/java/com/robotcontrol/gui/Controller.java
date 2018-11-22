package com.robotcontrol.gui;

import com.robotcontrol.parameters.dynamic.Position;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

public class Controller {

    private Set gCode;
    private Set positional;
    private Set settings;

    @FXML
    AnchorPane content;

    @FXML
    Label currentCoords;

    @FXML
    private void initialize() {
        currentCoords.textProperty().bind(Position.CURRENT_POSITION_STRING);
        changePaneToGCode(null);
    }

    @FXML
    public void changePaneToGCode(ActionEvent actionEvent) {
        if (gCode == null) {
            try {
                gCode = Collections.singleton(FXMLLoader.load(getClass().getClassLoader().getResource("GcodePane.fxml")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        content.getChildren().setAll(gCode);
    }

    @FXML
    public void changePaneToPositional(ActionEvent actionEvent) {
        if (positional == null) {
            try {
                positional = Collections.singleton(FXMLLoader.load(getClass().getClassLoader().getResource("PositionalPane.fxml")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        content.getChildren().setAll(positional);
    }

    @FXML
    public void changePaneToSettings(ActionEvent actionEvent) {
        if (settings == null) {
            try {
                settings = Collections.singleton(FXMLLoader.load(getClass().getClassLoader().getResource("SettingsPane.fxml")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        content.getChildren().setAll(settings);
    }
}

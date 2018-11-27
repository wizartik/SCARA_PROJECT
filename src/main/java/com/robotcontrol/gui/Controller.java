package com.robotcontrol.gui;

import com.jfoenix.controls.JFXButton;
import com.robotcontrol.parameters.dynamic.Position;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

public class Controller {

    private DropShadow shadow = new DropShadow(50, Color.web("#4059A9"));

    public JFXButton gcodeButton;
    public JFXButton positionalButton;
    public JFXButton settingsButton;
    private Set gCode;
    private Set positional;
    private Set settings;

    @FXML
    AnchorPane content;

    @FXML
    Label currentCoords;

    @FXML
    private void initialize() {
        try {
            gCode = Collections.singleton(FXMLLoader.load(getClass().getClassLoader().getResource("GcodePane.fxml")));
            positional = Collections.singleton(FXMLLoader.load(getClass().getClassLoader().getResource("PositionalPane.fxml")));
            settings = Collections.singleton(FXMLLoader.load(getClass().getClassLoader().getResource("SettingsPane.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }


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

    public void gcodeEntered(MouseEvent mouseEvent) {
        gcodeButton.setEffect(shadow);
    }

    public void gcodeExited(MouseEvent mouseEvent) {
        gcodeButton.setEffect(null);
    }

    public void positionalEntered(MouseEvent mouseEvent) {
        positionalButton.setEffect(shadow);
    }

    public void positionalExited(MouseEvent mouseEvent) {
        positionalButton.setEffect(null);
    }

    public void settingsEntered(MouseEvent mouseEvent) {
        settingsButton.setEffect(shadow);
    }

    public void settingsExited(MouseEvent mouseEvent) {
        settingsButton.setEffect(null);
    }
}

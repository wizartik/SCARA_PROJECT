package com.robotcontrol.gui;

import com.jfoenix.controls.JFXButton;
import com.robotcontrol.parameters.dynamic.Position;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

public class Controller {

    public JFXButton gcodeButton;
    public JFXButton positionalButton;
    public JFXButton settingsButton;
    private Set gCode;
    private Set positional;
    private Set settings;

    public static final PseudoClass PSEUDO_CLASS_FOO = PseudoClass.getPseudoClass("foo");

    @FXML
    AnchorPane content;

    @FXML
    Label currentCoords;

    @FXML
    private void initialize() {
        try {
            gCode = Collections.singleton(FXMLLoader.load(getClass().getClassLoader().getResource("fxml/GcodePane.fxml")));
            positional = Collections.singleton(FXMLLoader.load(getClass().getClassLoader().getResource("fxml/PositionalPane.fxml")));
            settings = Collections.singleton(FXMLLoader.load(getClass().getClassLoader().getResource("fxml/SettingsPane.fxml")));
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
                gCode = Collections.singleton(FXMLLoader.load(getClass().getClassLoader().getResource("fxml/GcodePane.fxml")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        selected(gcodeButton);
        content.getChildren().setAll(gCode);
    }

    @FXML
    public void changePaneToPositional(ActionEvent actionEvent) {
        if (positional == null) {
            try {
                positional = Collections.singleton(FXMLLoader.load(getClass().getClassLoader().getResource("fxml/PositionalPane.fxml")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        selected(positionalButton);
        content.getChildren().setAll(positional);
    }

    @FXML
    public void changePaneToSettings(ActionEvent actionEvent) {
        if (settings == null) {
            try {
                settings = Collections.singleton(FXMLLoader.load(getClass().getClassLoader().getResource("fxml/SettingsPane.fxml")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        selected(settingsButton);
        content.getChildren().setAll(settings);
    }

    private void selected(Button button){
        gcodeButton.getStylesheets().removeAll("css/selected.css");
        positionalButton.getStylesheets().removeAll("css/selected.css");
        settingsButton.getStylesheets().removeAll("css/selected.css");

        button.getStylesheets().add("css/selected.css");
    }
}
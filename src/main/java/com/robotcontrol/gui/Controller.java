package com.robotcontrol.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.Collections;

public class Controller {

    @FXML
    AnchorPane content;

    @FXML
    private void initialize(){
        changePaneToGCode(null);
    }

    @FXML
    public void changePaneToGCode(ActionEvent actionEvent) {
        try {
            content.getChildren().setAll(Collections.singleton(FXMLLoader.load(getClass().getClassLoader().getResource("GcodePane.fxml"))));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void changePaneToPositional(ActionEvent actionEvent){
        try {
            content.getChildren().setAll(Collections.singleton(FXMLLoader.load(getClass().getClassLoader().getResource("PositionalPane.fxml"))));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

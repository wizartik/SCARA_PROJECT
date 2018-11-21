package com.robotcontrol.gui;

import com.robotcontrol.comm.CommunicationController;
import com.robotcontrol.parameters.dynamic.Communication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;


public class SettingsController {

    @FXML
    Label connectionLabel;

    @FXML
    private void initialize(){
        connectionLabel.textProperty().bind(Communication.CONNECTION_STATUS);
    }


    public void connect(ActionEvent actionEvent) {
        CommunicationController.createWiFiConnection();
    }

    public void disconnect(ActionEvent actionEvent) {
        CommunicationController.closeWiFiConnection();
    }


}

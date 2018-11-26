package com.robotcontrol.gui;

import com.jfoenix.controls.JFXTextField;
import com.robotcontrol.comm.CommunicationController;
import com.robotcontrol.exc.BoundsViolation;
import com.robotcontrol.exc.NoConnection;
import com.robotcontrol.gui.util.ErrorHandler;
import com.robotcontrol.gui.util.Util;
import com.robotcontrol.movement.MovementController;
import com.robotcontrol.parameters.dynamic.Communication;
import com.robotcontrol.parameters.dynamic.DynUtil;
import com.robotcontrol.parameters.dynamic.Position;
import com.robotcontrol.util.SettingsUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;


public class SettingsController {

    @FXML
    Label connectionLabel;

    @FXML
    Label currentHomeCoords;

    @FXML
    Label currentMode;

    @FXML
    public JFXTextField posX;
    @FXML
    public JFXTextField posY;
    @FXML
    public JFXTextField posZ;

    private MovementController movementController;

    @FXML
    private void initialize() {
        connectionLabel.textProperty().bind(Communication.CONNECTION_STATUS);
        currentHomeCoords.textProperty().bind(Position.HOME_COORDS_STRING);
        currentMode.textProperty().bind(DynUtil.CURRENT_MODE_STRING);
        movementController = MovementController.getInstance();
    }


    public void connect(ActionEvent actionEvent) {
        CommunicationController.createWiFiConnection();
        SettingsUtil.setTimerToCheckConnection();
    }

    public void disconnect(ActionEvent actionEvent) {
        CommunicationController.closeWiFiConnection();
        SettingsUtil.stopTimer();
    }

    public void switchToSelective() {
        SettingsUtil.switchToSelective();
    }

    public void switchToDrawing() {
        SettingsUtil.switchToDrawing();
    }

    public void calibrate(ActionEvent actionEvent) {
        try {
            movementController.startCalibrating();
        } catch (IOException e) {
            ErrorHandler.showException(e);
        } catch (NoConnection noConnection) {
            ErrorHandler.showNoConnection(noConnection);
        }
    }

    public void changeHomeCoords() {
        double[] newHomeCoords = Util.getFieldsArray(posX, posY, posZ);
        try {
            SettingsUtil.changeHomeCoords(newHomeCoords);
        } catch (BoundsViolation boundsViolation) {
            ErrorHandler.showBoundViolation(boundsViolation);
        }
    }
}

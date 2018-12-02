package com.robotcontrol.gui;

import com.jfoenix.controls.JFXSpinner;
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
import com.robotcontrol.util.CommUtil;
import com.robotcontrol.util.SettingsUtil;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.io.IOException;


public class SettingsController {

    @FXML
    public JFXSpinner spinner;
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

    private Timeline timeline;

    private MovementController movementController;

    @FXML
    private void initialize() {
        connectionLabel.textProperty().bind(Communication.CONNECTION_STATUS);
        currentHomeCoords.textProperty().bind(Position.HOME_COORDS_STRING);
        currentMode.textProperty().bind(DynUtil.CURRENT_MODE_STRING);
        movementController = MovementController.getInstance();
        spinner.setVisible(false);
    }


    public void connect(ActionEvent actionEvent) {
        startSpinner();
        CommunicationController.createWiFiConnection();
        SettingsUtil.setTimerToCheckConnection();
    }

    public void disconnect(ActionEvent actionEvent) {
        spinner.setVisible(false);
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

    private void startSpinner(){
        spinner.setVisible(true);
        timeline = new Timeline(new KeyFrame(
                Duration.millis(500),
                ae -> checkConnection()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void checkConnection(){
        if (CommUtil.isConnected()){
            spinner.setVisible(false);
            timeline.stop();
        }
    }
}

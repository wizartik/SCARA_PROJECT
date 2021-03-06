package com.robotcontrol.gui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSpinner;
import com.robotcontrol.comm.CommunicationController;
import com.robotcontrol.gui.util.Delta;
import com.robotcontrol.movement.ParametersController;
import com.robotcontrol.parameters.dynamic.DynUtil;
import com.robotcontrol.parameters.dynamic.Motion;
import com.robotcontrol.util.CommUtil;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

import static com.robotcontrol.parameters.dynamic.Position.CURRENT_ANGLES_STRING;
import static com.robotcontrol.parameters.dynamic.Position.CURRENT_POSITION_STRING;

public class Controller {

    @FXML
    public JFXButton gcodeButton;
    public JFXButton positionalButton;
    public JFXButton settingsButton;
    public JFXButton closeButton;
    public JFXButton minimizeButton;
    public Label mode;
    private Set gCode;
    private Set positional;
    private Set settings;

    @FXML
    AnchorPane content;

    @FXML
    Label currentCoords;

    @FXML
    Label currentAngles;

    @FXML
    Label movement;

    @FXML
    ImageView wifiImage;

    private Timeline timeline;

    private Delta dragDelta = new Delta();

    FadeTransition fadeTransition;

    @FXML
    public JFXSpinner connectionSpinner;

    @FXML
    private void initialize() {
        try {
            gCode = Collections.singleton(FXMLLoader.load(getClass().getClassLoader().getResource("fxml/GcodePane.fxml")));
            positional = Collections.singleton(FXMLLoader.load(getClass().getClassLoader().getResource("fxml/PositionalPane.fxml")));
            settings = Collections.singleton(FXMLLoader.load(getClass().getClassLoader().getResource("fxml/SettingsPane.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        setButtonImages();

        currentCoords.textProperty().bind(CURRENT_POSITION_STRING);
        currentAngles.textProperty().bind(CURRENT_ANGLES_STRING);
        mode.textProperty().bind(DynUtil.CURRENT_MODE_STRING);

        setWifiImage();
        startConnection();
        startCoordsDisplaying();

        changePaneToPositional(null);
    }

    private void startConnection() {
        connectionSpinner.setVisible(true);
        CommunicationController.createWiFiConnection();
        startSpinner();
    }

    private void setWifiImage() {
        fadeTransition = new FadeTransition(Duration.seconds(2), wifiImage);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.1);
        fadeTransition.setCycleCount(Animation.INDEFINITE);
        fadeTransition.setAutoReverse(true);
        fadeTransition.play();
        wifiImage.setImage(new Image("images/wifi.png"));
    }

    private void startSpinner() {
        connectionSpinner.setVisible(true);
        timeline = new Timeline(new KeyFrame(
                Duration.millis(500),
                ae -> checkConnection()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void checkConnection() {
        if (CommUtil.isConnected()) {
            connectionSpinner.setVisible(false);
            wifiImage.setVisible(true);
        } else {
            connectionSpinner.setVisible(true);
            wifiImage.setVisible(false);
        }
    }

    private void startCoordsDisplaying() {
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(100),
                ae -> updateDisplayedCoords()));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private boolean last = false;

    private void updateDisplayedCoords() {
        CURRENT_POSITION_STRING.set(ParametersController.getCurrentCoordsString());
        CURRENT_ANGLES_STRING.set(ParametersController.getCurrentAnglesString());

        if (last != Motion.MOVING) {
            movement.setText(Motion.MOVING ? "moving" : "not moving");
            last = Motion.MOVING;
        }
    }

    private void setButtonImages() {
        ImageView gCodeImage = new ImageView(new Image("images/gCode.png"));
        gcodeButton.setGraphic(gCodeImage);

        ImageView positionalImage = new ImageView(new Image("images/positional.png"));
        positionalButton.setGraphic(positionalImage);

        ImageView settingsImage = new ImageView(new Image("images/settings.png"));
        settingsButton.setGraphic(settingsImage);

        ImageView closeImage = new ImageView(new Image("images/close.png"));
        closeButton.setGraphic(closeImage);

        ImageView minimizeImage = new ImageView(new Image("images/minimize.png"));
        minimizeButton.setGraphic(minimizeImage);



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

    private void selected(Button button) {
        gcodeButton.getStylesheets().removeAll("css/selected.css");
        positionalButton.getStylesheets().removeAll("css/selected.css");
        settingsButton.getStylesheets().removeAll("css/selected.css");

        button.getStylesheets().add("css/selected.css");
    }

    public void hBoxMouseDragged(MouseEvent mouseEvent) {
        Stage stage = (Stage) settingsButton.getScene().getWindow();
        stage.setX(mouseEvent.getScreenX() + dragDelta.getX());
        stage.setY(mouseEvent.getScreenY() + dragDelta.getY());
    }

    public void hBoxMousePressed(MouseEvent mouseEvent) {
        Stage stage = (Stage) settingsButton.getScene().getWindow();
        dragDelta.setX(stage.getX() - mouseEvent.getScreenX());
        dragDelta.setY(stage.getY() - mouseEvent.getScreenY());
    }

    public void close() {
        com.sun.javafx.application.PlatformImpl.tkExit();
        Platform.exit();
    }

    public void minimize() {
        Stage stage = (Stage) settingsButton.getScene().getWindow();
        stage.setIconified(true);
    }
}
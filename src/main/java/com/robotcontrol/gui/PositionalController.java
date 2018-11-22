package com.robotcontrol.gui;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.robotcontrol.exc.BoundsViolation;
import com.robotcontrol.exc.NoConnection;
import com.robotcontrol.movement.MovementController;
import com.robotcontrol.util.math.Converter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;

import static com.robotcontrol.gui.util.ErrorHandler.*;
import static com.robotcontrol.gui.util.Util.getFieldsArray;

public class PositionalController {

    MovementController movementController;

    @FXML
    public JFXTextField posX;
    @FXML
    public JFXTextField posY;
    @FXML
    public JFXTextField posZ;

    @FXML
    public JFXTextField ang1;
    @FXML
    public JFXTextField ang2;
    @FXML
    public JFXTextField ang3;

    @FXML
    public JFXTextField tPosX;
    @FXML
    public JFXTextField tPosY;
    @FXML
    public JFXTextField tPosZ;

    @FXML
    public JFXTextField tAng1;
    @FXML
    public JFXTextField tAng2;
    @FXML
    public JFXTextField tAng3;

    @FXML
    JFXComboBox<String> comboBox;
    @FXML
    JFXComboBox<String> tComboBox;

    @FXML
    private void initialize() throws IOException {
        ObservableList<String> elements = FXCollections.observableArrayList("radians", "degrees");
        comboBox.setItems(elements);
        comboBox.getSelectionModel().selectFirst();
        tComboBox.setItems(elements);
        tComboBox.getSelectionModel().selectFirst();
        movementController = MovementController.getInstance();
    }


    @FXML
    public void positionalGo(ActionEvent actionEvent) {
        double[] coords = getPositionalArray();
        if (coords != null) {
            try {
                movementController.moveToPointPos(coords);
            } catch (BoundsViolation boundsViolation) {
                showBoundViolation(boundsViolation);
            } catch (IOException e) {
                showException(e);
            } catch (NoConnection noConnection) {
                showNoConnection(noConnection);
            }
        }
    }

    @FXML
    public void angularGo(ActionEvent actionEvent) {
        double[] angles = getAngularArray();

        if (comboBox.getValue().equalsIgnoreCase("degrees")) {
            angles = Converter.toRad(angles);
        }

        if (angles != null) {
            try {
                movementController.moveToPointAng(angles);
            } catch (BoundsViolation boundsViolation) {
                showBoundViolation(boundsViolation);
            } catch (IOException e) {
                showException(e);
            } catch (NoConnection noConnection) {
                showNoConnection(noConnection);
            }
        }

    }

    @FXML
    public void travelAngGo(ActionEvent actionEvent) {
        double[] angles = getAngTravelArray();

        if (angles != null) {
            if (tComboBox.getValue().equalsIgnoreCase("degrees")) {
                angles = Converter.toRad(angles);
            }

            try {
                movementController.travelByAng(angles);
            } catch (BoundsViolation boundsViolation) {
                showBoundViolation(boundsViolation);
            } catch (IOException e) {
                showException(e);
            } catch (NoConnection noConnection) {
                showNoConnection(noConnection);
            }
        }
    }

    @FXML
    public void travelPosGo(ActionEvent actionEvent) {
        double[] coords = getPosTravelArray();
        if (coords != null) {
            try {
                movementController.travelByPos(coords);
            } catch (BoundsViolation boundsViolation) {
                showBoundViolation(boundsViolation);
            } catch (IOException e) {
                showException(e);
            } catch (NoConnection noConnection) {
                showNoConnection(noConnection);
            }
        }
    }


    private double[] getPositionalArray() {
        return getFieldsArray(posX, posY, posZ);
    }

    private double[] getAngularArray() {
        return getFieldsArray(ang1, ang2, ang3);
    }

    private double[] getPosTravelArray() {
        return getFieldsArray(tPosX, tPosY, tPosZ);
    }

    private double[] getAngTravelArray() {
        return getFieldsArray(tAng1, tAng2, tAng3);
    }
}

package com.robotcontrol.gui.gCodePane;

import com.jfoenix.controls.JFXTextArea;
import com.robotcontrol.calc.CalculateController;
import com.robotcontrol.exc.*;
import com.robotcontrol.gui.util.ErrorHandler;
import com.robotcontrol.movement.MovementController;
import com.robotcontrol.parameters.dynamic.Files;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static com.robotcontrol.util.FilesHandler.checkFile;

public class GCodeController {

    @FXML
    Label fileNameLabel;

    @FXML
    JFXTextArea gCodeArea;


    @FXML
    private void initialize() {
        fileNameLabel.textProperty().bind(Files.CURRENT_FILE_NAME);
    }

    public void openFile(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        try {
            File chosenFile = fileChooser.showOpenDialog(null);
            setCurrentFile(chosenFile);
        } catch (WrongExtension wrongExtension) {
            ErrorHandler.showWrongExtension(wrongExtension);
        }
    }


    private void setCurrentFile(File file) throws WrongExtension {
        if (checkFile(file)) {
            Files.CURRENT_FILE = file;
            Files.CURRENT_FILE_NAME.set(file.getName());
        }
    }

    public void fileCalc() {
        try {
            CalculateController.calculateContourPath(Files.CURRENT_FILE);
        } catch (WrongExtension wrongExtension) {
            ErrorHandler.showWrongExtension(wrongExtension);
        } catch (BoundsViolation boundsViolation) {
            ErrorHandler.showBoundViolation(boundsViolation);
        } catch (WrongInputData wrongInputData) {
            ErrorHandler.showWrongInputData(wrongInputData);
        } catch (IOException e) {
            ErrorHandler.showException(e);
        } catch (ImpossibleToImplement impossibleToImplement) {
            ErrorHandler.showImpossibleToImplement(impossibleToImplement);
        }
    }

    public void areaCalc() {
        try {
            CalculateController.calculateContourPathByList(stringToList(gCodeArea.getText()));
        } catch (WrongInputData wrongInputData) {
            ErrorHandler.showWrongInputData(wrongInputData);
        } catch (BoundsViolation boundsViolation) {
            ErrorHandler.showBoundViolation(boundsViolation);
        } catch (ImpossibleToImplement impossibleToImplement) {
            ErrorHandler.showImpossibleToImplement(impossibleToImplement);
        }
    }

    public void go(){
        try {
            MovementController.getInstance().moveByGCode();
        } catch (NoConnection noConnection) {
            ErrorHandler.showNoConnection(noConnection);
        } catch (IOException e) {
            ErrorHandler.showException(e);
        }
    }

    private List<String> stringToList(String string) {
        return Arrays.asList(string.split("\n"));
    }
}

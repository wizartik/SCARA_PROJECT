package com.robotcontrol.gui.gCodePane;

import com.robotcontrol.exc.WrongExtension;
import com.robotcontrol.gui.util.ErrorHandler;
import com.robotcontrol.parameters.dynamic.Files;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;

import java.io.File;

import static com.robotcontrol.util.FilesHandler.checkFile;

public class GCodeController {

    @FXML
    Label fileNameLabel;

    @FXML
    private void initialize(){
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
}

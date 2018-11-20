package com.robotcontrol.gui.gCodePane;

import com.robotcontrol.exc.WrongExtension;
import com.robotcontrol.gui.util.ErrorHandler;
import com.robotcontrol.parameters.dynamic.Misc;
import com.robotcontrol.util.FilesHandler;
import javafx.event.ActionEvent;
import javafx.stage.FileChooser;

import java.io.File;

public class GCodeController {
    public void openFile(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        try {
            File chosenFile = fileChooser.showOpenDialog(null);
            FilesHandler.checkFile(chosenFile);
            Misc.CURRENT_FILE = chosenFile;
        } catch (WrongExtension wrongExtension) {
            ErrorHandler.showWrongExtension(wrongExtension);
        }
    }
}

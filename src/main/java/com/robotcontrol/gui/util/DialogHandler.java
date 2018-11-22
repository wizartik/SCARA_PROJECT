package com.robotcontrol.gui.util;

import javafx.scene.control.Alert;

public class DialogHandler {

    public static void showDialog(String title, String header, String content){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void recalculatePath(){
        showDialog("Please recalculate path!", "Some coordinates changed, recalculate the path!", "");
    }

    public static void nothingToProcess(){
        showDialog("Nothing to process!", "pls select file or create G-code path and click calculate", "wtf");
    }
}

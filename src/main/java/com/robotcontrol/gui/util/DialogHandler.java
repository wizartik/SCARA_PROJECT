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

    public static void cantCalculateWhileBusy(){
        showDialog("Robot is busy!", "Movement is in process, wait till the end!", "");
    }

    public static void recalculatePath(){
        showDialog("Please recalculate path!", "Some coordinates changed, recalculate the path!", "");
    }

    public static void nothingToProcess(){
        showDialog("Nothing to process!", "pls select file or create G-code path and click calculate", "wtf");
    }

    public static void currentlyMoving(){
        showDialog("Robot is busy!", "motors are working now, wait for end of the operation", "or reboot program if motors arent actually moving");
    }

    public static void showMotorCrash(int num){
        showDialog("Endstop yield!","Link "+num+"hits endstop!", "stop and recalibrate!");
    }
}

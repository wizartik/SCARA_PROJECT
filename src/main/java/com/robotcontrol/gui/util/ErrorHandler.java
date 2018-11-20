package com.robotcontrol.gui.util;

import com.robotcontrol.exc.WrongExtension;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ErrorHandler {

    public static void showWrongExtension(WrongExtension wrongExtension){
        String title = "Wrong file extension!";
        String headerText = wrongExtension.getMessage();
        String contextText = wrongExtension.getAddMessage();

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        wrongExtension.printStackTrace(pw);
        String exceptionText = sw.toString();

        showError(title, headerText, contextText, exceptionText);
    }

    private static void showError(String title, String headerText, String contextText, String exceptionText){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contextText);

        Label label = new Label("The exception stacktrace was:");

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        alert.getDialogPane().setExpandableContent(expContent);
        alert.getDialogPane().setMinSize(600, 500);
        alert.showAndWait();
    }
}

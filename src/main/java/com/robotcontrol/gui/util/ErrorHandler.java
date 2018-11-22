package com.robotcontrol.gui.util;

import com.robotcontrol.exc.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ErrorHandler {

    public static void showException(Exception e){
        String title = e.getClass().getName();
        String headerText = e.getMessage();
        String exceptionText = getExceprionText(e);

        showError(title, headerText, title, exceptionText);
    }

    public static void showNoConnection(NoConnection noConnection){
        String title = "No connection!";
        String headerText = noConnection.getMessage();
        String exceptionText = getExceprionText(noConnection);

        showError(title, headerText, title, exceptionText);

    }

    public static void showWrongInputData(WrongInputData wrongInputData){
        String title = "Wrong input data!";
        String headerText = wrongInputData.getMessage();
        String contextText = wrongInputData.getgCode();
        String exceptionText = getExceprionText(wrongInputData);

        showError(title, headerText, contextText, exceptionText);
    }

    public static void showImpossibleToImplement(ImpossibleToImplement impossibleToImplement){
        String title = "Impossible to implement!";
        String headerText = impossibleToImplement.getMessage();
        String contextText = impossibleToImplement.getGCode();
        String exceptionText = getExceprionText(impossibleToImplement);

        showError(title, headerText, contextText, exceptionText);
    }

    public static void showWrongExtension(WrongExtension wrongExtension){
        String title = "Wrong file extension!";
        String headerText = wrongExtension.getMessage();
        String contextText = wrongExtension.getAddMessage();
        String exceptionText = getExceprionText(wrongExtension);

        showError(title, headerText, contextText, exceptionText);
    }

    public static void showBoundViolation(BoundsViolation boundsViolation){
        String title = "Bounds violation";
        String headerText = boundsViolation.getMessage();
        String contextText = boundsViolation.getgCode();
        String exceptionText = getExceprionText(boundsViolation);

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

    private static String getExceprionText(Exception e){
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }
}

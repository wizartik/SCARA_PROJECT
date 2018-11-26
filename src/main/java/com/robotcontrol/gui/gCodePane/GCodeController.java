package com.robotcontrol.gui.gCodePane;

import com.jfoenix.controls.JFXTextArea;
import com.robotcontrol.calc.CalculateController;
import com.robotcontrol.exc.*;
import com.robotcontrol.gui.util.Drawing;
import com.robotcontrol.gui.util.ErrorHandler;
import com.robotcontrol.movement.MovementController;
import com.robotcontrol.parameters.dynamic.DynUtil;
import com.robotcontrol.parameters.dynamic.Files;
import com.robotcontrol.util.progress.Progress;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

import static com.robotcontrol.util.FilesHandler.checkFile;

public class GCodeController {

    @FXML
    public Pane drawingPane;
    public Label action;
    public ProgressIndicator calcIndicator;

    @FXML
    Label fileNameLabel;

    @FXML
    JFXTextArea gCodeArea;


    @FXML
    private void initialize() {
        fileNameLabel.textProperty().bind(Files.CURRENT_FILE_NAME);
        action.textProperty().bind(DynUtil.CURRENT_ACTION);
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
            ExecutorService exec = Executors.newFixedThreadPool(2);
            startProgressWatching();
            System.out.println("here");
            List<Callable<Integer>> tasks = new ArrayList<>(2);
            tasks.add(() -> {
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
                return null;
            });
            tasks.add(() -> {
                drawPath();
                return null;
            });
            tasks.add(() -> {
                finishProgressWatching();
                return null;
            });
            exec.invokeAll(tasks);

//            try {
//                futures.get(0).get();
//            } catch (ExecutionException ex) {
//                ex.getCause().printStackTrace();
//            }
        } catch (InterruptedException e) {
            ErrorHandler.showException(e);
        }
    }

    public void areaCalc() {

        try {
            ExecutorService exec = Executors.newFixedThreadPool(2);
            startProgressWatching();
            List<Callable<Integer>> tasks = new ArrayList<>(2);
            tasks.add(() -> {
                try {
                    CalculateController.calculateContourPathByList(stringToList(gCodeArea.getText()));
                } catch (BoundsViolation boundsViolation) {
                    ErrorHandler.showBoundViolation(boundsViolation);
                } catch (WrongInputData wrongInputData) {
                    ErrorHandler.showWrongInputData(wrongInputData);
                } catch (ImpossibleToImplement impossibleToImplement) {
                    ErrorHandler.showImpossibleToImplement(impossibleToImplement);
                }
                return 1;
            });
            tasks.add(() -> {
                drawPath();
                return 1;
            });
            tasks.add(() -> {
                finishProgressWatching();
                return 1;
            });
            exec.invokeAll(tasks);
        } catch (InterruptedException e) {
            ErrorHandler.showException(e);
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

    private void drawPath(){
        if (DynUtil.CURRENT_CONTOUR_PATH != null && !DynUtil.CURRENT_CONTOUR_PATH.getgCodeList().isEmpty()){
            Drawing.addPathToPane(drawingPane, DynUtil.CURRENT_CONTOUR_PATH.getgCodeList());
        }
    }

    private List<String> stringToList(String string) {
        return Arrays.asList(string.split("\n"));
    }

    private void startProgressWatching(){
        DynUtil.progress = new Progress();
        if (DynUtil.progressTimer == null) {
            DynUtil.progressTimer = new Timeline(new KeyFrame(
                    Duration.millis(100),
                    ae -> checkProgress()));
            DynUtil.progressTimer.setCycleCount(Animation.INDEFINITE);
        }
        DynUtil.progressTimer.play();
    }

    private void finishProgressWatching(){
        if (DynUtil.progressTimer != null) {
            DynUtil.progressTimer.stop();
        }

        calcIndicator.setProgress(1);
    }

    private void checkProgress(){
        System.out.println(DynUtil.progress.getGeneralProgress());
        calcIndicator.setProgress(DynUtil.progress.getGeneralProgress());
    }
}

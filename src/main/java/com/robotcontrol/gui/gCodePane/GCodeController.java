package com.robotcontrol.gui.gCodePane;

import com.jfoenix.controls.JFXTextArea;
import com.robotcontrol.calc.CalculateController;
import com.robotcontrol.exc.*;
import com.robotcontrol.gui.util.DialogHandler;
import com.robotcontrol.gui.util.Drawing;
import com.robotcontrol.gui.util.ErrorHandler;
import com.robotcontrol.movement.MovementController;
import com.robotcontrol.movement.ParametersController;
import com.robotcontrol.parameters.dynamic.DynUtil;
import com.robotcontrol.parameters.dynamic.Files;
import com.robotcontrol.util.progress.CurrentAction;
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
import java.util.Arrays;
import java.util.List;

import static com.robotcontrol.parameters.dynamic.DynUtil.FORBID_CALCULATION;
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
        Thread calcThread = getFileCalcThread();
        Thread finishThread = getFinishThread(calcThread);

        startCalculation(calcThread, finishThread);
    }

    public void areaCalc() {
        Thread calcThread = getAreaCalcThread();
        Thread finishThread = getFinishThread(calcThread);

        startCalculation(calcThread, finishThread);
    }

    public void go() {
        try {
            MovementController.getInstance().moveByGCode();
        } catch (NoConnection noConnection) {
            ErrorHandler.showNoConnection(noConnection);
        } catch (IOException e) {
            ErrorHandler.showException(e);
        }
    }

    private void drawPath() {
        if (DynUtil.CURRENT_CONTOUR_PATH != null && !DynUtil.CURRENT_CONTOUR_PATH.getgCodeList().isEmpty()) {
            Drawing.addPathToPane(drawingPane, DynUtil.CURRENT_CONTOUR_PATH.getgCodeList());
        }
    }

    private List<String> stringToList(String string) {
        return Arrays.asList(string.split("\n"));
    }

    private void startProgressWatching() {
        DynUtil.progress = new Progress();
        if (DynUtil.progressTimeline == null) {
            createProgressTimeline();
        }
        DynUtil.progressTimeline.play();
    }

    private void createProgressTimeline(){
        DynUtil.progressTimeline = new Timeline(new KeyFrame(
                Duration.millis(100),
                ae -> checkProgress()));
        DynUtil.progressTimeline.setCycleCount(Animation.INDEFINITE);
    }

    private void finishProgressWatching() {
        calcIndicator.setProgress(1);
        ParametersController.setCurrentAction(CurrentAction.FINISHED);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        DynUtil.progressTimeline.pause();
    }

    private void checkProgress() {
        calcIndicator.setProgress(DynUtil.progress.getGeneralProgress());
        DynUtil.CURRENT_ACTION.set(DynUtil.CURRENT_ACTION_STRING);
    }

    private void startDrawing() {
        createDrawingTimer();
        DynUtil.drawingTimeline.play();
    }

    private void createDrawingTimer(){
        DynUtil.drawingTimeline = new Timeline(new KeyFrame(
                Duration.millis(1000),
                ae -> draw()));
        DynUtil.drawingTimeline.setCycleCount(60);
    }

    private void draw() {
        if (DynUtil.progress.getGeneralProgress() >= 1 && DynUtil.drawingTimeline != null) {
            drawPath();
            DynUtil.drawingTimeline.stop();
        }
    }

    private void waitForException() {
        createExceptionTimeline();
        DynUtil.exceptionTimeline.play();
    }

    private void createExceptionTimeline(){
        DynUtil.exceptionTimeline = new Timeline(new KeyFrame(
                Duration.millis(1000),
                ae -> checkException()));
        DynUtil.exceptionTimeline.setCycleCount(60);
    }

    private void checkException() {
        if (DynUtil.producedException != null) {
            Exception ex = DynUtil.producedException;
            if (ex instanceof WrongExtension) {
                ErrorHandler.showWrongExtension((WrongExtension) ex);
            } else if (ex instanceof BoundsViolation) {
                ErrorHandler.showBoundViolation((BoundsViolation) ex);
            } else if (ex instanceof WrongInputData) {
                ErrorHandler.showWrongInputData((WrongInputData) ex);
            } else if (ex instanceof ImpossibleToImplement) {
                ErrorHandler.showImpossibleToImplement((ImpossibleToImplement) ex);
            } else {
                ErrorHandler.showException(ex);
            }
            ParametersController.setCurrentAction(CurrentAction.ERROR);
            DynUtil.producedException = null;
        }
    }

    private Thread getFileCalcThread() {
        return new Thread(() -> {
            try {
                CalculateController.calculateContourPath(Files.CURRENT_FILE);
            } catch (Exception ex) {
                DynUtil.producedException = ex;
            }
        });
    }

    private Thread getAreaCalcThread() {
        return new Thread(() -> {
            try {
                CalculateController.calculateContourPathByList(stringToList(gCodeArea.getText()));
            } catch (Exception ex) {
                DynUtil.producedException = ex;
            }
        });
    }

    private Thread getFinishThread(Thread calcThread) {
        return new Thread(() -> {
            try {
                calcThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            finishProgressWatching();
        });
    }

    private void startCalculation(Thread calcThread, Thread finishThread) {
        if (FORBID_CALCULATION){
            DialogHandler.cantCalculateWhileBusy();
            return;
        }

        startProgressWatching();
        waitForException();
        calcThread.start();
        startDrawing();
        finishThread.start();
    }
}

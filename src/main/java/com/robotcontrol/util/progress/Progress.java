package com.robotcontrol.util.progress;

public class Progress {
    private double progressOfParsing = 0;

    private double progressOfCalc = 0;

    private double progressOfConverting = 0;

    private double parsingCoef = 0.05;
    private double calcCoef = 0.90;
    private double convertingCoef = 0.05;

    private CurrentAction currentAction;

    public double getProgressOfParsing() {
        return progressOfParsing;
    }

    public void setProgressOfParsing(int current, int size) {
        this.progressOfParsing = ((double) current + 1) / (size);
    }

    public double getProgressOfCalc() {
        return progressOfCalc;
    }

    public void setProgressOfCalc(int current, int size) {
        this.progressOfCalc = ((double) current + 1) / (size);
    }

    public double getProgressOfConverting() {
        return progressOfConverting;
    }

    public void setProgressOfConverting(int current, int size) {
        this.progressOfConverting = ((double) current + 1) / (size);
    }

    public CurrentAction getCurrentAction() {
        return currentAction;
    }

    public void setCurrentAction(CurrentAction currentAction) {
        this.currentAction = currentAction;
    }

    public double getGeneralProgress(){
        return (progressOfParsing * parsingCoef) + (progressOfCalc * calcCoef) + (progressOfConverting * convertingCoef);
    }
}

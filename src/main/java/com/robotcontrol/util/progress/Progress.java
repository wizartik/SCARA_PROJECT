package com.robotcontrol.util.progress;

public class Progress {
    private double progressOfParsing = 0;

    private double progressOfCalc = 0;

    private double progressOfConverting = 0;

    private double progressOfDrawing = 0;

    private CurrentAction currentAction;

    public double getProgressOfParsing() {
        return progressOfParsing;
    }

    public void setProgressOfParsing(int current, int size) {
        this.progressOfParsing = ((double) size) / (current + 1);
    }

    public double getProgressOfCalc() {
        return progressOfCalc;
    }

    public void setProgressOfCalc(int current, int size) {
        this.progressOfCalc = ((double) size) / (current + 1);
    }

    public double getProgressOfConverting() {
        return progressOfConverting;
    }

    public void setProgressOfConverting(int current, int size) {
        this.progressOfConverting = ((double) size) / (current + 1);
    }

    public double getProgressOfDrawing() {
        return progressOfDrawing;
    }

    public void setProgressOfDrawing(int current, int size) {
        this.progressOfDrawing = ((double) size) / (current + 1);
    }

    public CurrentAction getCurrentAction() {
        return currentAction;
    }

    public void setCurrentAction(CurrentAction currentAction) {
        this.currentAction = currentAction;
    }

    public double getGeneralProgress(){
//        System.out.println("progressOfParsing = " + progressOfParsing + " progressOfCalc = " + progressOfCalc + " progressOfConverting = " + progressOfConverting + " progressOfDrawing = " + progressOfDrawing);
        return (progressOfParsing * 0.15) + (progressOfCalc * 0.5) + (progressOfConverting * 0.2) + (progressOfDrawing * 0.15);
    }
}

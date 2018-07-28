package com.robotcontrol.calc.contouringControl.entities.data;

import com.robotcontrol.calc.contouringControl.entities.GCode.GCodeType;

public class Container {
    private GCodeType gCodeType;
    private String line;

    private double[] startPosition;
    private double[] finalPosition;
    private double staticVelocity;
    private double[] centerPosition;
    private double radius = 0;
    private double pauseInUs = 0;
    private boolean IJ = false;
    private double I = 0.0;
    private double J = 0.0;

    public Container() {

    }

    public GCodeType getgCodeType() {
        return gCodeType;
    }

    public void setgCodeType(GCodeType gCodeType) {
        this.gCodeType = gCodeType;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public double[] getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(double[] startPosition) {
        this.startPosition = startPosition;
    }

    public double[] getFinalPosition() {
        return finalPosition;
    }

    public void setFinalPosition(double[] finalPosition) {
        this.finalPosition = finalPosition;
    }

    public double getStaticVelocity() {
        return staticVelocity;
    }

    public void setStaticVelocity(double staticVelocity) {
        this.staticVelocity = staticVelocity;
    }

    public double[] getCenterPosition() {
        return centerPosition;
    }

    public void setCenterPosition(double[] centerPosition) {
        this.centerPosition = centerPosition;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public double getPauseInUs() {
        return pauseInUs;
    }

    public void setPauseInUs(final double pauseInUs) {
        this.pauseInUs = pauseInUs;
    }

    public boolean isIJ() {
        return IJ;
    }

    public void setIJ(boolean IJ) {
        this.IJ = IJ;
    }

    public double getI() {
        return I;
    }

    public void setI(double i) {
        I = i;
    }

    public double getJ() {
        return J;
    }

    public void setJ(double j) {
        J = j;
    }
}

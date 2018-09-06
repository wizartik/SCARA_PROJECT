package com.robotcontrol.calc.contouringControl.entities.data;

import com.robotcontrol.calc.contouringControl.entities.GCode.GCodeType;

import java.util.Arrays;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Container)) return false;
        Container container = (Container) o;
        return Double.compare(container.getStaticVelocity(), getStaticVelocity()) == 0 &&
                Double.compare(container.getRadius(), getRadius()) == 0 &&
                Double.compare(container.getPauseInUs(), getPauseInUs()) == 0 &&
                isIJ() == container.isIJ() &&
                Double.compare(container.getI(), getI()) == 0 &&
                Double.compare(container.getJ(), getJ()) == 0 &&
                getgCodeType() == container.getgCodeType() &&
                Objects.equals(getLine(), container.getLine()) &&
                Arrays.equals(getStartPosition(), container.getStartPosition()) &&
                Arrays.equals(getFinalPosition(), container.getFinalPosition()) &&
                Arrays.equals(getCenterPosition(), container.getCenterPosition());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(getgCodeType(), getLine(), getStaticVelocity(), getRadius(), getPauseInUs(), isIJ(), getI(), getJ());
        result = 31 * result + Arrays.hashCode(getStartPosition());
        result = 31 * result + Arrays.hashCode(getFinalPosition());
        result = 31 * result + Arrays.hashCode(getCenterPosition());
        return result;
    }

    @Override
    public String toString() {
        return "Container{" +
                "gCodeType=" + gCodeType +
                ", line='" + line + '\'' +
                ", startPosition=" + Arrays.toString(startPosition) +
                ", finalPosition=" + Arrays.toString(finalPosition) +
                ", staticVelocity=" + staticVelocity +
                ", centerPosition=" + Arrays.toString(centerPosition) +
                ", radius=" + radius +
                ", pauseInUs=" + pauseInUs +
                ", IJ=" + IJ +
                ", I=" + I +
                ", J=" + J +
                '}';
    }
}

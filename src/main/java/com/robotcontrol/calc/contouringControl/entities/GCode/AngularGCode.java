package com.robotcontrol.calc.contouringControl.entities.GCode;

import calc.util.MathCalc;

import java.util.Arrays;
import java.util.Objects;

public class AngularGCode extends MotionGCode {

    private double radius;

    private double[] centerPosition;

    public AngularGCode() {
    }

    public AngularGCode(double[] startPosition, double[] finalPosition,
                        double staticVelocity, double acceleration,
                        String gCode, GCodeType gCodeType,
                        double radius) {
        super(startPosition, finalPosition, staticVelocity, acceleration, gCode, gCodeType);
        this.radius = radius;
        setDistance(MathCalc.angularLength(startPosition, finalPosition, radius));
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public double[] getCenterPosition() {
        return centerPosition;
    }

    public void setCenterPosition(double[] centerPosition) {
        this.centerPosition = centerPosition;
    }

    public void init(){
        setDistance(MathCalc.angularLength(getStartPosition(), getFinalPosition(), radius));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AngularGCode)) return false;
        if (!super.equals(o)) return false;
        AngularGCode that = (AngularGCode) o;
        return Double.compare(that.getRadius(), getRadius()) == 0 &&
                Arrays.equals(getCenterPosition(), that.getCenterPosition());
    }

    @Override
    public int hashCode() {

        int result = Objects.hash(super.hashCode(), getRadius());
        result = 31 * result + Arrays.hashCode(getCenterPosition());
        return result;
    }

    @Override
    public String toString() {
        return "AngularGCode{" +
                "radius=" + radius +
                ", centerPosition=" + Arrays.toString(centerPosition) +
                "} " + super.toString();
    }
}

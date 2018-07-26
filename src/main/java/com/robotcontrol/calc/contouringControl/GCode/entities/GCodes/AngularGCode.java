package com.robotcontrol.calc.contouringControl.GCode.entities.GCodes;

import calc.util.MathCalc;

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


}

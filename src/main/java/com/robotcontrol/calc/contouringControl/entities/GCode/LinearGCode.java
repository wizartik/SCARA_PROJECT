package com.robotcontrol.calc.contouringControl.entities.GCode;

import calc.util.MathCalc;

public class LinearGCode extends MotionGCode {
    private double[] axisDirections;

    public LinearGCode() {
    }

    public LinearGCode(double[] startPosition, double[] finalPosition,
                       double staticVelocity, double acceleration,
                       String gCode, GCodeType gCodeType) {
        super(startPosition, finalPosition, staticVelocity, acceleration, gCode, gCodeType);
        init();
    }

    public void makeAxisVelocities(){
        this.axisDirections = new double[]
                {getFinalPosition()[0] - getStartPosition()[0],
                        getFinalPosition()[1] - getStartPosition()[1],
                        getFinalPosition()[2] - getStartPosition()[2]};
        this.axisDirections = MathCalc.velocityCustomize(this.axisDirections,
                getStaticVelocity());
    }

    public double[] getAxisDirections() {
        return axisDirections;
    }

    public void setAxisDirections(double[] axisDirections) {
        this.axisDirections = axisDirections;
    }

    @Override
    public void setFinalPosition(double[] finalPosition) {
        super.setFinalPosition(finalPosition);
        if (getFinalPosition() != null && getStartPosition() != null){
            init();
        }
    }

    @Override
    public void setStartPosition(double[] startPosition) {
        super.setStartPosition(startPosition);
        if (getFinalPosition() != null && getStartPosition() != null){
            init();
        }
    }

    public void init(){
        setDistance(MathCalc.linearLength(getStartPosition(), getFinalPosition()));
        makeAxisVelocities();
    }
}

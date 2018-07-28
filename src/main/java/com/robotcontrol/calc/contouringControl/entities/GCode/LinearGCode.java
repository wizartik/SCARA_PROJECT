package com.robotcontrol.calc.contouringControl.entities.GCode;


import com.robotcontrol.util.math.Geometry;
import com.robotcontrol.util.math.Physics;

import java.util.Arrays;

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
        this.axisDirections = Physics.velocityCustomize(this.axisDirections,
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
        setDistance(Geometry.linearLength(getStartPosition(), getFinalPosition()));
        makeAxisVelocities();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LinearGCode)) return false;
        if (!super.equals(o)) return false;
        LinearGCode that = (LinearGCode) o;
        return Arrays.equals(getAxisDirections(), that.getAxisDirections());
    }

    @Override
    public int hashCode() {

        int result = super.hashCode();
        result = 31 * result + Arrays.hashCode(getAxisDirections());
        return result;
    }

    @Override
    public String toString() {
        return "LinearGCode{" +
                "axisDirections=" + Arrays.toString(axisDirections) +
                "} " + super.toString();
    }
}

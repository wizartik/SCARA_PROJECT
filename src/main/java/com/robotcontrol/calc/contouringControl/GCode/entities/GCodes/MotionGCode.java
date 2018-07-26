package com.robotcontrol.calc.contouringControl.GCode.entities.GCodes;

import com.robotcontrol.calc.contouringControl.GCode.entities.Point;

import java.util.ArrayList;

public class MotionGCode extends GCode{

    private double[] finalPosition;

    // start and final angular velocities, needed to check transition
    // validation between G codes
    private double[] startAngVelocities;
    private double[] finalAngVelocities;

    //velocity of the static part
    private double staticVelocity;

    private double acceleration;

    private double distance;

    //start with this velocity
    private double startVelocity;
    //end with this velocity
    private double finalVelocity;

    private ArrayList<Point> gCodePath = new ArrayList<>();

    public MotionGCode() {
    }

    /**
     * @param startPosition  start position of the current G code [x, y, z] in cm.
     * @param finalPosition  final position of the current G code [x, y, z] in cm.
     * @param staticVelocity specified velocity of the current G code in cm/s.
     * @param acceleration   acceleration in cm/s^2.
     * @param gCode          G code string.
     */
    public MotionGCode(double[] startPosition, double[] finalPosition,
                       double staticVelocity, double acceleration,
                       String gCode, GCodeType gCodeType) {
        super(startPosition, gCode, gCodeType);

        this.finalPosition = finalPosition;
        this.staticVelocity = staticVelocity;
        this.acceleration = acceleration;
    }

    /**
     * @return start position array of the current G code.
     * [x, y, z].
     */
    public double[] getFinalPosition() {
        return finalPosition;
    }


    /**
     * @return specified staticVelocity for this G code.
     */
    public double getVelocity() {
        return staticVelocity;
    }

    /**
     * @return specified acceleration in cm/s^2.
     */
    public double getAcceleration() {
        return acceleration;
    }

    /**
     * @return total distance traveled in cm.
     */
    public double getDistance() {
        return distance;
    }

    /**
     * Should be initialized first by initialize().
     *
     * @return full path of the current GCode in Points.
     */
    public ArrayList<Point> getgCodePath() {
        return gCodePath;
    }

    public double getStaticVelocity() {
        return staticVelocity;
    }

    public double getStartVelocity() {
        return startVelocity;
    }

    public double getFinalVelocity() {
        return finalVelocity;
    }



    public void setFinalPosition(double[] finalPosition) {
        this.finalPosition = finalPosition;
    }


    public void setStaticVelocity(double staticVelocity) {
        this.staticVelocity = staticVelocity;
    }

    public void setAcceleration(double acceleration) {
        this.acceleration = acceleration;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public void setStartVelocity(double startVelocity) {
        this.startVelocity = startVelocity;
    }

    public void setFinalVelocity(double finalVelocity) {
        this.finalVelocity = finalVelocity;
    }

    public void setgCodePath(ArrayList<Point> gCodePath) {
        this.gCodePath = gCodePath;
    }

    /**
     * @return start angular velocities of the current G code.
     */
    public double[] getStartAngVelocities() {
        return startAngVelocities;
    }

    /**
     * @return final angular velocities of the current G code.
     */
    public double[] getFinalAngVelocities() {
        return finalAngVelocities;
    }

    public void setStartAngVelocities(double[] startAngVelocities) {
        this.startAngVelocities = startAngVelocities;
    }

    public void setFinalAngVelocities(double[] finalAngVelocities) {
        this.finalAngVelocities = finalAngVelocities;
    }
}

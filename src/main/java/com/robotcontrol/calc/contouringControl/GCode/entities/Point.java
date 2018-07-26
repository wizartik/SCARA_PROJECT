package com.robotcontrol.calc.contouringControl.GCode.entities;

import java.util.Arrays;
import java.util.Objects;

public class Point {

    private double[] position;
    private double velocity;
    private double[] axisVelocities;
    private double acceleration;
    private double[] angles;
    private double[] angularVelocities;
    private double time;
    private String GCode;

    public Point() {
    }

    public Point(double[] position, double velocity, double[] axisVelocities,
                 double acceleration, double[] angles, double[] angularVelocities,
                 double time, String GCode) {
        this.position = position;
        this.velocity = velocity;
        this.axisVelocities = axisVelocities;
        this.acceleration = acceleration;
        this.angles = angles;
        this.angularVelocities = angularVelocities;
        this.time = time;
        this.GCode = GCode;
    }

    public double[] getPosition() {
        return position;
    }

    public double getVelocity() {
        return velocity;
    }

    public double[] getAxisVelocities() {
        return axisVelocities;
    }

    public double getAcceleration() {
        return acceleration;
    }

    public double[] getAngles() {
        return angles;
    }

    public double[] getAngularVelocities() {
        return angularVelocities;
    }

    public double getTime() {
        return time;
    }

    public String getGCode() {
        return GCode;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Point)) return false;
        Point point = (Point) o;
        return Double.compare(point.getVelocity(), getVelocity()) == 0 &&
                Double.compare(point.getAcceleration(), getAcceleration()) == 0 &&
                Double.compare(point.getTime(), getTime()) == 0 &&
                Arrays.equals(getPosition(), point.getPosition()) &&
                Arrays.equals(getAxisVelocities(), point.getAxisVelocities()) &&
                Arrays.equals(getAngles(), point.getAngles()) &&
                Arrays.equals(getAngularVelocities(), point.getAngularVelocities()) &&
                Objects.equals(getGCode(), point.getGCode());
    }

    @Override
    public int hashCode() {

        int result = Objects.hash(getVelocity(), getAcceleration(), getTime(), getGCode());
        result = 31 * result + Arrays.hashCode(getPosition());
        result = 31 * result + Arrays.hashCode(getAxisVelocities());
        result = 31 * result + Arrays.hashCode(getAngles());
        result = 31 * result + Arrays.hashCode(getAngularVelocities());
        return result;
    }

    @Override
    public String toString() {
        return "Point{" +
                "position=" + Arrays.toString(position) +
                ", velocity=" + velocity +
                ", axisVelocities=" + Arrays.toString(axisVelocities) +
                ", acceleration=" + acceleration +
                ", angles=" + Arrays.toString(angles) +
                ", angularVelocities=" + Arrays.toString(angularVelocities) +
                ", time=" + time +
                ", GCode='" + GCode + '\'' +
                '}';
    }
}

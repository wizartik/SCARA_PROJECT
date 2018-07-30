package com.robotcontrol.calc.positionalControl.entities;

import java.util.Objects;

public class SingleJointPoint {
    private double angle;

    private long time;

    public SingleJointPoint() {
    }

    public SingleJointPoint(double angle, long time) {
        this.angle = angle;
        this.time = time;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SingleJointPoint)) return false;
        SingleJointPoint that = (SingleJointPoint) o;
        return Double.compare(that.getAngle(), getAngle()) == 0 &&
                getTime() == that.getTime();
    }

    @Override
    public int hashCode() {

        return Objects.hash(getAngle(), getTime());
    }

    @Override
    public String toString() {
        return "SingleJointPoint{" +
                "angle=" + angle +
                ", time=" + time +
                '}';
    }
}

package com.robotcontrol.calc.contouringControl.entities;

import com.robotcontrol.calc.DHParameters.SCARADH;

import java.util.Arrays;
import java.util.Objects;

public class Point {

    private double[] position;
    private long time;

    public Point() {
    }

    public Point(double[] position, long time) {
        this.position = position;
        this.time = time;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public double[] getAngles() {
        return SCARADH.inverseKinematics(position);
    }

    public double[] getPosition() {
        return position;
    }

    public void setPosition(double[] position) {
        this.position = position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Point)) return false;
        Point point = (Point) o;
        return Double.compare(point.getTime(), getTime()) == 0 &&
                Arrays.equals(getPosition(), point.getPosition());
    }

    @Override
    public int hashCode() {

        int result = Objects.hash(getTime());
        result = 31 * result + Arrays.hashCode(getPosition());
        return result;
    }

    @Override
    public String toString() {
        return "Point{" +
                "position=" + Arrays.toString(position) +
                ", time=" + time +
                '}';
    }
}

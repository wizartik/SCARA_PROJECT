package com.robotcontrol.calc.positionalControl.entities;

import java.util.List;
import java.util.Objects;

public class SingleJointPath {
    private List<SingleJointPoint> jointPoints;

    private long time;

    public SingleJointPath() {
    }

    public SingleJointPath(List<SingleJointPoint> jointPoints) {
        this.jointPoints = jointPoints;
    }

    public List<SingleJointPoint> getJointPoints() {
        return jointPoints;
    }

    public void setJointPoints(List<SingleJointPoint> jointPoints) {
        this.jointPoints = jointPoints;
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
        if (!(o instanceof SingleJointPath)) return false;
        SingleJointPath that = (SingleJointPath) o;
        return getTime() == that.getTime() &&
                Objects.equals(getJointPoints(), that.getJointPoints());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getJointPoints(), getTime());
    }

    @Override
    public String toString() {
        return "SingleJointPath{" +
                "jointPoints=" + jointPoints +
                ", time=" + time +
                '}';
    }
}

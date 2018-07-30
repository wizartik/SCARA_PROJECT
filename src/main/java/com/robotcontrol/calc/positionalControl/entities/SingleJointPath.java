package com.robotcontrol.calc.positionalControl.entities;

import java.util.List;
import java.util.Objects;

public class SingleJointPath {
    private List<SingleJointPoint> stepperPath;

    private long time;

    public SingleJointPath() {
    }

    public SingleJointPath(List<SingleJointPoint> stepperPath) {
        this.stepperPath = stepperPath;
    }

    public List<SingleJointPoint> getStepperPath() {
        return stepperPath;
    }

    public void setStepperPath(List<SingleJointPoint> stepperPath) {
        this.stepperPath = stepperPath;
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
                Objects.equals(getStepperPath(), that.getStepperPath());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getStepperPath(), getTime());
    }

    @Override
    public String toString() {
        return "SingleJointPath{" +
                "stepperPath=" + stepperPath +
                ", time=" + time +
                '}';
    }
}

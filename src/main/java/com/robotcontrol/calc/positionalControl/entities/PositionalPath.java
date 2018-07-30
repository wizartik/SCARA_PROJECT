package com.robotcontrol.calc.positionalControl.entities;

import com.robotcontrol.util.Utility;

import java.util.Arrays;
import java.util.Objects;

public class PositionalPath {

    private SingleJointPath[] path;

    private long fullTime;
    public PositionalPath() {
    }

    public PositionalPath(SingleJointPath[] path) {
        this.path = path;

        fullTime = Utility.findLargest(path[0].getTime(), path[1].getTime(),
                path[2].getTime());
    }

    public SingleJointPath[] getPath() {
        return path;
    }

    public void setPath(SingleJointPath[] path) {
        this.path = path;
    }

    public long getFullTime() {
        return fullTime;
    }

    public void setFullTime(long fullTime) {
        this.fullTime = fullTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PositionalPath)) return false;
        PositionalPath that = (PositionalPath) o;
        return getFullTime() == that.getFullTime() &&
                Arrays.equals(getPath(), that.getPath());
    }

    @Override
    public int hashCode() {

        int result = Objects.hash(getFullTime());
        result = 31 * result + Arrays.hashCode(getPath());
        return result;
    }

    @Override
    public String toString() {
        return "PositionalPath{" +
                "path=" + Arrays.toString(path) +
                ", fullTime=" + fullTime +
                '}';
    }
}

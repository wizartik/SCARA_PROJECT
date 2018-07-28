package com.robotcontrol.calc.contouringControl.entities.path;

import com.robotcontrol.calc.contouringControl.entities.GCode.GCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Path {
    private List<GCode> gCodeList;

    private double fullDistance;

    private long fullTime;

    public Path() {
    }

    public Path(ArrayList<GCode> gCodeList) {
        this.gCodeList = gCodeList;
    }

    public List<GCode> getgCodeList() {
        return gCodeList;
    }

    public void setgCodeList(List<GCode> gCodeList) {
        this.gCodeList = gCodeList;
    }

    public double getFullDistance() {
        return fullDistance;
    }

    public void setFullDistance(double fullDistance) {
        this.fullDistance = fullDistance;
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
        if (!(o instanceof Path)) return false;
        Path path = (Path) o;
        return Double.compare(path.getFullDistance(), getFullDistance()) == 0 &&
                Double.compare(path.getFullTime(), getFullTime()) == 0 &&
                Objects.equals(getgCodeList(), path.getgCodeList());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getgCodeList(), getFullDistance(), getFullTime());
    }

    @Override
    public String toString() {
        return "Path{" +
                "fullDistance=" + fullDistance +
                ", fullTime=" + fullTime +
                '}';
    }
}

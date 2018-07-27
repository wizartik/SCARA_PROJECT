package com.robotcontrol.calc.contouringControl.entities.path;

import com.robotcontrol.calc.contouringControl.entities.GCode.GCode;

import java.util.List;

public class Path {
    private List<GCode> gCodeList;

    private double fullDistance;

    private double fullTime;

    public Path(List<GCode> gCodeList) {
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

    public double getFullTime() {
        return fullTime;
    }

    public void setFullTime(double fullTime) {
        this.fullTime = fullTime;
    }
}

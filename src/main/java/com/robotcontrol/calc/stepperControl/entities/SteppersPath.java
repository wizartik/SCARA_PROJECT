/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.robotcontrol.calc.stepperControl.entities;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class SteppersPath {

    private List<SteppersPoint> steppersPoints;

    private int[] steps = new int[3];

    public SteppersPath() {
    }

    public SteppersPath(List<SteppersPoint> steppersPoints) {
        this.steppersPoints = steppersPoints;
    }

    public List<SteppersPoint> getSteppersPoints() {
        return steppersPoints;
    }

    public void setSteppersPoints(List<SteppersPoint> steppersPoints) {
        this.steppersPoints = steppersPoints;
    }

    public int[] getSteps() {
        return steps;
    }

    public void setSteps(int[] steps) {
        this.steps = steps;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SteppersPath)) return false;
        SteppersPath that = (SteppersPath) o;
        return Objects.equals(getSteppersPoints(), that.getSteppersPoints()) &&
                Arrays.equals(getSteps(), that.getSteps());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(getSteppersPoints());
        result = 31 * result + Arrays.hashCode(getSteps());
        return result;
    }

    @Override
    public String toString() {
        return "SteppersPath{" +
                "steppersPoints=" + steppersPoints +
                ", steps=" + Arrays.toString(steps) +
                '}';
    }
}

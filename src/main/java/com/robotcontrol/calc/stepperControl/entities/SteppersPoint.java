/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.robotcontrol.calc.stepperControl.entities;

import java.util.Arrays;

public class SteppersPoint {

    private int[] stepsDelays;

    public SteppersPoint() {
    }

    public SteppersPoint(int[] stepsDelays) {
        this.stepsDelays = stepsDelays;
    }

    public int[] getStepsDelays() {
        return stepsDelays;
    }

    public void setStepsDelays(int[] stepsDelays) {
        this.stepsDelays = stepsDelays;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SteppersPoint)) return false;
        SteppersPoint that = (SteppersPoint) o;
        return Arrays.equals(getStepsDelays(), that.getStepsDelays());
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(getStepsDelays());
    }

    @Override
    public String toString() {
        return "SteppersPoint{" +
                "stepsDelays=" + Arrays.toString(stepsDelays) +
                '}';
    }
}

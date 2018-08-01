/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.robotcontrol.calc.stepperControl.entities;

import java.util.Objects;

public class Remaining {
    private int timeRemaining;
    private double angleRemaining;

    public Remaining() {
    }

    public Remaining(int timeRemaining, double angleRemaining) {
        this.timeRemaining = timeRemaining;
        this.angleRemaining = angleRemaining;
    }

    public int getTimeRemaining() {
        return timeRemaining;
    }

    public void setTimeRemaining(int timeRemaining) {
        this.timeRemaining = timeRemaining;
    }

    public double getAngleRemaining() {
        return angleRemaining;
    }

    public void setAngleRemaining(double angleRemaining) {
        this.angleRemaining = angleRemaining;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Remaining)) return false;
        Remaining remaining = (Remaining) o;
        return getTimeRemaining() == remaining.getTimeRemaining() &&
                Double.compare(remaining.getAngleRemaining(), getAngleRemaining()) == 0;
    }

    @Override
    public int hashCode() {

        return Objects.hash(getTimeRemaining(), getAngleRemaining());
    }

    @Override
    public String toString() {
        return "Remaining{" +
                "timeRemaining=" + timeRemaining +
                ", angleRemaining=" + angleRemaining +
                '}';
    }
}

package com.robotcontrol.calc.contouringControl.entities.GCode;

import java.util.Objects;

public class PauseGCode extends GCode {
    private double pauseInUs;

    public PauseGCode() {
    }

    public PauseGCode(double[] startPosition, String gCode, GCodeType gCodeType, double pauseInUs) {
        super(startPosition, gCode, gCodeType);
        this.pauseInUs = pauseInUs;
    }

    public double getPauseInUs() {
        return pauseInUs;
    }

    public void setPauseInUs(double pauseInUs) {
        this.pauseInUs = pauseInUs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PauseGCode)) return false;
        if (!super.equals(o)) return false;
        PauseGCode that = (PauseGCode) o;
        return Double.compare(that.getPauseInUs(), getPauseInUs()) == 0;
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), getPauseInUs());
    }

    @Override
    public String toString() {
        return "PauseGCode{" +
                "pauseInUs=" + pauseInUs +
                "} " + super.toString();
    }
}

package com.robotcontrol.calc.contouringControl.entities.GCode;

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
}

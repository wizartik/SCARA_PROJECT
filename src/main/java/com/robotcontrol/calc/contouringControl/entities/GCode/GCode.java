package com.robotcontrol.calc.contouringControl.entities.GCode;

public class GCode {

    private double startTime;
    private double finalTime;

    private double[] startPosition;

    private double previousVelocity;
    private double nextVelocity;

    private String gCode;

    private GCodeType gCodeType;


    public GCode() {
    }

    /**
     * @param startPosition  start position of the current G code [x, y, z] in cm.
     * @param gCode          G code string.
     * @param gCodeType type of the G code.
     */
    public GCode(double[] startPosition, String gCode, GCodeType gCodeType) {
        this.startPosition = startPosition;
        this.gCode = gCode;
        this.gCodeType = gCodeType;
    }

    /**
     * @return start time of the current G code in μs.
     */
    public double getStartTime() {
        return startTime;
    }

    /**
     * @return final time of the current G code in μs.
     */
    public double getFinalTime() {
        return finalTime;
    }

    /**
     * @return start position array of the current G code.
     * [x, y, z].
     */
    public double[] getStartPosition() {
        return startPosition;
    }


    /**
     * @return static velocity of the previous G code.
     */
    public double getPreviousVelocity() {
        return previousVelocity;
    }

    /**
     * @return static velocity of the next G code.
     */
    public double getNextVelocity() {
        return nextVelocity;
    }

    /**
     * @return string of current GCode.
     */
    public String getGCode() {
        return gCode;
    }

    /**
     * @return type of the G code.
     */
    public GCodeType getGCodeType() {
        return gCodeType;
    }

    public void setStartTime(double startTime) {
        this.startTime = startTime;
    }

    public void setFinalTime(double finalTime) {
        this.finalTime = finalTime;
    }

    public void setStartPosition(double[] startPosition) {
        this.startPosition = startPosition;
    }

    public void setPreviousVelocity(double previousVelocity) {
        this.previousVelocity = previousVelocity;
    }

    public void setNextVelocity(double nextVelocity) {
        this.nextVelocity = nextVelocity;
    }

    public void setgCode(String gCode) {
        this.gCode = gCode;
    }

    public void setgCodeType(GCodeType gCodeType) {
        this.gCodeType = gCodeType;
    }
}

package com.robotcontrol.parameters.constant;

/**
 * Physical parameters of the robot.
 * All dimensions are in cm.
 */
public class PhysicalParameters {
    /**
     * Length of the first link for drawing.
     */
    public final static double FIRST_LINK_DRAWING = 26.4;

    /**
     * Length of the second link for drawing.
     */
    public final static double SECOND_LINK_DRAWING = 14;

    /**
     * Length of the first ling for selecting, assembling, pick and place, etc.
     */
    public final static double FIRST_LINK_SELECTIVE = 26.4;

    /**
     * Length of the second ling for selecting, assembling, pick and place, etc.
     */
    public final static double SECOND_LINK_SELECTIVE = 18;

    /**
     * Height of the working area.
     */
    public final static double HEIGHT = 20;

    /**
     * Height that will be increased per one revolution in Z axis in cm.
     */
    public final static double INCREMENT_PER_REVOLUTION = 0.8;

    /**
     * One step of the stepper motor in degrees.
     */
    public final static double STEP = 1.8;


    public final static double[] REDUCTION_RATIO = new double[]{14.0625, 14.0625, 1};
}

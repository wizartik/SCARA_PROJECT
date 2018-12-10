package com.robotcontrol.parameters.constant;

public class Safety {

    /**
     * Maximum allowed height working body can lift.
     */
    public final static double MAX_HEIGHT_COORD = 30;

    /**
     * Minimum allowed height working body can lift.
     */
    public final static double MIN_HEIGHT_COORD = 0;

    /**
     * Maximum allowed operating distance for drawing.
     */
    public final static double MAX_RADIUS_DRAWING = 44.41;

    /**
     * Minimum allowed operating distance for drawing.
     */
    public final static double MIN_RADIUS_DRAWING = 11;

    /**
     * Maximum allowed operating distance for pick and place.
     */
    public final static double MAX_RADIUS_SELECTIVE = 44.41;

    /**
     * Minimum allowed operating distance for pick and place.
     */
    public final static double MIN_RADIUS_SELECTIVE = 11;

    /**
     * Maximum angular velocity difference among 2 G codes utmost angular
     * velocities. Bigger difference will cause serious influence
     * on trajectory.
     */
    public final static double MAX_VELOCITY_DIFFERENCE = 1;

    public final static double[] MAX_ANGLES = {Double.MAX_VALUE, 3, 200};

    public final static double[] MIN_ANGLES = {-Double.MAX_VALUE, -3, 0};

}

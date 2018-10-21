package com.robotcontrol.parameters.constant;

public class Safety {

    /**
     * Maximum allowed height working body can lift.
     */
    public final static double MAX_HEIGHT_COORD = 200;

    /**
     * Minimum allowed height working body can lift.
     */
    public final static double MIN_HEIGHT_COORD = -100;

    /**
     * Maximum allowed operating distance for drawing.
     */
    public final static double MAX_RADIUS_DRAWING = 4000;

    /**
     * Minimum allowed operating distance for drawing.
     */
    public final static double MIN_RADIUS_DRAWING = -100;

    /**
     * Maximum allowed operating distance for pick and place.
     */
    public final static double MAX_RADIUS_SELECTIVE = 400;

    /**
     * Minimum allowed operating distance for pick and place.
     */
    public final static double MIN_RADIUS_SELECTIVE = 0;

    /**
     * Maximum angular velocity difference among 2 G codes utmost angular
     * velocities. Bigger difference will cause serious influence
     * on trajectory.
     */
    public final static double MAX_VELOCITY_DIFFERENCE = 0.3;

}

package com.robotcontrol.parameters.constant;

public class Motion {

    /**
     * Time Gap for calculations in microseconds.
     */
    public final static long TIME_GAP = 250;

    /**
     * Maximum allowed acceleration due to big torque in cm/s^2.
     */
    public final static double MAX_ACCELERATION = 8;

    /**
     * Normal acceleration if initial acceleration is not specified (in cm/s^2).
     */
    public final static double NORMAL_ACCELERATION = 2;

    /**
     * Maximum allowed velocity for no-load G00 in cm/s.
     */
    public final static double MAX_VELOCITY = 2;

    /**
     * Normal velocity if initial velocity is not specified (in cm/s).
     */
    public final static double NORMAL_VELOCITY = 1;

    public final static double[] NORMAL_ANG_VELOCITIES = {0.5, 1, 20};

    public final static double[] MAX_ANG_VELOCITIES = {2, 2, 12};

    public final static double[] NORMAL_ANG_ACCELERATIONS = {1, 2, 150};

    public final static double MAX_ANG_ACCELERATION = 2;

}

package com.robotcontrol.parameters.dynamic;

public class Motion {

    /**
     * Number of microsteps per step.
     */
    public static byte MICROSTEPS[] = {8, 8, 2};

    public static double[] ANG_VELOCITIES = com.robotcontrol.parameters.constant.Motion.NORMAL_ANG_VELOCITIES;

    public static double[] ANG_ACCELERATIONS = com.robotcontrol.parameters.constant.Motion.NORMAL_ANG_ACCELERATIONS;

    public static boolean MOVING = false;

}

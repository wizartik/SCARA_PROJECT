package com.robotcontrol.parameters.dynamic;

public class Motion {

    /**
     * Number of microsteps per step.
     */
    public static byte MICROSTEPS[] = {8, 8, 1};

    public static double ANG_VELOCITY = com.robotcontrol.parameters.constant
            .Motion.NORMAL_ANG_VELOCITY;

    public static double ANG_ACCELERATION = com.robotcontrol.parameters.constant
            .Motion.NORMAL_ANG_ACCELERATION;

    public static boolean MOVING = false;
}

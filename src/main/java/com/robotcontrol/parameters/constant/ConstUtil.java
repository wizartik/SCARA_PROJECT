package com.robotcontrol.parameters.constant;

import static com.robotcontrol.parameters.constant.Motion.MAX_ACCELERATION;
import static com.robotcontrol.parameters.constant.Motion.MAX_VELOCITY;

/**
 * Created by Valerii on 03.09.2017.
 */
public class ConstUtil {

    /**
     * Allowed filename extensions. If this array doesn't contain
     * extension of the file, that file may be not supported.
     */
    public final static String[] ALLOWED_FILENAME_EXTENSIONS = {"txt",
            "gcode",
            "ngc"};

    public final static char[] KNOWN_COMMANDS = {'G', 'X', 'Y', 'Z', 'F',
            'R', 'I', 'J', 'P', 'U'};

    /**
     * Minimum allowed length of the G code. It's the length of the
     * acceleration or deceleration from 0 to max speed.
     */
    public final static double MIN_LENGTH = Math.pow(MAX_VELOCITY, 2)
            / (2 * MAX_ACCELERATION);


    public final static double MAX_SINGLE_LENGTH = 10;

}

package com.robotcontrol.parameters.dynamic;

import java.util.Arrays;

public class Position {
    /**
     * Coordinates of the home position.
     */
    public static double[] HOME_COORDS = {10, 10, 10};

    public static double[] DESIRED_POSITION = HOME_COORDS;


    /**
     * Current position of the working body.
     */
    public static double[] CURRENT_POSITION = Arrays.copyOf(HOME_COORDS, HOME_COORDS.length);

}

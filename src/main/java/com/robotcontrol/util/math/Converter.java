package com.robotcontrol.util.math;

import java.util.Arrays;

public class Converter {
    /**
     * @param inUs time in microseconds.
     * @return time in seconds.
     */
    public static double toSec(double inUs) {
        return inUs / 1000_000;
    }

    /**
     * @param inSec time in seconds.
     * @return time in microseconds.
     */
    public static double toUSec(double inSec) {
        return inSec * 1000_000;
    }

    /**
     * @param inMPerUs velocity in m/us.
     * @return velocity in m/s.
     */
    public static double toMPerSec(double inMPerUs) {
        return inMPerUs * 1000_000;
    }

    /**
     * @param inMPerSec velocity in m/us.
     * @return velocity in m/s.
     */
    public static double toMPerUSec(double inMPerSec) {
        return inMPerSec / 1000_000;
    }


    /**
     * Transforms velocity value from notation, which is used in G code to
     * one that is used in program.
     *
     * @param external value that need to be converted in (cm/m).
     * @return converted value in (cm/s).
     */
    public static double toInternalVel(double external){
        return external / 60;
    }

    /**
     * Transforms velocity value from notation, which is used in program to
     * one that is used in G code.
     *
     * @param internal value that need to be converted in (cm/s).
     * @return converted value in (cm/m).
     */
    public static double toExternalVel(double internal){
        return internal * 60;
    }


    public static double[] toRad(double[] degrees){
        return Arrays.stream(degrees).map(Math::toRadians).toArray();
    }

    public static double[] toDegrees(double[] rads){
        return Arrays.stream(rads).map(Math::toDegrees).toArray();
    }

}

package com.robotcontrol.util;

import com.robotcontrol.calc.contouringControl.entities.GCode.GCodeType;
import com.robotcontrol.calc.contouringControl.entities.Point;

import java.util.Arrays;

public class Utility {

    /**
     * Creates Point by given data.
     *
     * @param position       coordinates in m.
     * @param time           current time in us.
     * @return new initialized point.
     */
    public static Point makePoint(double[] position, long time, boolean createNewArray) {
        double[] coords = createNewArray ? Arrays.copyOf(position, position.length) : position;

        return new Point(coords, time);
    }


    /**
     * Checks if string is a number.
     *
     * @param str string to check
     * @return true if string is a number, false - if not.
     */
    public static boolean isNumeric(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        int i = 0;
        if (str.charAt(0) == '-') {
            if (str.length() > 1) {
                i++;
            } else {
                return false;
            }
        }
        for (; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i)) && str.charAt(i) != '.') {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks G code type by number.
     *
     * @param number number of G code
     * @return GCodeType.
     */
    public static GCodeType gCodeByNumber(double number) {
        GCodeType gCodeType = GCodeType.GARBAGE;

        if (number == 0) {
            gCodeType = GCodeType.G00;
        } else if (number == 1) {
            gCodeType = GCodeType.G01;
        } else if (number == 2) {
            gCodeType = GCodeType.G02;
        } else if (number == 3) {
            gCodeType = GCodeType.G03;
        } else if (number == 4) {
            gCodeType = GCodeType.G04;
        }

        return gCodeType;
    }

    public static String makeG01String(double[] finalCoords, double velocity) {
        return "G01 X" +
                finalCoords[0] +
                " Y" +
                finalCoords[1] +
                " Z" +
                finalCoords[2] +
                " F" +
                velocity;
    }

    public static String makeG02String(double[] finalCoords, double radius,
                                       double velocity) {
        return "G02 X" +
                finalCoords[0] +
                " Y" +
                finalCoords[1] +
                " Z" +
                finalCoords[2] +
                " R" +
                radius +
                " F" +
                velocity;
    }

    public static String makeG03String(double[] finalCoords, double radius,
                                       double velocity) {
        return "G03 X" +
                finalCoords[0] +
                " Y" +
                finalCoords[1] +
                " Z" +
                finalCoords[2] +
                " R" +
                radius +
                " F" +
                velocity * 60;
    }

    /**
     * Finds largest of three numbers and returns it.
     *
     * @param x first number.
     * @param y second number.
     * @param z third number.
     * @return largest number;
     */
    public static long findLargest(long x, long y, long z) {
        if (x >= y && x >= z) {
            return x;
        } else if (y >= x && y >= z) {
            return y;
        } else {
            return z;
        }
    }

    /**
     * Transforms array of Long to array of long.
     *
     * @param array Long array.
     * @return long array.
     */
    public static long[] wrappedToPrimitive(Long[] array) {
        long[] a = new long[array.length];

        for (int i = 0; i < a.length; i++) {
            a[i] = array[i];
        }
        return a;
    }

    public static boolean containsNaN(double[] arr) {
        for (double anArr : arr) {
            if (Double.isNaN(anArr)) {
                return true;
            }
        }
        return false;
    }
}

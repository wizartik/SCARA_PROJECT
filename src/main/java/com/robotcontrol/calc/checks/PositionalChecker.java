/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.robotcontrol.calc.checks;

import com.robotcontrol.exc.BoundsViolation;
import com.robotcontrol.parameters.constant.Safety;

import static com.robotcontrol.parameters.dynamic.Position.HOME_COORDS;

public class PositionalChecker {

    public static void checkPositionalPathAngular(double[] startAngles,
                                                  double[] finalAngles) throws BoundsViolation {
        checkPositionalPath(startAngles, finalAngles);
    }

    public static void checkCoords(double[] coords) throws BoundsViolation {
        checkPositionalPath(HOME_COORDS, coords);
    }

    public static void checkPositionalPath(double[] startPosition,
                                           double[] finalPosition) throws BoundsViolation {

            double[] startCoords = new double[]{startPosition[0],
                                                finalPosition[1],
                                                0};
            double[] finalCoords = new double[]{startCoords[0],
                                                finalPosition[1],
                                                0};

            checkLength(startCoords, finalCoords);
            checkHeight(startPosition[2]);
            checkHeight(finalPosition[2]);
    }


    private static void checkLength(double[] startCoords, double[] finalCoords)
            throws BoundsViolation {
        GCodeChecker.checkLength(startCoords, finalCoords, "");
    }

    private static void checkHeight(double height) throws BoundsViolation {
        if (height > Safety.MAX_HEIGHT_COORD) {
            throw new BoundsViolation("G code tries to violate allowed " +
                    "bounds. Maximum height of working area is " +
                    Safety.MAX_HEIGHT_COORD);
        }

        if (height < Safety.MIN_HEIGHT_COORD) {
            throw new BoundsViolation("G code tries to violate allowed " +
                    "bounds. Minimum height of working area is " + Safety.MIN_HEIGHT_COORD);
        }
    }
}

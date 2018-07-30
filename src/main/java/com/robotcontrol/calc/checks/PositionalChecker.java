/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.robotcontrol.calc.checks;

import com.robotcontrol.calc.DHParameters.SCARADH;
import com.robotcontrol.exc.BoundsViolation;
import com.robotcontrol.parameters.constant.Safety;
import com.robotcontrol.parameters.dynamic.DynSafety;
import com.robotcontrol.util.math.Geometry;

public class PositionalChecker {

    public static void checkPositionalPathAngular(double[] startAngles,
                                                  double[] finalAngles) throws BoundsViolation {
        checkPositionalPath(SCARADH.inverseKinematics(startAngles),
                SCARADH.inverseKinematics(finalAngles));
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
        double[] zero = new double[]{0, 0, 0};

        if (Geometry.linearLength(zero, startCoords) > DynSafety.MAX_RADIUS
                || Geometry.linearLength(zero, finalCoords) > DynSafety.MAX_RADIUS) {
            throw new BoundsViolation("G code tries to violate allowed " +
                    "bounds. Maximum radius of working area is " +
                    DynSafety.MAX_RADIUS);
        }

        if (Geometry.linearLength(zero, startCoords) < DynSafety.MIN_RADIUS
                || Geometry.linearLength(zero, finalCoords) < DynSafety.MIN_RADIUS) {
            throw new BoundsViolation("G code tries to violate allowed " +
                    "bounds. Minimum radius of working area is " +
                    DynSafety.MAX_RADIUS);
        }
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
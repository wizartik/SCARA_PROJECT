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

import static com.robotcontrol.parameters.dynamic.DynSafety.MIN_RADIUS;
import static com.robotcontrol.parameters.dynamic.Position.HOME_COORDS;
import static java.lang.Math.abs;

public class PositionalChecker {

    public static void checkPositionalPathAngular(double[] startAngles,
                                                  double[] finalAngles) throws BoundsViolation {

        checkPositionalPath(SCARADH.forwardKinematics(startAngles), SCARADH.forwardKinematics(finalAngles));
        checkAngles(startAngles);
        checkAngles(finalAngles);
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

        checkCollision(startCoords, finalCoords, "");
        GCodeChecker.checkUtmostPoints(startCoords, "");
        GCodeChecker.checkUtmostPoints(finalCoords, "");
        checkLength(startCoords, finalCoords);
        checkHeight(startPosition[2]);
        checkHeight(finalPosition[2]);
    }

    static void checkCollision(double[] startCoords, double[] finalCoords, String gCode) throws BoundsViolation {
        String errorMessage = "G code intersects minimum allowed radius and cannot be performed!";

        double rr = MIN_RADIUS * MIN_RADIUS;

        double x01 = startCoords[0];
        double y01 = startCoords[1];

        double x02 = finalCoords[0];
        double y02 = finalCoords[1];

        if ((x01) * (x01) + (y01) * (y01) <= rr) {
            throw new BoundsViolation(errorMessage, gCode);
        }
        if ((x02) * (x02) + (y02) * (y02) <= rr) {
            throw new BoundsViolation(errorMessage, gCode);
        }

        if (x01 == x02) {
            if ((y01 < 0 && y02 > 0 || y01 > 0 && y02 < 0) && abs(x01) <= MIN_RADIUS) {
                throw new BoundsViolation(errorMessage, gCode);
            }
        }
        if (y01 == y02) {
            if ((x01 < 0 && x02 > 0 || x01 > 0 && x02 < 0) && abs(y01) <= MIN_RADIUS){
                throw new BoundsViolation(errorMessage, gCode);
            }
        }

        double a = (y01 - y02) / (x01 - x02);
        double b = y01 - a * x01;
        double xp = (-b) / (a + 1 / a);
        double yp = a * xp + b;

        if (x01 < xp && x02 > xp || x02 < xp && x01 > xp)
            if ((xp) * (xp) + (yp) * (yp) <= rr){
                throw new BoundsViolation(errorMessage, gCode);
            }
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

    private static void checkAngles(double[] angles) throws BoundsViolation {
        for (int i = 0; i < angles.length; i++) {
            if (angles[i] > Safety.MAX_ANGLES[i] || angles[i] < Safety.MIN_ANGLES[i]) {
                throw new BoundsViolation("Angle of motor " + (i + 1) + "is out of allowed bounds " + angles[i]
                        + " allowed bounds are MAX = " + Safety.MAX_ANGLES[i] + " MIN = " + Safety.MIN_ANGLES[i]);
            }
        }
    }
}

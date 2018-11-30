package com.robotcontrol.calc.checks;

import com.robotcontrol.calc.contouringControl.entities.GCode.AngularGCode;
import com.robotcontrol.calc.contouringControl.entities.GCode.GCode;
import com.robotcontrol.calc.contouringControl.entities.GCode.GCodeType;
import com.robotcontrol.calc.contouringControl.entities.GCode.MotionGCode;
import com.robotcontrol.exc.BoundsViolation;
import com.robotcontrol.parameters.constant.Motion;
import com.robotcontrol.parameters.constant.Safety;
import com.robotcontrol.parameters.dynamic.DynSafety;
import com.robotcontrol.util.math.Geometry;

import static com.robotcontrol.calc.checks.PositionalChecker.checkCollision;

public class GCodeChecker {

    /**
     * Checks if G-code is suitable to perform specified movement. If it has points out of the working area, special
     * exception is thrown. If its velocity is not suitable it will be changed to normal value.
     *
     * @param gCode G-code to be checked.
     * @throws BoundsViolation if G-code has points out of the working area.
     */
    public static void checkGCode(GCode gCode) throws BoundsViolation {
        checkUtmostPoints(gCode);
        checkVelocity(gCode);

        if (gCode instanceof MotionGCode){
            checkCollision(gCode.getStartPosition(), ((MotionGCode) gCode).getFinalPosition(), gCode.getGCode());
        }

        if (gCode instanceof AngularGCode){
            checkAngularGCode((AngularGCode) gCode);
        }
    }

    /**
     * Checks utmost points of the G-code if one of them is out of the working area BoundsViolation exception is thrown.
     *
     * @param gCode G-code to be checked.
     * @throws BoundsViolation if one of the utmost points of the G-code is out of the working area.
     */
    private static void checkUtmostPoints(GCode gCode) throws BoundsViolation {

        if (gCode instanceof MotionGCode) {

            double[] startCoords = new double[]{gCode.getStartPosition()[0],
                                                gCode.getStartPosition()[1],
                                                0};
            double[] finalCoords = new double[]{((MotionGCode) gCode).getFinalPosition()[0],
                                                ((MotionGCode) gCode).getFinalPosition()[1],
                                                0};


            checkUtmostPoints(startCoords, gCode.getGCode());
            checkUtmostPoints(finalCoords, gCode.getGCode());
            checkLength(startCoords, finalCoords, gCode.getGCode());
            checkHeight(gCode.getStartPosition()[2], gCode.getGCode());
            checkHeight(((MotionGCode) gCode).getFinalPosition()[2], gCode.getGCode());
        }
    }

    /**
     * Checks given points to be in the working area by calculating length from point to the [0,0,0] coordinate
     * without height (z coordinate).
     *
     * @param startCoords first point.
     * @param finalCoords second point.
     * @param gCode G-code to be added to exception message if exception is thrown.
     * @throws BoundsViolation if point is out of working area.
     */
    static void checkLength(double[] startCoords, double[] finalCoords,
                                    String gCode) throws BoundsViolation {
        double[] zero = new double[]{0, 0, 0};

        double startLength = Geometry.linearLength(zero, startCoords);
        double finalLength = Geometry.linearLength(zero, finalCoords);

        if ((startLength > DynSafety.MAX_RADIUS) || (finalLength > DynSafety.MAX_RADIUS)) {
            throw new BoundsViolation("G code tries to violate allowed " +
                                      "bounds. Maximum radius of working area is " +
                                      DynSafety.MAX_RADIUS, gCode);
        }

        if ((startLength < DynSafety.MIN_RADIUS) || (finalLength < DynSafety.MIN_RADIUS)) {
            throw new BoundsViolation("G code tries to violate allowed " +
                                      "bounds. Minimum radius of working area is " +
                                      DynSafety.MIN_RADIUS, gCode);
        }
    }

    static void checkUtmostPoints(double[] coords, String gCode) throws BoundsViolation {
        if (coords[0] >= DynSafety.MAX_RADIUS || coords[1] >= DynSafety.MAX_RADIUS){
            throw new BoundsViolation("G code tries to violate allowed " +
                    "bounds. Maximum radius of working area is " +
                    DynSafety.MAX_RADIUS + " coordinates are (X = " + coords[0] + "; Y = " + coords[1] + ")", gCode);
        }
    }

    /**
     * Checks height to be not bigger and not less than allowed one by working area (uses only z coordinate).
     *
     * @param height height of the point to be checked.
     * @param gCode G-code to be added to exception message if exception is thrown.
     * @throws BoundsViolation if point is out of working area.
     */
    private static void checkHeight(double height, String gCode) throws BoundsViolation {
        if (height > Safety.MAX_HEIGHT_COORD) {
            throw new BoundsViolation("G code tries to violate allowed " +
                                    "bounds. Maximum height of working area is " +
                                    Safety.MAX_HEIGHT_COORD, gCode);
        }

        if (height < Safety.MIN_HEIGHT_COORD) {
            throw new BoundsViolation("G code tries to violate allowed " +
                                    "bounds. Minimum height of working area is " +
                                    Safety.MIN_HEIGHT_COORD, gCode);
        }
    }

    /**
     * Checks G-code's velocity to be not negative and not bigger than allowed, if so makes it positive
     * or equals normal value for specific G-code.
     *
     * @param gCode G-code to be checked.
     */
    private static void checkVelocity(GCode gCode) {
        if (gCode instanceof MotionGCode) {
            double velocity = ((MotionGCode) gCode).getStaticVelocity();
            if (velocity <= 0){
                ((MotionGCode) gCode).setStaticVelocity(-velocity);
            }

            if (velocity >= Motion.MAX_VELOCITY){
                if (gCode.getGCodeType() == GCodeType.G00){
                    ((MotionGCode) gCode).setStaticVelocity(Motion.MAX_VELOCITY);
                } else {
                    ((MotionGCode) gCode).setStaticVelocity(Motion.NORMAL_VELOCITY);
                }
            }
        }
    }

    // TODO: 29.07.2018
    private static void checkAngularGCode(AngularGCode gCode){

    }
}

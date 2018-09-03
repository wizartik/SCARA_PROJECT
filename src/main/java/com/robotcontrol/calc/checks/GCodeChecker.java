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

public class GCodeChecker {
    public static void checkGCode(GCode gCode) throws BoundsViolation {
        checkUtmostPoints(gCode);
        checkVelocity(gCode);

        if (gCode instanceof AngularGCode){
            checkAngularGCode((AngularGCode) gCode);
        }
    }

    private static void checkUtmostPoints(GCode gCode) throws BoundsViolation {

        if (gCode instanceof MotionGCode) {

            double[] startCoords = new double[]{gCode.getStartPosition()[0],
                                                gCode.getStartPosition()[1],
                                                0};
            double[] finalCoords = new double[]{((MotionGCode) gCode).getFinalPosition()[0],
                                                ((MotionGCode) gCode).getFinalPosition()[1],
                                                0};

            checkLength(startCoords, finalCoords, gCode.getGCode());
            checkHeight(gCode.getStartPosition()[2], gCode.getGCode());
            checkHeight(((MotionGCode) gCode).getFinalPosition()[2], gCode.getGCode());
        }
    }

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
                                      DynSafety.MAX_RADIUS, gCode);
        }
    }

    static void checkHeight(double height, String gCode) throws BoundsViolation {
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

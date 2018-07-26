package com.robotcontrol.calc.contouringControl.GCode.controllers;

import calc.data.Constants;
import calc.util.MathCalc;
import calc.util.Utility;
import com.robotcontrol.calc.contouringControl.GCode.entities.GCodes.LinearGCode;
import exc.BoundsViolation;
import exc.ImpossibleToImplement;

import static java.lang.Math.abs;

public class G01Handler {

    public static void calcPath(LinearGCode gCode, double startTime)
            throws BoundsViolation, ImpossibleToImplement {
        initialize(gCode);
        calculate(gCode, startTime);
    }

    /**
     * Initializes data needed to calculate path.
     */
    private static void initialize(LinearGCode gCode) {

        gCode.setStartVelocity(MathCalc.makeUtmostVelocity(gCode.getStaticVelocity(), gCode.getPreviousVelocity()));

        gCode.setFinalVelocity(MathCalc.makeUtmostVelocity(gCode.getStaticVelocity(), gCode.getNextVelocity()));

        gCode.setStartAngVelocities(MathCalc.makeAngVelocities(
                gCode.getAxisDirections(),
                gCode.getStartVelocity(),
                gCode.getStartPosition()));

        gCode.setFinalAngVelocities(MathCalc.makeAngVelocities(
                gCode.getAxisDirections(),
                gCode.getFinalVelocity(),
                gCode.getFinalPosition()));
    }

    /**
     * Generates G code path of points.
     */
    private static void calculate(LinearGCode gCode, double startTime) throws
            ImpossibleToImplement,
            BoundsViolation {
        gCode.setStartTime(startTime);
        makeGCodePath(gCode);
        double finalTime = gCode.getgCodePath().get(gCode.getgCodePath().size() - 1).getTime();
        gCode.setFinalTime(finalTime);
    }

    /**
     * Calculates path of the GCode depending on the distance.
     * If path has enough length for dynamic and static parts
     * velocity trajectory will be like this:
     * __________
     * /|          |\
     * / |          | \
     * /  |          |  \
     * /   |          |   \
     * /    |          |    \
     * /     |          |     \
     * /      |          |      \
     * /dynamic|  static  |dynamic\
     * <p>
     * If not, like this:
     * /
     * /
     * /
     * /
     *
     */
    private static void makeGCodePath(LinearGCode gCode) throws
            ImpossibleToImplement,
            BoundsViolation {

        double fullLength = gCode.getDistance();


        double accLength = MathCalc.accLength(gCode.getStartVelocity(),
                gCode.getStaticVelocity(), gCode.getAcceleration());


        double decLength = MathCalc.accLength(gCode.getStaticVelocity(),
                gCode.getFinalVelocity(), gCode.getAcceleration());


        if (fullLength > accLength + decLength) {
            fullPath(gCode);
        } else {
            incompletePath(gCode);
        }
    }

    /**
     * Calculates full path of GCode by concatenating dynamic and static parts.
     *
     */
    private static void fullPath(LinearGCode gCode)
            throws BoundsViolation {

        makeDynamicPath(gCode.getStartTime(), gCode.getStartPosition(),
                gCode.getAcceleration(), gCode.getStartVelocity(),
                gCode.getStaticVelocity(), gCode);

        double lastTime = gCode.getgCodePath().get(gCode.getgCodePath().size() - 1).getTime();
        double[] lastPosition = gCode.getgCodePath().get(gCode.getgCodePath().size() - 1).getPosition();

        makeStaticPath(lastTime, lastPosition, gCode);

        lastTime = gCode.getgCodePath().get(gCode.getgCodePath().size() - 1).getTime();
        lastPosition = gCode.getgCodePath().get(gCode.getgCodePath().size() - 1).getPosition();

        makeDynamicPath(lastTime, lastPosition,
                gCode.getAcceleration(),
                gCode.getStaticVelocity(), gCode.getFinalVelocity(), gCode);
    }

    /**
     * Calculates full path of GCode by creating dynamic path only.
     *
     */
    private static void incompletePath(LinearGCode gCode) throws
            ImpossibleToImplement, BoundsViolation {

        double time;
        if (gCode.getStartVelocity() != gCode.getFinalVelocity()) {
            time = abs((2 * gCode.getDistance()) / (abs(gCode.getStartVelocity())
                    + abs(gCode.getFinalVelocity())));
            double acceleration = abs(gCode.getStartVelocity() -
                    gCode.getFinalVelocity()) / time;

            if (acceleration > Constants.MAX_ACCELERATION) {
                throw new ImpossibleToImplement(("specified acceleration is" +
                        " higher than max allowed " +
                        acceleration + " > " + Constants.MAX_ACCELERATION),
                        gCode.getGCode());
            }
            gCode.setAcceleration(acceleration);
            makeDynamicPath(gCode.getStartTime(), gCode.getStartPosition(),
                    acceleration, gCode.getStartVelocity(),
                    gCode.getFinalVelocity(), gCode);
        } else {
            gCode.setAcceleration(Constants.MAX_ACCELERATION);

            time = Math.sqrt(gCode.getDistance() / gCode.getAcceleration());
            double maxVelocity = time * gCode.getAcceleration();

            makeDynamicPath(gCode.getStartTime(),
                    gCode.getStartPosition(), gCode.getAcceleration(),
                    gCode.getStartVelocity(), maxVelocity, gCode);

            double lastTime = gCode.getgCodePath()
                    .get(gCode.getgCodePath().size() - 1).getTime();
            double[] lastPosition = gCode.getgCodePath()
                    .get(gCode.getgCodePath().size() - 1).getPosition();

            makeDynamicPath(lastTime, lastPosition,
                    gCode.getAcceleration(), maxVelocity, gCode
                            .getFinalVelocity(), gCode);

        }
    }

    /**
     * Calculates dynamic part of the path with given velocity transition.
     * <p>
     * direction [vx, vy, vz] in cm/s.
     *
     * @param pathStartTime     start time for the path that will be calculated
     *                          in us.
     * @param pathStartPosition start position for the path that will be
     *                          calculated.
     * @param acceleration      acceleration of the current path in cm/s^2.
     * @param startVelocity     initial velocity of the current path in cm/s.
     * @param finalVelocity     final velocity of the current path in cm/s.
     */
    private static void makeDynamicPath(double pathStartTime,
                                        double[] pathStartPosition, double acceleration,
                                        double startVelocity, double finalVelocity, LinearGCode gCode)
            throws BoundsViolation {
        double[] axisDirections = gCode.getAxisDirections();

        double fullTime = MathCalc.accTimeInUs(startVelocity, finalVelocity,
                acceleration);
        double pointsNumber = (fullTime / Constants.TIME_GAP);
        int counter = (int) pointsNumber;
        double remainder = pointsNumber - counter;

        if (startVelocity > finalVelocity) acceleration = -acceleration;

        double[] axisAccelerations = MathCalc.velocityCustomize(axisDirections,
                acceleration);
        double[] axisStartVelocities = MathCalc.velocityCustomize(axisDirections,
                startVelocity);

        double t = 0;
        for (int i = 0; i <= counter; i++) {

            double currentVelocity = startVelocity
                    + acceleration * MathCalc.toSec(t);
            double x = pathStartPosition[0]
                    + axisStartVelocities[0] * MathCalc.toSec(t)
                    + axisAccelerations[0] * Math.pow(MathCalc.toSec(t), 2)
                    / 2;

            double y = pathStartPosition[1]
                    + axisStartVelocities[1] * MathCalc.toSec(t)
                    + axisAccelerations[1] * Math.pow(MathCalc.toSec(t), 2)
                    / 2;

            double z = pathStartPosition[2]
                    + axisStartVelocities[2] * MathCalc.toSec(t)
                    + axisAccelerations[2] * Math.pow(MathCalc.toSec(t), 2)
                    / 2;

            double[] position = {x, y, z};

            double currentTime = gCode.getStartTime() + pathStartTime + t;
            gCode.getgCodePath().add(Utility.makePoint(position, axisDirections,
                    currentVelocity, acceleration,
                    currentTime, gCode.getGCode()));

            //to calculate last point which has different time gap
            if (i == counter - 1) {
                t += Constants.TIME_GAP * (remainder + 1);
            } else {
                t += Constants.TIME_GAP;
            }
        }
    }

    /**
     * Calculates static part of the path with specified velocity
     * (staticVelocity).
     *
     * @param pathStartTime     start time for the path that will be calculated
     *                          in us.
     * @param pathStartPosition start position for the path that
     *                          will be calculated.
     */
    private static void makeStaticPath(double pathStartTime,
                    double[] pathStartPosition,
                    LinearGCode gCode)
            throws BoundsViolation {

        //length of the static part
        double pathLength = MathCalc.linearLength(pathStartPosition,
                gCode.getFinalPosition())
                - MathCalc.accLength(gCode.getStaticVelocity(), gCode.getFinalVelocity(),
                gCode.getAcceleration());

        double pointsNumber = MathCalc.pointsNumber(pathLength, gCode.getStaticVelocity());

        int counter = (int) pointsNumber;
        //remainder of the rounding
        double remainder = pointsNumber - counter;

        double[] axisDirections = gCode.getAxisDirections();

        double t = 0;
        for (int i = 0; i <= counter; i++) {
            double[] position = {axisDirections[0] * MathCalc.toSec(t)
                    + pathStartPosition[0],
                    axisDirections[1] * MathCalc.toSec(t)
                            + pathStartPosition[1],
                    axisDirections[2] * MathCalc.toSec(t)
                            + pathStartPosition[2]};

            double currentTime = gCode.getStartTime() + pathStartTime + t;
            gCode.getgCodePath().add(Utility.makePoint(position, axisDirections,
                    gCode.getStaticVelocity(),
                    0, currentTime, gCode.getGCode()));

            //to calculate last point which has different time gap
            if (i == counter - 1) {
                t += Constants.TIME_GAP * (remainder + 1);
            } else {
                t += Constants.TIME_GAP;
            }
        }
    }
}

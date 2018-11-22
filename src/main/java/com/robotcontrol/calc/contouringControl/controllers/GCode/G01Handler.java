package com.robotcontrol.calc.contouringControl.controllers.GCode;

import com.robotcontrol.calc.contouringControl.entities.GCode.LinearGCode;
import com.robotcontrol.exc.BoundsViolation;
import com.robotcontrol.exc.ImpossibleToImplement;
import com.robotcontrol.util.Utility;
import com.robotcontrol.util.math.Converter;
import com.robotcontrol.util.math.Geometry;
import com.robotcontrol.util.math.Physics;

import static com.robotcontrol.parameters.constant.Motion.MAX_ACCELERATION;
import static com.robotcontrol.parameters.constant.Motion.TIME_GAP;
import static java.lang.Math.abs;

class G01Handler {

    static void calcPath(LinearGCode gCode, long startTime)
            throws BoundsViolation, ImpossibleToImplement {
        initialize(gCode);
        calculate(gCode, startTime);
    }

    /**
     * Initializes data needed to calculate path.
     */
    static void initialize(LinearGCode gCode) {

        gCode.init();

        gCode.setStartVelocity(Physics.makeUtmostVelocity(gCode.getStaticVelocity(), gCode.getPreviousVelocity()));

        gCode.setFinalVelocity(Physics.makeUtmostVelocity(gCode.getStaticVelocity(), gCode.getNextVelocity()));

        gCode.setStartAngVelocities(Physics.makeAngVelocities(
                gCode.getAxisDirections(),
                gCode.getStartVelocity(),
                gCode.getStartPosition()));

        gCode.setFinalAngVelocities(Physics.makeAngVelocities(
                gCode.getAxisDirections(),
                gCode.getFinalVelocity(),
                gCode.getFinalPosition()));
    }

    /**
     * Generates G code path of points.
     */
    private static void calculate(LinearGCode gCode, long startTime) throws
            ImpossibleToImplement,
            BoundsViolation {
        gCode.setStartTime(startTime);
        makeGCodePath(gCode);
        long finalTime = gCode.getgCodePath().get(gCode.getgCodePath().size() - 1).getTime();
        gCode.setFinalTime(finalTime);
    }

    /**
     * Calculates path of the GCode depending on the distance.
     * If path has enough length for dynamic and static parts
     * velocity trajectory will be like this:
     *         __________
     *       /|          |\
     *      / |          | \
     *     /  |          |  \
     *    /   |          |   \
     *   /    |          |    \
     *  /     |          |     \
     * /      |          |      \
     * /dynamic|  static  |dynamic\
     * <p>
     * If not, like this:
     *    /
     *   /
     *  /
     * /
     *
     */
    private static void makeGCodePath(LinearGCode gCode) throws
            ImpossibleToImplement,
            BoundsViolation {

        double fullLength = gCode.getDistance();


        double accLength = Physics.accLength(gCode.getStartVelocity(),
                gCode.getStaticVelocity(), gCode.getAcceleration());


        double decLength = Physics.accLength(gCode.getStaticVelocity(),
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
    private static void fullPath(LinearGCode gCode) {

        makeDynamicPath(gCode.getStartTime(), gCode.getStartPosition(),
                gCode.getAcceleration(), gCode.getStartVelocity(),
                gCode.getStaticVelocity(), gCode);

        long lastTime = gCode.getgCodePath().get(gCode.getgCodePath().size() - 1).getTime();
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
            ImpossibleToImplement {

        double time;
        if (gCode.getStartVelocity() != gCode.getFinalVelocity()) {
            time = abs((2 * gCode.getDistance()) / (abs(gCode.getStartVelocity())
                    + abs(gCode.getFinalVelocity())));

            double acceleration = abs(gCode.getStartVelocity() - gCode.getFinalVelocity()) / time;

            if (acceleration > MAX_ACCELERATION) {
                throw new ImpossibleToImplement(("specified acceleration is" +
                        " higher than max allowed " +
                        acceleration + " > " + MAX_ACCELERATION),
                        gCode.getGCode());
            }
            gCode.setAcceleration(acceleration);
            makeDynamicPath(gCode.getStartTime(), gCode.getStartPosition(),
                    acceleration, gCode.getStartVelocity(),
                    gCode.getFinalVelocity(), gCode);
        } else {
            gCode.setAcceleration(MAX_ACCELERATION);

            time = Math.sqrt(gCode.getDistance() / gCode.getAcceleration());
            double maxVelocity = time * gCode.getAcceleration();

            makeDynamicPath(gCode.getStartTime(),
                    gCode.getStartPosition(), gCode.getAcceleration(),
                    gCode.getStartVelocity(), maxVelocity, gCode);

            long lastTime = gCode.getgCodePath().get(gCode.getgCodePath().size() - 1).getTime();

            double[] lastPosition = gCode.getgCodePath().get(gCode.getgCodePath().size() - 1).getPosition();

            makeDynamicPath(lastTime, lastPosition, gCode.getAcceleration(), maxVelocity, gCode.getFinalVelocity(), gCode);
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
    private static void makeDynamicPath(long pathStartTime,
                                        double[] pathStartPosition, double acceleration,
                                        double startVelocity, double finalVelocity, LinearGCode gCode) {
        double[] axisDirections = gCode.getAxisDirections();

        double fullTime = Physics.accTimeInUs(startVelocity, finalVelocity, acceleration);
        double pointsNumber = (fullTime / TIME_GAP);
        int counter = (int) pointsNumber;
        double remainder = pointsNumber - counter;

        if (startVelocity > finalVelocity){
            acceleration = -acceleration;
        }

        double[] axisAccelerations = Physics.velocityCustomize(axisDirections, acceleration);
        double[] axisStartVelocities = Physics.velocityCustomize(axisDirections, startVelocity);

        long t = 0;
        for (int i = 0; i <= counter; i++) {

            double x = pathStartPosition[0]
                    + axisStartVelocities[0] * Converter.toSec(t)
                    + axisAccelerations[0] * Math.pow(Converter.toSec(t), 2)
                    / 2;

            double y = pathStartPosition[1]
                    + axisStartVelocities[1] * Converter.toSec(t)
                    + axisAccelerations[1] * Math.pow(Converter.toSec(t), 2)
                    / 2;

            double z = pathStartPosition[2]
                    + axisStartVelocities[2] * Converter.toSec(t)
                    + axisAccelerations[2] * Math.pow(Converter.toSec(t), 2)
                    / 2;

            double[] position = {x, y, z};

            long currentTime = pathStartTime + t;
            gCode.getgCodePath().add(Utility.makePoint(position, currentTime));

            //to calculate last point which has different time gap
            if (i == counter - 1) {
                t += TIME_GAP * (remainder + 1);
            } else {
                t += TIME_GAP;
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
    private static void makeStaticPath(long pathStartTime,
                    double[] pathStartPosition,
                    LinearGCode gCode) {

        //length of the static part
        double pathLength = Geometry.linearLength(pathStartPosition, gCode.getFinalPosition())
                - Physics.accLength(gCode.getStaticVelocity(), gCode.getFinalVelocity(), gCode.getAcceleration());

        double pointsNumber = Physics.pointsNumber(pathLength, gCode.getStaticVelocity());

        int counter = (int) pointsNumber;
        //remainder of the rounding
        double remainder = pointsNumber - counter;

        double[] axisDirections = gCode.getAxisDirections();

        long t = 0;
        for (int i = 0; i <= counter; i++) {
            double[] position = {axisDirections[0] * Converter.toSec(t) + pathStartPosition[0],
                    axisDirections[1] * Converter.toSec(t) + pathStartPosition[1],
                    axisDirections[2] * Converter.toSec(t) + pathStartPosition[2]};

            long currentTime = pathStartTime + t;
            gCode.getgCodePath().add(Utility.makePoint(position, currentTime));

            //to calculate last point which has different time gap
            if (i == counter - 1) {
                t += TIME_GAP * (remainder + 1);
            } else {
                t += TIME_GAP;
            }
        }
    }
}

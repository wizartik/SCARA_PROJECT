package com.robotcontrol.calc.contouringControl.controllers.GCode;

import com.robotcontrol.calc.contouringControl.entities.GCode.AngularGCode;
import com.robotcontrol.calc.contouringControl.entities.Point;
import com.robotcontrol.exc.BoundsViolation;
import com.robotcontrol.exc.ImpossibleToImplement;
import com.robotcontrol.util.Utility;
import com.robotcontrol.util.math.Converter;
import com.robotcontrol.util.math.Geometry;
import com.robotcontrol.util.math.Physics;

import java.util.Arrays;

import static com.robotcontrol.parameters.constant.Motion.MAX_ACCELERATION;
import static com.robotcontrol.parameters.constant.Motion.TIME_GAP;

class G02Handler {
    static void calcPath(AngularGCode gCode, long startTime) throws
            BoundsViolation, ImpossibleToImplement {
        initialize(gCode);
        calculate(gCode, startTime);
    }

    /**
     * Initializes data needed to calculate path.
     */
    static void initialize(AngularGCode gCode) {

        gCode.init();

        gCode.setCenterPosition(Geometry.findCenterG02(gCode.getStartPosition(),
                gCode.getFinalPosition(), gCode.getRadius()));

        gCode.setStartVelocity(Physics.makeUtmostVelocity(gCode.getStaticVelocity(), gCode.getPreviousVelocity()));
        gCode.setFinalVelocity(Physics.makeUtmostVelocity(gCode.getStaticVelocity(), gCode.getNextVelocity()));

        double[] axisStartDirections;
        axisStartDirections = Physics.makeG02AxisDirections(gCode.getCenterPosition(), gCode.getStartPosition());
        double[] axisFinalDirections;
        axisFinalDirections = Physics.makeG02AxisDirections(gCode.getCenterPosition(), gCode.getFinalPosition());

        gCode.setStartAngVelocities(Physics.makeAngVelocities(axisStartDirections,
                gCode.getStartVelocity(),
                gCode.getStartPosition()));

        gCode.setFinalAngVelocities(Physics.makeAngVelocities(axisFinalDirections,
                gCode.getFinalVelocity(),
                gCode.getFinalPosition()));
    }

    /**
     * Generates G code path of points.
     */
    private static void calculate(AngularGCode gCode, long startTime)
            throws ImpossibleToImplement, BoundsViolation {
        gCode.setStartTime(startTime);
        makeGCodePath(gCode);
        gCode.setFinalTime(gCode.getgCodePath().get(gCode.getgCodePath().size() - 1).getTime());
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
     */
    private static void makeGCodePath(AngularGCode gCode)
            throws ImpossibleToImplement, BoundsViolation {


        double accLength = Physics.angleOfAcceleration(gCode.getStartVelocity(),
                gCode.getStaticVelocity(), gCode.getAcceleration(), gCode.getRadius())
                * gCode.getRadius();

        double decLength = Physics.angleOfAcceleration(gCode.getStaticVelocity(),
                gCode.getFinalVelocity(), gCode.getAcceleration(), gCode.getRadius())
                * gCode.getRadius();

        if (gCode.getDistance() > accLength + decLength) {
            fullPath(gCode);
        } else {
            incompletePath(gCode);
        }
    }

    /**
     * Calculates full path of GCode by concatenating dynamic and static parts.
     */
    private static void fullPath(AngularGCode gCode) throws BoundsViolation {

        makeDynamicPath(gCode.getStartTime(), gCode.getStartPosition(),
                gCode.getAcceleration(),
                gCode.getStartVelocity(), gCode.getStaticVelocity(), gCode);

        long lastTime = gCode.getgCodePath().get(gCode.getgCodePath().size() - 1).getTime();
        double[] lastPosition = gCode.getgCodePath().get(gCode.getgCodePath().size() - 1).getPosition();

        makeStaticPath(lastTime, lastPosition, gCode);

        lastTime = gCode.getgCodePath().get(gCode.getgCodePath().size() - 1).getTime();
        lastPosition = gCode.getgCodePath().get(gCode.getgCodePath().size() - 1)
                .getPosition();

        makeDynamicPath(lastTime, lastPosition,
                gCode.getAcceleration(),
                gCode.getStaticVelocity(), gCode.getFinalVelocity(), gCode);
    }

    /**
     * Calculates full path of GCode by creating dynamic path only.
     * Create velocity path:
     *     /
     *   /
     * /
     * if possible,
     *     /\
     *   /   \
     * /      \
     * if not.
     */
    private static void incompletePath(AngularGCode gCode)
            throws ImpossibleToImplement {

        double time;
        if (gCode.getStartVelocity() != gCode.getFinalVelocity()) {

            time = Math.abs((2 * gCode.getDistance()) / (Math.abs(gCode.getStartVelocity())
                    + Math.abs(gCode.getFinalVelocity())));
            double acceleration = Math.abs(gCode.getStartVelocity() - gCode.getFinalVelocity())
                    / time;

            if (acceleration > MAX_ACCELERATION) {
                throw new ImpossibleToImplement(("specified acceleration is" +
                        " higher than max allowed " +
                        acceleration + " > " + MAX_ACCELERATION),
                        gCode.getGCode());
            }
            gCode.setAcceleration(acceleration);
            makeDynamicPath(gCode.getStartTime(), gCode.getStartPosition(),
                    gCode.getAcceleration(),
                    gCode.getStartVelocity(), gCode.getFinalVelocity(), gCode);
        } else {
            gCode.setAcceleration(MAX_ACCELERATION);

            time = Math.sqrt(gCode.getDistance() / gCode.getAcceleration());
            double maxVelocity = time * gCode.getAcceleration();

            makeDynamicPath(gCode.getStartTime(), gCode.getStartPosition(),
                    gCode.getAcceleration(), gCode.getStartVelocity(),
                    maxVelocity, gCode);

            long lastTime = gCode.getgCodePath().get(gCode.getgCodePath().size() - 1).getTime();
            double[] lastPosition = gCode.getgCodePath().get(gCode.getgCodePath().size() - 1).getPosition();

            makeDynamicPath(lastTime, lastPosition,
                    gCode.getAcceleration(), maxVelocity,
                    gCode.getFinalVelocity(), gCode);
        }
    }

    /**
     * Calculates dynamic part of the path with given velocity transition.
     * Splits up given arc into many small ones and calculates path as sum
     * of chords.
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
                                        double startVelocity, double finalVelocity,
                                        AngularGCode gCode) {

        if (startVelocity > finalVelocity) acceleration = -acceleration;

        double[] currentPosition = Arrays.copyOf(pathStartPosition, pathStartPosition.length);

        double currentAngle = Math.atan2(currentPosition[1] - gCode.getCenterPosition()[1],
                                         currentPosition[0] - gCode.getCenterPosition()[0]);
        long t = 0;
        while (true) {
            double currentVelocity = startVelocity +
                    acceleration * Converter.toSec(t);
            //if last point

            boolean finalAcceleration = ((acceleration > 0) && (currentVelocity > finalVelocity));
            boolean finalDeceleration = ((acceleration < 0) && (currentVelocity < finalVelocity));

            if (finalAcceleration || finalDeceleration) {
                break;
            }

            double chordLength = Math.abs(currentVelocity * Converter.toSec(TIME_GAP));
            double segmentAngle = Geometry.segmentAngle(gCode.getRadius(), chordLength);

            double nextX = gCode.getCenterPosition()[0] + gCode.getRadius() * Math.cos(currentAngle - segmentAngle);
            double nextY = gCode.getCenterPosition()[1] + gCode.getRadius() * Math.sin(currentAngle - segmentAngle);
            double nextZ = currentPosition[2];

            double[] nextPosition = new double[]{nextX, nextY, nextZ};

            long currentTime = pathStartTime + t;
            Point point = Utility.makePoint(currentPosition, currentTime);
            gCode.getgCodePath().add(point);
            System.arraycopy(nextPosition, 0, currentPosition, 0, nextPosition.length);

            t += TIME_GAP;
            currentAngle -= segmentAngle;
        }
    }

    /**
     * Calculates static part of the path with specified velocity
     * (staticVelocity).
     * Splits up given arc into many small ones and calculates path as sum
     * of chords.
     *
     * @param pathStartTime     start time for the path that will be calculated
     *                          in us.
     * @param pathStartPosition start position for the path that will be
     *                          calculated.
     */
    private static void makeStaticPath(long pathStartTime,
                                       double[] pathStartPosition,
                                       AngularGCode gCode) {

        //find points number
        double angleOfDeceleration = Physics.angleOfAcceleration(gCode.getStaticVelocity(),
                gCode.getFinalVelocity(), gCode.getAcceleration(), gCode.getRadius());

        double[] currentPosition = Arrays.copyOf(pathStartPosition, pathStartPosition.length);

        double currentAngle;

        currentAngle = Math.atan2(pathStartPosition[1] - gCode.getCenterPosition()[1],
                                  pathStartPosition[0] - gCode.getCenterPosition()[0]);


        double angleOfStaticPath = (Geometry.findAngle(pathStartPosition,
                gCode.getFinalPosition(), gCode.getRadius()) - angleOfDeceleration);


        double finalX = gCode.getCenterPosition()[0] + gCode.getRadius() * Math.cos(currentAngle - angleOfStaticPath);
        double finalY = gCode.getCenterPosition()[1] + gCode.getRadius() * Math.sin(currentAngle - angleOfStaticPath);
        double finalZ = gCode.getCenterPosition()[2];

        double[] pathFinalPosition = {finalX, finalY, finalZ};

        double oneChordLength = gCode.getStaticVelocity() * Converter.toSec(TIME_GAP);
        double oneSegmentAngle = Geometry.segmentAngle(gCode.getRadius(), oneChordLength);
        double oneArcLength = Geometry.arcLength(gCode.getRadius(), oneChordLength);
        double fullPathLength = Geometry.angularLength(pathStartPosition, pathFinalPosition, gCode.getRadius());
        double pointsNumber = fullPathLength / oneArcLength;


        int counter = (int) pointsNumber;
        //remainder of the rounding
        double remainder = pointsNumber - counter;
        long t = 0;

        for (int i = 0; i <= counter + 1; i++) {

            double nextX = gCode.getCenterPosition()[0] + gCode.getRadius() * Math.cos(currentAngle - oneSegmentAngle);
            double nextY = gCode.getCenterPosition()[1] + gCode.getRadius() * Math.sin(currentAngle - oneSegmentAngle);
            double nextZ = currentPosition[2];

            double[] nextPosition = {nextX, nextY, nextZ};

            long currentTime = pathStartTime + t;

            Point point = Utility.makePoint(currentPosition, currentTime);

            gCode.getgCodePath().add(point);

            //to calculate last point which has different time gap and arc angle
            if (i == counter - 1) {
                t += TIME_GAP * remainder;
                currentAngle -= oneSegmentAngle * remainder;
            } else {
                t += TIME_GAP;
                currentAngle -= oneSegmentAngle;
            }
            System.arraycopy(nextPosition, 0, currentPosition,
                    0, nextPosition.length);
        }
    }
}

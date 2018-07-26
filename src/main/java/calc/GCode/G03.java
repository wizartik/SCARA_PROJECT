package calc.GCode;

import com.robotcontrol.calc.contouringControl.GCode.entities.Point;
import calc.data.Constants;
import calc.util.MathCalc;
import calc.util.Utility;
import exc.BoundsViolation;
import exc.ImpossibleToImplement;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A circular or helical arc is specified using either G02 (clockwise arc)
 * or G03 (counterclockwise arc). The axis of the circle or helix must be
 * parallel to the X-, Y- or Z-axis of the machine coordinate system.
 * The axis (or, equivalently, the plane perpendicular to the axis) is
 * selected with G17 (Z-axis, XY-plane), G18 (Y-axis, XZ-plane)
 * or G19 (X-axis, YZ-plane). If the arc is circular, it lies in a plane
 * parallel to the selected plane.
 */
public class G03 implements GCode {

    private double startTime;
    private double finalTime;

    private double[] startPosition;
    private double[] finalPosition;
    private double[] centerPosition;

    // start and final angular velocities, needed to check transition
    // validation between G codes
    private double[] startAngVelocities;
    private double[] finalAngVelocities;

    private double radius;

    //velocity of the static part
    private double staticVelocity;

    private double previousVelocity;
    private double nextVelocity;

    private double acceleration;

    private double distance;

    //start with this velocity
    private double startVelocity;
    //end with this velocity
    private double finalVelocity;

    private String gCode;

    private ArrayList<Point> gCodePath = new ArrayList<>();

    /**
     * @param startPosition  start position of the current G code [x, y, z] in cm.
     * @param finalPosition  final position of the current G code [x, y, z] in cm.
     * @param radius         radius of the circle in cm.
     * @param staticVelocity specified velocity of the current G code in cm/s.
     * @param acceleration   acceleration in cm/s^2.
     * @param gCode          G code string.
     */
    public G03(double[] startPosition, double[] finalPosition, double radius,
               double staticVelocity, double acceleration, String gCode) {
        this.startPosition = startPosition;
        this.finalPosition = finalPosition;
        this.radius = radius;
        this.staticVelocity = staticVelocity;
        this.acceleration = acceleration;
        this.gCode = gCode;
        centerPosition = MathCalc.findCenterG03(startPosition, finalPosition,
                radius);
        distance = MathCalc.angularLength(startPosition, finalPosition, radius);
    }

    /**
     * @return start time of the current G code in μs.
     */
    @Override
    public double getStartTime() {
        return startTime;
    }

    /**
     * @return final time of the current G code in μs.
     */
    @Override
    public double getFinalTime() {
        return finalTime;
    }

    /**
     * @return start position array of the current G code.
     * [x, y, z].
     */
    @Override
    public double[] getStartPosition() {
        return startPosition;
    }

    /**
     * @return start position array of the current G code.
     * [x, y, z].
     */
    @Override
    public double[] getFinalPosition() {
        return finalPosition;
    }

    /**
     * @return static velocity of the previous G code.
     */
    @Override
    public double getPreviousVelocity() {
        return previousVelocity;
    }

    /**
     * @return static velocity of the next G code.
     */
    @Override
    public double getNextVelocity() {
        return nextVelocity;
    }

    /**
     * @return start angular velocities of the current G code.
     */
    @Override
    public double[] getStartAngVelocities() {
        return startAngVelocities;
    }

    /**
     * @return final angular velocities of the current G code.
     */
    @Override
    public double[] getFinalAngVelocities() {
        return finalAngVelocities;
    }

    /**
     * @return radius of the circle in cm.
     */
    @Override
    public double getRadius() {
        return radius;
    }

    /**
     * @return specified staticVelocity for this G code.
     */
    @Override
    public double getVelocity() {
        return staticVelocity;
    }


    /**
     * @return specified acceleration in cm/s^2.
     */
    @Override
    public double getAcceleration() {
        return acceleration;
    }

    /**
     * @return total distance traveled in cm.
     */
    @Override
    public double getDistance() {
        return distance;
    }

    /**
     * @return string of current GCode.
     */
    @Override
    public String getGCode() {
        return gCode;
    }

    /**
     * @return type of the G code.
     */
    @Override
    public GCodeType getGCodeType() {
        return GCodeType.G03;
    }


    /**
     * @return center coords if circular else null;
     */
    @Override
    public double[] getCenter() {
        return centerPosition;
    }

    /**
     * Should be initialized first by initialize().
     *
     * @return full path of the current GCode in Points.
     */
    @Override
    public ArrayList<Point> getGCodePath() {
        return gCodePath;
    }

    /**
     * Initializes data needed to calculate path.
     *
     * @param previousVelocity velocity of the previous G code in cm/s.
     * @param nextVelocity     velocity of the nex G code in cm/s.
     */
    @Override
    public void initialize(double previousVelocity, double nextVelocity) {
        this.previousVelocity = previousVelocity;
        this.nextVelocity = nextVelocity;

        startVelocity = MathCalc.makeUtmostVelocity(staticVelocity,
                previousVelocity);
        finalVelocity = MathCalc.makeUtmostVelocity(staticVelocity,
                nextVelocity);

        double[] axisStartDirections;
        axisStartDirections = MathCalc.makeG03AxisDirections(centerPosition,
                startPosition);
        double[] axisFinalDirections;
        axisFinalDirections = MathCalc.makeG03AxisDirections(centerPosition,
                finalPosition);

        startAngVelocities = MathCalc.makeAngVelocities(axisStartDirections,
                startVelocity,
                startPosition);

        finalAngVelocities = MathCalc.makeAngVelocities(axisFinalDirections,
                finalVelocity,
                finalPosition);
    }

    /**
     * Generates G code path of points.
     */
    @Override
    public void calculate(double startTime) throws ImpossibleToImplement, BoundsViolation {
        this.startTime = startTime;
        gCodePath = makeGCodePath();
        finalTime = gCodePath.get(gCodePath.size() - 1).getTime();
    }

    /**
     * Calculates path of the GCode depending on the distance.
     * If path has enough length for dynamic and static parts velocity
     * trajectory will be like this:
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
     * @return path in Points.
     */
    private ArrayList<Point> makeGCodePath() throws ImpossibleToImplement, BoundsViolation {

        double fullLength = MathCalc.angularLength(startPosition,
                finalPosition, radius);

        double accLength = MathCalc.angleOfAcceleration(startVelocity,
                staticVelocity, acceleration, radius) * radius;
        double decLength = MathCalc.angleOfAcceleration(staticVelocity,
                finalVelocity, acceleration, radius) * radius;

        if (fullLength > accLength + decLength) {
            return fullPath();
        } else {
            return incompletePath(fullLength);
        }
    }

    /**
     * Calculates full path of GCode by concatenating dynamic and static parts.
     *
     * @return full path of GCode in Points.
     */
    private ArrayList<Point> fullPath() throws BoundsViolation {
        ArrayList<Point> path = new ArrayList<>();

        path.addAll(makeDynamicPath(startTime, startPosition,
                acceleration,
                startVelocity, staticVelocity));

        double lastTime = path.get(path.size() - 1).getTime();
        double[] lastPosition = path.get(path.size() - 1).getPosition();

        path.addAll(makeStaticPath(lastTime, lastPosition));

        lastTime = path.get(path.size() - 1).getTime();
        lastPosition = path.get(path.size() - 1).getPosition();

        path.addAll(makeDynamicPath(lastTime, lastPosition,
                acceleration,
                staticVelocity, finalVelocity));

        return path;
    }

    /**
     * Calculates full path of GCode by creating dynamic path only.
     *
     * @param fullLength full length of the path in cm.
     * @return full path of GCode in Points.
     */
    private ArrayList<Point> incompletePath(double fullLength)
            throws ImpossibleToImplement, BoundsViolation {

        double time;
        if (startVelocity != finalVelocity) {
            time = Math.abs((2 * fullLength)
                    / (Math.abs(startVelocity) + Math.abs(finalVelocity)));
            double acceleration = Math.abs(startVelocity - finalVelocity)
                    / time;

            if (acceleration > Constants.MAX_ACCELERATION) {
                throw new ImpossibleToImplement(("specified acceleration is" +
                        " higher than max allowed " +
                        acceleration + " > " + Constants.MAX_ACCELERATION),
                        gCode);
            }
            this.acceleration = acceleration;
            return makeDynamicPath(startTime, startPosition,
                    acceleration, startVelocity,
                    finalVelocity);
        } else {
            this.acceleration = Constants.MAX_ACCELERATION;

            time = Math.sqrt(fullLength / acceleration);
            double maxVelocity = time * acceleration;

            ArrayList<Point> path = makeDynamicPath(startTime, startPosition,
                    acceleration, startVelocity,
                    maxVelocity);

            double lastTime = path.get(path.size() - 1).getTime();
            double[] lastPosition = path.get(path.size() - 1).getPosition();

            path.addAll(makeDynamicPath(lastTime, lastPosition, acceleration,
                    maxVelocity, finalVelocity));
            return path;
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
     * @return path in Points.
     */
    private ArrayList<Point> makeDynamicPath(double pathStartTime,
                                             double[] pathStartPosition, double acceleration,
                                             double startVelocity, double finalVelocity) throws BoundsViolation {

        ArrayList<Point> path = new ArrayList<>();
        if (startVelocity > finalVelocity) {
            acceleration = -acceleration;
        }
        double[] currentPosition = Arrays.copyOf(pathStartPosition,
                pathStartPosition.length);

        double currentAngle = Math.atan2(currentPosition[1] - centerPosition[1],
                currentPosition[0] - centerPosition[0]);
        double t = 0;
        while (true) {
            double currentVelocity = startVelocity
                    + acceleration * MathCalc.toSec(t);

            //if last point
            if (((acceleration > 0) && (currentVelocity > finalVelocity))
                    || ((acceleration < 0)
                    && (currentVelocity < finalVelocity))) {
                break;
            }
            double chordLength = Math.abs(currentVelocity
                    * MathCalc.toSec(Constants.TIME_GAP));
            double segmentAngle = MathCalc.segmentAngle(radius, chordLength);

            double[] nextPosition = new double[]{centerPosition[0]
                    + radius * Math.cos(currentAngle + segmentAngle),
                    centerPosition[1]
                            + radius * Math.sin(currentAngle + segmentAngle),
                    currentPosition[2]};

            double[] axisDirections = {nextPosition[0] - currentPosition[0],
                    nextPosition[1] - currentPosition[1],
                    nextPosition[2] - currentPosition[2]};
            double currentTime = startTime + pathStartTime + t;
            Point point = Utility.makePoint(currentPosition, axisDirections,
                    currentVelocity, acceleration,
                    currentTime, gCode);
            path.add(point);
            System.arraycopy(nextPosition, 0, currentPosition,
                    0, nextPosition.length);

            t += Constants.TIME_GAP;
            currentAngle += segmentAngle;
        }
        return path;
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
     * @return path in Points.
     */
    private ArrayList<Point> makeStaticPath(double pathStartTime,
                                            double[] pathStartPosition) throws BoundsViolation {
        ArrayList<Point> path = new ArrayList<>();

        //find points number
        double angleOfDeceleration = MathCalc.angleOfAcceleration(staticVelocity,
                finalVelocity, acceleration, radius);
        double[] currentPosition = Arrays.copyOf(pathStartPosition,
                pathStartPosition.length);

        double currentAngle;
        currentAngle = Math.atan2(pathStartPosition[1] - centerPosition[1],
                pathStartPosition[0] - centerPosition[0]);

        double angleOfStaticPath = MathCalc.findAngle(pathStartPosition,
                finalPosition, radius)
                - angleOfDeceleration;

        double[] pathFinalPosition = {centerPosition[0]
                + radius * Math.cos(currentAngle + angleOfStaticPath),
                centerPosition[1]
                        + radius * Math.sin(currentAngle + angleOfStaticPath),
                centerPosition[2]};

        double oneChordLength = staticVelocity
                * MathCalc.toSec(Constants.TIME_GAP);
        double oneSegmentAngle = MathCalc.segmentAngle(radius, oneChordLength);
        double oneArcLength = MathCalc.arcLength(radius, oneChordLength);
        double fullPathLength = MathCalc.angularLength(pathStartPosition,
                pathFinalPosition,
                radius);

        double pointsNumber = fullPathLength / oneArcLength;

        int counter = (int) pointsNumber;
        //remainder of the rounding
        double remainder = pointsNumber - counter;
        double t = 0;

        for (int i = 0; i <= counter + 1; i++) {
            double[] nextPosition = {centerPosition[0]
                    + radius * Math.cos(currentAngle + oneSegmentAngle),
                    centerPosition[1]
                            + radius * Math.sin(currentAngle + oneSegmentAngle),
                    currentPosition[2]};
            double[] axisDirections = {nextPosition[0] - currentPosition[0],
                    nextPosition[1] - currentPosition[1],
                    nextPosition[2] - currentPosition[2]};

            double currentTime = startTime + pathStartTime + t;

            Point point = Utility.makePoint(currentPosition, axisDirections,
                    staticVelocity, 0,
                    currentTime, gCode);
            path.add(point);

            //to calculate last point which has different time gap and arc angle
            if (i == counter - 1) {
                t += Constants.TIME_GAP * remainder;
                currentAngle += oneSegmentAngle * remainder;
            } else {
                t += Constants.TIME_GAP;
                currentAngle += oneSegmentAngle;
            }
            System.arraycopy(nextPosition, 0, currentPosition,
                    0, nextPosition.length);
        }
        return path;
    }
}

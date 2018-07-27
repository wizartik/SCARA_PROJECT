package calc.GCode;

import com.robotcontrol.calc.contouringControl.entities.Point;
import calc.data.Constants;
import calc.util.MathCalc;
import calc.util.Utility;
import exc.BoundsViolation;
import exc.ImpossibleToImplement;

import java.util.ArrayList;

import static java.lang.Math.abs;

/**
 * For linear motion at feed rate (for cutting or not), program:
 * G01 X~ Y~ Z~ A~, where all the axis words are optional, except
 * that at least one must be used. The G01 is optional if the current
 * motion mode is G01. This will produce coordinated linear motion to
 * the destination point at the current feed rate (or slower if the
 * machine will not testAll that fast).
 */
public class G01 implements GCode {

    private double startTime;
    private double finalTime;

    private double[] startPosition;
    private double[] finalPosition;

    // start and final angular velocities, needed to check transition
    // validation between G codes
    private double[] startAngVelocities;
    private double[] finalAngVelocities;

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
     * @param staticVelocity specified velocity of the current G code in cm/s.
     * @param acceleration   acceleration in cm/s^2.
     * @param gCode          G code string.
     */
    public G01(double[] startPosition, double[] finalPosition,
               double staticVelocity, double acceleration,
               String gCode) {
        this.startPosition = startPosition;
        this.finalPosition = finalPosition;
        this.staticVelocity = staticVelocity;
        this.acceleration = acceleration;
        this.gCode = gCode;
        this.distance = MathCalc.linearLength(startPosition, finalPosition);
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
     * @return radius if circular else -1;
     */
    @Override
    public double getRadius() {
        return -1;
    }

    /**
     * @return center coords if circular else null;
     */
    @Override
    public double[] getCenter() {
        return null;
    }

    /**
     * @return type of the G code.
     */
    @Override
    public GCodeType getGCodeType() {
        return GCodeType.G01;
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
        distance = MathCalc.linearLength(startPosition, finalPosition);

        double[] axisDirections = {finalPosition[0] - startPosition[0],
                finalPosition[1] - startPosition[1],
                finalPosition[2] - startPosition[2]};

        startAngVelocities = MathCalc.makeAngVelocities(axisDirections,
                startVelocity,
                startPosition);

        finalAngVelocities = MathCalc.makeAngVelocities(axisDirections,
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
     * If path has enough length for dynamic and static parts
     * velocity trajectory will be like this:
     *          __________
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
     * @return path in Points.
     */
    private ArrayList<Point> makeGCodePath() throws ImpossibleToImplement, BoundsViolation {

        double fullLength = MathCalc.linearLength(startPosition, finalPosition);
        double accLength = MathCalc.accLength(startVelocity, staticVelocity,
                acceleration);

        double decLength = MathCalc.accLength(staticVelocity, finalVelocity,
                acceleration);

        double[] axisDirections = {finalPosition[0] - startPosition[0],
                finalPosition[1] - startPosition[1],
                finalPosition[2] - startPosition[2]};

        if (fullLength > accLength + decLength) {
            return fullPath(axisDirections);
        } else {
            return incompletePath(axisDirections, fullLength);
        }
    }

    /**
     * Calculates full path of GCode by concatenating dynamic and static parts.
     *
     * @param axisDirections axis velocities needed to count direction
     *                       [vx, vy, vz] in cm/s.
     * @return full path of GCode in Points.
     */
    private ArrayList<Point> fullPath(double[] axisDirections) throws BoundsViolation {
        ArrayList<Point> path = new ArrayList<>();

        path.addAll(makeDynamicPath(axisDirections, startTime, startPosition,
                acceleration,
                startVelocity, staticVelocity));

        double lastTime = path.get(path.size() - 1).getTime();
        double[] lastPosition = path.get(path.size() - 1).getPosition();

        path.addAll(makeStaticPath(axisDirections, lastTime, lastPosition));

        lastTime = path.get(path.size() - 1).getTime();
        lastPosition = path.get(path.size() - 1).getPosition();

        path.addAll(makeDynamicPath(axisDirections, lastTime, lastPosition,
                acceleration,
                staticVelocity, finalVelocity));
        return path;
    }

    /**
     * Calculates full path of GCode by creating dynamic path only.
     *
     * @param axisDirections axis velocities needed to count direction
     *                       [vx, vy, vz] in cm/s.
     * @param fullLength     full length of the path in cm.
     * @return full path of GCode in Points.
     */
    private ArrayList<Point> incompletePath(double[] axisDirections,
                                            double fullLength) throws ImpossibleToImplement, BoundsViolation {

        double time;
        if (startVelocity != finalVelocity) {
            time = abs((2 * fullLength) / (abs(startVelocity)
                    + abs(finalVelocity)));
            double acceleration = abs(startVelocity - finalVelocity) / time;

            if (acceleration > Constants.MAX_ACCELERATION) {
                throw new ImpossibleToImplement(("specified acceleration is" +
                        " higher than max allowed " +
                        acceleration + " > " + Constants.MAX_ACCELERATION),
                        gCode);
            }
            this.acceleration = acceleration;
            return makeDynamicPath(axisDirections, startTime, startPosition,
                    acceleration, startVelocity, finalVelocity);
        } else {
            this.acceleration = Constants.MAX_ACCELERATION;

            time = Math.sqrt(fullLength / acceleration);
            double maxVelocity = time * acceleration;

            ArrayList<Point> path = makeDynamicPath(axisDirections, startTime,
                    startPosition, acceleration,
                    startVelocity, maxVelocity);

            double lastTime = path.get(path.size() - 1).getTime();
            double[] lastPosition = path.get(path.size() - 1).getPosition();

            path.addAll(makeDynamicPath(axisDirections, lastTime, lastPosition,
                    acceleration, maxVelocity, finalVelocity));

            return path;
        }
    }

    /**
     * Calculates dynamic part of the path with given velocity transition.
     *
     * @param axisDirections    axis velocities needed to count
     *                          direction [vx, vy, vz] in cm/s.
     * @param pathStartTime     start time for the path that will be calculated
     *                          in us.
     * @param pathStartPosition start position for the path that will be
     *                          calculated.
     * @param acceleration      acceleration of the current path in cm/s^2.
     * @param startVelocity     initial velocity of the current path in cm/s.
     * @param finalVelocity     final velocity of the current path in cm/s.
     * @return path in Points.
     */
    private ArrayList<Point> makeDynamicPath(double[] axisDirections,
                                             double pathStartTime, double[] pathStartPosition,
                                             double acceleration, double startVelocity, double finalVelocity) throws BoundsViolation {

        ArrayList<Point> path = new ArrayList<>();

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

            double currentTime = startTime + pathStartTime + t;
            path.add(Utility.makePoint(position, axisDirections,
                    currentVelocity, acceleration,
                    currentTime, gCode));

            //to calculate last point which has different time gap
            if (i == counter - 1) {
                t += Constants.TIME_GAP * (remainder + 1);
            } else {
                t += Constants.TIME_GAP;
            }
        }
        return path;
    }

    /**
     * Calculates static part of the path with specified velocity
     * (staticVelocity).
     *
     * @param axisDirections    axis velocities needed to count direction
     *                          [vx, vy, vz] in cm/s.
     * @param pathStartTime     start time for the path that will be calculated
     *                          in us.
     * @param pathStartPosition start position for the path that
     *                          will be calculated.
     * @return path in Points.
     */
    private ArrayList<Point> makeStaticPath(double[] axisDirections,
                                            double pathStartTime,
                                            double[] pathStartPosition) throws BoundsViolation {
        ArrayList<Point> path = new ArrayList<>();

        //length of the static part
        double pathLength = MathCalc.linearLength(pathStartPosition, finalPosition)
                - MathCalc.accLength(staticVelocity, finalVelocity,
                acceleration);
        double pointsNumber = MathCalc.pointsNumber(pathLength, staticVelocity);

        int counter = (int) pointsNumber;
        //remainder of the rounding
        double remainder = pointsNumber - counter;

        axisDirections = MathCalc.velocityCustomize(axisDirections,
                staticVelocity);

        double t = 0;
        for (int i = 0; i <= counter; i++) {
            double[] position = {axisDirections[0] * MathCalc.toSec(t)
                    + pathStartPosition[0],
                    axisDirections[1] * MathCalc.toSec(t)
                            + pathStartPosition[1],
                    axisDirections[2] * MathCalc.toSec(t)
                            + pathStartPosition[2]};

            double currentTime = startTime + pathStartTime + t;
            path.add(Utility.makePoint(position, axisDirections, staticVelocity,
                    0, currentTime, gCode));

            //to calculate last point which has different time gap
            if (i == counter - 1) {
                t += Constants.TIME_GAP * (remainder + 1);
            } else {
                t += Constants.TIME_GAP;
            }
        }

        return path;
    }
}

package calc.util;

import calc.DHParameters.SCARADH;
import calc.data.Constants;

import java.util.ArrayList;

import static calc.data.Constants.TIME_GAP;
import static java.lang.Math.*;

/**
 * Created by Valerii on 03.09.2017.
 */
public class MathCalc {


    /**
     * Creates linear acceleration/deceleration path of velocity.
     *
     * @param startV       initial velocity in cm/s.
     * @param finalV       desired velocity in cm/s.
     * @param acceleration given acceleration in cm/s^2.
     * @return acceleration path of velocity.
     * <p>
     * [t, v].
     * <p>
     * t - us.
     * v = cm/s.
     */
    public static ArrayList<Double[]> accelerationFull(double startV,
                                                       double finalV,
                                                       double acceleration) {
        ArrayList<Double[]> accelerationPath = new ArrayList<>();

        acceleration = Math.abs(acceleration);

        double fullTime = accTimeInUs(startV, finalV, acceleration);

        //how much point should it calc
        int points = (int) (fullTime / TIME_GAP);
        double t = 0;
        double v = startV;
        for (int i = 0; i < points; i++) {
            if ((finalV - startV) > 0) {
                v += acceleration * toSec(TIME_GAP);
            } else {
                v -= acceleration * toSec(TIME_GAP);
            }
            t += TIME_GAP;

            accelerationPath.add(new Double[]{t, v});
        }

        if (accelerationPath.size() == 0) {
            accelerationPath.add(new Double[]{(double) 0, startV});
        }
        return accelerationPath;
    }

    /**
     * Calculates time needed to pass given distance with constant acceleration.
     *
     * @param length        given length in m.
     * @param startVelocity given initial velocity in m/s.
     * @return time in s.
     */
    public static double accTimeByLength(double length, double startVelocity,
                                         double acceleration) {
        acceleration = Math.abs(acceleration);
        double D = pow(startVelocity, 2) - 4 * acceleration / 2;
        double t = ((-startVelocity + sqrt(D)) / (2 * acceleration / 2));
        return abs(t);
    }

    /**
     * @param inUs time in microseconds.
     * @return time in seconds.
     */
    public static double toSec(double inUs) {
        return inUs / 1000_000;
    }

    /**
     * @param inSec time in seconds.
     * @return time in microseconds.
     */
    public static double toUSec(double inSec) {
        return inSec * 1000_000;
    }

    /**
     * @param inMPerUs velocity in m/us.
     * @return velocity in m/s.
     */
    public static double toMPerSec(double inMPerUs) {
        return inMPerUs * 1000_000;
    }

    /**
     * @param inMPerSec velocity in m/us.
     * @return velocity in m/s.
     */
    public static double toMPerUSec(double inMPerSec) {
        return inMPerSec / 1000_000;
    }


    /**
     * Transforms velocity value from notation, which is used in G code to
     * one that is used in program.
     *
     * @param external value that need to be converted in (cm/m).
     * @return converted value in (cm/s).
     */
    public static double toInternalVel(double external){
        return external / 60;
    }

    /**
     * Transforms velocity value from notation, which is used in program to
     * one that is used in G code.
     *
     * @param internal value that need to be converted in (cm/s).
     * @return converted value in (cm/m).
     */
    public static double toExternalVel(double internal){
        return internal * 60;
    }

    /**
     * Calculates total distance an point travels during the acceleration.
     *
     * @param startV initial velocity in m/s.
     * @param finalV initial velocity in m/s.
     * @return distance in m.
     */
    public static double accLength(double startV, double finalV,
                                   double acceleration) {
        double t = toSec(accTimeInUs(startV, finalV, acceleration));
        acceleration = Math.abs(acceleration);

        if (startV > finalV) {
            acceleration = -acceleration;
        }

        return startV * t + (acceleration * pow(t, 2)) / 2;
    }

    /**
     * Calculates full time of acceleration path.
     *
     * @param startV initial velocity in m/s.
     * @param finalV final velocity in m/s.
     * @return full time of acceleration in us.
     */
    public static double accTimeInUs(double startV, double finalV,
                                     double acceleration) {
        acceleration = Math.abs(acceleration);
        return toUSec(((Math.abs(Math.abs(finalV) - Math.abs(startV)))
                / acceleration));
    }


    /**
     * Calculates length between 2 points for the linear path.
     *
     * @param startCoords first point [x, y, z] in m.
     * @param finalCoords final point [x, y, z] in m.
     * @return length between 2 points in m.
     */
    public static double linearLength(double[] startCoords,
                                      double[] finalCoords) {
        double x0 = startCoords[0];
        double y0 = startCoords[1];
        double z0 = startCoords[2];

        double xr = finalCoords[0];
        double yr = finalCoords[1];
        double zr = finalCoords[2];

        return sqrt(pow((x0 - xr), 2) + pow((y0 - yr), 2) + pow((z0 - zr), 2));
    }

    /**
     * Calculates total distance an point travels in circle
     * in counterclockwise direction.
     * Angle between 2 points should be less than pi.
     *
     * @param startCoords first point [x, y, z] in m.
     * @param finalCoords final point [x, y, z] in m.
     * @param radius      radius of the circle in m.
     * @return total distance an point travels in m.
     */
    public static double angularLength(double[] startCoords,
                                       double[] finalCoords, double radius) {
        return findAngle(startCoords, finalCoords, radius) * radius;
    }

    /**
     * Finds the angles in between given coordinates in circle
     * with given radius and counterclockwise direction.
     * Angle between 2 points should be less than pi.
     *
     * @param startCoords start coordinates [x, y, z] in m.
     * @param finalCoords final coordinates [x, y, z] in m.
     * @param radius      radius of the circle in m.
     * @return angle in rad.
     */
    public static double findAngle(double[] startCoords, double[] finalCoords,
                                   double radius) {
        double distance = linearLength(startCoords, finalCoords);

        return abs(acos((2 * pow(radius, 2) - pow(distance, 2))
                / (2 * pow(radius, 2))));
    }

    /**
     * Finds the center coordinates of circle for G02 code (clockwise arc).
     * Angle between 2 points should be less than pi.
     *
     * @param startCoords first point [x, y, z] in m.
     * @param finalCoords final point [x, y, z] in m.
     * @param radius      radius of the circle in m.
     * @return center of the circle [x, y, z] in m.
     */
    public static double[] findCenterG02(double[] startCoords,
                                         double[] finalCoords, double radius) {
        double x0 = startCoords[0];
        double y0 = startCoords[1];

        double x1 = finalCoords[0];
        double y1 = finalCoords[1];

        double d = sqrt(pow((x0 - x1), 2) + pow((y0 - y1), 2));
        double h = sqrt(pow(radius, 2) - pow(d / 2, 2));

        double u = (x1 - x0) / d;
        double v = (y1 - y0) / d;

        double xc = (x0 + x1) / 2 + h * v;
        double yc = (y0 + y1) / 2 - h * u;

        return new double[]{xc, yc, startCoords[2]};
    }

    /**
     * Finds the center coordinates of circle for G02 code
     * (counter-clockwise arc).
     * Angle between 2 points should be less than pi.
     *
     * @param startCoords first point [x, y, z] in m.
     * @param finalCoords final point [x, y, z] in m.
     * @param radius      radius of the circle in m.
     * @return center of the circle [x, y, z] in m.
     */
    public static double[] findCenterG03(double[] startCoords,
                                         double[] finalCoords, double radius) {
        double x0 = startCoords[0];
        double y0 = startCoords[1];

        double x1 = finalCoords[0];
        double y1 = finalCoords[1];

        double d = sqrt(pow((x0 - x1), 2) + pow((y0 - y1), 2));
        double h = sqrt(pow(radius, 2) - pow(d / 2, 2));

        double u = (x1 - x0) / d;
        double v = (y1 - y0) / d;

        double xc = (x0 + x1) / 2 - h * v;
        double yc = (y0 + y1) / 2 + h * u;

        return new double[]{xc, yc, startCoords[2]};
    }

    /**
     * Calculates axis velocities so the sum velocity will be equal
     * to the desired one.
     *
     * @param directions array of axis directions.
     * @param v          desired sum velocity in m/s.
     * @return required axis velocities [vx, vy, vz] in m/s.
     */
    public static double[] velocityCustomize(double[] directions, double v) {

        double vx = directions[0];
        double vy = directions[1];
        double vz = directions[2];

        double coef = v / (sqrt(pow(vx, 2) + pow(vy, 2) + pow(vz, 2)));

        if (Double.isNaN(coef)) {
            return new double[]{0, 0, 0};
        }

        vx *= coef;
        vy *= coef;
        vz *= coef;

        return new double[]{vx, vy, vz};
    }

    /**
     * Calculates number of the points in defined path and with defined
     * velocity.
     *
     * @param length   of the path in m.
     * @param velocity of the point in m/s.
     * @return number of points.
     */
    public static double pointsNumber(double length, double velocity) {
        return length / (velocity * toSec(Constants.TIME_GAP));
    }

    /**
     * Calculates segment angle represented by radius and chord length in rad.
     *
     * @param radius      radius of the circle in m.
     * @param chordLength length of the chord in m.
     * @return angle of the segment in rad.
     */
    public static double segmentAngle(double radius, double chordLength) {
        return 2 * Math.asin(chordLength / (2 * radius));
    }

    /**
     * Calculates length of an arc represented by radius and chord length in m.
     *
     * @param radius      radius of the circle in m.
     * @param chordLength length of the chord in m.
     * @return length of the arc in m.
     */
    public static double arcLength(double radius, double chordLength) {
        return radius * segmentAngle(radius, chordLength);
    }

    /**
     * Calculates angle of the arc that will be passed during acceleration.
     *
     * @param startVelocity start velocity in m/s.
     * @param finalVelocity final velocity in m/s.
     * @param acceleration  acceleration in m/s^2.
     * @return angle of acceleration in rads.
     */
    public static double angleOfAcceleration(double startVelocity,
                                             double finalVelocity,
                                             double acceleration,
                                             double radius) {
        if (startVelocity > finalVelocity) acceleration = -acceleration;

        double t = 0;
        double angle = 0;
        while (true) {
            double currentVelocity = startVelocity
                    + acceleration * MathCalc.toSec(t);

            if (((acceleration > 0) && (currentVelocity > finalVelocity) ||
                    ((acceleration < 0) && (currentVelocity < finalVelocity)))) {
                break;
            }

            double chordLength = Math.abs(currentVelocity
                    * MathCalc.toSec(Constants.TIME_GAP));
            double segmentAngle = MathCalc.segmentAngle(radius, chordLength);

            angle += segmentAngle;
            t += Constants.TIME_GAP;
        }
        return angle;
    }

    /**
     * Makes angular velocities for one point.
     *
     * @param axisDirections axis directions.
     * @param linearVelocity linear velocity.
     * @param coords         position of the point.
     * @return angular velocities for the point.
     */
    public static double[] makeAngVelocities(double[] axisDirections,
                                             double linearVelocity,
                                             double[] coords) {
        double[] velocities = velocityCustomize(axisDirections, linearVelocity);
        double[] angles = SCARADH.inverseKinematics(coords);

        return SCARADH.velocityKinematics(angles, velocities);
    }

    /**
     * Calculates utmost velocity of the path.
     *
     * @param staticVelocity static velocity of the path.
     * @param outerVelocity  velocity of next/previous G code path.
     * @return real start/final velocity of the path.
     */
    public static double makeUtmostVelocity(double staticVelocity,
                                            double outerVelocity) {
        return (outerVelocity == 0) ? 0 : (outerVelocity + staticVelocity) / 2;
    }

    /**
     * Calculates axis directions of the circle in current coordinates.
     *
     * @param center coordinates of the center.
     * @param coords coordinates of the point.
     * @return axis directions.
     */
    public static double[] makeG02AxisDirections(double[] center,
                                                 double[] coords) {
        double angle = atan2(coords[1] - center[1], coords[0] - center[0]);

        double vx = -sin(angle);
        double vy = cos(angle);
        return new double[]{vx, vy, 0};
    }

    /**
     * Calculates axis directions of the circle in current coordinates.
     *
     * @param center coordinates of the center.
     * @param coords coordinates of the point.
     * @return axis directions.
     */
    public static double[] makeG03AxisDirections(double[] center,
                                                 double[] coords) {
        double[] directions = makeG02AxisDirections(center, coords);

        return new double[]{-directions[0], -directions[1], directions[2]};
    }

    /**
     * Find new radius of the new circle that represented by new start
     * coordinates, old final coordinates, and old center coordinates.
     *
     * @param startCoords start coordinates (new ones).
     * @param finalCoords old final coordinates.
     * @param oldCenter   old center coordinates.
     * @return radius of the new circle.
     */
    public static double findRadius(double[] startCoords, double[] finalCoords,
                                    double[] oldCenter) {


        double x0 = startCoords[0];
        double y0 = startCoords[1];

        double xr = finalCoords[0];
        double yr = finalCoords[1];

        double xc = oldCenter[0];
        double yc = oldCenter[1];

        double x0c = (x0 + xr) / 2;
        double y0c = (y0 + yr) / 2;

        //normal of the сhord
        double lineXNorm = (xr - x0);
        double lineYNorm = (yr - y0);

        //equation of the line, which is perpendicular to the chord
        double A = lineXNorm;
        double B = lineYNorm;
        double C = -lineXNorm * x0c - lineYNorm * y0c;

        //line, which is perpendicular to the previous line and goes through
        // the old center coordinates
        double nA = B;
        double nB = -A;
        double nC = -B * xc + A * yc;

        nC = -nC;
        C = -C;

        //find new center by the Cramer's rule
        double delta = A * nB - B * nA;
        double deltaX = C * nB - B * nC;
        double deltaY = A * nC - C * nA;

        double x = deltaX / delta;
        double y = deltaY / delta;

        return linearLength(startCoords, new double[]{x, y, startCoords[2]});
    }
}

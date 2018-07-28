package com.robotcontrol.util.math;

import com.robotcontrol.calc.DHParameters.SCARADH;

import static com.robotcontrol.parameters.constant.Motion.TIME_GAP;
import static com.robotcontrol.util.math.Converter.toSec;
import static com.robotcontrol.util.math.Converter.toUSec;
import static com.robotcontrol.util.math.Geometry.segmentAngle;
import static java.lang.Math.*;

public class Physics {
    /**
     * Calculates total distance a point travels during the acceleration.
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
        return length / (velocity * toSec(TIME_GAP));
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
                    + acceleration * toSec(t);

            if (((acceleration > 0) && (currentVelocity > finalVelocity) ||
                    ((acceleration < 0) && (currentVelocity < finalVelocity)))) {
                break;
            }

            double chordLength = Math.abs(currentVelocity
                    * toSec(TIME_GAP));
            double segmentAngle = segmentAngle(radius, chordLength);

            angle += segmentAngle;
            t += TIME_GAP;
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

}

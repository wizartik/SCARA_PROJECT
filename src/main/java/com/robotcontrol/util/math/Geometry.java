package com.robotcontrol.util.math;

import static java.lang.Math.*;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class Geometry {

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

package com.robotcontrol.calc.contouringControl.controllers.path;


import com.robotcontrol.parameters.constant.ConstUtil;
import com.robotcontrol.util.Utility;
import com.robotcontrol.calc.checks.GCodeChecker;
import com.robotcontrol.calc.contouringControl.controllers.GCode.GCodeController;
import com.robotcontrol.calc.contouringControl.entities.GCode.*;
import com.robotcontrol.calc.contouringControl.entities.path.Path;
import com.robotcontrol.exc.BoundsViolation;
import com.robotcontrol.exc.ImpossibleToImplement;
import com.robotcontrol.util.math.Converter;
import com.robotcontrol.util.math.Geometry;

import java.util.ArrayList;
import java.util.List;

import static com.robotcontrol.parameters.constant.Safety.MAX_VELOCITY_DIFFERENCE;

public class PathController {

    public static Path makePath(List<GCode> gCodes) throws BoundsViolation, ImpossibleToImplement {
        Path path = new Path();
        path.setgCodeList(gCodes);

        processPath(path);

        path.setFullTime(path.getgCodeList().get(path.getgCodeList().size() - 1).getFinalTime());

        return path;
    }

    /**
     * Initializes GCode objects by initializing each object with it's
     * previous and next velocity.
     */
    private static void initializeGCodePath(Path path) {

        double previousVelocity = 0;
        for (int i = 0; i < path.getgCodeList().size() - 1; i++) {

            if (path.getgCodeList().get(i) instanceof MotionGCode) {
                double nextVelocity = ((MotionGCode) path.getgCodeList()
                        .get(i + 1)).getStaticVelocity();

                path.getgCodeList().get(i).setPreviousVelocity(previousVelocity);
                path.getgCodeList().get(i).setNextVelocity(nextVelocity);

                previousVelocity = ((MotionGCode) path.getgCodeList()
                        .get(i)).getStaticVelocity();
                GCodeController.initialize(path.getgCodeList().get(i));

            }
        }

        path.getgCodeList().get(path.getgCodeList().size() - 1)
                .setNextVelocity(0);
        path.getgCodeList().get(path.getgCodeList().size() - 1)
                .setPreviousVelocity(previousVelocity);
    }

    /**
     * Remakes list of G codes, so it won't contain any G code with length
     * less than allowed. So this method combines together short G codes.
     *
     * @param path path needed to be processed.
     */
    private static void adjustDistances(Path path) throws BoundsViolation {
        int index = -1;
        boolean adjust = false;
        ArrayList<GCode> newGCodes = new ArrayList<>(path.getgCodeList().size());

        double[] startCoords;
        double[] finalCoords;

        for (int i = 0; i < path.getgCodeList().size(); i++) {
            if (path.getgCodeList().get(i) instanceof MotionGCode) {
                if (adjust) {
                    startCoords = path.getgCodeList().get(index).getStartPosition();
                    finalCoords = ((MotionGCode) path.getgCodeList().get(i))
                            .getFinalPosition();

                    double length = Geometry.linearLength(startCoords, finalCoords);
                    if (length > ConstUtil.MIN_LENGTH) {

                        GCode gCode = makeGCode(startCoords, finalCoords,
                                path.getgCodeList().get(i));

                        addGCode(gCode, newGCodes, path);
                        adjust = false;
                    }
                } else if (((MotionGCode) path.getgCodeList().get(i))
                        .getDistance() >= ConstUtil.MIN_LENGTH) {
                    addGCode(path.getgCodeList().get(i), newGCodes, path);
                } else {
                    index = i;
                    adjust = true;
                }
            } else if (path.getgCodeList().get(i).getGCodeType() != GCodeType.GARBAGE) {
                newGCodes.add(path.getgCodeList().get(i));
            }
        }
        newGCodes.trimToSize();
        path.setgCodeList(newGCodes);
    }

    /**
     * Adds G code to specified list and adds its length to the fullLength.
     *
     * @param gCode  G code to be added.
     * @param gCodes list of the G codes.
     */
    private static void addGCode(GCode gCode, ArrayList<GCode> gCodes, Path path) throws BoundsViolation {
        GCodeChecker.checkGCode(gCode);
        gCodes.add(gCode);
        path.setFullDistance(path.getFullDistance() + ((MotionGCode) gCode).getDistance());
    }

    /**
     * Creates new G code (G01) with specified parameters.
     *
     * @param startCoords start position.
     * @param finalCoords final position.
     * @return GCode object.
     */
    private static GCode makeG01(double[] startCoords, double[] finalCoords,
                                 GCode gCode) {
        String gCodeLine = Utility.makeG01String(finalCoords,
                Converter.toExternalVel(((MotionGCode) gCode).getStaticVelocity()));

        return new LinearGCode(startCoords, finalCoords,
                ((MotionGCode) gCode).getStaticVelocity(),
                ((MotionGCode) gCode).getAcceleration(), gCodeLine,
                gCode.getGCodeType());
    }

    /**
     * Creates new G code (G02) with specified parameters which is maximum
     * similar to previous GCode object.
     *
     * @param startCoords start position.
     * @param finalCoords final position.
     * @param gCode       previous GCode object.
     * @return GCode object
     */
    private static GCode makeG02(double[] startCoords, double[] finalCoords,
                                 GCode gCode) {

        String gCodeLine = Utility.makeG02String(finalCoords, ((AngularGCode) gCode).getRadius(),
                Converter.toExternalVel(((AngularGCode) gCode).getStaticVelocity()));

        return new AngularGCode(startCoords,
                finalCoords, ((MotionGCode) gCode).getStaticVelocity(), (
                (MotionGCode) gCode).getAcceleration(), gCodeLine,
                gCode.getGCodeType(), ((AngularGCode) gCode).getRadius());
    }

    /**
     * Creates new G code (G02) with specified parameters which is maximum
     * similar to previous GCode object.
     *
     * @param startCoords start position.
     * @param finalCoords final position.
     * @param gCode       previous GCode object.
     * @return GCode object.
     */
    private static GCode makeG03(double[] startCoords, double[] finalCoords,
                                 GCode gCode) {

        String gCodeLine = Utility.makeG03String(finalCoords, ((AngularGCode) gCode).getRadius(),
                Converter.toExternalVel(((AngularGCode) gCode).getStaticVelocity()));

        return new AngularGCode(startCoords,
                finalCoords, ((MotionGCode) gCode).getStaticVelocity(), (
                (MotionGCode) gCode).getAcceleration(), gCodeLine,
                gCode.getGCodeType(), ((AngularGCode) gCode).getRadius());
    }

    /**
     * Creates new G code with start position in startCoords and final in
     * finalCoords and velocity from previous G code depending on the type of
     * last G code (gCode). If gCode is circular and big enough to not
     * neglect it's trajectory, it will create new circular G code, which
     * will have path maximum similar to gCode's path.
     *
     * @param startCoords start position of the new G code.
     * @param finalCoords final position of the new G code.
     * @param gCode       last G code.
     * @return new adjusted G code.
     */
    private static GCode makeGCode(double[] startCoords, double[] finalCoords,
                                   GCode gCode) {
        GCode buff = null;
        if (gCode != null) {
            GCodeType gCodeType = gCode.getGCodeType();

            // allowedDist shows if current G code is big (important) and we
            // should add all previous path to it because it can be big and
            // important arc or this G code don't cause big affect on whole
            // path and it will be enough to just follow predetermined final
            // coordinates and make it as line
            boolean allowedDist = ((MotionGCode) gCode).getDistance() >= ConstUtil.MIN_LENGTH;
            if (gCodeType.equals(GCodeType.G02) && allowedDist) {
                buff = makeG02(startCoords, finalCoords, gCode);
            } else if (gCodeType.equals(GCodeType.G03) && allowedDist) {
                buff = makeG03(startCoords, finalCoords, gCode);
            } else {
                gCode.setgCodeType(GCodeType.G01);
                buff = makeG01(startCoords, finalCoords, gCode);
            }
        }
        return buff;
    }

    /**
     * Forces stop among 2 G codes if the difference between their
     * utmost angular velocity is big enough to make serious influence
     * on trajectory. G codes should be initialized.
     *
     * @param gCodes bunch of G codes need to be processed.
     */
    private static void adjustHalts(List<GCode> gCodes) {

        double[] previousVelocities = new double[3];

        //to initialize first G code.
        makeHalt(null, gCodes.get(0));
        for (int i = 1; i < gCodes.size() - 1; i++) {
            double[] velocities = ((MotionGCode) gCodes.get(i))
                    .getStartAngVelocities();

            if (needToMakeHalt(previousVelocities, velocities)) {
                makeHalt(gCodes.get(i - 1), gCodes.get(i));
            }
            previousVelocities = ((MotionGCode) gCodes.get(i))
                    .getFinalAngVelocities();
        }
        //to initialize last one
        makeHalt(gCodes.get(gCodes.size() - 1), null);
    }

    /**
     * Compares angular velocities and if on of the differences is bigger
     * than allowed one returns true;
     *
     * @param previousVel previous angular velocity.
     * @param vel         angular velocity.
     * @return true if need to stop, false if not.
     */
    private static boolean needToMakeHalt(double[] previousVel, double[] vel) {
        for (int i = 0; i < previousVel.length; i++) {
            if (previousVel[i] - vel[i] > MAX_VELOCITY_DIFFERENCE) {
                return true;
            }
        }
        return false;
    }

    /**
     * Makes halt among 2 G codes.
     *
     * @param previous previous G code.
     * @param current  current G code;
     */
    private static void makeHalt(GCode previous, GCode current) {
        if (previous != null) {
            previous.setNextVelocity(0);
        }
        if (current != null) {
            current.setPreviousVelocity(0);
        }
    }

    /**
     * Calculates path of each G code. Initializes fullPath.
     *
     * @throws ImpossibleToImplement thrown if its impossible to implement
     *                               this G code.
     */
    private static void calculateGCodes(Path path)
            throws ImpossibleToImplement, BoundsViolation {
        long startTime = 0;

        for (int i = 0; i < path.getgCodeList().size(); i++) {
            GCodeController.calcPath(path.getgCodeList().get(i), startTime);
            startTime = path.getgCodeList().get(i).getFinalTime();
        }
    }

    /**
     * Creates fully processed path of GCode objects and path of points.
     *
     * @throws ImpossibleToImplement if one of G codes is impossible to
     *                               implement.
     */
    private static void processPath(Path path)
            throws ImpossibleToImplement, BoundsViolation {

        adjustDistances(path);
        initializeGCodePath(path);
        adjustHalts(path.getgCodeList());
        calculateGCodes(path);
    }
}

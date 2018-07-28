package calc.contouringControl;

import calc.GCode.*;
import calc.StepperConverter;
import calc.data.Constants;
import calc.data.Dynamic;
import calc.util.MathCalc;
import calc.util.Utility;
import com.robotcontrol.calc.contouringControl.entities.Point;
import com.robotcontrol.exc.BoundsViolation;
import com.robotcontrol.exc.ImpossibleToImplement;
import com.robotcontrol.exc.WrongExtension;
import com.robotcontrol.exc.WrongInputData;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * This class fully represents a path that will be executed. It contains list
 * of G code strings, list of G code objects (GCode), list of Point, list for
 * steppers path.
 */
public class Path {

    /**
     * List of GCode objects that represents path.
     */
    private ArrayList<GCode> gCodes = new ArrayList<>();

    /**
     * List of strings that created this path.
     */
    private ArrayList<String> gCodeStings;

    /**
     * Full path in points.
     */
    private ArrayList<Point> fullPath = new ArrayList<>();

    /**
     * Path for motors.
     */
    private long[][] steppersPath;

    /**
     * Total distance of the path.
     */
    private double fullLength = 0;

    /**
     * Time to pass whole path.
     */
    private double fullTime;

    /**
     * Initializes all fields in the class.
     *
     * @param gCode list of strings of G code.
     * @throws WrongInputData        if there is a typo or incorrect input of data.
     * @throws ImpossibleToImplement if one of the G code commands is
     *                               impossible to implement.
     */
    public Path(ArrayList<String> gCode) throws WrongInputData,
            ImpossibleToImplement, BoundsViolation {
        this.gCodeStings = gCode;
        processPath(gCode, gCodes, fullPath);
    }

    /**
     * Initializes all fields in the class.
     *
     * @param gCode file that contains list of strings of G code.
     * @throws WrongInputData        if there is a typo or incorrect input of data.
     * @throws ImpossibleToImplement if one of the G code commands is
     *                               impossible to implement.
     */
    public Path(File gCode) throws IOException, WrongExtension, WrongInputData,
            ImpossibleToImplement, BoundsViolation {
        ArrayList<String> list = Utility.gCodeFileToList(gCode);
        processPath(list, gCodes, fullPath);
    }

    /**
     * @return list that contains points which represent full path.
     */
    public ArrayList<Point> getFullPath() {
        return fullPath;
    }

    /**
     * @return list of strings which represent this path.
     */
    public ArrayList<String> getgCodeStings() {
        return gCodeStings;
    }

    /**
     * @return full distance of whole path.
     */
    public double getFullLength() {
        return fullLength;
    }

    /**
     * @return list of GCode objects.
     */
    public ArrayList<GCode> getGCodes() {
        return gCodes;
    }

    /**
     * @return path for steppers [t1, t2, t3], where t1/t2/t3 - time in
     * microseconds that should pass after which first/second/third stepper
     * have to make step.
     */
    public long[][] getSteppersPath() {
        return steppersPath;
    }

    /**
     * @return time needed to pass this path in microseconds.
     */
    public double getFullTime() {
        return fullTime;
    }

    /**
     * Parses list of strings into G codes and fills the list of GCode with
     * GCode objects.
     *
     * @param list   list of strings.
     * @param gCodes list of GCode.
     * @return filled list of GCode objects.
     * @throws WrongInputData if there is wrong data in G code string.
     */
    private ArrayList<GCode> createGcodes(ArrayList<String> list,
                                          ArrayList<GCode> gCodes) throws WrongInputData {

        DataFromList data = new DataFromList(list);
        ArrayList<DataFromLine> dataFromList = data.getDataFromList();

        GCode gCode;

        for (int i = 0; i < dataFromList.size(); i++) {
            gCode = chooseGCode(dataFromList.get(i));
            if (gCode != null) {
                gCodes.add(gCode);
            }
        }
        return gCodes;
    }


    /**
     * Creates GCode object from dataFromLine object.
     *
     * @param dataFromLine data from one line in G code.
     * @return GCode object.
     */
    private GCode chooseGCode(DataFromLine dataFromLine) {
        GCodeType gCodeType = dataFromLine.getgCodeType();
        GCode gCode;

        double[] startPosition = dataFromLine.getStartPosition();
        double[] finalPosition = dataFromLine.getFinalPosition();
        double staticVelocity = dataFromLine.getStaticVelocity();
        double acceleration = Constants.NORMAL_ACCELERATION;
        String line = dataFromLine.getLine();

        switch (gCodeType) {
            case G00:
                gCode = new G00(startPosition, finalPosition, Constants.MAX_VELOCITY,
                        Constants.MAX_ACCELERATION, line);
                break;
            case G01:
                gCode = new G01(startPosition, finalPosition, staticVelocity,
                        acceleration, line);
                break;
            case G02:
                gCode = new G02(startPosition, finalPosition,
                        dataFromLine.getRadius(), staticVelocity, acceleration,
                        line);
                break;
            case G03:
                gCode = new G03(startPosition, finalPosition,
                        dataFromLine.getRadius(), staticVelocity, acceleration,
                        line);
                break;
            case G04:
                gCode = new G04(startPosition, dataFromLine.getPauseInUs(),
                        line);
                break;
            default:
                gCode = null;
                break;
        }
        return gCode;
    }

    /**
     * Initializes GCode objects by initializing each object with it's
     * previous and next velocity.
     *
     * @param gCodes list of GCode objects.
     * @return initialized list of GCode objects.
     */
    private ArrayList<GCode> initializeGCodePath(ArrayList<GCode> gCodes) {

        double previousVelocity = 0;
        for (int i = 0; i < gCodes.size() - 1; i++) {

            double nextVelocity = gCodes.get(i + 1).getVelocity();
            gCodes.get(i).initialize(previousVelocity, nextVelocity);
            previousVelocity = gCodes.get(i).getVelocity();
        }
        gCodes.get(gCodes.size() - 1).initialize(previousVelocity, 0);
        return gCodes;
    }

    /**
     * Remakes list of G codes, so it won't contain any G code with length
     * less than allowed. So this method combining together short G codes.
     *
     * @param gCodes list of G code objects.
     * @return redone list of G code objects.
     */
    private ArrayList<GCode> adjustDistances(ArrayList<GCode> gCodes) {
        int index = -1;
        boolean adjust = false;
        ArrayList<GCode> newGCodes = new ArrayList<>();

        double[] startCoords;
        double[] finalCoords;

        double previousVelocity = 0;
        for (int i = 0; i < gCodes.size(); i++) {
            if (adjust) {
                startCoords = gCodes.get(index).getStartPosition();
                finalCoords = gCodes.get(i).getFinalPosition();

                double length = MathCalc.linearLength(startCoords, finalCoords);
                if (length > Constants.MIN_LENGTH) {
                    GCode gCode = makeGCode(startCoords, finalCoords,
                            previousVelocity, gCodes.get(i));
                    addGCode(gCode, newGCodes);
                    adjust = false;
                }
            } else if (gCodes.get(i).getDistance() >= Constants.MIN_LENGTH) {
                addGCode(gCodes.get(i), newGCodes);
                previousVelocity = gCodes.get(i).getVelocity();
            } else {
                index = i;
                adjust = true;
            }
        }
        return newGCodes;
    }

    /**
     * Adds G code to specified list and adds its length to the fullLength.
     *
     * @param gCode  G code to be added.
     * @param gCodes list of the G codes.
     */
    private void addGCode(GCode gCode, ArrayList<GCode> gCodes) {
        gCodes.add(gCode);
        fullLength += gCode.getDistance();
    }

    /**
     * Creates new G code (G01) with specified parameters.
     *
     * @param startCoords start position.
     * @param finalCoords final position.
     * @param velocity    linear velocity.
     * @return GCode object.
     */
    private GCode makeG01(double[] startCoords, double[] finalCoords,
                          double velocity) {
        String gCodeLine = Utility.makeG01String(finalCoords,
                MathCalc.toExternalVel(velocity));
        return new G01(startCoords, finalCoords, velocity,
                Constants.NORMAL_ACCELERATION, gCodeLine);
    }

    /**
     * Creates new G code (G02) with specified parameters which is maximum
     * similar to previous GCode object.
     *
     * @param startCoords start position.
     * @param finalCoords final position.
     * @param velocity    linear velocity.
     * @param gCode       previous GCode object.
     * @return GCode object
     */
    private GCode makeG02(double[] startCoords, double[] finalCoords,
                          double velocity, GCode gCode) {

        double radius = MathCalc.findRadius(startCoords,
                finalCoords, gCode.getCenter());

        String gCodeLine = Utility.makeG02String(finalCoords, radius,
                MathCalc.toExternalVel(velocity));
        return new G02(startCoords, finalCoords, radius, velocity,
                Constants.NORMAL_ACCELERATION, gCodeLine);
    }

    /**
     * Creates new G code (G02) with specified parameters which is maximum
     * similar to previous GCode object.
     *
     * @param startCoords start position.
     * @param finalCoords final position.
     * @param velocity    linear velocity.
     * @param gCode       previous GCode object.
     * @return GCode object.
     */
    private GCode makeG03(double[] startCoords, double[] finalCoords,
                          double velocity, GCode gCode) {
        double radius = MathCalc.findRadius(startCoords,
                finalCoords, gCode.getCenter());

        String gCodeLine = Utility.makeG03String(finalCoords, radius,
                MathCalc.toExternalVel(velocity));
        return new G03(startCoords, finalCoords, radius, velocity,
                Constants.NORMAL_ACCELERATION, gCodeLine);
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
    private GCode makeGCode(double[] startCoords, double[] finalCoords,
                            double velocity, GCode gCode) {
        GCode buff = null;
        if (gCode != null) {
            GCodeType gCodeType = gCode.getGCodeType();

            // allowedDist shows if current G code is big (important) and we
            // should add all previous path to it because it can be big and
            // important arc or this G code don't cause big affect on whole
            // path and it will be enough to just follow predetermined final
            // coordinates and make it as line
            boolean allowedDist = gCode.getDistance() >= Constants.MIN_LENGTH;
            if (gCodeType.equals(GCodeType.G02) && allowedDist) {
                buff = makeG02(startCoords, finalCoords, velocity, gCode);
            } else if (gCodeType.equals(GCodeType.G03) && allowedDist) {
                buff = makeG03(startCoords, finalCoords, velocity, gCode);
            } else {
                buff = makeG01(startCoords, finalCoords, velocity);
            }
        }
        return buff;
    }

    /**
     * Forces stop among 2 G codes if the difference between their
     * utmost angular velocity is big enough to make serious influence
     * on trajectory. G codes should be initialized by initialize() method.
     *
     * @param gCodes bunch of G codes need to be processed.
     */
    private void adjustHalts(ArrayList<GCode> gCodes) {

        double[] previousVelocities = new double[3];

        //to initialize first G code.
        makeHalt(null, gCodes.get(0));
        for (int i = 1; i < gCodes.size() - 1; i++) {
            double[] velocities = gCodes.get(i).getStartAngVelocities();

            if (needToMakeHalt(previousVelocities, velocities)) {
                makeHalt(gCodes.get(i - 1), gCodes.get(i));
            }
            previousVelocities = gCodes.get(i).getFinalAngVelocities();
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
    private boolean needToMakeHalt(double[] previousVel, double[] vel) {
        for (int i = 0; i < previousVel.length; i++) {
            if (previousVel[i] - vel[i] > Constants.MAX_VELOCITY_DIFFERENCE) {
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
    private void makeHalt(GCode previous, GCode current) {
        if (previous != null) {
            previous.initialize(previous.getPreviousVelocity(), 0);
        }
        if (current != null) {
            current.initialize(0, current.getNextVelocity());
        }
    }

    /**
     * Calculates path of each G code. Initializes fullPath.
     *
     * @param gCodes list of G codes.
     * @throws ImpossibleToImplement thrown if its impossible to implement
     *                               this G code.
     */
    private ArrayList<Point> calculateGCodes(ArrayList<GCode> gCodes,
                                             ArrayList<Point> fullPath)
            throws ImpossibleToImplement, BoundsViolation {
        double startTime = 0;

        for (int i = 0; i < gCodes.size(); i++) {
            gCodes.get(i).calculate(startTime);
            if (gCodes.get(i).getGCodePath() != null) {
                fullPath.addAll(gCodes.get(i).getGCodePath());
            }
            startTime = gCodes.get(i).getFinalTime();
        }
        return fullPath;
    }


    /**
     * Creates fully processed path of GCode objects and path of points.
     *
     * @param list     list of strings of G code.
     * @param gCodes   list where will be GCode objects.
     * @param fullPath list where will be Point objects.
     * @throws WrongInputData        if line of G code has wrong data.
     * @throws ImpossibleToImplement if one of G codes is impossible to
     *                               implement.
     */
    private void processPath(ArrayList<String> list, ArrayList<GCode> gCodes,
                             ArrayList<Point> fullPath)
            throws WrongInputData, ImpossibleToImplement, BoundsViolation {
        gCodes = createGcodes(list, gCodes);
        gCodes = adjustDistances(gCodes);
        gCodes = initializeGCodePath(gCodes);
        this.fullPath = calculateGCodes(gCodes, fullPath);
        this.gCodes = gCodes;
//        this.steppersPath = makeStepperPath(this.fullPath);
        this.fullTime = fullPath.get(fullPath.size() - 1).getTime();
    }

    /**
     * Creates path for steppers.
     *
     * @param fullPath full path in Point;
     * @return path for steppers [t1, t2, t3], where t1/t2/t3 - time in
     * microseconds that should pass after which first/second/third stepper
     * have to make step.
     */
    private long[][] makeStepperPath(ArrayList<Point> fullPath) {
        double[][] angles = new double[4][getFullPath().size()];

        for (int i = 0; i < angles.length; i++) {
            angles[0][i] = getFullPath().get(i).getTime();
            angles[1][i] = getFullPath().get(i).getAngles()[0];
            angles[2][i] = getFullPath().get(i).getAngles()[1];
            angles[3][i] = getFullPath().get(i).getAngles()[2];
        }

        return StepperConverter.convertPath(angles, Dynamic.MICROSTEPS);
    }
}


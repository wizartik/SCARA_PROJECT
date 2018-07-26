package calc.GCode;

import com.robotcontrol.calc.contouringControl.GCode.entities.Point;
import exc.ImpossibleToImplement;

import java.util.ArrayList;


/**
 * G04 makes the machine stop what it's doing or dwell for a specified length
 * of time.
 */
public class G04 implements GCode {

    private double startTime;
    private double finalTime;

    private double[] startPosition;
    private double[] finalPosition;

    // start and final angular velocities, needed to check transition
    // validation between G codes
    private double[] startAngVelocities = {0, 0, 0};
    private double[] finalAngVelocities = {0, 0, 0};

    //velocity of the static part
    private double staticVelocity = 0;

    private double previousVelocity;
    private double nextVelocity;

    private double acceleration = 0;

    private double distance = 0;

    private double pauseInUs;

    //start with this velocity
    private double startVelocity;
    //end with this velocity
    private double finalVelocity;

    private String gCode;

    private ArrayList<Point> gCodePath = new ArrayList<>();

    /**
     * @param position  start position of the current G code [x, y, z] in cm.
     * @param pauseInUs pause in microseconds.
     * @param gCode     G code string.
     */
    public G04(double[] position, double pauseInUs, String gCode) {
        this.startPosition = position;
        this.finalPosition = position;
        this.staticVelocity = 0;
        this.acceleration = 0;
        this.gCode = gCode;
        this.distance = 0;
        this.pauseInUs = pauseInUs;
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
        return GCodeType.G04;
    }

    /**
     * Should be initialized first by initialize().
     *
     * @return full path of the current GCode in Points.
     */
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

        this.startVelocity = 0;
        this.finalVelocity = 0;
    }

    /**
     * Generates G code path of points.
     */
    @Override
    public void calculate(double startTime) throws ImpossibleToImplement {
        this.startTime = startTime;
        this.finalTime = startTime + pauseInUs;
    }
}

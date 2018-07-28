package calc.GCode;

import com.robotcontrol.calc.contouringControl.entities.Point;
import calc.data.Constants;
import calc.util.MathCalc;
import com.robotcontrol.exc.BoundsViolation;
import com.robotcontrol.exc.ImpossibleToImplement;

import java.util.ArrayList;

/**
 * For rapid linear motion, program: G0 X~ Y~ Z~ A~ where all
 * the axis words are optional, except that at least one must
 * be used. The G00 is optional if the current motion mode is G0.
 * This will produce coordinated linear motion to the destination
 * point at the current traverse rate (or slower if the machine
 * will not testAll that fast). It is expected that cutting will not
 * take place when a G00 command is executing.
 */
public class G00 implements GCode{

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

    private G01 g01Implementation;

    /**
     * @param startPosition start position of the current G code [x, y, z] in cm.
     * @param finalPosition final position of the current G code [x, y, z] in cm.
     * @param staticVelocity specified velocity of the current G code in cm/s.
     * @param gCode G code string.
     */
    public G00(double[] startPosition, double[] finalPosition,
               double staticVelocity, double acceleration,
               String gCode) {
        this.startPosition = startPosition;
        this.finalPosition = finalPosition;
        this.staticVelocity = staticVelocity;
        this.acceleration = acceleration;
        this.gCode = gCode;
        this.g01Implementation = new G01(startPosition, finalPosition,
                                         Constants.MAX_VELOCITY,
                                         Constants.MAX_ACCELERATION, gCode);
        this.distance = g01Implementation.getDistance();
    }

    /**
     * Initializes data needed to calculate path.
     *
     * @param previousVelocity velocity of the previous G code in cm/s.
     * @param nextVelocity velocity of the nex G code in cm/s.
     */
    @Override
    public void initialize (double previousVelocity, double nextVelocity) {
        this.previousVelocity = previousVelocity;
        this.nextVelocity = nextVelocity;

        g01Implementation.initialize(previousVelocity, nextVelocity);
        startVelocity = MathCalc.makeUtmostVelocity(staticVelocity,
                                                    previousVelocity);
        finalVelocity = MathCalc.makeUtmostVelocity(staticVelocity,
                                                    nextVelocity);
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
        g01Implementation.calculate(startTime);
        gCodePath = g01Implementation.getGCodePath();
        finalTime = gCodePath.get(gCodePath.size() - 1).getTime();
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
    public double getAcceleration(){
        return acceleration;
    }

    /**
     * @return total distance traveled in cm.
     */
    @Override
    public double getDistance(){
        return distance;
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
        return GCodeType.G00;
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

}

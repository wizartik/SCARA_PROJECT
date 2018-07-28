package calc.GCode;

import com.robotcontrol.calc.contouringControl.entities.Point;
import com.robotcontrol.exc.BoundsViolation;
import com.robotcontrol.exc.ImpossibleToImplement;

import java.util.ArrayList;

/**
 * Created by Valerii on 03.09.2017.
 */
public interface GCode {

    /**
     * @return start time of the current G code in μs.
     */
    double getStartTime();

    /**
     * @return final time of the current G code in μs.
     */
    double getFinalTime();

    /**
     * @return start position array of the current G code.
     * [x, y, z].
     */
    double[] getStartPosition();

    /**
     * @return start position array of the current G code.
     * [x, y, z].
     */
    double[] getFinalPosition();

    /**
     * @return start angular velocities of the current G code.
     */
    double[] getStartAngVelocities();

    /**
     * @return final angular velocities of the current G code.
     */
    double[] getFinalAngVelocities();

    /**
     * @return specified velocity for this G code.
     */
    double getVelocity();

    /**
     * @return specified acceleration in cm/s^2.
     */
    double getAcceleration();

    /**
     * @return total distance traveled in cm.
     */
    double getDistance();

    /**
     * @return string of current GCode.
     */
    String getGCode();

    /**
     * @return radius if circular else -1;
     */
    double getRadius();

    /**
     * @return center coords if circular else null;
     */
    double[] getCenter();

    /**
     * @return static velocity of the previous G code.
     */
    double getPreviousVelocity();

    /**
     * @return static velocity of the next G code.
     */
    double getNextVelocity();

    /**
     * @return type of the G code.
     */
    GCodeType getGCodeType();

    /**
     * @return full path of the current GCode in points.
     */
    ArrayList<Point> getGCodePath();

    /**
     * Initializes data needed to calculate path.
     *
     * @param previousVelocity velocity of the previous G code in cm/s.
     * @param nextVelocity velocity of the nex G code in cm/s.
     * @throws ImpossibleToImplement if its impossible to implement current
     *                               G code.
     */
    void initialize (double previousVelocity, double nextVelocity);

    /**
     * Generates G code path of points.
     *
     * @param startTime initial time of the current G code in us.
     */
    void calculate (double startTime) throws ImpossibleToImplement, BoundsViolation;
}

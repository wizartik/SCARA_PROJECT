package calc.checks;

import calc.GCode.GCodeType;
import com.robotcontrol.calc.contouringControl.entities.Point;
import calc.data.Constants;
import calc.data.Dynamic;
import calc.util.MathCalc;
import com.robotcontrol.exc.BoundsViolation;

public class Checker {

    /**
     * Checks velocity, if greater than allowed returns allowed, else returns
     * origin. If velocity < 0, returns positive velocity.
     *
     * @param velocity checking velocity.
     * @param gCodeType type of G code.
     * @return chosen velocity.
     */
    public static double checkVelocity(double velocity, GCodeType gCodeType) {
        if (velocity >= Constants.MAX_VELOCITY) {
            if (gCodeType.equals(GCodeType.G00)) {
                return Constants.MAX_VELOCITY;
            } else {
                return Constants.NORMAL_VELOCITY;
            }
        } else if (velocity < 0) {
            return -velocity;
        }
        return velocity;
    }

    /**
     * Checks if point is out of allowed area.
     *
     * @param point checking point.
     * @throws BoundsViolation when point is out of allowed area.
     */
    public static void checkBoundViolation(Point point) throws BoundsViolation {
        checkRadius(point);
        checkHeight(point);
    }

    /**
     * Checks if point is out of allowed working plane.
     *
     * @param point checking point.
     * @throws BoundsViolation when point is out of allowed area.
     */
    private static void checkRadius(Point point) throws BoundsViolation {
        double[] position = point.getPosition();
        position[2] = 0.0;

        double radius = MathCalc.linearLength(position, new double[]{0, 0, 0});
        if (radius > Dynamic.MAX_RADIUS) {
            StringBuilder excMsg = new StringBuilder();
            excMsg.append("Operating distance is greater than allowed ");
            excMsg.append(moreThan(radius, Dynamic.MAX_RADIUS));
            excMsg.append(". \n");
            throw new BoundsViolation(excMsg.toString(), point.getGCode());
        } else if (radius < Dynamic.MIN_RADIUS) {
            StringBuilder excMsg = new StringBuilder();
            excMsg.append("Operating distance is less than allowed ");
            excMsg.append(lessThan(Dynamic.MIN_RADIUS, radius));
            excMsg.append(". \n");
            throw new BoundsViolation(excMsg.toString(), point.getGCode());
        }
    }

    /**
     * Checks if point is out of allowed working height.
     *
     * @param point checking point.
     * @throws BoundsViolation when point is out of allowed area.
     */
    private static void checkHeight(Point point) throws BoundsViolation {

        double height = point.getPosition()[2];
        if (height > Constants.MAX_HEIGHT_COORD) {
            StringBuilder excMsg = new StringBuilder();
            excMsg.append("Operating height is greater than allowed ");
            excMsg.append(moreThan(height, Constants.MAX_HEIGHT_COORD));
            excMsg.append(". \n");
            throw new BoundsViolation(excMsg.toString(), point.getGCode());
        } else if (height < Constants.MIN_HEIGHT_COORD) {
            StringBuilder excMsg = new StringBuilder();
            excMsg.append("Operating height is less than allowed ");
            excMsg.append(lessThan(Constants.MIN_HEIGHT_COORD, height));
            excMsg.append(". \n");
            throw new BoundsViolation(excMsg.toString(), point.getGCode());
        }
    }

    /**
     * Creates a string with message that one num is bigger.
     *
     * @param greater greater number;
     * @param less less number
     * @return string (greater > less).
     */
    private static String moreThan (double greater, double less){
        return "(" + greater + " > " + less + ")";
    }

    /**
     * Creates a string with message that one num is less.
     *
     * @param greater greater number;
     * @param less less number
     * @return string (less < greater).
     */
    private static String lessThan (double greater, double less) {
        return "(" + less + " < " + greater + ")";
    }
}

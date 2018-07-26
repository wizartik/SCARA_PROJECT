package calc.data;

public class Dynamic {

    /**
     * Length of the first link in cm.
     */
    public static double FIRST_LINK_LENGTH = Constants.FIRST_LINK_SELECTIVE;
    /**
     * Length of the second link in cm.
     */
    public static double SECOND_LINK_LENGTH = Constants.SECOND_LINK_SELECTIVE;

    /**
     * Maximum allowed operating distance.
     */
    public static double MAX_RADIUS = Constants.MAX_RADIUS_DRAWING + 200;

    /**
     * Minimum allowed operating distance.
     */
    public static double MIN_RADIUS = Constants.MIN_RADIUS_DRAWING - 200;

    /**
     * Define in which direction arm's elbow will be directed.
     * \            /
     *  \         /
     *   \      /
     *    * or *
     *   /      \
     *  /        \
     * /          \
     */
    public static boolean DIRECTION = true;

    /**
     * Coordinates of the home position.
     */
    public static double[] HOME_COORDS = {10, 10, 10};


    /**
     * Current position of the working body.
     */
    public static double[] CURRENT_POSITION = HOME_COORDS;

    /**
     * Number of microsteps per step.
     */
    public static byte MICROSTEPS = 8;
}

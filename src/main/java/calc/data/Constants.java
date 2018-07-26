package calc.data;

/**
 * Created by Valerii on 03.09.2017.
 */
public class Constants {

    /**
     * Length of the first link for drawing.
     */
    public final static double FIRST_LINK_DRAWING = 20;

    /**
     * Length of the second link for drawing.
     */
    public final static double SECOND_LINK_DRAWING = 20;

    /**
     * Length of the first ling for selecting, assembling, pick and place, etc.
     */
    public final static double FIRST_LINK_SELECTIVE = 20;

    /**
     * Length of the second ling for selecting, assembling, pick and place, etc.
     */
    public final static double SECOND_LINK_SELECTIVE = 20;

    /**
     * Height that will be increased per one revolution in Z axis in cm.
     */
    public final static double INCREMENT_PER_REVOLUTION = 0.8;

    /**
     * Maximum allowed height working body can lift.
     */
    public final static double MAX_HEIGHT_COORD = 20;

    /**
     * Minimum allowed height working body can lift.
     */
    public final static double MIN_HEIGHT_COORD = -20;

    /**
     * Maximum allowed operating distance for drawing.
     */
    public final static double MAX_RADIUS_DRAWING = 40;

    /**
     * Minimum allowed operating distance for drawing.
     */
    public final static double MIN_RADIUS_DRAWING = 10;

    /**
     * Maximum allowed operating distance for pick and place.
     */
    public final static double MAX_RADIUS_SELECTIVE = 40;

    /**
     * Minimum allowed operating distance for pick and place.
     */
    public final static double MIN_RADIUS_SELECTIVE = 10;

    /**
     * Time Gap for calculations in microseconds.
     */
    public final static double TIME_GAP = 250;

    /**
     * Maximum allowed acceleration due to big torque in cm/s^2.
     */
    public final static double MAX_ACCELERATION = 150;

    /**
     * Normal acceleration if initial acceleration is not specified (in cm/s^2).
     */
    public final static double NORMAL_ACCELERATION = 100;

    /**
     * Maximum allowed velocity for no-load G00 in cm/s.
     */
    public final static double MAX_VELOCITY = 2;

    /**
     * Normal velocity if initial velocity is not specified (in cm/s).
     */
    public final static double NORMAL_VELOCITY = 1;

    /**
     * Allowed filename extensions. If this array doesn't contain
     * extension of the file, that file may be not supported.
     */
    public final static String[] ALLOWED_FILENAME_EXTENSIONS = {"txt",
            "gcode",
            "ngc"};

    public final static char[] KNOWN_COMMANDS = {'G', 'X', 'Y', 'Z', 'F',
            'R', 'I', 'J', 'P', 'U'};

    /**
     * Minimum allowed length of the G code. It's the length of the
     * acceleration or deceleration from 0 to max speed.
     */
    public final static double MIN_LENGTH = Math.pow(MAX_VELOCITY, 2)
            / (2 * MAX_ACCELERATION);

    /**
     * Maximum angular velocity difference among 2 G codes utmost angular
     * velocities. Bigger difference will cause serious influence
     * on trajectory.
     */
    public final static double MAX_VELOCITY_DIFFERENCE = 0.3;

    public final static double STEP = 1.8;
//    /**
//     * Maximum time of smooth path of acceleration in microseconds.
//     */
//    public final static double MAX_SMOOTH_PATH_TIME = 50_000;
}

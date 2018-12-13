package com.robotcontrol.movement;

import com.robotcontrol.calc.DHParameters.SCARADH;
import com.robotcontrol.gui.util.DialogHandler;
import com.robotcontrol.parameters.constant.PhysicalParameters;
import com.robotcontrol.parameters.dynamic.DynUtil;
import com.robotcontrol.parameters.dynamic.Motion;
import com.robotcontrol.parameters.dynamic.Position;
import com.robotcontrol.util.math.Converter;
import com.robotcontrol.util.progress.CurrentAction;

import java.text.DecimalFormat;
import java.util.Arrays;

import static com.robotcontrol.parameters.dynamic.Motion.MOVING;
import static com.robotcontrol.parameters.dynamic.Position.*;

public class ParametersController {

    public static boolean isMoving() {
        return Motion.MOVING;
    }

    public static void startedMovement(double[] finalPosition) {
        MOVING = true;
        DESIRED_POSITION = Arrays.copyOf(finalPosition, finalPosition.length);
    }

    public static void startedAngMovement(double[] finalAngles) {
        MOVING = true;
        DESIRED_ANGLES = Arrays.copyOf(finalAngles, finalAngles.length);
    }

    public static void finishedMovement() {
        MOVING = false;

        if (!Arrays.equals(DESIRED_POSITION, CURRENT_POSITION)) {
            setCurrentCoods(DESIRED_POSITION);
        } else if (!Arrays.equals(DESIRED_ANGLES, CURRENT_ANGLES)) {
            setCurrentAngles(DESIRED_ANGLES);
        }
    }

    public static void startedCalibration() {
        MOVING = true;
    }

    public static void finishedCalibration() {
        MOVING = false;
        setCurrentCoods(getPositionAfterCalibration());
    }

    public static void setCurrentCoods(double[] coords) {
        CURRENT_POSITION = Arrays.copyOf(coords, coords.length);
        DESIRED_POSITION = Arrays.copyOf(coords, coords.length);
        CURRENT_ANGLES = Arrays.copyOf(SCARADH.inverseKinematics(coords), coords.length);
        DESIRED_ANGLES = Arrays.copyOf(CURRENT_ANGLES, CURRENT_ANGLES.length);
        updateDisplayedValues();
    }

    public static void setCurrentAngles(double[] angles) {
        CURRENT_POSITION = Arrays.copyOf(SCARADH.forwardKinematics(angles), angles.length);
        DESIRED_POSITION = Arrays.copyOf(CURRENT_POSITION, CURRENT_POSITION.length);
        CURRENT_ANGLES = Arrays.copyOf(angles, angles.length);
        DESIRED_ANGLES = Arrays.copyOf(angles, angles.length);
        updateDisplayedValues();
    }

    private static void updateDisplayedValues() {
        DISPLAYED_CURRENT_COORDS = Arrays.copyOf(CURRENT_POSITION, CURRENT_POSITION.length);
        DISPLAYED_CURRENT_ANGLES = Arrays.copyOf(CURRENT_ANGLES, CURRENT_ANGLES.length);
    }

    public static void setProgress(double progress) {
        progress *= 100;
        DecimalFormat decimalFormat = new DecimalFormat("##");
        DynUtil.CURRENT_PROGRESS.set(decimalFormat.format(progress) + "%");
    }

    public static String getCurrentCoordsString() {
        DecimalFormat decimalFormat = new DecimalFormat("##.#");
        return "[" + decimalFormat.format(DISPLAYED_CURRENT_COORDS[0]) + "; "
                + decimalFormat.format(DISPLAYED_CURRENT_COORDS[1]) + "; "
                + decimalFormat.format(DISPLAYED_CURRENT_COORDS[2]) + "]";
    }

    public static String getCurrentAnglesString() {
        DecimalFormat decimalFormat = new DecimalFormat("##.#");
        return "[" + decimalFormat.format(DISPLAYED_CURRENT_ANGLES[0]) + "; "
                + decimalFormat.format(DISPLAYED_CURRENT_ANGLES[1]) + "; "
                + decimalFormat.format(DISPLAYED_CURRENT_ANGLES[2]) + "]";
    }

    public static void motorCrash(int number) {
        DialogHandler.showMotorCrash(number);
        MOVING = false;
    }

    public static void setCurrentAction(CurrentAction action) {
        DynUtil.CURRENT_ACTION_STRING = action.toString();
    }

    public static void updateCurrentCoords(int first, int second, int third) {
        double firstChange = ((double) first) / Motion.MICROSTEPS[0] / PhysicalParameters.REDUCTION_RATIO[0] * Math.toRadians(PhysicalParameters.STEP);
        double secondChange = ((double) second) / Motion.MICROSTEPS[0] / PhysicalParameters.REDUCTION_RATIO[0] * Math.toRadians(PhysicalParameters.STEP);
        double thirdChange = ((double) third) / Motion.MICROSTEPS[0] / PhysicalParameters.REDUCTION_RATIO[0] * Math.toRadians(PhysicalParameters.STEP);

        DISPLAYED_CURRENT_ANGLES = new double[]{CURRENT_ANGLES[0] + firstChange, CURRENT_ANGLES[1] + secondChange, CURRENT_ANGLES[2] + thirdChange};
        DISPLAYED_CURRENT_COORDS = SCARADH.forwardKinematics(DISPLAYED_CURRENT_ANGLES);
    }

    private static double[] getPositionAfterCalibration() {
        return SCARADH.forwardKinematics(Converter.toRad(Position.ANGLES_AFTER_CALIBRATION));
    }
}

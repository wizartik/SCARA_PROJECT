package com.robotcontrol.movement;

import com.robotcontrol.calc.DHParameters.SCARADH;
import com.robotcontrol.gui.util.DialogHandler;
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
        System.out.println("CURRENT_POSITION " + Arrays.toString(CURRENT_POSITION));
        System.out.println("DESIRED_POSITION " + Arrays.toString(DESIRED_POSITION));
        System.out.println("CURRENT_ANGLES   " + Arrays.toString(CURRENT_ANGLES));
        System.out.println("DESIRED_ANGLES " + Arrays.toString(DESIRED_ANGLES));


        if (!Arrays.equals(DESIRED_POSITION, CURRENT_POSITION)) {
            System.out.println("finished coords");
            setCurrentCoods(DESIRED_POSITION);
        } else if (!Arrays.equals(DESIRED_ANGLES, CURRENT_ANGLES)){
            System.out.println("finished angs");
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
    }

    public static void setCurrentAngles(double[] angles){
        CURRENT_POSITION = Arrays.copyOf(SCARADH.forwardKinematics(angles), angles.length);
        DESIRED_POSITION = Arrays.copyOf(CURRENT_POSITION, CURRENT_POSITION.length);
        CURRENT_ANGLES = Arrays.copyOf(angles, angles.length);
        DESIRED_ANGLES = Arrays.copyOf(angles, angles.length);
    }

    public static void setProgress(double progress) {
        progress *= 100;
        DecimalFormat decimalFormat = new DecimalFormat("##");
        DynUtil.CURRENT_PROGRESS.set(decimalFormat.format(progress) + "%");
    }

    public static String getCurrentCoordsString() {
        DecimalFormat decimalFormat = new DecimalFormat("##.#");
        return "[" + decimalFormat.format(CURRENT_POSITION[0]) + "; " + decimalFormat.format(CURRENT_POSITION[1]) + "; " + decimalFormat.format(CURRENT_POSITION[2]) + "]";
    }

    public static void motorCrash(int number) {
        DialogHandler.showMotorCrash(number);
        MOVING = false;
    }

    public static void setCurrentAction(CurrentAction action) {
        DynUtil.CURRENT_ACTION_STRING = action.toString();
    }

    private static double[] getPositionAfterCalibration() {
        return SCARADH.forwardKinematics(Converter.toRad(Position.ANGLES_AFTER_CALIBRATION));
    }
}

package com.robotcontrol.movement;

import com.robotcontrol.calc.DHParameters.SCARADH;
import com.robotcontrol.parameters.dynamic.Motion;
import com.robotcontrol.parameters.dynamic.Position;
import com.robotcontrol.util.math.Converter;

import java.text.DecimalFormat;
import java.util.Arrays;

import static com.robotcontrol.parameters.dynamic.Motion.MOVING;
import static com.robotcontrol.parameters.dynamic.Position.CURRENT_POSITION;
import static com.robotcontrol.parameters.dynamic.Position.CURRENT_POSITION_STRING;
import static com.robotcontrol.parameters.dynamic.Position.DESIRED_POSITION;

public class ParametersController {

    public static boolean isMoving() {
        return Motion.MOVING;
    }

    public static void startedMovement(double[] finalPosition) {
        MOVING = true;
        DESIRED_POSITION = finalPosition;
    }

    public static void finishedMovement() {
        MOVING = false;
        setCurrentCoods(DESIRED_POSITION);
    }

    public static void startedCalibration() {
        MOVING = true;
    }

    public static void finishedCalibration() {
        MOVING = false;
        setCurrentCoods(getPositionAfterCalibration());
    }

    public static void setCurrentCoods(double[] coords){
        CURRENT_POSITION = Arrays.copyOf(coords, coords.length);
        CURRENT_POSITION_STRING.set(getCurrentCoordsString());
    }

    public static String getCurrentCoordsString(){
        DecimalFormat decimalFormat = new DecimalFormat("##.#");
        return "[" + decimalFormat.format(CURRENT_POSITION[0]) + "; " + decimalFormat.format(CURRENT_POSITION[1]) + "; " + decimalFormat.format(CURRENT_POSITION[2]) + "]";
    }

    private static double[] getPositionAfterCalibration(){
        return SCARADH.forwardKinematics(Converter.toRad(Position.ANGLES_AFTER_CALIBRATION));
    }
}

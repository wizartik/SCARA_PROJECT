package com.robotcontrol.movement;

import com.robotcontrol.calc.DHParameters.SCARADH;
import com.robotcontrol.parameters.dynamic.Motion;
import com.robotcontrol.parameters.dynamic.Position;
import com.robotcontrol.util.math.Converter;

import static com.robotcontrol.parameters.dynamic.Motion.MOVING;
import static com.robotcontrol.parameters.dynamic.Position.CURRENT_POSITION;
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
        CURRENT_POSITION = DESIRED_POSITION;
    }

    public static void startedCalibration() {
        MOVING = true;
    }

    public static void finishedCalibration() {
        MOVING = false;
        CURRENT_POSITION = getPositionAfterCalibration();
    }

    private static double[] getPositionAfterCalibration(){
        return SCARADH.forwardKinematics(Converter.toRad(Position.ANGLES_AFTER_CALIBRATION));
    }
}

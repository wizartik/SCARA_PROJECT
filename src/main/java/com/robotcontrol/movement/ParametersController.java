package com.robotcontrol.movement;

import com.robotcontrol.parameters.dynamic.Motion;

import static com.robotcontrol.parameters.dynamic.Motion.MOVING;
import static com.robotcontrol.parameters.dynamic.Position.CURRENT_POSITION;
import static com.robotcontrol.parameters.dynamic.Position.DESIRED_POSITION;

public class ParametersController {

    public static boolean isMoving(){
        return Motion.MOVING;
    }

    public static void startedMovement(double[] finalPosition){
        MOVING = true;
        DESIRED_POSITION = finalPosition;
    }

    public static void finishedMovement(){
        MOVING = false;
        CURRENT_POSITION = DESIRED_POSITION;
    }
}

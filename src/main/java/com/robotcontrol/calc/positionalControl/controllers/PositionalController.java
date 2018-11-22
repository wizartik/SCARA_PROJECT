/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.robotcontrol.calc.positionalControl.controllers;

import com.robotcontrol.calc.DHParameters.SCARADH;
import com.robotcontrol.calc.checks.PositionalChecker;
import com.robotcontrol.calc.positionalControl.entities.PositionalPath;
import com.robotcontrol.exc.BoundsViolation;
import com.robotcontrol.parameters.dynamic.Motion;

public class PositionalController {

    public static PositionalPath moveToPointAng(double[] startAngles,
                                                double[] finalAngles) throws BoundsViolation {
        PositionalChecker.checkPositionalPathAngular(startAngles, finalAngles);
        return MotionHandler.makePath(startAngles, finalAngles,
                Motion.ANG_VELOCITY, Motion.ANG_ACCELERATION);
    }

    public static PositionalPath moveToPointPos(double[] startPosition,
                                                double[] finalPosition) throws BoundsViolation {
        PositionalChecker.checkPositionalPath(startPosition, finalPosition);
        return MotionHandler.makePath(SCARADH.inverseKinematics(startPosition),
                SCARADH.inverseKinematics(finalPosition),
                Motion.ANG_VELOCITY, Motion.ANG_ACCELERATION);
    }

}

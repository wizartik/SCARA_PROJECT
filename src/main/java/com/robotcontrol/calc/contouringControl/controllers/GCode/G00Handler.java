package com.robotcontrol.calc.contouringControl.controllers.GCode;

import com.robotcontrol.calc.contouringControl.entities.GCode.LinearGCode;
import com.robotcontrol.exc.BoundsViolation;
import com.robotcontrol.exc.ImpossibleToImplement;

import static com.robotcontrol.parameters.constant.Motion.MAX_ACCELERATION;
import static com.robotcontrol.parameters.constant.Motion.MAX_VELOCITY;

class G00Handler {
    static void calcPath(LinearGCode gCode, long startTime) throws
            BoundsViolation, ImpossibleToImplement {
        gCode.setStaticVelocity(MAX_VELOCITY);
        gCode.setAcceleration(MAX_ACCELERATION);
        G01Handler.calcPath(gCode, startTime);
    }
}

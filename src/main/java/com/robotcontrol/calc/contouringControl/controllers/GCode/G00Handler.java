package com.robotcontrol.calc.contouringControl.controllers.GCode;

import calc.data.Constants;
import com.robotcontrol.calc.contouringControl.entities.GCode.LinearGCode;
import exc.BoundsViolation;
import exc.ImpossibleToImplement;

class G00Handler {
    static void calcPath(LinearGCode gCode, double startTime) throws
            BoundsViolation, ImpossibleToImplement {
        gCode.setStaticVelocity(Constants.MAX_VELOCITY);
        gCode.setAcceleration(Constants.MAX_ACCELERATION);
        G01Handler.calcPath(gCode, startTime);
    }
}

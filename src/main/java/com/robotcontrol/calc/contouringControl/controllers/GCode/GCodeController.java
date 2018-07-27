package com.robotcontrol.calc.contouringControl.controllers.GCode;

import calc.util.MathCalc;
import com.robotcontrol.calc.contouringControl.entities.GCode.AngularGCode;
import com.robotcontrol.calc.contouringControl.entities.GCode.LinearGCode;
import com.robotcontrol.calc.contouringControl.entities.GCode.MotionGCode;
import com.robotcontrol.calc.contouringControl.entities.Point;
import exc.BoundsViolation;
import exc.ImpossibleToImplement;

import java.util.ArrayList;

public class GCodeController {

    public static void calcPath(MotionGCode gCode, double startTime) throws
            BoundsViolation, ImpossibleToImplement {

        if (gCode.getGCodeType() != null){
            makeEmptyPath(gCode);
            switch (gCode.getGCodeType()){
                case G00:
                    G00Handler.calcPath((LinearGCode) gCode, startTime);
                    break;
                case G01:
                    G01Handler.calcPath((LinearGCode) gCode, startTime);
                    break;
                case G02:
                    G02Handler.calcPath((AngularGCode) gCode, startTime);
                    break;
                case G03:
                    G03Handler.calcPath((AngularGCode) gCode, startTime);
                    break;
            }
        }
    }

    private static void makeEmptyPath(MotionGCode gCode){
        int pointsNumber = (int) (MathCalc.pointsNumber(gCode.getDistance(),
                gCode.getVelocity()) * 1.2);
        ArrayList<Point> path = new ArrayList<>(pointsNumber);
        gCode.setgCodePath(path);
    }
}

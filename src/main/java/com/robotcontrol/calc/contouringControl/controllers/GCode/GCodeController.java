package com.robotcontrol.calc.contouringControl.controllers.GCode;

import calc.util.MathCalc;
import com.robotcontrol.calc.contouringControl.entities.GCode.*;
import com.robotcontrol.calc.contouringControl.entities.Point;
import com.robotcontrol.exc.BoundsViolation;
import com.robotcontrol.exc.ImpossibleToImplement;

import java.util.ArrayList;

public class GCodeController {

    public static void calcPath(GCode gCode, double startTime) throws
            BoundsViolation, ImpossibleToImplement {

        if (gCode.getGCodeType() != null && gCode instanceof MotionGCode){
            makeEmptyPath((MotionGCode) gCode);
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

    public static void initialize(GCode gCode){
        if (gCode instanceof LinearGCode){
            G01Handler.initialize((LinearGCode) gCode);
        } else if (gCode instanceof AngularGCode){
            if (gCode.getGCodeType() == GCodeType.G02){
                G02Handler.initialize((AngularGCode) gCode);
            } else if (gCode.getGCodeType() == GCodeType.G03){
                G03Handler.initialize((AngularGCode) gCode);
            }
        }
    }
}

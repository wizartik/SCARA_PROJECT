package com.robotcontrol.tests;

import com.robotcontrol.calc.contouringControl.controllers.data.LineHandler;
import com.robotcontrol.calc.contouringControl.entities.GCode.AngularGCode;
import com.robotcontrol.calc.contouringControl.entities.GCode.GCode;
import com.robotcontrol.calc.contouringControl.entities.GCode.GCodeType;
import com.robotcontrol.calc.contouringControl.entities.GCode.MotionGCode;
import exc.BoundsViolation;
import exc.ImpossibleToImplement;
import exc.WrongInputData;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws BoundsViolation, ImpossibleToImplement, exc.BoundsViolation, exc.ImpossibleToImplement, WrongInputData {

        MotionGCode motionGCode = new MotionGCode(new double[]{1,2,3}, new
                double[]{5,6,7}, 10, 10, "helllllo", GCodeType.G01);

        GCode gCode = (LineHandler.makeGCode(motionGCode, "G03 X40 Y50 Z60.5 " +
                "R3"));

        System.out.println(gCode.getGCode());
        System.out.println(Arrays.toString(gCode.getStartPosition()));
        System.out.println(gCode.getGCodeType());

        MotionGCode motionGCode1 = (MotionGCode) gCode;

        System.out.println(Arrays.toString(motionGCode1.getFinalPosition()));
        System.out.println(motionGCode1.getStaticVelocity());
        System.out.println(motionGCode1.getAcceleration());

        AngularGCode angularGCode = (AngularGCode) gCode;

        System.out.println("radius " + angularGCode.getRadius());
        System.out.println("center " + Arrays.toString(angularGCode.getCenterPosition()));

    }
}

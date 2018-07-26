package com.robotcontrol.tests;

import calc.GCode.G02;
import calc.data.Constants;
import com.robotcontrol.calc.contouringControl.GCode.controllers.GCodeController;
import com.robotcontrol.calc.contouringControl.GCode.entities.GCodes.AngularGCode;
import com.robotcontrol.calc.contouringControl.GCode.entities.GCodes.GCodeType;
import exc.BoundsViolation;
import exc.ImpossibleToImplement;

public class Main {
    public static void main(String[] args) throws BoundsViolation, ImpossibleToImplement, exc.BoundsViolation, exc.ImpossibleToImplement {
        double[] startPosition = {10, 10, 10};
        double[] finalPosition = {15, 15, 15};
        double radius = 5;
        double staticVelocity = Constants.NORMAL_VELOCITY;
        double acceleration = Constants.NORMAL_ACCELERATION;
        String gCode = "gCode";
        GCodeType gCodeType = GCodeType.G02;

        G02 g02 = new G02(startPosition, finalPosition, radius,
                staticVelocity, acceleration, gCode);

        g02.initialize(0,0);
        g02.calculate(0);

        AngularGCode angularGCode = new AngularGCode(startPosition,
                finalPosition, staticVelocity, acceleration, gCode,
                gCodeType, radius);
        GCodeController.calcPath(angularGCode, 0);


        System.out.println(angularGCode.getgCodePath().size());
        System.out.println(g02.getGCodePath().size());
        System.out.println(angularGCode.getDistance());
        for (int i = 0; i < angularGCode.getgCodePath().size(); i++) {
            System.out.println(angularGCode.getgCodePath().get(i).equals
                    (g02.getGCodePath().get(i)));
        }
    }
}

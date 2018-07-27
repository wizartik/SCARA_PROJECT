package com.robotcontrol.tests;

import calc.GCode.G03;
import calc.data.Constants;
import com.robotcontrol.calc.contouringControl.GCode.controllers.GCodeController;
import com.robotcontrol.calc.contouringControl.GCode.entities.GCodes.AngularGCode;
import com.robotcontrol.calc.contouringControl.GCode.entities.GCodes.GCodeType;
import exc.BoundsViolation;
import exc.ImpossibleToImplement;

public class Main {
    public static void main(String[] args) throws BoundsViolation, ImpossibleToImplement, exc.BoundsViolation, exc.ImpossibleToImplement {
        double[] startPosition = {15, 15, 15};
        double[] finalPosition = {10, 10, 10};
        double radius = 5;
        double staticVelocity = Constants.NORMAL_VELOCITY;
        double acceleration = Constants.NORMAL_ACCELERATION;
        String gCode = "gCode";
        GCodeType gCodeType = GCodeType.G03;

        G03 g03 = new G03(startPosition, finalPosition, radius,
                staticVelocity, acceleration, gCode);

        g03.initialize(0,0);
        g03.calculate(0);

        AngularGCode angularGCode = new AngularGCode(startPosition,
                finalPosition, staticVelocity, acceleration, gCode,
                gCodeType, radius);
        GCodeController.calcPath(angularGCode, 0);


        System.out.println(angularGCode.getgCodePath().size());
        System.out.println(g03.getGCodePath().size());
        System.out.println(angularGCode.getDistance());
        for (int i = 0; i < angularGCode.getgCodePath().size(); i++) {
            System.out.println(angularGCode.getgCodePath().get(i).equals
                    (g03.getGCodePath().get(i)));
        }
    }
}

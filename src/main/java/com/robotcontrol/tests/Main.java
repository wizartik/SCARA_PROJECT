package com.robotcontrol.tests;

import com.robotcontrol.calc.contouringControl.controllers.data.DataController;
import com.robotcontrol.calc.contouringControl.controllers.path.PathController;
import com.robotcontrol.calc.contouringControl.entities.path.ContourPath;
import com.robotcontrol.calc.stepperControl.controllers.ContourPathHandler;
import com.robotcontrol.calc.stepperControl.entities.SteppersPath;
import com.robotcontrol.exc.BoundsViolation;
import com.robotcontrol.exc.ImpossibleToImplement;
import com.robotcontrol.exc.WrongExtension;
import com.robotcontrol.exc.WrongInputData;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws WrongExtension, BoundsViolation, WrongInputData, IOException, ImpossibleToImplement {

        ContourPath contourPath = PathController.makePath(DataController.convertToGCode(new File("D:\\GCodes\\test.txt")));

        SteppersPath steppersPath = ContourPathHandler.makeStepperPath(contourPath);

//

    }
}

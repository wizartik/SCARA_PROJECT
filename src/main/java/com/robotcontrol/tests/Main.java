package com.robotcontrol.tests;

import com.robotcontrol.calc.contouringControl.controllers.data.DataController;
import com.robotcontrol.calc.contouringControl.controllers.path.PathController;
import com.robotcontrol.calc.contouringControl.entities.path.ContourPath;
import com.robotcontrol.calc.stepperControl.controllers.PathConverter;
import com.robotcontrol.calc.stepperControl.entities.SteppersPath;
import com.robotcontrol.exc.BoundsViolation;
import com.robotcontrol.exc.ImpossibleToImplement;
import com.robotcontrol.exc.WrongExtension;
import com.robotcontrol.exc.WrongInputData;
import jssc.SerialPortException;

import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws WrongExtension, BoundsViolation, WrongInputData, IOException, ImpossibleToImplement, SerialPortException {

        ContourPath contourPath = PathController.makePath(DataController.convertToGCode(new File("D:\\GCodes\\test.txt")));
//
        SteppersPath steppersPath = PathConverter.convertToSteppersPath(contourPath);//
//

//        PortController.sendSteppersPath(steppersPath);
//        System.out.println(steppersPath.getSteppersPoints().size());

        // getting serial ports list into the array
//

    }
}

package com.robotcontrol.calc;

import com.robotcontrol.calc.contouringControl.controllers.data.DataController;
import com.robotcontrol.calc.contouringControl.controllers.path.PathController;
import com.robotcontrol.calc.contouringControl.entities.GCode.GCode;
import com.robotcontrol.calc.stepperControl.controllers.PathConverter;
import com.robotcontrol.calc.stepperControl.controllers.SeparateContourController;
import com.robotcontrol.exc.BoundsViolation;
import com.robotcontrol.exc.ImpossibleToImplement;
import com.robotcontrol.exc.WrongExtension;
import com.robotcontrol.exc.WrongInputData;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.robotcontrol.parameters.dynamic.DynUtil.CURRENT_CONTOUR_PATH;
import static com.robotcontrol.parameters.dynamic.DynUtil.CURRENT_CONTOUR_STEPPER_PATH;
import static com.robotcontrol.parameters.dynamic.DynUtil.FRACTIONAL_CONTOUR_STEPPER_PATH;

public class CalculateController {

    public static void calculateContourPath(File file) throws WrongExtension, BoundsViolation, WrongInputData, IOException, ImpossibleToImplement {
        if (file != null) {
            List<GCode> gCodes = DataController.convertToGCode(file);
            calculateContourPath(gCodes);
        }
    }

    public static void calculateContourPath(List<GCode> gCodes) throws BoundsViolation, ImpossibleToImplement {
        CURRENT_CONTOUR_PATH = PathController.makePath(gCodes);
        FRACTIONAL_CONTOUR_STEPPER_PATH = SeparateContourController.divideAndCalculate(CURRENT_CONTOUR_PATH);
        CURRENT_CONTOUR_STEPPER_PATH = PathConverter.convertToSteppersPath(CURRENT_CONTOUR_PATH);
    }

    public static void calculateContourPathByList(List<String> gCodes) throws WrongInputData, BoundsViolation, ImpossibleToImplement {
        if (gCodes != null && !gCodes.isEmpty()) {
            calculateContourPath(DataController.convertToGCode(gCodes));
        }
    }
}

package com.robotcontrol.calc.stepperControl.controllers;

import com.robotcontrol.calc.contouringControl.entities.GCode.GCode;
import com.robotcontrol.calc.contouringControl.entities.GCode.MotionGCode;
import com.robotcontrol.calc.contouringControl.entities.path.ContourPath;
import com.robotcontrol.calc.stepperControl.entities.SteppersPath;
import com.robotcontrol.parameters.constant.ConstUtil;

import java.util.ArrayList;
import java.util.List;

import static com.robotcontrol.parameters.dynamic.DynUtil.FRACTIONAL_CONTOUR_PATH;

public class SeparateContourController {

    public static List<SteppersPath> divideAndCalculate(ContourPath contourPath){
        FRACTIONAL_CONTOUR_PATH = dividePath(contourPath);
        return convertPath(FRACTIONAL_CONTOUR_PATH);
    }

    private static List<ContourPath> dividePath(ContourPath contourPath){
        List<ContourPath> contourPaths = new ArrayList<>();
        ContourPath partionalPath = new ContourPath(new ArrayList<>());

        for (int i = 0; i < contourPath.getgCodeList().size(); i++) {
            GCode gCode = contourPath.getgCodeList().get(i);

            if (gCode instanceof MotionGCode) {
                double pastDistance = partionalPath.getFullDistance();
                double currentDistance = pastDistance + ((MotionGCode) gCode).getDistance();

                if (currentDistance > ConstUtil.MAX_SINGLE_LENGTH){
                    contourPaths.add(partionalPath);
                    partionalPath = new ContourPath(new ArrayList<>());
                    partionalPath.getgCodeList().add(gCode);
                    partionalPath.setFullDistance(((MotionGCode) gCode).getDistance());
                    partionalPath.setFullTime(gCode.getFinalTime());
                } else {
                    partionalPath.getgCodeList().add(gCode);
                    partionalPath.setFullTime(partionalPath.getFullTime() + gCode.getFinalTime() - gCode.getStartTime());
                    partionalPath.setFullDistance(partionalPath.getFullDistance() + ((MotionGCode) gCode).getDistance());
                }
            }
        }
        return contourPaths;
    }

    private static List<SteppersPath> convertPath(List<ContourPath> contourPaths){
        List<SteppersPath> steppersPaths = new ArrayList<>();
        for (ContourPath contourPath : contourPaths) {
            steppersPaths.add(PathConverter.convertToSteppersPath(contourPath));
        }
        return steppersPaths;
    }

}

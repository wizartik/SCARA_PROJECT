/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.robotcontrol.calc.stepperControl.controllers;

import com.robotcontrol.calc.contouringControl.entities.GCode.GCode;
import com.robotcontrol.calc.contouringControl.entities.GCode.MotionGCode;
import com.robotcontrol.calc.contouringControl.entities.path.ContourPath;
import com.robotcontrol.calc.stepperControl.entities.Remaining;
import com.robotcontrol.calc.stepperControl.entities.SteppersPath;
import com.robotcontrol.parameters.constant.PhysicalParameters;
import com.robotcontrol.parameters.dynamic.Motion;

import java.util.ArrayList;
import java.util.List;

import static com.robotcontrol.calc.stepperControl.controllers.StepsHandler.addSteps;

public class ContourPathHandler {

    public static SteppersPath makeStepperPath(ContourPath contourPath){
        List<List<Integer>> lists = new ArrayList<>(3);

        for (int i = 0; i < 3; i++) {
            lists.add(new ArrayList<Integer>());
        }

        for (int i = 0; i < contourPath.getgCodeList().size(); i++) {
            GCode gCode = contourPath.getgCodeList().get(i);
            if (gCode instanceof MotionGCode) {
                addPointsFromGCode((MotionGCode) gCode, lists, PhysicalParameters.REDUCTION_RATIO);
            }
        }

        return StepsHandler.makeStepperPath(lists);
    }

    private static void addPointsFromGCode(MotionGCode gCode, List<List<Integer>> lists, double[] reductionRatio){

        double step = Math.toRadians(PhysicalParameters.STEP) / Motion.MICROSTEPS;

        double previousAngle1 = gCode.getgCodePath().get(0).getAngles()[0];
        double angleReminder1 = 0;
        int timeRemainder1 = 0;
        long previousTime1 = 0;

        double previousAngle2 = gCode.getgCodePath().get(0).getAngles()[1];
        double angleReminder2 = 0;
        int timeRemainder2 = 0;
        long previousTime2 = 0;

        double previousAngle3 = gCode.getgCodePath().get(0).getAngles()[2];
        double angleReminder3 = 0;
        int timeRemainder3 = 0;
        long previousTime3 = 0;
        for (int i = 1; i < gCode.getgCodePath().size(); i++) {

            double currentAngle1 = gCode.getgCodePath().get(i).getAngles()[0];
            angleReminder1 += (currentAngle1 - previousAngle1) * Math.abs(reductionRatio[0]);
            previousAngle1 = currentAngle1;

            long currentTime1 = gCode.getgCodePath().get(i).getTime();
            timeRemainder1 += currentTime1 - previousTime1;
            previousTime1 = currentTime1;

            double currentAngle2 = gCode.getgCodePath().get(i).getAngles()[1];
            angleReminder2 += (currentAngle2 - previousAngle2) * Math.abs(reductionRatio[1]);
            previousAngle2 = currentAngle2;

            long currentTime2 = gCode.getgCodePath().get(i).getTime();
            timeRemainder2 += currentTime2 - previousTime2;
            previousTime2 = currentTime2;

            double currentAngle3 = gCode.getgCodePath().get(i).getAngles()[2];
            angleReminder3 += (currentAngle3 - previousAngle3) * Math.abs(reductionRatio[2]);
            previousAngle3 = currentAngle3;

            long currentTime3 = gCode.getgCodePath().get(i).getTime();
            timeRemainder3 += currentTime3 - previousTime3;
            previousTime3 = currentTime3;

            if (Math.abs(angleReminder1) > step) {
                Remaining remaining = addSteps(angleReminder1, step, timeRemainder1, lists.get(0));
                angleReminder1 = remaining.getAngleRemaining();
                timeRemainder1 = remaining.getTimeRemaining();
            }

            if (Math.abs(angleReminder2) > step) {
                Remaining remaining = addSteps(angleReminder2, step, timeRemainder2, lists.get(1));
                angleReminder2 = remaining.getAngleRemaining();
                timeRemainder2 = remaining.getTimeRemaining();
            }

            if (Math.abs(angleReminder3) > step) {
                Remaining remaining = addSteps(angleReminder3, step, timeRemainder3, lists.get(2));
                angleReminder3 = remaining.getAngleRemaining();
                timeRemainder3 = remaining.getTimeRemaining();
            }

        }
        if (Math.abs(angleReminder1) > 0.5 * step) {
            lists.get(0).add((int) (Math.signum(angleReminder1) * timeRemainder1));
        }
        if (Math.abs(angleReminder2) > 0.5 * step) {
            lists.get(1).add((int) (Math.signum(angleReminder2) * timeRemainder2));
        }
        if (Math.abs(angleReminder3) > 0.5 * step) {
            lists.get(2).add((int) (Math.signum(angleReminder3) * timeRemainder3));
        }
    }
}

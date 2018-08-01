package com.robotcontrol.calc.stepperControl.controllers;

import com.robotcontrol.calc.positionalControl.entities.PositionalPath;
import com.robotcontrol.calc.positionalControl.entities.SingleJointPath;
import com.robotcontrol.calc.positionalControl.entities.SingleJointPoint;
import com.robotcontrol.calc.stepperControl.entities.Remaining;
import com.robotcontrol.calc.stepperControl.entities.SteppersPath;
import com.robotcontrol.parameters.constant.PhysicalParameters;
import com.robotcontrol.parameters.dynamic.Motion;

import java.util.ArrayList;
import java.util.List;

import static com.robotcontrol.calc.stepperControl.controllers.StepsHandler.addSteps;

public class JointPathHandler {

    static SteppersPath makeStepperPath(PositionalPath positionalPath) {
        List<List<Integer>> lists = new ArrayList<>(positionalPath.getPath().length);

        for (int i = 0; i < positionalPath.getPath().length; i++) {
            lists.add(makeSingleStepperPath(positionalPath.getPath()[i],
                    PhysicalParameters.REDUCTION_RATIO[i]));
            ((ArrayList<Integer>) lists.get(i)).trimToSize();
        }

        return StepsHandler.makeStepperPath(lists);
    }

    static List<Integer> makeSingleStepperPath(SingleJointPath path, double reductionRatio) {

        List<SingleJointPoint> points = path.getJointPoints();

        double step = Math.toRadians(PhysicalParameters.STEP) / Motion.MICROSTEPS;

        List<Integer> timePath = new ArrayList<>((int) (path.getJointPoints().size() * 0.5));

        double previousAngle = points.get(0).getAngle();
        double angleReminder = 0;
        int timeRemainder = 0;
        long previousTime = 0;
        for (int i = 1; i < points.size(); i++) {
            double currentAngle = points.get(i).getAngle();
            angleReminder += (currentAngle - previousAngle) * Math.abs(reductionRatio);
            previousAngle = currentAngle;

            long currentTime = points.get(i).getTime();
            timeRemainder += currentTime - previousTime;
            previousTime = currentTime;

            if (Math.abs(angleReminder) > step) {
                Remaining remaining = addSteps(angleReminder, step, timeRemainder, timePath);
                angleReminder = remaining.getAngleRemaining();
                timeRemainder = remaining.getTimeRemaining();
            }
        }
        if (Math.abs(angleReminder) > 0.5 * step) {
            timePath.add((int) (Math.signum(angleReminder) * timeRemainder));
        }

        ((ArrayList<Integer>) timePath).trimToSize();
        return timePath;
    }
}

package com.robotcontrol.tests;

import com.robotcontrol.calc.positionalControl.controllers.PositionalController;
import com.robotcontrol.calc.positionalControl.entities.PositionalPath;
import com.robotcontrol.calc.stepperControl.controllers.PathConverter;
import com.robotcontrol.calc.stepperControl.entities.SteppersPath;
import com.robotcontrol.calc.stepperControl.entities.SteppersPoint;
import com.robotcontrol.exc.BoundsViolation;

import java.util.List;

import static com.robotcontrol.parameters.dynamic.Position.CURRENT_POSITION;

public class MainTest {
    public static void main(String[] args) throws BoundsViolation {
        PositionalPath positionalPath = PositionalController.moveToPointPos(CURRENT_POSITION, new double[]{10,10,12});
        SteppersPath steppersPath = PathConverter.convertToSteppersPath(positionalPath);
        List<SteppersPoint> steppersPoints = steppersPath.getSteppersPoints();

        long total = 0;
        for (int i = 0; i < steppersPoints.size(); i++) {
            total += steppersPoints.get(i).getStepsDelays()[2];
            System.out.println(steppersPoints.get(i).getStepsDelays()[2]);
        }

        System.out.println(total);
    }
}

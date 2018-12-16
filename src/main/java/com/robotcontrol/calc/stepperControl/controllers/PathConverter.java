/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.robotcontrol.calc.stepperControl.controllers;

import com.robotcontrol.calc.contouringControl.entities.path.ContourPath;
import com.robotcontrol.calc.positionalControl.entities.PositionalPath;
import com.robotcontrol.calc.stepperControl.entities.SteppersPath;
import com.robotcontrol.parameters.dynamic.DynUtil;
import com.robotcontrol.util.progress.CurrentAction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PathConverter {

    public static SteppersPath convertToSteppersPath(ContourPath contourPath){
        if (DynUtil.progress != null){
            DynUtil.progress.setCurrentAction(CurrentAction.CONVERTING);
        }

        SteppersPath steppersPath = ContourPathHandler.makeStepperPath(contourPath);
        trimList(steppersPath.getSteppersPoints());

        long q = 0;
        long w = 0;
        for (int i = 0; i < steppersPath.getSteppersPoints().size(); i++) {
            q += Math.abs(steppersPath.getSteppersPoints().get(i).getStepsDelays()[0]);
            w += Math.abs(steppersPath.getSteppersPoints().get(i).getStepsDelays()[1]);
            System.out.println(Arrays.toString(steppersPath.getSteppersPoints().get(i).getStepsDelays()));
        }

        System.out.println("qwe " + contourPath.getFullTime());
        System.out.println(q);
        System.out.println(w);


        return steppersPath;
    }

    public static SteppersPath convertToSteppersPath(PositionalPath positionalPath){
        SteppersPath steppersPath = JointPathHandler.makeStepperPath(positionalPath);
        trimList(steppersPath.getSteppersPoints());
        return steppersPath;
    }

    private static void trimList(List list){
        if (list instanceof ArrayList){
            ((ArrayList) list).trimToSize();
        }
    }

}

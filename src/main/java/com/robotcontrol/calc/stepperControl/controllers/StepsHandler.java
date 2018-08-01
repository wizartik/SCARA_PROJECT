/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.robotcontrol.calc.stepperControl.controllers;

import com.robotcontrol.calc.stepperControl.entities.Remaining;
import com.robotcontrol.calc.stepperControl.entities.SteppersPath;
import com.robotcontrol.calc.stepperControl.entities.SteppersPoint;
import com.robotcontrol.util.Utility;

import java.util.ArrayList;
import java.util.List;

class StepsHandler {

    static SteppersPath makeStepperPath(List<List<Integer>> lists){
        int largestSize = (int) Utility.findLargest(lists.get(0).size(), lists.get(1).size(), lists.get(2).size());

        List<SteppersPoint> steppersPoints = new ArrayList<>(largestSize);
        for (int i = 0; i < largestSize; i++) {
            int[] delays = new int[lists.size()];

            for (int j = 0; j < delays.length; j++) {
                if (lists.get(j).size() > i) {
                    delays[j] = lists.get(j).get(i);
                } else {
                    delays[j] = 0;
                }
            }
            steppersPoints.add(new SteppersPoint(delays));
        }

        return new SteppersPath(steppersPoints);
    }



    static Remaining addSteps(double angle, double step, int time,
                                      List<Integer> path) {

        double pointsNumber = (angle / step);
        int counter = (int) pointsNumber;
        double angleRemaining = angle - counter * step;

        int realTime = (int) (time - time * angleRemaining / angle);
        if (angle > 0) {
            for (int i = 0; i < counter; i++) {
                path.add(realTime / counter);
            }
        } else {
            counter = Math.abs(counter);
            for (int i = 0; i < counter; i++) {
                path.add(-realTime / counter);
            }
        }

        double timeRemaining = time - realTime;
        return new Remaining((int) timeRemaining, angleRemaining);
    }
}

/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.robotcontrol.comm;

import com.robotcontrol.calc.stepperControl.entities.SteppersPoint;

import static com.robotcontrol.parameters.constant.Communication.*;

public class DataFormer {
    public String makeHeaderMovement(int stepsNumber) {
        return MOVEMENT_HEADER + stepsNumber;
    }

    public String makeFooterMovement() {
        return MOVEMENT_FOOTER;
    }


    public String makeDataLine(SteppersPoint steppersPoint) {
        StringBuilder dataLine = new StringBuilder();
        int[] stepsDelays = steppersPoint.getStepsDelays();

        for (int i = 0; i < stepsDelays.length; i++) {
            dataLine.append(stepsDelays[i]);
            if (i != stepsDelays.length - 1) {
                dataLine.append(SEPARATOR);
            }
        }
        return dataLine.toString();
    }
}

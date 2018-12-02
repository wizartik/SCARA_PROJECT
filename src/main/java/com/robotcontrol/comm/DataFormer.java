/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.robotcontrol.comm;

import com.robotcontrol.calc.stepperControl.entities.SteppersPath;
import com.robotcontrol.calc.stepperControl.entities.SteppersPoint;
import com.robotcontrol.parameters.constant.Communication;

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
        dataLine.append('\n');
        return dataLine.toString();
    }

    public String makeDataForOneStepper(SteppersPath steppersPath, int stepper) {
        StringBuilder data = new StringBuilder(steppersPath.getSteppersPoints().size());

        data.append(MOVEMENT_HEADER).append(stepper);
        appendDataNumberInfo(data, steppersPath, stepper);
//        data.append(SEPARATOR);
        appendData(data, steppersPath, stepper);
//        data.append(MOVEMENT_FOOTER);

        data.append('\n');

        return data.toString();
    }

    private void appendDataNumberInfo(StringBuilder data, SteppersPath steppersPath, int stepper) {
        data.append(Communication.DATA_NUMBER_START);
        data.append(steppersPath.getSteps()[stepper]);
        data.append(Communication.DATA_NUMBER_END);
    }

    private void appendData(StringBuilder data, SteppersPath steppersPath, int stepper) {

        boolean staticPart = false;
        int staticPartNumber = 1;
        int value = 0;

        for (int i = 0; i < steppersPath.getSteppersPoints().size(); i++) {
            int delay = steppersPath.getSteppersPoints().get(i).getStepsDelays()[stepper];
            if (delay == 0) {
                break;
            }

            if (i + 1 < steppersPath.getSteppersPoints().size()) {
                int nextDelay = steppersPath.getSteppersPoints().get(i + 1).getStepsDelays()[stepper];
                if (almostEqual(delay, nextDelay)) {
                    staticPart = true;
                    staticPartNumber++;
                    value = delay;
                } else if (staticPart) {
                    data.append(DATA_STATIC_SIGN);
                    data.append(staticPartNumber);
                    data.append(DATA_STATIC_VALUE);
                    data.append(value);
                    data.append(DATA_STATIC_END);
                    data.append(SEPARATOR);

                    staticPart = false;
                    value = 0;
                    staticPartNumber = 1;
                } else {
                    data.append(delay);
                    data.append(SEPARATOR);
                }
            } else {
                data.append(delay);
                data.append(SEPARATOR);
            }
        }
    }

    private boolean almostEqual(int firstDelay, int secondDelay) {
        return ((firstDelay + 1) == secondDelay) || ((firstDelay - 1) == secondDelay) || (firstDelay == secondDelay);
    }
}

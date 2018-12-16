package com.robotcontrol.comm.wifi;

import com.robotcontrol.movement.ParametersController;
import com.robotcontrol.parameters.dynamic.Motion;
import com.robotcontrol.util.CommUtil;

import static com.robotcontrol.parameters.constant.Communication.*;

public class DataReceiverResolver implements DataReceiverListener {

    @Override
    public void onDataReceiveEvent(String data) {
//        new Thread(() -> resolveData(data)).start();
        resolveData(data);
    }

    private void resolveData(String data) {
        System.out.println("received: " + data);
        switch (data) {
            case MESSAGE_MOVEMENT_FINISHED:
                ParametersController.finishedMovement();
                break;
            case MESSAGE_CONNECTED:
                CommUtil.setStatusConnected();
                break;
            case MESSAGE_CALIBRATION_FINISHED:
                ParametersController.finishedCalibration();
                break;
            case MESSAGE_CRASH1:
                ParametersController.motorCrash(1);
                break;
            case MESSAGE_CRASH2:
                ParametersController.motorCrash(2);
                break;
            case MESSAGE_CRASH3:
                ParametersController.motorCrash(3);
                break;
            default:
                if (data.startsWith(String.valueOf(UPDATE_FIRST))) {
                    parseUpdateCoords(data);
                }
                break;
        }
    }


    private void parseUpdateCoords(String data) {
        int qIndex = 0;
        int wIndex = data.indexOf(UPDATE_SECOND);
        int eIndex = data.indexOf(UPDATE_THIRD);

        int first = Integer.valueOf(data.substring(qIndex + 1, wIndex));
        int second = Integer.valueOf(data.substring(wIndex + 1, eIndex));
        int third = Integer.valueOf(data.substring(eIndex + 1));

        if (Motion.MOVING) {
            ParametersController.updateCurrentCoords(first, second, third);
        }
    }
}

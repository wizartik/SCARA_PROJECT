package com.robotcontrol.comm.wifi;

import com.robotcontrol.movement.ParametersController;
import com.robotcontrol.util.CommUtil;

import static com.robotcontrol.parameters.constant.Communication.*;

public class DataReceiverResolver implements DataReceiverListener {

    @Override
    public void onDataReceiveEvent(String data) {
        new Thread(() -> resolveData(data)).start();
    }

    private void resolveData(String data) {
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
        }
    }
}

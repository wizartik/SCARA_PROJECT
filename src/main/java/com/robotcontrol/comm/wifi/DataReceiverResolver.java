package com.robotcontrol.comm.wifi;

import com.robotcontrol.movement.ParametersController;
import com.robotcontrol.util.CommUtil;

public class DataReceiverResolver implements DataReceiverListener {

    @Override
    public void onDataReceiveEvent(String data) {
        new Thread(() -> resolveData(data)).start();
    }

    private void resolveData(String data) {
        System.out.println(data);
        switch (data) {
            case "finished":
                ParametersController.finishedMovement();
                break;
            case "connected":
                CommUtil.setStatusConnected();
                break;
            case "finished calibration":
                ParametersController.finishedCalibration();
                break;
        }
    }
}

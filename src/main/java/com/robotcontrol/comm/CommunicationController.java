package com.robotcontrol.comm;

import com.robotcontrol.calc.stepperControl.entities.SteppersPath;
import com.robotcontrol.comm.wifi.WifiController;
import com.robotcontrol.exc.NoConnection;
import com.robotcontrol.util.CommUtil;

import java.io.IOException;

import static com.robotcontrol.parameters.dynamic.Communication.WIFI_CONTROLLER;

public class CommunicationController {
    public static void createWiFiConnection() {
        if (WIFI_CONTROLLER != null) {
            return;
        }
        new Thread(() -> {
            try {
                WIFI_CONTROLLER = new WifiController();
                if (CommUtil.isConnected()) {
                    CommUtil.setStatusConnected();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void closeWiFiConnection() {
        if (WIFI_CONTROLLER == null) {
            return;
        }
        try {
            WIFI_CONTROLLER.closeConnection();
            WIFI_CONTROLLER = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendData(SteppersPath steppersPath, int stepper) throws NoConnection, IOException {
        CommUtil.checkConnection();
        WIFI_CONTROLLER.sendData(steppersPath, stepper);
    }

    public static void sendString(String string) throws NoConnection, IOException {
        CommUtil.checkConnection();
        WIFI_CONTROLLER.sendString(string);
    }


}

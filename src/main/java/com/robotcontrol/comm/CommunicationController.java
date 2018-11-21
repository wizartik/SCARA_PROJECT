package com.robotcontrol.comm;

import com.robotcontrol.comm.wifi.WifiController;
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
}

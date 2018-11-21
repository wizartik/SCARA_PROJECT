package com.robotcontrol.comm;

import com.robotcontrol.comm.wifi.WifiController;

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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}

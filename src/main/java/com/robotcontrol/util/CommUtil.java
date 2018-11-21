package com.robotcontrol.util;

import com.robotcontrol.exc.NoConnection;

import static com.robotcontrol.parameters.dynamic.Communication.WIFI_CONTROLLER;

public class CommUtil {
    public static void checkConnection() throws NoConnection {
        if (WIFI_CONTROLLER == null || !WIFI_CONTROLLER.isConnected()){
            throw new NoConnection("ESP32 is not connected");
        }
    }
}

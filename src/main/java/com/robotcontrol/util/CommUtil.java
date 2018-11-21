package com.robotcontrol.util;

import com.robotcontrol.exc.NoConnection;
import com.robotcontrol.parameters.dynamic.Communication;

import static com.robotcontrol.parameters.dynamic.Communication.WIFI_CONTROLLER;

public class CommUtil {
    public static void checkConnection() throws NoConnection {
        if (WIFI_CONTROLLER == null || !WIFI_CONTROLLER.isConnected()){
            throw new NoConnection("ESP32 is not connected");
        }
    }

    public static boolean isConnected(){
        return WIFI_CONTROLLER != null && WIFI_CONTROLLER.isConnected();
    }

    public static void setStatusConnected(){
        Communication.CONNECTION_STATUS.set("connected");
    }

    public static void setStatusDisconnected(){
        Communication.CONNECTION_STATUS.set("disconnected");
    }
}

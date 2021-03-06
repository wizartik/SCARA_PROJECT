/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.robotcontrol.parameters.dynamic;

import com.robotcontrol.comm.wifi.WifiController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import jssc.SerialPort;

public class Communication {
    public static SerialPort SERIAL_PORT = null;
    public static String PORT_NAME = null;

    public static WifiController WIFI_CONTROLLER;

    public static StringProperty CONNECTION_STATUS = new SimpleStringProperty("disconnected");

}

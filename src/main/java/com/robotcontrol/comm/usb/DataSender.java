/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.robotcontrol.comm.usb;

import jssc.SerialPortException;

import static com.robotcontrol.parameters.dynamic.Communication.SERIAL_PORT;

public class DataSender {
    boolean sendLine(String line) throws SerialPortException {
        if (SERIAL_PORT!= null && SERIAL_PORT.isOpened()) {
            return SERIAL_PORT.writeString(line);
        } else {
            return false;
        }
    }
}

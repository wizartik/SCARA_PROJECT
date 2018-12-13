/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.robotcontrol.comm.usb;

import com.robotcontrol.calc.stepperControl.entities.SteppersPath;
import com.robotcontrol.comm.DataFormer;
import jssc.SerialPortException;

import static com.robotcontrol.parameters.dynamic.Communication.PORT_NAME;

public class PortController {

    public static void setPortName(String name) {
        PORT_NAME = name;
    }

    public static void sendSteppersPath(SteppersPath steppersPath) throws SerialPortException {
        SerialPortWorker port = new SerialPortWorker();
        if (PORT_NAME == null) {
            return;
        }
        if (!port.isPortOpened()) {
            port.openConnection(PORT_NAME);
        }

        DataFormer dataFormer = new DataFormer();

        port.sendLine(dataFormer.makeHeaderMovement(steppersPath.getSteppersPoints().size()));
        for (int i = 0; i < steppersPath.getSteppersPoints().size(); i++) {
            port.sendLine(dataFormer.makeDataLine(steppersPath.getSteppersPoints().get(i)));
        }
        port.sendLine(dataFormer.makeFooterMovement());
    }
}

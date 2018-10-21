/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.robotcontrol.comm.serialPort;

import com.robotcontrol.parameters.constant.Communication;
import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;

import static com.robotcontrol.parameters.dynamic.Communication.SERIAL_PORT;

public class SerialPortWorker {
    public String[] getPortsNames() {
        return SerialPortList.getPortNames();
    }

    public boolean openConnection(String portName) throws SerialPortException {
        if (SERIAL_PORT != null && SERIAL_PORT.isOpened()) {
            return false;
        }
        SERIAL_PORT = new SerialPort(portName);

        boolean success = SERIAL_PORT.setParams(Communication.BAUDRATE,
                SerialPort.DATABITS_8,
                SerialPort.STOPBITS_1,
                SerialPort.PARITY_NONE);

        success &= SERIAL_PORT.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN |
                SerialPort.FLOWCONTROL_RTSCTS_OUT);
        SERIAL_PORT.addEventListener(new DataReceiver(), SerialPort.MASK_RXCHAR);

        return success;
    }

    public boolean closeConnection() throws SerialPortException {
        boolean success = true;
        if (SERIAL_PORT != null && SERIAL_PORT.isOpened()) {
            success = SERIAL_PORT.closePort();
        }

        if (!(SERIAL_PORT != null && SERIAL_PORT.isOpened())) {
            SERIAL_PORT = null;
        }
        return success;
    }

    boolean sendLine(String line) throws SerialPortException {
        DataSender dataSender = new DataSender();
        return dataSender.sendLine(line);
    }

    boolean isPortOpened(){
        return SERIAL_PORT != null && SERIAL_PORT.isOpened();
    }
}

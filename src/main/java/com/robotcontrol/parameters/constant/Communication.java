/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.robotcontrol.parameters.constant;

import jssc.SerialPort;

public class Communication {
    public final static String MOVEMENT_HEADER = "movement";

    public final static String MOVEMENT_FOOTER = "end";

    public final static Character SEPARATOR = '|';

    public final static Character END_LINE_SYMBOL = ';';

    public final static int BAUDRATE = SerialPort.BAUDRATE_115200;

    public final static int PORT = 14888;
}

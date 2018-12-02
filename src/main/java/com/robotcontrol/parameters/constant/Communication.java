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
    public final static String MOVEMENT_HEADER = "mo";

    public final static String MOVEMENT_FOOTER = "end";

    public final static Character SEPARATOR = '|';

    public final static Character END_LINE_SYMBOL = ';';

    public final static Character DATA_NUMBER_START = '(';
    public final static Character DATA_NUMBER_END = ')';

    public final static Character DATA_STATIC_SIGN = 'x';

    public final static Character DATA_STATIC_VALUE = 'v';

    public final static Character DATA_STATIC_END = 'e';

    public final static String MESSAGE_MOVEMENT_FINISHED = "finished";

    public final static String MESSAGE_CONNECTED = "connected";

    public final static String MESSAGE_CALIBRATION = "ca";

    public final static String MESSAGE_CALIBRATION_FINISHED = "finished calibration";

    public final static String MESSAGE_CRASH1 = "pizdec1";
    public final static String MESSAGE_CRASH2 = "pizdec2";
    public final static String MESSAGE_CRASH3 = "pizdec3";

    public final static int BAUDRATE = SerialPort.BAUDRATE_115200;

    public final static int PORT = 14888;
}

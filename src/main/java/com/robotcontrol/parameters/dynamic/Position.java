package com.robotcontrol.parameters.dynamic;

import com.robotcontrol.util.SettingsUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Arrays;

public class Position {
    /**
     * Coordinates of the home position.
     */
    public static double[] HOME_COORDS = {10, 10, 10};

    public static double[] DESIRED_POSITION = HOME_COORDS;

    public static StringProperty HOME_COORDS_STRING = new SimpleStringProperty(SettingsUtil.makeHomeCoordsString());

    /**
     * In degrees.
     */
    public static double[] ANGLES_AFTER_CALIBRATION = {0, -100, 0};

    /**
     * Current position of the working body.
     */
    public static double[] CURRENT_POSITION = Arrays.copyOf(HOME_COORDS, HOME_COORDS.length);

}

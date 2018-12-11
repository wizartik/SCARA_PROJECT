package com.robotcontrol.parameters.dynamic;

import com.robotcontrol.calc.DHParameters.SCARADH;
import com.robotcontrol.movement.ParametersController;
import com.robotcontrol.util.SettingsUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Position {
    /**
     * Coordinates of the home position.
     */
    public static double[] HOME_COORDS = {15, 15, 10};


    public static StringProperty HOME_COORDS_STRING = new SimpleStringProperty(SettingsUtil.makeHomeCoordsString());

    /**
     * In degrees.
     */
    public static double[] ANGLES_AFTER_CALIBRATION = {0, -100, 0};

    /**
     * Current position of the working body.
     */
    public static double[] CURRENT_POSITION = SCARADH.forwardKinematics(new double[]{0,0,10});

    public static double[] DESIRED_POSITION = CURRENT_POSITION;

    public static double[] CURRENT_ANGLES = SCARADH.inverseKinematics(CURRENT_POSITION);

    public static double[] DESIRED_ANGLES = CURRENT_ANGLES;

    public static StringProperty CURRENT_POSITION_STRING = new SimpleStringProperty(ParametersController.getCurrentCoordsString());

}

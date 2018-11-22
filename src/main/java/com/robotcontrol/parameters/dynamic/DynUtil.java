package com.robotcontrol.parameters.dynamic;

import com.robotcontrol.calc.contouringControl.entities.path.ContourPath;
import com.robotcontrol.calc.stepperControl.entities.SteppersPath;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class DynUtil {

    public static StringProperty CURRENT_MODE_STRING = new SimpleStringProperty("current: selective");

    public static ContourPath CURRENT_CONTOUR_PATH;

    public static SteppersPath CURRENT_CONTOUR_STEPPER_PATH;

}

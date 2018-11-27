package com.robotcontrol.parameters.dynamic;

import com.robotcontrol.calc.contouringControl.entities.path.ContourPath;
import com.robotcontrol.calc.stepperControl.entities.SteppersPath;
import com.robotcontrol.util.progress.Progress;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class DynUtil {

    public static StringProperty CURRENT_MODE_STRING = new SimpleStringProperty("current: selective");

    public static ContourPath CURRENT_CONTOUR_PATH;

    public static SteppersPath CURRENT_CONTOUR_STEPPER_PATH;

    public static Timeline timer;

    public static Progress progress;

    public static StringProperty CURRENT_ACTION = new SimpleStringProperty();

    public static String CURRENT_ACTION_STRING;

    public static Timeline progressTimeline;

    public static Timeline drawingTimeline;

    public static Timeline exceptionTimeline;

    public static Exception producedException;
}

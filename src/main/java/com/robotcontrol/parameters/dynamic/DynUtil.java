package com.robotcontrol.parameters.dynamic;

import com.robotcontrol.calc.contouringControl.entities.path.ContourPath;
import com.robotcontrol.calc.stepperControl.entities.SteppersPath;
import com.robotcontrol.util.progress.Progress;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.List;

public class DynUtil {

    public static StringProperty CURRENT_MODE_STRING = new SimpleStringProperty("current: selective");

    public static ContourPath CURRENT_CONTOUR_PATH;

    public static List<ContourPath> FRACTIONAL_CONTOUR_PATH;

    public static SteppersPath CURRENT_CONTOUR_STEPPER_PATH;

    public static List<SteppersPath> FRACTIONAL_CONTOUR_STEPPER_PATH;

    public static volatile Boolean FORBID_CALCULATION;

    public static Timeline timer;

    public static Progress progress;

    public static StringProperty CURRENT_ACTION = new SimpleStringProperty();

    public static StringProperty CURRENT_PROGRESS = new SimpleStringProperty();

    public static String CURRENT_ACTION_STRING;

    public static Timeline progressTimeline;

    public static Timeline drawingTimeline;

    public static Timeline exceptionTimeline;

    public static Exception producedException;
}

package com.robotcontrol.util;

import com.robotcontrol.calc.checks.PositionalChecker;
import com.robotcontrol.exc.BoundsViolation;
import com.robotcontrol.parameters.constant.PhysicalParameters;
import com.robotcontrol.parameters.constant.Safety;
import com.robotcontrol.parameters.dynamic.DynSafety;
import com.robotcontrol.parameters.dynamic.DynUtil;
import com.robotcontrol.parameters.dynamic.Links;
import com.robotcontrol.parameters.dynamic.Position;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.Arrays;

public class SettingsUtil {

    public static void switchToSelective() {
        Links.FIRST_LINK_LENGTH = PhysicalParameters.FIRST_LINK_SELECTIVE;
        Links.SECOND_LINK_LENGTH = PhysicalParameters.SECOND_LINK_SELECTIVE;
        DynSafety.MAX_RADIUS = Safety.MAX_RADIUS_SELECTIVE;
        DynSafety.MIN_RADIUS = Safety.MIN_RADIUS_SELECTIVE;
        DynUtil.CURRENT_MODE_STRING.set("current: selective");
    }

    public static void switchToDrawing() {
        Links.FIRST_LINK_LENGTH = PhysicalParameters.FIRST_LINK_DRAWING;
        Links.SECOND_LINK_LENGTH = PhysicalParameters.SECOND_LINK_DRAWING;
        DynSafety.MAX_RADIUS = Safety.MAX_RADIUS_DRAWING;
        DynSafety.MIN_RADIUS = Safety.MIN_RADIUS_DRAWING;
        DynUtil.CURRENT_MODE_STRING.set("current: drawing");
    }

    public static void changeHomeCoords(double[] homeCoords) throws BoundsViolation {
        if (homeCoords != null) {
            PositionalChecker.checkCoords(homeCoords);
            Position.HOME_COORDS = homeCoords;
            Position.HOME_COORDS_STRING.set(makeHomeCoordsString());
        }
    }

    public static String makeHomeCoordsString() {
        return "Current: " + Arrays.toString(Position.HOME_COORDS);
    }

    private static void checkConnection() {
        if (CommUtil.isConnected()) {
            CommUtil.setStatusConnected();
        } else {
            CommUtil.setStatusDisconnected();
        }
    }

    public static void setTimerToCheckConnection() {
        if (DynUtil.timer == null) {
            DynUtil.timer = new Timeline(new KeyFrame(
                    Duration.millis(500),
                    ae -> checkConnection()));
            DynUtil.timer.setCycleCount(Animation.INDEFINITE);
        }
        DynUtil.timer.play();
    }

    public static void stopTimer() {
        if (DynUtil.timer != null) {
            DynUtil.timer.stop();
        }
    }
}

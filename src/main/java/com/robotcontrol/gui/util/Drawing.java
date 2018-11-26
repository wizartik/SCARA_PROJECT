package com.robotcontrol.gui.util;

import com.robotcontrol.calc.contouringControl.entities.GCode.AngularGCode;
import com.robotcontrol.calc.contouringControl.entities.GCode.GCode;
import com.robotcontrol.calc.contouringControl.entities.GCode.GCodeType;
import com.robotcontrol.calc.contouringControl.entities.GCode.MotionGCode;
import com.robotcontrol.parameters.dynamic.DynSafety;
import com.robotcontrol.parameters.dynamic.DynUtil;
import com.robotcontrol.util.progress.CurrentAction;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.*;

import java.util.List;

public class Drawing {
    public static Path createPath(double xCenter, double yCenter, double multiplier, List<GCode> gCodes) {
        Path path = new Path();

        double xStart = xCenter + multiplier * gCodes.get(0).getStartPosition()[0];
        double yStart = yCenter - multiplier * gCodes.get(0).getStartPosition()[1];

        path.getElements().add(new MoveTo(xStart, yStart));

        for (int i = 0; i < gCodes.size(); i++) {
            if (DynUtil.progress != null){
                DynUtil.progress.setProgressOfDrawing(i, gCodes.size());
            }

            GCode gCode = gCodes.get(i);
            if (gCode instanceof MotionGCode) {
                double x = xCenter + multiplier * ((MotionGCode) gCode).getFinalPosition()[0];
                double y = yCenter - multiplier * ((MotionGCode) gCode).getFinalPosition()[1];

                if (gCode instanceof AngularGCode) {
                    double radius = multiplier * ((AngularGCode) gCode).getRadius();
                    ArcTo arcTo = new ArcTo();
                    arcTo.setRadiusX(radius);
                    arcTo.setRadiusY(radius);
                    arcTo.setX(x);
                    arcTo.setY(y);
                    arcTo.setSweepFlag(gCode.getGCodeType() == GCodeType.G02);
                    path.getElements().add(arcTo);
                } else {
                    LineTo lineTo = new LineTo(x, y);
                    path.getElements().add(lineTo);
                }
            }
        }
        return path;
    }

    public static void addPathToPane(Pane pane, List<GCode> gCodes){
        if (DynUtil.progress != null){
            DynUtil.progress.setCurrentAction(CurrentAction.DRAWING);
        }

        pane.getChildren().clear();
        double xCenter = pane.getWidth() / 2;
        double yCenter = pane.getHeight() / 2;

        double multiplier = calculateMultiplier(pane);

        addSafetyLinesToPane(pane);
        pane.getChildren().add(createPath(xCenter, yCenter, multiplier, gCodes));
    }

    private static void addSafetyLinesToPane(Pane pane){
        double xCenter = pane.getWidth() / 2;
        double yCenter = pane.getHeight() / 2;

        double multiplier = calculateMultiplier(pane);

        Circle minRadius = getCircle(xCenter, yCenter, DynSafety.MIN_RADIUS * multiplier);
        Circle maxRadius = getCircle(xCenter, yCenter, DynSafety.MAX_RADIUS * multiplier);

        pane.getChildren().addAll(minRadius, maxRadius);
    }

    private static Circle getCircle(double xCenter, double yCenter, double radius){
        Circle circle = new Circle(xCenter, yCenter, radius);
        circle.getStrokeDashArray().addAll(15.0, 10.0);
        circle.setFill(Paint.valueOf("TRANSPARENT"));
        circle.setStroke(Paint.valueOf("BLACK"));
        circle.setStrokeWidth(0.5);
        return circle;
    }

    private static double calculateMultiplier(Pane pane){
        return pane.getWidth() / DynSafety.MAX_RADIUS / 2;
    }
}

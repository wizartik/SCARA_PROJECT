/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.robotcontrol.calc.positionalControl.controllers;

import com.robotcontrol.calc.positionalControl.entities.PositionalPath;
import com.robotcontrol.calc.positionalControl.entities.SingleJointPath;
import com.robotcontrol.calc.positionalControl.entities.SingleJointPoint;
import com.robotcontrol.parameters.constant.Motion;
import com.robotcontrol.util.math.Converter;
import com.robotcontrol.util.math.Physics;

import java.util.ArrayList;
import java.util.List;

import static com.robotcontrol.parameters.constant.Motion.TIME_GAP;

class MotionHandler {

    static PositionalPath makePath(double[] startAngles, double[] finalAngles,
                                   double angVelocity, double angAcceleration) {
        SingleJointPath[] singleJointPaths = new SingleJointPath[startAngles.length];

        for (int i = 0; i < singleJointPaths.length; i++) {
            singleJointPaths[i] = makeSingleJointPath(startAngles[i],
                    finalAngles[i], angVelocity, angAcceleration);
            System.out.println(singleJointPaths[i].getJointPoints().size());
        }

        return new PositionalPath(singleJointPaths);
    }

    private static SingleJointPath makeSingleJointPath(double startAngle,
                                                       double finalAngle, double angVelocity, double angAcceleration) {

        double fullLength = Math.abs(finalAngle - startAngle);
        double accLength = Physics.accLength(0, angVelocity, angAcceleration);

        SingleJointPath singleJointPath;

        if (accLength * 2 < fullLength) {
            singleJointPath = makeFullPath(startAngle, finalAngle, angVelocity, angAcceleration);
        } else {
            singleJointPath = makeIncompletePath(startAngle, finalAngle);
        }

        singleJointPath.setTime(singleJointPath.getJointPoints()
                .get(singleJointPath.getJointPoints().size() - 1).getTime());

        return singleJointPath;
    }

    private static SingleJointPath makeFullPath(double startAngle, double
            finalAngle, double angVelocity, double angAcceleration) {

        boolean decrease = startAngle > finalAngle;

        double fullLength = Math.abs(finalAngle - startAngle);
        double accLength = Physics.accLength(0, angVelocity, angAcceleration);

        int pointsNumber = (int) Physics.pointsNumber(fullLength, angVelocity);

        SingleJointPath path = new SingleJointPath(new ArrayList<SingleJointPoint>(pointsNumber));

        //first dynamic part
        path.getJointPoints().addAll(makeDynamicPath(startAngle, 0,
                angVelocity, angAcceleration, 0, decrease));

        //static part
        int lastIndex = path.getJointPoints().size() - 1;

        long startTime = path.getJointPoints().get(lastIndex).getTime();
        double startPathAngle = path.getJointPoints().get(lastIndex).getAngle();

        double finalPathAngle;

        if (startAngle > finalAngle) {
            finalPathAngle = finalAngle + accLength;
        } else {
            finalPathAngle = finalAngle - accLength;
        }

        path.getJointPoints().addAll(makeStaticPath(startPathAngle, finalPathAngle,
                angVelocity, startTime));

        //second dynamic part
        lastIndex = path.getJointPoints().size() - 1;

        startTime = path.getJointPoints().get(lastIndex).getTime();
        startPathAngle = path.getJointPoints().get(lastIndex).getAngle();

        path.getJointPoints().addAll(makeDynamicPath(startPathAngle,
                angVelocity, 0, angAcceleration, startTime, decrease));

        return path;
    }

    private static SingleJointPath makeIncompletePath(double startAngle,
                                                      double finalAngle) {

        boolean decrease = startAngle > finalAngle;

        double angAcceleration = Motion.MAX_ANG_ACCELERATION;

        double distance = Math.abs(finalAngle - startAngle);

        double time = Math.sqrt(distance / angAcceleration);
        double maxVelocity = time * angAcceleration;

        SingleJointPath path = new SingleJointPath(new ArrayList<SingleJointPoint>(100));

        path.getJointPoints().addAll(makeDynamicPath(startAngle, 0,
                maxVelocity, angAcceleration, 0, decrease));

        int lastIndex = path.getJointPoints().size() - 1;

        long startTime = path.getJointPoints().get(lastIndex).getTime();

        double startPathAngle = path.getJointPoints().get(lastIndex).getAngle();

        path.getJointPoints().addAll(makeDynamicPath(startPathAngle,
                maxVelocity, 0, angAcceleration, startTime, decrease));

        return path;
    }

    private static List<SingleJointPoint> makeDynamicPath(double startAngle,
                                                          double startAngVel,
                                                          double finalAngVel,
                                                          double angAcceleration,
                                                          long startTime,
                                                          boolean decrease) {
        if (startAngVel > finalAngVel) {
            angAcceleration = -angAcceleration;
        }

        double fullTime = Physics.accTimeInUs(startAngVel, finalAngVel, angAcceleration);
        double pointsNumber = (fullTime / TIME_GAP);
        int counter = (int) pointsNumber;
        double remainder = pointsNumber - counter;


        List<SingleJointPoint> result = new ArrayList<>(counter + 1);

        long t = 0;
        for (int i = 0; i <= counter; i++) {

            long currentTime = startTime + t;

            double currentVelocity = startAngVel + angAcceleration * Converter.toSec(t) / 2;

            if (decrease) {
                currentVelocity = - currentVelocity;
            }

            double currentAngle = startAngle + currentVelocity * Converter.toSec(t);

            result.add(new SingleJointPoint(currentAngle, currentTime));
            //to calculate last point which has different time gap
            if (i == counter - 1) {
                t += TIME_GAP * (remainder + 1);
            } else {
                t += TIME_GAP;
            }
        }
        return result;
    }

    private static List<SingleJointPoint> makeStaticPath(double startAngle,
                                                         double finalAngle,
                                                         double angStatVelocity,
                                                         long startTime) {
        if (startAngle > finalAngle) {
            angStatVelocity = -angStatVelocity;
        }

        double pathLength = Math.abs(finalAngle - startAngle);
        double pointsNumber = Physics.pointsNumber(pathLength, angStatVelocity);

        int counter = (int) pointsNumber;

        List<SingleJointPoint> result = new ArrayList<>(counter + 1);

        //remainder of the rounding
        double remainder = pointsNumber - counter;
        long t = 0;
        for (int i = 0; i <= counter; i++) {

            long currentTime = startTime + t;

            double currentAngle = startAngle + angStatVelocity * Converter.toSec(t);

            result.add(new SingleJointPoint(currentAngle, currentTime));

            //to calculate last point which has different time gap
            if (i == counter - 1) {
                t += TIME_GAP * (remainder + 1);
            } else {
                t += TIME_GAP;
            }
        }
        return result;
    }
}

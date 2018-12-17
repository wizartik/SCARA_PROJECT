package com.robotcontrol.movement;

import com.robotcontrol.calc.contouringControl.entities.GCode.GCode;
import com.robotcontrol.calc.contouringControl.entities.GCode.MotionGCode;
import com.robotcontrol.calc.positionalControl.controllers.PositionalController;
import com.robotcontrol.calc.positionalControl.entities.PositionalPath;
import com.robotcontrol.calc.stepperControl.controllers.PathConverter;
import com.robotcontrol.calc.stepperControl.entities.SteppersPath;
import com.robotcontrol.comm.CommunicationController;
import com.robotcontrol.exc.BoundsViolation;
import com.robotcontrol.exc.NoConnection;
import com.robotcontrol.gui.util.DialogHandler;
import com.robotcontrol.parameters.dynamic.DynUtil;
import com.robotcontrol.parameters.dynamic.Motion;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.robotcontrol.parameters.dynamic.DynUtil.*;
import static com.robotcontrol.parameters.dynamic.Position.*;
import static com.robotcontrol.util.CommUtil.checkConnection;

public class MovementController {
    private static volatile MovementController instance;

    public static MovementController getInstance() {
        MovementController localInstance = instance;
        if (localInstance == null) {
            synchronized (MovementController.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new MovementController();
                }
            }
        }
        return localInstance;
    }

    private MovementController() {
    }

    public void moveByGCode() throws NoConnection, IOException {
        if (ParametersController.isMoving()) {
            return;
        }

        checkConnection();

        if ((CURRENT_CONTOUR_STEPPER_PATH != null) && (CURRENT_CONTOUR_PATH != null)) {
            double[] startCoords = CURRENT_CONTOUR_PATH.getgCodeList().get(0).getStartPosition();
            if (needToRecalculateContour(startCoords)) {
                DialogHandler.recalculatePath();
                return;
            }

            sendByWifi(CURRENT_CONTOUR_STEPPER_PATH);
            ParametersController.startedMovement(getFinalPosition(CURRENT_CONTOUR_PATH.getgCodeList()));
        } else {
            DialogHandler.nothingToProcess();
        }
    }

    public void moveByGCodeNew() throws NoConnection, IOException {
        if (ParametersController.isMoving()) {
            return;
        }

        checkConnection();

        if ((FRACTIONAL_CONTOUR_STEPPER_PATH != null) && (FRACTIONAL_CONTOUR_PATH != null)) {
            startMovingThread();
        } else {
            DialogHandler.nothingToProcess();
        }
    }


    public void moveToPointAng(double[] finalAngles) throws BoundsViolation, IOException, NoConnection {
        if (ParametersController.isMoving()) {
            return;
        }

        checkConnection();

        double[] currentAngles = CURRENT_ANGLES;

        System.out.println("current angles: " + Arrays.toString(currentAngles));
        System.out.println("final   angles: " + Arrays.toString(finalAngles));

        PositionalPath positionalPath = PositionalController.moveToPointAng(currentAngles, finalAngles);
        SteppersPath steppersPath = PathConverter.convertToSteppersPath(positionalPath);

        sendByWifi(steppersPath);

        ParametersController.startedAngMovement(finalAngles);
    }

    public void moveToPointPos(double[] finalPosition) throws BoundsViolation, IOException, NoConnection {
        if (ParametersController.isMoving()) {
            return;
        }

        System.out.println("current: " + Arrays.toString(CURRENT_POSITION));
        System.out.println("final  : " + Arrays.toString(finalPosition));

        checkConnection();
        PositionalPath positionalPath = PositionalController.moveToPointPos(CURRENT_POSITION, finalPosition);
        SteppersPath steppersPath = PathConverter.convertToSteppersPath(positionalPath);
        sendByWifi(steppersPath);

        ParametersController.startedMovement(finalPosition);
    }

    public void travelByPos(double[] differences) throws BoundsViolation, IOException, NoConnection {
        double[] finalPosition = new double[differences.length];
        Arrays.setAll(finalPosition, i -> CURRENT_POSITION[i] + differences[i]);
        moveToPointPos(finalPosition);
    }

    public void travelByAng(double[] differences) throws BoundsViolation, IOException, NoConnection {
        double[] currentAngles = Arrays.copyOf(CURRENT_ANGLES, CURRENT_ANGLES.length);
        double[] finalAngles = new double[differences.length];
        Arrays.setAll(finalAngles, i -> currentAngles[i] + differences[i]);
        moveToPointAng(finalAngles);
    }

    public void startCalibrating() throws IOException, NoConnection {
        CommunicationController.sendCallibration();
        ParametersController.startedCalibration();
    }

    public void goToHomeCoords() throws NoConnection, BoundsViolation, IOException {
        moveToPointPos(HOME_COORDS);
    }

    private boolean needToRecalculateContour(double[] startCoords) {
        return !Arrays.equals(startCoords, CURRENT_POSITION);
    }

    private void sendByWifi(SteppersPath steppersPath) throws IOException, NoConnection {
        CommunicationController.sendData(steppersPath, 0);
        CommunicationController.sendData(steppersPath, 1);
        CommunicationController.sendData(steppersPath, 2);
    }

    private double[] getFinalPosition(List<GCode> gCodes) {
        double[] finalPosition;
        GCode lastGCode = gCodes.get(gCodes.size() - 1);

        if (lastGCode instanceof MotionGCode) {
            finalPosition = ((MotionGCode) lastGCode).getFinalPosition();
        } else {
            finalPosition = lastGCode.getStartPosition();
        }

        return finalPosition;
    }

    private void startMovingThread() {
        createMovingThread().start();
    }

    private Thread createMovingThread() {
        return new Thread(() -> {
            DynUtil.FORBID_CALCULATION = true;
            int i = 0;

            while (i < FRACTIONAL_CONTOUR_PATH.size()) {
                if (!Motion.MOVING) {
                    try {
                        sendByWifi(FRACTIONAL_CONTOUR_STEPPER_PATH.get(i));
                        ParametersController.startedMovement(getFinalPosition(FRACTIONAL_CONTOUR_PATH.get(i).getgCodeList()));
                        i++;
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (NoConnection noConnection) {
                        noConnection.printStackTrace();
                    }
                    try {
                        TimeUnit.MILLISECONDS.sleep(50);
                    } catch (InterruptedException ignored) {
                    }
                } else {
                    Thread.yield();
                }
            }
            DynUtil.FORBID_CALCULATION = false;
        });
    }
}

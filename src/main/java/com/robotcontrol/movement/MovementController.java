package com.robotcontrol.movement;

import com.robotcontrol.calc.DHParameters.SCARADH;
import com.robotcontrol.calc.contouringControl.controllers.data.DataController;
import com.robotcontrol.calc.contouringControl.controllers.path.PathController;
import com.robotcontrol.calc.contouringControl.entities.GCode.GCode;
import com.robotcontrol.calc.contouringControl.entities.GCode.MotionGCode;
import com.robotcontrol.calc.contouringControl.entities.path.ContourPath;
import com.robotcontrol.calc.positionalControl.controllers.PositionalCotroller;
import com.robotcontrol.calc.positionalControl.entities.PositionalPath;
import com.robotcontrol.calc.stepperControl.controllers.PathConverter;
import com.robotcontrol.calc.stepperControl.entities.SteppersPath;
import com.robotcontrol.comm.wifi.WifiController;
import com.robotcontrol.exc.*;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static com.robotcontrol.parameters.dynamic.Position.CURRENT_POSITION;
import static com.robotcontrol.util.CommUtil.checkConnection;

public class MovementController {
    private static volatile MovementController instance;

    private WifiController wifiController;

    public static MovementController getInstance() throws IOException {
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

    public void moveByGCodeFile(File file) throws WrongExtension, BoundsViolation, WrongInputData, IOException, ImpossibleToImplement, NoConnection {
        if (ParametersController.isMoving()){
            return;
        }

        checkConnection();

        List<GCode> gCodes = DataController.convertToGCode(file);
        ContourPath contourPath = PathController.makePath(gCodes);
        SteppersPath steppersPath = PathConverter.convertToSteppersPath(contourPath);

        sendByWifi(steppersPath);

        ParametersController.startedMovement(getFinalPosition(gCodes));
    }

    public void moveToPointAng(double[] finalAngles) throws BoundsViolation, IOException, NoConnection {
        if (ParametersController.isMoving()){
            return;
        }

        checkConnection();

        double[] currentAngles = SCARADH.inverseKinematics(CURRENT_POSITION);
        PositionalPath positionalPath = PositionalCotroller.moveToPointAng(currentAngles, finalAngles);
        SteppersPath steppersPath = PathConverter.convertToSteppersPath(positionalPath);

        sendByWifi(steppersPath);

        double[] finalPosition = SCARADH.forwardKinematics(finalAngles);
        ParametersController.startedMovement(finalPosition);
    }

    public void moveToPointPos(double[] finalPosition) throws BoundsViolation, IOException, NoConnection {
        if (ParametersController.isMoving()){
            return;
        }

        checkConnection();

        PositionalPath positionalPath = PositionalCotroller.moveToPointPos(CURRENT_POSITION, finalPosition);
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
        double[] currentAngles = SCARADH.inverseKinematics(CURRENT_POSITION);
        double[] finalAngles = new double[differences.length];
        Arrays.setAll(finalAngles, i -> currentAngles[i] + differences[i]);
        moveToPointAng(finalAngles);
    }

    private void sendByWifi(SteppersPath steppersPath) throws IOException {
        wifiController.sendData(steppersPath, 0);
        wifiController.sendData(steppersPath, 1);
        wifiController.sendData(steppersPath, 2);
    }

    private double[] getFinalPosition(List<GCode> gCodes){
        double[] finalPosition;
        GCode lastGCode = gCodes.get(gCodes.size() - 1);

        if (lastGCode instanceof MotionGCode){
            finalPosition = ((MotionGCode) lastGCode).getFinalPosition();
        } else {
            finalPosition = lastGCode.getStartPosition();
        }

        return finalPosition;
    }
}

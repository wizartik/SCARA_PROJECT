package com.robotcontrol.calc.contouringControl.controllers.data;

import calc.data.Constants;
import calc.data.Dynamic;
import calc.util.MathCalc;
import calc.util.Utility;
import com.robotcontrol.calc.contouringControl.entities.GCode.*;
import com.robotcontrol.calc.contouringControl.entities.data.Container;
import exc.WrongInputData;

import java.util.Arrays;

import static com.robotcontrol.calc.contouringControl.entities.GCode.GCodeType.GARBAGE;

public class LineHandler {

    public static GCode makeGCode(GCode previousGCode, String line) throws
            WrongInputData {
        line = line.toUpperCase();
        Container container = new Container();

        prepareContainer(previousGCode, container, line);

        parseLine(line, container);
        checkData(container);

        return containerToGCode(container);
    }

    private static void checkData(Container container) throws WrongInputData {
        boolean circular = (container.getgCodeType().equals(GCodeType.G02)
                || container.getgCodeType().equals(GCodeType.G03));
        boolean zeroRadius = (container.getI() == 0.0 && container.getJ() == 0.0
                && container.getRadius() == 0.0);

        boolean wrongRadius = Math.abs(MathCalc.linearLength(container
                        .getStartPosition(),
                container.getCenterPosition())
                - MathCalc.linearLength(container.getFinalPosition(),
                container.getCenterPosition()))
                > 10e-5;

        if (circular && (zeroRadius || wrongRadius)) {
            throw new WrongInputData("This circular G code " +
                    " has no correct data for radius or center definition.",
                    container.getLine());
        }

        if (Utility.containsNaN(container.getStartPosition())
                || Utility.containsNaN(container.getFinalPosition())
                || Utility.containsNaN(container.getCenterPosition())){
            throw new WrongInputData("This G code has incorrect data",
                    container.getLine());
        }
    }

    private static GCode containerToGCode(Container container) {

        if (container.getgCodeType() == GCodeType.G00
                || container.getgCodeType() == GCodeType.G01 ){
            return containerToLinearGCode(container);
        } else if (container.getgCodeType() == GCodeType.G02
                || container.getgCodeType() == GCodeType.G03 ){
            return containerToAngularGCode(container);
        } else if (container.getgCodeType() == GCodeType.G04){
            return containerToPauseGCode(container);
        } else {
            return null;
        }
    }

    private static PauseGCode containerToPauseGCode(Container container){
        return new PauseGCode(container.getStartPosition(),
                container.getLine(), container.getgCodeType(),
                container.getPauseInUs());
    }

    private static AngularGCode containerToAngularGCode(Container container){
        return new AngularGCode(container.getStartPosition(),
                container.getFinalPosition(), container.getStaticVelocity(),
                Constants.NORMAL_ACCELERATION, container.getLine(),
                container.getgCodeType(), container.getRadius());
    }

    private static LinearGCode containerToLinearGCode(Container container){
        return new LinearGCode(container.getStartPosition(),
                container.getFinalPosition(), container.getStaticVelocity(),
                Constants.NORMAL_ACCELERATION, container.getLine(),
                container.getgCodeType());
    }

    /**
     * Parses line to data.
     *
     * @param line line of the current G code;
     * @throws WrongInputData if line has no correct data for radius or center
     *                        definition.
     */
    private static void parseLine(String line, Container container) throws
            WrongInputData {
        String[] words = line.split("\\s+");

        char c = words[0].charAt(0);

        if (!isKnown(c)) {
            container.setgCodeType(GARBAGE);
        }

        for (String word : words) {
            double value;
            String sValue;
            char letter;

            if (word.length() > 1
                    && Utility.isNumeric(sValue = word.substring(1))) {
                value = Double.parseDouble(sValue);
                letter = word.charAt(0);
            } else {
                continue;
            }

            boolean circular = parseWord(letter, value, container);
            if (circular) {
                makeCircular(container);
            }
        }
    }

    /**
     * If is known that G code is circular makes radius or center position.
     *
     * @throws WrongInputData if line has no correct data for radius or center
     *                        definition.
     */
    private static void makeCircular(Container container) throws WrongInputData {
        if (container.isIJ()) {
            container.getCenterPosition()[0] = container.getStartPosition()[0]
                    + container.getI();
            container.getCenterPosition()[1] = container.getStartPosition()[1]
                    + container.getJ();

            container.setRadius(MathCalc.linearLength(container.getCenterPosition(),
                    container.getStartPosition()));
        } else if (container.getRadius() > 0) {
            if (container.getgCodeType().equals(GCodeType.G02)) {
                container.setCenterPosition(MathCalc.findCenterG02(container.getStartPosition(),
                        container.getFinalPosition(),
                        container.getRadius()));
            } else if (container.getgCodeType().equals(GCodeType.G03)) {
                container.setCenterPosition(MathCalc.findCenterG03(container.getStartPosition(),
                        container.getFinalPosition(),
                        container.getRadius()));
            }
        } else {
            throw new WrongInputData("This circular G code" +
                    " has no correct data for radius or center definition.",
                    container.getLine());
        }
    }

    /**
     * Parses one word of line to data (G01 or X1.05 or F100 and so on).
     *
     * @param letter first letter of the word.
     * @param value  values that current word contains (number after first
     *               letter).
     * @return true if is known that G code is circular (word contains
     * definition of I, J, or radius).
     */
    private static boolean parseWord(char letter, double value, Container
            container) {
        boolean circular = false;
        switch (letter) {
            case 'G':
                container.setgCodeType(gCodeByNumber(value));
                break;
            case 'X':
                if (container.getgCodeType().equals(GCodeType.G04)) {
                    container.setPauseInUs(MathCalc.toUSec(value));
                } else {
                    container.getFinalPosition()[0] = value;
                }
                break;
            case 'Y':
                container.getFinalPosition()[1] = value;
                break;
            case 'Z':
                container.getFinalPosition()[2] = value;
                break;
            case 'F':
                container.setStaticVelocity(MathCalc.toInternalVel(value));
                break;
            case 'R':
                container.setRadius(value);
                circular = true;
                break;
            case 'I':
                container.setI(value);
                container.setIJ(true);
                circular = true;
                break;
            case 'J':
                container.setJ(value);
                container.setIJ(true);
                circular = true;
                break;
            case 'P':
            case 'U':
                container.setPauseInUs(MathCalc.toUSec(value));
                break;
        }
        return circular;
    }

    /**
     * Checks if command is known.
     *
     * @return true if known.
     */
    private static boolean isKnown(char c) {
        for (int i = 0; i < Constants.KNOWN_COMMANDS.length; i++) {
            if (c == Constants.KNOWN_COMMANDS[i]) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks G code type by number.
     *
     * @param num number of G code
     * @return GCodeType.
     */
    private static GCodeType gCodeByNumber(double num) {

        int number = (int) num;

        switch (number) {
            case 0:
                return GCodeType.G00;
            case 1:
                return GCodeType.G01;
            case 2:
                return GCodeType.G02;
            case 3:
                return GCodeType.G03;
            case 4:
                return GCodeType.G04;
            default:
                return GCodeType.GARBAGE;
        }
    }


    private static void prepareContainer(GCode gCode, Container container,
                                         String line){
        container.setLine(line);
        container.setStaticVelocity(Constants.NORMAL_VELOCITY);
        if (gCode == null){
            container.setStartPosition(Arrays.copyOf(Dynamic
                    .CURRENT_POSITION, Dynamic.CURRENT_POSITION.length));
            container.setFinalPosition(Arrays.copyOf(Dynamic
                    .CURRENT_POSITION, Dynamic.CURRENT_POSITION.length));
            container.setCenterPosition(Arrays.copyOf(Dynamic
                    .CURRENT_POSITION, Dynamic.CURRENT_POSITION.length));
        } else {
            container.setStartPosition(Arrays.copyOf(gCode.getStartPosition(),
                    gCode.getStartPosition().length));
            if (gCode instanceof MotionGCode){
                container.setStartPosition(Arrays.copyOf(((MotionGCode)
                        gCode).getFinalPosition(), ((MotionGCode) gCode)
                        .getFinalPosition().length));
                container.setStaticVelocity(((MotionGCode) gCode).getStaticVelocity());
            }
            container.setCenterPosition(Arrays.copyOf(container
                    .getStartPosition(), container.getStartPosition().length));
            container.setFinalPosition(Arrays.copyOf(container
                    .getStartPosition(), container.getStartPosition().length));
        }
    }
}

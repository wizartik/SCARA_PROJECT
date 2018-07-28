package calc.contouringControl;

import calc.GCode.GCodeType;
import calc.checks.Checker;
import calc.data.Constants;
import calc.util.MathCalc;
import calc.util.Utility;
import com.robotcontrol.exc.WrongInputData;

import java.util.Arrays;

/**
 * Parses line to data needed to create G code object. Line should be cleared
 * out of comments and other garbage.
 */
public class DataFromLine {

    private GCodeType gCodeType;
    private String line;

    private double[] startPosition;
    private double[] finalPosition;
    private double staticVelocity;
    private double[] centerPosition;
    private double radius = 0;
    private double pauseInUs = 0;
    private boolean IJ = false;
    private double I = 0.0;
    private double J = 0.0;

    /**
     * After this constructor object is ready to use.
     *
     * @param previousVelocity  velocity in cm/s from previous G code line.
     * @param previousGCodeType previous G code line type.
     * @param startPosition     start position of the current G code.
     * @param line              line of G code without comments and other garbage.
     * @throws WrongInputData if line has no correct data for radius or center
     *                        definition.
     */
    public DataFromLine(double previousVelocity, GCodeType previousGCodeType,
                        double[] startPosition, String line)
            throws WrongInputData {
        this.staticVelocity = previousVelocity;
        this.line = line.toUpperCase();
        this.gCodeType = previousGCodeType;
        this.startPosition = Arrays.copyOf(startPosition, startPosition.length);
        this.finalPosition = Arrays.copyOf(startPosition, startPosition.length);
        this.centerPosition = Arrays.copyOf(startPosition, startPosition.length);

        parseLine(this.line);

        if (gCodeType != GCodeType.G04 && gCodeType != GCodeType.G00 &&
                staticVelocity == 0) {
            staticVelocity = Constants.NORMAL_VELOCITY;
        }

        boolean circular = (this.gCodeType.equals(GCodeType.G02)
                || this.gCodeType.equals(GCodeType.G03));
        boolean zeroRadius = (this.I == 0.0 && this.J == 0.0
                && this.radius == 0.0);

        boolean wrongRadius = (MathCalc.linearLength(this.startPosition,
                this.centerPosition)
                - MathCalc.linearLength(this.finalPosition,
                this.centerPosition))
                > 10e-5;

        if (circular && (zeroRadius || wrongRadius)) {
            throw new WrongInputData("This circular G code data" +
                    " has no correct data for radius or center definition.",
                    line);
        }


    }

    public String getLine() {
        return line;
    }

    public GCodeType getgCodeType() {
        return gCodeType;
    }

    public double[] getStartPosition() {
        return startPosition;
    }

    public double[] getFinalPosition() {
        return finalPosition;
    }

    public double getStaticVelocity() {
        return staticVelocity;
    }

    public double[] getCenterPosition() {
        return centerPosition;
    }

    public double getRadius() {
        return radius;
    }

    public double getPauseInUs() {
        return pauseInUs;
    }

    /**
     * Parses line to data.
     *
     * @param line line of the current G code;
     * @throws WrongInputData if line has no correct data for radius or center
     *                        definition.
     */
    private void parseLine(String line) throws WrongInputData {
        String[] words = line.split("\\s+");

        char c = words[0].charAt(0);

        if (!isKnown(c)) {
            gCodeType = GCodeType.GARBAGE;
        }

        for (int i = 0; i < words.length; i++) {
            double value;
            String sValue;
            char letter;

            if (words[i].length() > 1
                    && Utility.isNumeric(sValue = words[i].substring(1))) {
                value = Double.parseDouble(sValue);
                letter = words[i].charAt(0);
            } else {
                continue;
            }

            boolean circular = parseWord(letter, value);
            if (circular) {
                makeCircular();
            }
        }
    }

    /**
     * If is known that G code is circular makes radius or center position.
     *
     * @throws WrongInputData if line has no correct data for radius or center
     *                        definition.
     */
    private void makeCircular() throws WrongInputData {
        if (IJ) {
            this.centerPosition[0] = this.startPosition[0] + this.I;
            this.centerPosition[1] = this.startPosition[1] + this.J;
            this.radius = MathCalc.linearLength(centerPosition,
                    startPosition);
        } else if (radius > 0) {
            if (gCodeType.equals(GCodeType.G02)) {
                this.centerPosition = MathCalc.findCenterG02(startPosition,
                        finalPosition,
                        radius);
            } else if (gCodeType.equals(GCodeType.G03)) {
                this.centerPosition = MathCalc.findCenterG03(startPosition,
                        finalPosition,
                        radius);
            }
        } else {
            throw new WrongInputData("This circular G code data" +
                    " has no correct data for radius or center definition.",
                    line);
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
    private boolean parseWord(char letter, double value) {
        boolean circular = false;
        switch (letter) {
            case 'G':
                gCodeType = Utility.gCodeByNumber(value);
                break;
            case 'X':
                if (gCodeType.equals(GCodeType.G04)) {
                    pauseInUs = MathCalc.toUSec(value);
                } else {
                    this.finalPosition[0] = value;
                }
                break;
            case 'Y':
                this.finalPosition[1] = value;
                break;
            case 'Z':
                this.finalPosition[2] = value;
                break;
            case 'F':
                this.staticVelocity = Checker.checkVelocity(MathCalc.toInternalVel(value),
                        gCodeType);
                break;
            case 'R':
                this.radius = value;
                circular = true;
                break;
            case 'I':
                I = value;
                IJ = true;
                circular = true;
                break;
            case 'J':
                J = value;
                IJ = true;
                circular = true;
                break;
            case 'P':
            case 'U':
                pauseInUs = value * 1000;
                break;
        }
        return circular;
    }

    /**
     * Checks if command is known.
     *
     * @return true if known.
     */
    private boolean isKnown(char c) {
        for (int i = 0; i < Constants.KNOWN_COMMANDS.length; i++) {
            if (c == Constants.KNOWN_COMMANDS[i]) {
                return true;
            }
        }
        return false;
    }
}

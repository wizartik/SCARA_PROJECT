package calc.util;

import calc.DHParameters.SCARADH;
import calc.GCode.GCodeType;
import calc.data.Constants;
import com.robotcontrol.calc.contouringControl.GCode.entities.Point;
import exc.BoundsViolation;
import exc.WrongExtension;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Utility {

    /**
     * Creates Point by given data.
     *
     * @param position coordinates in m.
     * @param axisDirections direction of the movement;
     * @param velocity velocity of the point in m/s.
     * @param acceleration current acceleration in m/s^2.
     * @param time current time in us.
     * @param gCode current G code line.
     * @return new initialized point.
     */
    public static Point makePoint(double[] position, double[] axisDirections,
                                  double velocity, double acceleration,
                                  double time, String gCode)
            throws BoundsViolation {
        double[] coords = Arrays.copyOf(position, position.length);
        double[] axisVelocities = MathCalc.velocityCustomize(axisDirections,
                                                             velocity);
        double[] angles = SCARADH.inverseKinematics(position);
        double[] angularVelocities = SCARADH.velocityKinematics(angles,
                                                                axisVelocities);

        Point point = new Point(coords, velocity, axisVelocities, acceleration,
                angles, angularVelocities, time, gCode);

//        Checker.checkBoundViolation(point);
        return point;
    }

    /**
     * Converts file into the list of strings of the lines in the file.
     *
     * @param file file that need to be converted.
     * @return ArrayList of lines in the file.
     * @throws WrongExtension thrown when file has forbidden extension or
     *                        doesn't has it at all.
     * @throws IOException thrown if somehow there is IOException.
     */
    public static ArrayList<String> gCodeFileToList(File file) throws
            WrongExtension, IOException {
        ArrayList<String> GCode;

        String fileName = file.getName();

        int i = fileName.lastIndexOf('.');
        String extension;

        if (i > 0){
            extension = fileName.substring(i + 1);
        } else {
            throw new WrongExtension("");
        }

        if (checkExtension(extension)){
            GCode = fileToList(file);
        } else {
            throw new WrongExtension(extension);
        }

        return GCode;
    }

    /**
     * Checks if extension is right and allowed.
     *
     * @param extension filename extension that will be checked.
     * @return true if extension is allowed, false if not.
     */
    private static boolean checkExtension(String extension) {

        for (int i = 0; i < Constants.ALLOWED_FILENAME_EXTENSIONS.length; i++) {
            String allowed = Constants.ALLOWED_FILENAME_EXTENSIONS[i];
            if (extension.equalsIgnoreCase(allowed)){
                return true;
            }
        }
        return false;
    }

    /**
     * Simply converts all line from file to list of Strings.
     *
     * @param file file that will be converted/
     * @return list of String lines.
     * @throws IOException if there is some errors.
     */
    private static ArrayList<String> fileToList(File file) throws IOException {

        ArrayList<String> lines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file)))
        {
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                lines.add(sCurrentLine);
            }
        } catch (IOException e) {
            throw new IOException(e);
        }
        return lines;
    }

    /**
     * Checks if string is a number.
     *
     * @param str string to check
     * @return true if string is a number, false - if not.
     */
    public static boolean isNumeric(String str)
    {
        try {
            Double.parseDouble(str);
        } catch(NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    /**
     * Checks G code type by number.
     *
     * @param number number of G code
     * @return GCodeType.
     */
    public static GCodeType gCodeByNumber(double number){
        GCodeType gCodeType = GCodeType.GARBAGE;

        if (number == 0){
            gCodeType = GCodeType.G00;
        } else if (number == 1){
            gCodeType = GCodeType.G01;
        } else if (number == 2){
            gCodeType = GCodeType.G02;
        } else if (number == 3){
            gCodeType = GCodeType.G03;
        } else if (number == 4){
            gCodeType = GCodeType.G04;
        }

        return gCodeType;
    }

    public static String makeG01String(double[] finalCoords, double velocity) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("G01 X");
        stringBuilder.append(finalCoords[0]);
        stringBuilder.append(" Y");
        stringBuilder.append(finalCoords[1]);
        stringBuilder.append(" Z");
        stringBuilder.append(finalCoords[2]);
        stringBuilder.append(" F");
        stringBuilder.append(velocity * 60);
        return stringBuilder.toString();
    }

    public static String makeG02String(double[] finalCoords, double radius,
                                       double velocity){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("G02 X");
        stringBuilder.append(finalCoords[0]);
        stringBuilder.append(" Y");
        stringBuilder.append(finalCoords[1]);
        stringBuilder.append(" Z");
        stringBuilder.append(finalCoords[2]);
        stringBuilder.append(" R");
        stringBuilder.append(radius);
        stringBuilder.append(" F");
        stringBuilder.append(velocity * 60);
        return stringBuilder.toString();
    }

    public static String makeG03String(double[] finalCoords, double radius,
                                       double velocity){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("G03 X");
        stringBuilder.append(finalCoords[0]);
        stringBuilder.append(" Y");
        stringBuilder.append(finalCoords[1]);
        stringBuilder.append(" Z");
        stringBuilder.append(finalCoords[2]);
        stringBuilder.append(" R");
        stringBuilder.append(radius);
        stringBuilder.append(" F");
        stringBuilder.append(velocity * 60);
        return stringBuilder.toString();
    }

    /**
     * Finds largest of three numbers and returns it.
     *
     * @param x first number.
     * @param y second number.
     * @param z third number.
     * @return largest number;
     */
    public static int findLargest(int x, int y, int z){
        if ( x > y && x > z ) {
            return x;
        }
        else if ( y > x && y > z ) {
            return y;
        }
        else {
            return z;
        }
    }

    /**
     * Transforms array of Long to array of long.
     *
     * @param array Long array.
     * @return long array.
     */
    public static long[] wrappedToPrimitive(Long[] array){
        long[] a = new long[array.length];

        for (int i = 0; i < a.length; i++) {
            a[i] = array[i];
        }
        return a;
    }
}

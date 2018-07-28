package calc.contouringControl;

import calc.GCode.GCodeType;
import calc.data.Dynamic;
import com.robotcontrol.exc.WrongInputData;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Creates ArrayList of DataFromLine objects from list of strings.
 */
public class DataFromList {

    private ArrayList<DataFromLine> dataFromList = new ArrayList<>();
    private ArrayList<String> initialList;

    /**
     * @param gCodeList list of G codes.
     * @throws WrongInputData if there are no G commands in list.
     */
    public DataFromList(ArrayList<String> gCodeList) throws WrongInputData {
        this.initialList = new ArrayList<>(gCodeList);

        makeData();
        if (dataFromList.size() == 0) {
            throw new WrongInputData("Current G code path does not" +
                    " contain any GCode command.", "There are no" +
                    " commands.");
        }

    }

    public ArrayList<DataFromLine> getDataFromList() {
        return dataFromList;
    }

    /**
     * Deletes all text in brackets and after %. Also deletes line if it
     * contains less then 2 characters. Adds processed line to processedList.
     *
     * @param line line that should be processed.
     */
    private String clearGarbage(String line) {
        //delete all text in ()
        line = line.replaceAll("\\(([^)]+)\\)", "");

        //delete all after %
        int j = line.indexOf('%');
        if (j != -1) {
            line = line.substring(0, j);
        }

        line = line.toUpperCase();
        return line;
    }

    /**
     * Makes DataFromLine objects for every line in initialList.
     *
     * @throws WrongInputData if line in G code has wrong data.
     */
    private void makeData() throws WrongInputData {
        double previousVelocity = 0;
        double[] startPosition = Dynamic.CURRENT_POSITION;
        GCodeType previousGCodeType = GCodeType.GARBAGE;

        for (int i = 0; i < initialList.size(); i++) {
            String line = clearGarbage(initialList.get(i));

            DataFromLine dataFromLine;
            if (line != null && line.length() > 1) {
                dataFromLine = new DataFromLine(previousVelocity,
                        previousGCodeType, startPosition, line);
            } else {
                continue;
            }

            dataFromList.add(dataFromLine);

            previousGCodeType = dataFromLine.getgCodeType();
            previousVelocity = dataFromLine.getStaticVelocity();
            startPosition = Arrays.copyOf(dataFromLine.getFinalPosition(),
                    dataFromLine.getFinalPosition().length);
        }

    }
}

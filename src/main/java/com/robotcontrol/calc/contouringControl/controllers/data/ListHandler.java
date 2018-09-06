package com.robotcontrol.calc.contouringControl.controllers.data;

import com.robotcontrol.calc.contouringControl.entities.GCode.GCode;
import com.robotcontrol.exc.WrongInputData;
import org.magicwerk.brownies.collections.GapList;

import java.util.ArrayList;
import java.util.List;

class ListHandler {

    static List<GCode> makeGCodeList(ArrayList<String> source) throws WrongInputData {
        GapList<GCode> result = new GapList<>(source.size());
        makeData(source, result);
        result.trimToSize();
        if (result.size() == 0) {
            throw new WrongInputData("Current G code path does not" +
                    " contain any GCode command.", "There are no" +
                    " commands.");
        }

        return result;
    }

    /**
     * Deletes all text in brackets and after %. Also deletes line if it
     * contains less then 2 characters. Adds processed line to processedList.
     *
     * @param line line that should be processed.
     */
    private static String clearGarbage(String line) {
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
    private static void makeData(List<String> initialList, List<GCode> result)
            throws WrongInputData {

        GCode previousGCode = null;

        for (String initialGCode : initialList) {
            String line = clearGarbage(initialGCode);
            if (line != null && line.length() > 1) {
                previousGCode = LineHandler.makeGCode(previousGCode, line);

                if (previousGCode != null) {
                    result.add(previousGCode);
                }
            }

        }

    }

}

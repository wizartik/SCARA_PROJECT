package com.robotcontrol.calc.contouringControl.controllers.data;

import com.robotcontrol.calc.contouringControl.entities.GCode.GCode;
import com.robotcontrol.exc.WrongInputData;
import com.robotcontrol.movement.ParametersController;
import com.robotcontrol.parameters.dynamic.DynUtil;
import com.robotcontrol.util.progress.CurrentAction;
import org.magicwerk.brownies.collections.GapList;

import java.util.List;

class ListHandler {

    static List<GCode> makeGCodeList(List<String> source) throws WrongInputData {
        ParametersController.setCurrentAction(CurrentAction.Parsing);

        GapList<GCode> result = new GapList<>(source.size());
        makeData(source, result);
        result.trimToSize();

        System.out.println(result);

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

        //delete all after ;
        int k = line.indexOf(';');
        if (k != -1) {
            line = line.substring(0, k);
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


        for (int i = 0; i < initialList.size(); i++) {
            String initialGCode = initialList.get(i);
            if (DynUtil.progress != null) {
                DynUtil.progress.setProgressOfParsing(i, initialList.size());
            }
            String line = clearGarbage(initialGCode);
            if (line != null && line.length() > 1) {
                previousGCode = LineHandler.makeGCode(previousGCode, line);
                System.out.println("   " + previousGCode);
                if (previousGCode != null) {
                    result.add(previousGCode);
                }
            }

        }
    }

}

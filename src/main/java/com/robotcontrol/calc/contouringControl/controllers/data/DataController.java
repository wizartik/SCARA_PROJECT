package com.robotcontrol.calc.contouringControl.controllers.data;

import com.robotcontrol.calc.contouringControl.entities.GCode.GCode;
import com.robotcontrol.exc.BoundsViolation;
import com.robotcontrol.exc.WrongExtension;
import com.robotcontrol.exc.WrongInputData;
import com.robotcontrol.util.FilesHandler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataController {

    public static List<GCode> convertToGCode(List<String> sourceList)
            throws WrongInputData {
        return ListHandler.makeGCodeList(sourceList);
    }

    public static List<GCode> convertToGCode(File sourceFile)
            throws IOException, WrongExtension, WrongInputData, BoundsViolation {
        ArrayList<String> source = FilesHandler.gCodeFileToList(sourceFile);
        return ListHandler.makeGCodeList(source);
    }

    public static GCode convertToGCode(String sourceGCodeLine) throws WrongInputData {
        return LineHandler.makeGCode(null, sourceGCodeLine);
    }

}

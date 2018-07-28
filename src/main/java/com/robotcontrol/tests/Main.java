package com.robotcontrol.tests;

import com.robotcontrol.calc.contouringControl.controllers.data.DataController;
import com.robotcontrol.calc.contouringControl.controllers.path.PathController;
import com.robotcontrol.calc.contouringControl.entities.GCode.GCode;
import com.robotcontrol.exc.BoundsViolation;
import com.robotcontrol.exc.ImpossibleToImplement;
import com.robotcontrol.exc.WrongExtension;
import com.robotcontrol.exc.WrongInputData;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class Main {
    public static void main(String[] args) throws BoundsViolation, ImpossibleToImplement, com.robotcontrol.exc.BoundsViolation, com.robotcontrol.exc.ImpossibleToImplement, WrongInputData, IOException, WrongExtension {

//        MotionGCode motionGCode = new MotionGCode(new double[]{1,2,3}, new
//                double[]{5,6,7}, 10, 10, "helllllo", GCodeType.G01);
//
//        GCode gCode = (LineHandler.makeGCode(motionGCode, "G03 X40 Y50 Z60.5 " +
//                "R3"));
//
//        System.out.println(gCode.getGCode());
//        System.out.println(Arrays.toString(gCode.getStartPosition()));
//        System.out.println(gCode.getGCodeType());
//
//        MotionGCode motionGCode1 = (MotionGCode) gCode;
//
//        System.out.println(Arrays.toString(motionGCode1.getFinalPosition()));
//        System.out.println(motionGCode1.getStaticVelocity());
//        System.out.println(motionGCode1.getAcceleration());
//
//        AngularGCode angularGCode = (AngularGCode) gCode;
//
//        System.out.println("radius " + angularGCode.getRadius());
//        System.out.println("center " + Arrays.toString(angularGCode.getCenterPosition()));

        File file = new File("D:\\GCodes\\cartman.ngc");

        ArrayList<GCode> gCodes = DataController.convertToGCode(file);

        Date date = new Date();
//        new Path(file);
        Date date1 = new Date();

        System.out.println("old " + (date1.getTime() - date.getTime()));


        try {
            date = new Date();
            PathController.makePath(DataController.convertToGCode(file));
            date1 = new Date();
            System.out.println("new " + (date1.getTime() - date.getTime()));
        } catch (BoundsViolation boundsViolation) {
            boundsViolation.printStackTrace();
        } catch (ImpossibleToImplement impossibleToImplement) {
            impossibleToImplement.printStackTrace();
            System.out.println(impossibleToImplement.getGCode());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WrongExtension wrongExtension) {
            wrongExtension.printStackTrace();
        } catch (WrongInputData wrongInputData) {
            wrongInputData.printStackTrace();
        }

    }
}

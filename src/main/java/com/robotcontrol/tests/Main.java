package com.robotcontrol.tests;

import com.robotcontrol.calc.contouringControl.controllers.data.DataController;
import com.robotcontrol.calc.contouringControl.controllers.path.PathController;
import com.robotcontrol.calc.contouringControl.entities.path.Path;
import com.robotcontrol.exc.BoundsViolation;
import com.robotcontrol.exc.ImpossibleToImplement;
import com.robotcontrol.exc.WrongExtension;
import com.robotcontrol.exc.WrongInputData;

import java.io.File;
import java.io.IOException;
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



        Date date = new Date();
        for (int i = 0; i < 20; i++) {
//            new Path(file);
        }
        Date date1 = new Date();

        System.out.println("old " + (date1.getTime() - date.getTime()));



            date = new Date();
        Path path = null;
        try {
            path = PathController.makePath(DataController.convertToGCode(file));
        } catch (BoundsViolation boundsViolation) {
            boundsViolation.printStackTrace();
        } catch (ImpossibleToImplement impossibleToImplement) {
            impossibleToImplement.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WrongExtension wrongExtension) {
            wrongExtension.printStackTrace();
        } catch (WrongInputData wrongInputData) {
            wrongInputData.printStackTrace();
            System.out.println(wrongInputData.getgCode());
        }
        date1 = new Date();
            System.out.println("new " + (date1.getTime() - date.getTime()));

        System.out.println(path);
        
        long num = 0;


//
//        List<Point> points = ((MotionGCode) path.getgCodeList().get(0)).getgCodePath();
//
//
//        long big = 0;
//        long prev = ((MotionGCode) path.getgCodeList().get(1)).getgCodePath()
//                .get(0).getTime();
//        for (int i = 1; i < path.getgCodeList().size(); i++) {
//            System.out.println("start " + path.getgCodeList().get(i).getStartTime());
//            System.out.println("final " + path.getgCodeList().get(i).getFinalTime());
//            for (int j = 0; j < ((MotionGCode) path.getgCodeList().get(i)).getgCodePath().size(); j++) {
//                long now = ((MotionGCode) path.getgCodeList().get(i))
//                        .getgCodePath()
//                        .get(j).getTime();
//
//                if (now - prev > big) {
//                    big = now - prev;
////                    System.out.println("in " + big);
////                    System.out.println(((MotionGCode) path.getgCodeList().get(i))
////                            .getgCodePath()
////                            .get(j));
//                }
//
//                prev = now;
//            }
//
//        }
//
//        System.out.println("biggest gap " + big);
//        System.out.println("points " + num);

    }
}

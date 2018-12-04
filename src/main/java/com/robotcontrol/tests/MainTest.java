package com.robotcontrol.tests;

import com.robotcontrol.calc.DHParameters.SCARADH;
import com.robotcontrol.exc.BoundsViolation;

import java.util.Arrays;

public class MainTest {
    public static void main(String[] args) throws BoundsViolation {
//        double[] initial = {0, 0, Math.random() * 10};
//
//        for (int i = 0; i < 40; i++) {
////            double[] initial = {0, Math.PI/2, Math.random() * 10};
//            initial[0] += 0.1;
//            initial[1] += 0.1;
//
//            double[] forwardKinematics = SCARADH.forwardKinematics(initial);
//            double[] inverseKinematics = SCARADH.inverseKinematics(forwardKinematics);
//
//            System.out.println("initial=" + Arrays.toString(initial));
//            System.out.println("inverse=" + Arrays.toString(inverseKinematics));
//            System.out.println("forward=" + Arrays.toString(forwardKinematics));
//            double[] forward = SCARADH.forwardKinematics(inverseKinematics);
//            System.out.println("result =" + Arrays.toString(forward));
//            System.out.println();
//        }

        for (int i = 0; i < 50; i++) {
            double[] initCoords = new double[] {Math.random()*100,Math.random()*100,Math.random()*100};

            double[] resultAngl = SCARADH.inverseKinematics(initCoords);
            double[] resultCoords = SCARADH.forwardKinematics(resultAngl);
            double[] secondAngl = SCARADH.inverseKinematics(resultCoords);

            System.out.println("init coords   =" + Arrays.toString(initCoords));
            System.out.println("result coords =" + Arrays.toString(resultCoords));
            System.out.println("result angles =" + Arrays.toString(resultAngl));
            System.out.println("second angles =" + Arrays.toString(secondAngl));
            System.out.println();
        }

//        PositionalPath positionalPath = PositionalController.moveToPointPos(CURRENT_POSITION, new double[]{10,10,12});
//        SteppersPath steppersPath = PathConverter.convertToSteppersPath(positionalPath);
//        List<SteppersPoint> steppersPoints = steppersPath.getSteppersPoints();
//
//        long total = 0;
//        for (int i = 0; i < steppersPoints.size(); i++) {
//            total += steppersPoints.get(i).getStepsDelays()[2];
//            System.out.println(steppersPoints.get(i).getStepsDelays()[2]);
//        }
//
//        System.out.println(total);
    }
}

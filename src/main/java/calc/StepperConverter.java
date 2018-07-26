package calc;

import calc.data.Constants;
import calc.data.Dynamic;
import calc.util.Utility;

import java.util.ArrayList;

public class StepperConverter {

    /**
     * Converts path of angles to stepper path.
     *
     * @param angles     array of angles [t, T1, T2, T3], where t is time in
     *                   microseconds, T1 - angle of first stepper, T2 - angle
     *                   of second stepper, T3 - angle of third stepper.
     * @param microsteps microsteps per real step.
     * @return path for steppers [t1, t2, t3], where t1/t2/t3 - time in
     * microseconds that should pass after which first/second/third stepper
     * have to make step.
     */
    public static long[][] convertPath(double[][] angles, byte microsteps) {
        ArrayList<Long> stepper1 = new ArrayList<>();
        ArrayList<Long> stepper2 = new ArrayList<>();
        ArrayList<Long> stepper3 = new ArrayList<>();

        double realStep = Math.toRadians(Constants.STEP) / Dynamic.MICROSTEPS;

        double t1LastTime = 0;
        double t1AngleReminder = 0;

        double t2LastTime = 0;
        double t2AngleReminder = 0;

        double t3LastTime = 0;
        double t3AngleReminder = 0;

        for (int i = 0; i < angles[0].length; i++) {
            t1AngleReminder += angles[1][i];
            t2AngleReminder += angles[2][i];
            t3AngleReminder += angles[3][i];

            if (t1AngleReminder >= realStep) {
                t1AngleReminder -= realStep;
                stepper1.add((long) (angles[0][i] - t1LastTime));
                t1LastTime = angles[0][i];
            }

            if (t2AngleReminder >= realStep) {
                t2AngleReminder -= realStep;
                stepper2.add((long) (angles[0][i] - t2LastTime));
                t2LastTime = angles[0][i];
            }

            if (t3AngleReminder >= realStep) {
                t3AngleReminder -= realStep;
                stepper3.add((long) (angles[0][i] - t3LastTime));
                t3LastTime = angles[0][i];
            }
        }


        return makePath(stepper1, stepper2, stepper3);
    }

    private static long[][] makePath(ArrayList<Long> stepper1,
                                     ArrayList<Long> stepper2,
                                     ArrayList<Long> stepper3) {
        int arrLength = Utility.findLargest(stepper1.size(), stepper2.size(),
                stepper3.size());

        long[][] stepperPath = new long[3][arrLength];

        long[] a = Utility.wrappedToPrimitive((Long[]) stepper1.toArray());
        long[] b = Utility.wrappedToPrimitive((Long[]) stepper2.toArray());
        long[] c = Utility.wrappedToPrimitive((Long[]) stepper3.toArray());

        System.arraycopy(a, 0, stepperPath[0], 0, a.length);
        System.arraycopy(b, 0, stepperPath[1], 0, b.length);
        System.arraycopy(c, 0, stepperPath[2], 0, c.length);

        return stepperPath;
    }
}

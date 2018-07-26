package calc.DHParameters;

import calc.data.Dynamic;

import static calc.data.Dynamic.FIRST_LINK_LENGTH;
import static calc.data.Dynamic.SECOND_LINK_LENGTH;
import static calc.data.Constants.INCREMENT_PER_REVOLUTION;
import static java.lang.Math.*;

public class SCARADH {

    /**
     * Calculates forward kinematics.
     * If z = 0 then T3 = 0.
     *
     * @param angles initial angles in rad.
     * @return coordinates of the working point [x, y, z] in cm.
     *
     */
    public static double[] forwardKinematics(double[] angles){
        double T1 = angles[0];
        double T2 = angles[1];
        double T3 = angles[2];

        double x = FIRST_LINK_LENGTH * cos(T1)
                   + SECOND_LINK_LENGTH * cos(T2 + T1);

        double y = FIRST_LINK_LENGTH * sin(T1)
                   + SECOND_LINK_LENGTH * sin(T2 + T1);

        double z = T3 * INCREMENT_PER_REVOLUTION / Math.toRadians(360);

        return new double[]{x, y, z};
    }

    /**
     * Calculates inverse kinematics.
     *
     * @param coords desired coordinates of the working point [x, y, z] in cm.
     * @return angles in rad.
     */
    public static double[] inverseKinematics(double[] coords){
        double x = coords[0];
        double y = coords[1];
        double z = coords[2];

        double cT2 = (pow(x, 2) + pow(y, 2) - pow(FIRST_LINK_LENGTH, 2)
                    - pow(SECOND_LINK_LENGTH, 2))
                    / (2 * FIRST_LINK_LENGTH * SECOND_LINK_LENGTH);
        double sT2 = sqrt(1 - pow(cT2, 2));

        if (Dynamic.DIRECTION) {
            sT2 = -sT2;
        }
        double T2 = atan2(sT2, cT2);

        double cT1 = (x * (FIRST_LINK_LENGTH + SECOND_LINK_LENGTH * cT2)
                    + y * SECOND_LINK_LENGTH * sT2) / (pow(x, 2) + pow(y, 2));

        double sT1 = sqrt(1 - pow(cT1, 2));

        if (Dynamic.DIRECTION) {
            sT1 = -sT1;
        }
        double T1 = atan2(sT1, cT1);

        double T3 = z / (INCREMENT_PER_REVOLUTION / Math.toRadians(360));

        return new double[]{T1, T2, T3};
    }

    /**
     * Calculates angular velocities needed to follow given linear velocities.
     *
     * T2 SHOULD NOT be equal 0.
     *
     * @param angles given angles in rad.
     * @param lVelocities given linear velocities in cm/s.
     * @return angular velocities in rad/s.
     */
    public static double[] velocityKinematics (double[] angles,
                                               double[] lVelocities){
        double T1 = angles[0];
        double T2 = angles[1];
        double T3 = angles[2];

        double vx = lVelocities[0];
        double vy = lVelocities[1];
        double vz = lVelocities[2];

        if (T2 == 0) {
            T2 = 10E-6;
        }

        double w1 = 1 / (FIRST_LINK_LENGTH * SECOND_LINK_LENGTH * sin(T2))
                    * (vx * SECOND_LINK_LENGTH * cos(T1 + T2)
                        + vy * SECOND_LINK_LENGTH * sin(T1 + T2));

        double w2 = -1 / (FIRST_LINK_LENGTH * SECOND_LINK_LENGTH * sin(T2))
                    * (vx * (FIRST_LINK_LENGTH * cos(T1)
                        + SECOND_LINK_LENGTH * cos(T1 + T2))
                        + vy * (FIRST_LINK_LENGTH * sin(T1)
                        + SECOND_LINK_LENGTH * sin(T1 + T2)));

        double w3 = vz / (INCREMENT_PER_REVOLUTION / Math.toRadians(360));

        return new double[]{w1, w2, w3};
    }
}

package com.robotcontrol.parameters.dynamic;

import static com.robotcontrol.parameters.constant.PhysicalParameters.FIRST_LINK_SELECTIVE;
import static com.robotcontrol.parameters.constant.PhysicalParameters.SECOND_LINK_SELECTIVE;

public class Links {
    /**
     * Length of the first link in cm.
     */
    public static double FIRST_LINK_LENGTH = FIRST_LINK_SELECTIVE;
    /**
     * Length of the second link in cm.
     */
    public static double SECOND_LINK_LENGTH = SECOND_LINK_SELECTIVE;


    /**
     * Define in which direction arm's elbow will be directed.
     * \            /
     *  \         /
     *   \      /
     *    * or *
     *   /      \
     *  /        \
     * /          \
     */
    public static boolean DIRECTION = true;

}

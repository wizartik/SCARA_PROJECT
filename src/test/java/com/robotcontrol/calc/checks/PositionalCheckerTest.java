/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.robotcontrol.calc.checks;

import com.robotcontrol.calc.DHParameters.SCARADH;
import com.robotcontrol.exc.BoundsViolation;
import org.junit.Test;
import org.mockito.Mockito;

import static junit.framework.TestCase.assertEquals;


public class PositionalCheckerTest {
    @Test(expected = BoundsViolation.class)
    public void CheckPositionalPathStartBoundsTest() throws BoundsViolation {
        PositionalChecker.checkPositionalPath(new double[]{0,0,0}, new double[]{0,0,0});
    }

    @Test
    public void someTest(){
        assertEquals(1,1);

        SCARADH scaradhMock = Mockito.mock(SCARADH.class);

    }
}
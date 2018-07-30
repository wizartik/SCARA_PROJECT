package com.robotcontrol.tests;

import com.robotcontrol.calc.positionalControl.controllers.PositionalCotroller;
import com.robotcontrol.calc.positionalControl.entities.PositionalPath;
import com.robotcontrol.exc.BoundsViolation;
import com.robotcontrol.exc.ImpossibleToImplement;
import com.robotcontrol.exc.WrongExtension;
import com.robotcontrol.exc.WrongInputData;

import java.io.IOException;
import java.util.Date;

public class Main {
    public static void main(String[] args) throws WrongExtension, BoundsViolation, WrongInputData, IOException, ImpossibleToImplement {

        Date date = new Date();

            PositionalPath positionalPath = PositionalCotroller.moveToPointAng
                    (new double[]{1, 2, -1}, new
                            double[]{2, 4, 1});

        Date date1 = new Date();

        System.out.println(date1.getTime() - date.getTime());
    }
}

package tests;

import calc.contouringControl.Path;
import com.robotcontrol.exc.BoundsViolation;
import com.robotcontrol.exc.ImpossibleToImplement;
import com.robotcontrol.exc.WrongExtension;
import com.robotcontrol.exc.WrongInputData;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class MainTest {
    public static void main(String[] args) throws ImpossibleToImplement, WrongInputData, IOException, WrongExtension, BoundsViolation, InterruptedException {
        Date date = new Date();
        Path path = new Path(new File("D:\\GCode\\cartman.ngc"));
        long time  = new Date().getTime() - date.getTime();
        long[][] a = path.getSteppersPath();
        Thread.sleep(5000);
        for (int i = 0; i < a[0].length; i++) {
            System.out.println(a[0] + "    " + a[1] + "    " + a[2]);
        }

        System.out.println(time);
    }
}

package com.robotcontrol.gui.util;

import com.jfoenix.controls.JFXTextField;
import com.robotcontrol.util.Utility;

public class Util {
    public static double[] getFieldsArray(JFXTextField field1, JFXTextField field2, JFXTextField field3) {
        if (!isFieldsValid(field1, field2, field3)) {
            return null;
        }
        double x = Double.parseDouble(field1.getText());
        double y = Double.parseDouble(field2.getText());
        double z = Double.parseDouble(field3.getText());

        return new double[]{x, y, z};
    }

    private static boolean isFieldsValid(JFXTextField... field) {
        for (JFXTextField aField : field) {
            aField.setText(aField.getText().replaceAll(",", "."));
            if (!Utility.isNumeric(aField.getText())) {
                return false;
            }
        }
        return true;
    }}

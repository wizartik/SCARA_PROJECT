package com.robotcontrol.gui.util;

import com.jfoenix.controls.JFXTextField;
import com.robotcontrol.util.Utility;

public class Util {
    public static double[] getFieldsArray(JFXTextField field1, JFXTextField field2, JFXTextField field3) {
        if (!isFieldsValid(field1, field2, field3)) {
            return null;
        }

        double x = field1.getText().isEmpty() ? 0 : Double.parseDouble(field1.getText());
        double y = field2.getText().isEmpty() ? 0 : Double.parseDouble(field2.getText());
        double z = field3.getText().isEmpty() ? 0 : Double.parseDouble(field3.getText());

        return new double[]{x, y, z};
    }

    private static boolean isFieldsValid(JFXTextField... field) {

        boolean atLeastOneAssigned = false;

        for (JFXTextField aField : field) {
            aField.setText(aField.getText().replaceAll(",", "."));
            if (aField.getText().isEmpty()) {
                continue;
            }
            if (!Utility.isNumeric(aField.getText())) {
                return false;
            }

            double num = Double.parseDouble(aField.getText());
            atLeastOneAssigned |= num != 0;
        }
        return atLeastOneAssigned;
    }}

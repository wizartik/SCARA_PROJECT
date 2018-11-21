package com.robotcontrol.parameters.dynamic;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.File;

public class Misc {

    public static File CURRENT_FILE;

    public static StringProperty CURRENT_FILE_NAME = new SimpleStringProperty("file name");
}

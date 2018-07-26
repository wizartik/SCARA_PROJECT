package com.robotcontrol.util;

import calc.data.Constants;
import exc.WrongExtension;

import java.io.*;
import java.util.ArrayList;

public class FilesHandler {

    /**
     * Converts file into the list of strings of the lines in the file.
     *
     * @param file file that need to be converted.
     * @return ArrayList of lines in the file.
     * @throws WrongExtension thrown when file has forbidden extension or
     *                        doesn't has it at all.
     * @throws IOException    thrown if somehow there is IOException.
     */
    public static ArrayList<String> gCodeFileToList(File file) throws
            WrongExtension, IOException {
        ArrayList<String> GCode;

        String fileName = file.getName();

        int i = fileName.lastIndexOf('.');
        String extension;

        if (i > 0) {
            extension = fileName.substring(i + 1);
        } else {
            throw new WrongExtension("");
        }

        if (checkExtension(extension)) {
            GCode = fileToList(file);
        } else {
            throw new WrongExtension(extension);
        }

        return GCode;
    }

    /**
     * Checks if extension is right and allowed.
     *
     * @param extension filename extension that will be checked.
     * @return true if extension is allowed, false if not.
     */
    private static boolean checkExtension(String extension) {

        for (int i = 0; i < Constants.ALLOWED_FILENAME_EXTENSIONS.length; i++) {
            String allowed = Constants.ALLOWED_FILENAME_EXTENSIONS[i];
            if (extension.equalsIgnoreCase(allowed)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Simply converts all lines from file to list of Strings.
     *
     * @param file file that will be converted/
     * @return list of String lines.
     * @throws IOException if there is some errors.
     */
    private static ArrayList<String> fileToList(File file) throws IOException {

        ArrayList<String> lines = new ArrayList<>(countLines(file));

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                lines.add(sCurrentLine);
            }
        } catch (IOException e) {
            throw new IOException(e);
        }
        return lines;
    }


    /**
     * Counts all lines in the given file.
     *
     * @param file given file.
     * @return numbers of lines in the file.
     * @throws IOException if there is something wrong. (not program's fault).
     */
    private static int countLines(File file) throws IOException {
        try (InputStream is = new BufferedInputStream(new FileInputStream(file))) {
            byte[] c = new byte[1024];
            int count = 0;
            int readChars = 0;
            boolean empty = true;
            while ((readChars = is.read(c)) != -1) {
                empty = false;
                for (int i = 0; i < readChars; ++i) {
                    if (c[i] == '\n') {
                        ++count;
                    }
                }
            }
            return (count == 0 && !empty) ? 1 : count;
        }
    }

}

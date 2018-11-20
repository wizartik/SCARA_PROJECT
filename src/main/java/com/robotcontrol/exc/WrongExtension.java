package com.robotcontrol.exc;

import com.robotcontrol.parameters.constant.ConstUtil;

/**
 * This exception is thrown when file has wrong of forbidden extension.
 */
public class WrongExtension extends Exception {

    private String message;
    private String addMessage;

    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param fileExtension wrong extension that caused this exception.
     */
    public WrongExtension(String fileExtension) {

        if (fileExtension.isEmpty()){
            this.message = "This file has no extension, please pick another " +
                    "file.";
        } else {
            this.message = "This filename extension \"" + fileExtension + "\" " +
                    " is not a G code or is not supported.";
        }
        StringBuilder line = new StringBuilder();
        line.append("Allowed file name extensions:\n");
        for (int i = 0; i < ConstUtil.ALLOWED_FILENAME_EXTENSIONS.length; i++) {
            line.append(ConstUtil.ALLOWED_FILENAME_EXTENSIONS[i]);
            line.append("\n");
        }
        addMessage = line.toString();
    }

    @Override
    public String getMessage() {
        return message;
    }

    public String getAddMessage() {
        return addMessage;
    }
}

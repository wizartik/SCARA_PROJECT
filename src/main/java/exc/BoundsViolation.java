package exc;

/**
 * This exception is thrown if the point is out of the allowed bounds.
 */
public class BoundsViolation extends Exception{
    String gCode;

    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public BoundsViolation(String message, String gCode) {
        super(message);
        this.gCode = gCode;
    }

    public String getgCode() {
        return gCode;
    }
}

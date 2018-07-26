package exc;

/**
 * This exception describes situation when specified process cant be
 * implemented cause of the impossible initial data (too high
 * acceleration of velocity, etc).
 */
public class ImpossibleToImplement extends Exception{
    private String GCode;

    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     * @param GCode G code that has caused this exception.
     */
    public ImpossibleToImplement(String message, String GCode) {
        super(message);
        this.GCode = GCode;
    }

    public String getGCode() {
        return GCode;
    }
}

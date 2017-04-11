package ie.wit.application.exceptions;

/**
 * Created by joewe on 08/04/2017.
 */
public class NoUserLoggedInException extends Exception
{
    /**
     * Constructs a new exception with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public NoUserLoggedInException()
    {
        super("There is currently no user logged in");
    }

    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for                later retrieval by the {@link #getMessage()} method.
     */
    public NoUserLoggedInException(String message)
    {
        super(message);
    }
}

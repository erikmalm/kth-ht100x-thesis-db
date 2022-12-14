package se.erikmalm.exceptions;

public class AdhocDBException extends Exception{
    public AdhocDBException(String message) {
        super(message);
    }

    public AdhocDBException(String message, Throwable cause) {
        super(message, cause);
    }
}

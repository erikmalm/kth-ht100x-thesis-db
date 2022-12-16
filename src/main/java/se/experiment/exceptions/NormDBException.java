package se.experiment.exceptions;

public class NormDBException extends Exception {

    public NormDBException(String message) {
        super(message);
    }

    public NormDBException(String message, Throwable cause) {
        super(message, cause);
    }
}

package core.servicethree.util;

public class CabumException extends Exception {

    public CabumException(String message) {
        super(message);
    }

    public CabumException(String message, Throwable cause) {
        super(message, cause);
    }

    public CabumException(Throwable cause) {
        super(cause);
    }

    public CabumException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public CabumException() {
    }
}

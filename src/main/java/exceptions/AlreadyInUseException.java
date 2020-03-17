package exceptions;


public class AlreadyInUseException extends Exception {
    public AlreadyInUseException(String message) {
        super(message);
    }
}

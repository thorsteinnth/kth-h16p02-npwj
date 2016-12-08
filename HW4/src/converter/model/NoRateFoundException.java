package converter.model;

/**
 * Exception thrown when rate is not found in the database
 */
public class NoRateFoundException extends Exception{
    public NoRateFoundException(String message) {
        super(message);
    }
}

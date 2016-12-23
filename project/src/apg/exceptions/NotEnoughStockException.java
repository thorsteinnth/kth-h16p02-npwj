package apg.exceptions;

import javax.ejb.ApplicationException;

@ApplicationException(rollback=true)
public class NotEnoughStockException extends Exception{
    public NotEnoughStockException(String message) {
        super(message);
    }
}

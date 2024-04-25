package wms.rest.wms.exception;

/**
 * Custom exception class to handle cases where the is not enough stock
 * available for an operation.
 *
 * @author Mikkel Stavelie.
 * @version 1.0.
 */
public class NotEnoughStockException extends Exception{
    public NotEnoughStockException(String s) {
        super(s);
    }
}

package wms.rest.wms.exception;

/**
 * Custom exception to handle cases where a Customer already exists.
 *
 * @author Mikkel Stavelie.
 * @version 1.0.
 */
public class CustomerAlreadyExistsException extends Exception{
    public CustomerAlreadyExistsException(String message){
        super(message);
    }
}

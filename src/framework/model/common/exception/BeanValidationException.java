/**
 * 
 */
package framework.model.common.exception;


/**
 * @author 
 *
 */
@SuppressWarnings("serial")
public class BeanValidationException extends RuntimeException {

    /**
     * @param message
     * @param e
     */
    public BeanValidationException(String message, Exception e){
    	super(message, e);
    }
    
    /**
     * @param message
     */
    public BeanValidationException(String message){
    	super(message);
    }
    
    /**
     * @param e
     */
    public BeanValidationException(Exception e){
    	super(e);
    }
}

/**
 * 
 */
package framework.model.common.exception;


/**
 * @author 
 *
 */
@SuppressWarnings("serial")
public class ActionValidationException extends RuntimeException {

    /**
     * @param message
     * @param e
     */
    public ActionValidationException(String message, Exception e){
    	super(message, e);
    }
    
    /**
     * @param message
     */
    public ActionValidationException(String message){
    	super(message);
    }
    
    /**
     * @param e
     */
    public ActionValidationException(Exception e){
    	super(e);
    }
}

package framework.model.common.exception;

import javax.servlet.ServletException;

@SuppressWarnings("serial")
public class ControllerNotFoundException extends ServletException{

    /**
     * @param message
     * @param e
     */
    public ControllerNotFoundException(String message, Exception e){
    	super(message, e);
    }

    /**
     * @param message
     */
    public ControllerNotFoundException(String message){
    	super(message);
    }

    /**
     * @param e
     */
    public ControllerNotFoundException(Exception e){
    	super(e);
    }
}

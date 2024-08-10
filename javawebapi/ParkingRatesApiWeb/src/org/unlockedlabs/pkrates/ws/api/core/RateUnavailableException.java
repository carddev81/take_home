/**
 * 
 */
package org.unlockedlabs.pkrates.ws.api.core;

/**
 * Class used for unavailable rate exceptions based up spec.
 * 
 * <h2>Current Unavailable Scenarios</h2>
 * <ul>
 *  <li>User supplied date/time spans more than one rate.</li>
 *  <li>User supplied date/time range spans more than one day.</li>
 *  <li>Rate does not conmpletely encapsulate a date/time range.</li>
 * </ul>
 * @author Richard Salas
 * @version 1.0.0
 */
public class RateUnavailableException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Creates an instance of the RateUnavailableException class.
     */
    public RateUnavailableException() {
    }//end constructor

    /**
     * Creates an instance of the RateUnavailableException class using the given message.
     * @param message the detail message.
     */
    public RateUnavailableException(String message) {
        super(message);
    }//end constructor

    /**
     * Creates an instance of the RateUnavailableException class using the given cause.
     * @param cause the cause of the exception
     */
    public RateUnavailableException(Throwable cause) {
        super(cause);
    }//end constructor

    /**
     * Creates an instance of the RateUnavailableException class using the given message and cause.
     * 
     * @param message the detail message.
     * @param cause the cause of the exception
     */
    public RateUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }//end constructor

    /**
     * Creates an instance of the RateUnavailableException class using the given message, cause, enableSuppression, and writableStackTrace.
     * 
     * @param message  the detail message.
     * @param cause the cause of the exception
     * @param enableSuppression whether or not suppression is enabled or disabled
     * @param writableStackTrace whether or not the stack trace should be writable
     */
    public RateUnavailableException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }//end constructor

}//end class

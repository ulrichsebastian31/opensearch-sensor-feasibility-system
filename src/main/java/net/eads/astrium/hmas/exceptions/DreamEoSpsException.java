/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eads.astrium.hmas.exceptions;

import javax.xml.ws.WebFault;

/**
 *
 * @author re-sulrich
 */
@WebFault(name = "Exception", targetNamespace = "http://www.opengis.net/ows/1.1")
public class DreamEoSpsException extends Exception {
    
    protected static final long serialVersionUID = -6279041538977056569L;
    protected net.opengis.ows.x11.ExceptionDocument exception;
    
    
    public DreamEoSpsException() {
        super();
    }
    
    public DreamEoSpsException(String message) {
        super(message);
    }
    
    public DreamEoSpsException(String message, Throwable cause) {
        super(message, cause);
    }

    public DreamEoSpsException(String message, net.opengis.ows.x11.ExceptionDocument exception) {
        super(message);
        this.exception = exception;
    }

    public DreamEoSpsException(String message, net.opengis.ows.x11.ExceptionDocument exception, Throwable cause) {
        super(message, cause);
        this.exception = exception;
    }

    public net.opengis.ows.x11.ExceptionDocument getFaultInfo() {
        return this.exception;
    }
}

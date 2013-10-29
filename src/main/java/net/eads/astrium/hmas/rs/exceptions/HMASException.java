/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eads.astrium.hmas.rs.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import net.opengis.ows.x11.ExceptionDocument;

/**
 *
 * @author re-sulrich
 */
public class HMASException extends WebApplicationException {
    
    
    HMASException(Response response)
    {
        super(response);
    }
    
     /**
      * Format an exception message 
      * @param message
      * @param type
      * @return 
      */
     public static String getExceptionText(String message, String type)
     {
         String exceptionText = null;
         
         if (type.contains("xml")) {
             exceptionText = getXMLOWSException(message);
         }
         if (type.equals("text/plain")) {
             exceptionText = message;
         }
         
         return exceptionText;
     }
     
     public static String getXMLOWSException(String message) {
        
        ExceptionDocument exception = ExceptionDocument.Factory.newInstance();
        exception.addNewException().addExceptionText(message);
        
        return exception.xmlText();
     }
}

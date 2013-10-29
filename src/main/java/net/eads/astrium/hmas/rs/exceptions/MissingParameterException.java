/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eads.astrium.hmas.rs.exceptions;

import javax.ws.rs.core.Response;
import net.eads.astrium.dream.util.FormatHandler;

/**
 *
 * @author re-sulrich
 */
public class MissingParameterException extends HMASException {
    
    /**
      * Creates a 400 request
     */
     public MissingParameterException() {
         super(Response.status(Response.Status.BAD_REQUEST).entity("Missing Parameter Exception").build());
     }

     /**
      * Creates a 400 request with information in text/plain
      * @param message the String that is the entity of the 400 response.
      */
     public MissingParameterException(String message) {
         super(Response.status(Response.Status.BAD_REQUEST).entity(message).type("text/plain").build());
     }
     /**
      * Creates a 400 request with information in the given MIME type
      * @param message the String that is the entity of the 400 response.
      * @param type the MIME encoding type of the Exception
      */
     public MissingParameterException(String message, String type) {
         super(
                 Response.status(Response.Status.BAD_REQUEST)
                        .entity(getExceptionText(message,type))
                        .type(FormatHandler.isSupportedType(type))
                        .build());
     }
     /**
      * Creates a 400 request with information in the given MIME type
      * @param message the String that is the entity of the 400 response.
      * @param type the MIME encoding type of the Exception
      */
     public MissingParameterException(String message, String[] outputTypes) {
         super(
                 Response.status(Response.Status.BAD_REQUEST)
                        .entity(getExceptionText(message,FormatHandler.getFirstSupportedType(outputTypes)))
                        .type(FormatHandler.getFirstSupportedType(outputTypes))
                        .build());
     }
     
}

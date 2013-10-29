/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eads.astrium.hmas.rs.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.*;

/**
 *
 * @author re-sulrich
 */
public class RequestNotFoundException extends HMASException {
    
    /**
      * Create a HTTP 401 (Unauthorized) exception.
     */
     public RequestNotFoundException() {
         super(Response.status(Status.NOT_FOUND).build());
     }

     /**
      * Create a HTTP 404 (Not Found) exception.
      * @param message the String that is the entity of the 404 response.
      */
     public RequestNotFoundException(String request) {
         super(
                 Response.status(Status.NOT_FOUND)
                 .entity("Request " + request + " not found.")
                 .type("text/plain")
                 .build()
             );
     }
}

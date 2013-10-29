/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eads.astrium.hmas.rs.exceptions;

import javax.ws.rs.core.Response;

/**
 *
 * @author re-sulrich
 */
public class ServiceNotFoundException extends HMASException {
    
    /**
      * Create a HTTP 401 (Unauthorized) exception.
     */
     public ServiceNotFoundException() {
         super(Response.status(Response.Status.NOT_FOUND).build());
     }

     /**
      * Create a HTTP 404 (Not Found) exception.
      * @param message the String that is the entity of the 404 response.
      */
     public ServiceNotFoundException(String service) {
         super(
                 Response.status(Response.Status.NOT_FOUND)
                 .entity("Service " + service + " not found.")
                 .type("text/plain")
                 .build()
             );
     }
}

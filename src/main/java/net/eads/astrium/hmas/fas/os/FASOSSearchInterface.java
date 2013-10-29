/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eads.astrium.hmas.fas.os;

import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.multipart.FormDataMultiPart;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import net.eads.astrium.hmas.exceptions.GetFeasibilityFault;
import net.eads.astrium.hmas.fas.FASWorker;
import net.eads.astrium.hmas.fas.configuration.exceptions.FASNotFoundException;
import net.eads.astrium.hmas.fas.configuration.exceptions.SensorNotFoundException;
import net.eads.astrium.hmas.fas.rs.FASWebService;
import net.eads.astrium.hmas.rs.exceptions.ServiceNotFoundException;
import net.opengis.eosps.x20.GetFeasibilityDocument;
import net.opengis.eosps.x20.GetFeasibilityResponseDocument;

/**
 *
 * @author re-sulrich
 */
@Path("fas/os/search")
public class FASOSSearchInterface {

    /**
     * Map containing the URL parameters
     */ 
    @Context 
    volatile UriInfo ui;
    
    @Context
    volatile HttpContext httpContext;
    
//    private URI admin = httpContext.getUriInfo().getBaseUri();//.resolve("fasadmin/");
    
    
//    @POST
//    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @GET
    public Response FASAdminPostRequestParser() throws GetFeasibilityFault, ParseException, SensorNotFoundException
    {
        Response response = null;
        
        
        try {
            String fasId = "Sentinel1";
            
            FASWorker worker = null;
            try {
                worker = new FASWorker(fasId);
            } catch (FASNotFoundException ex) {
                Logger.getLogger(FASWebService.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (worker == null)
            {
                throw new ServiceNotFoundException(fasId);
            }
            
            
            
            if (worker == null)
            {
                throw new ServiceNotFoundException(fasId);
            }

            GetFeasibilityDocument request = OSFeasibilityParser.parseGetFeasibilityGetMethod(
                    ui.getQueryParameters()                                     //Request
                 );
            
            GetFeasibilityResponseDocument feasibility = worker.getFeasibility(request);
            
            org.w3.x2005.atom.FeedDocument createResponse = OSFeasibilityResponseCreator.createResponse(feasibility, ui.getBaseUri().toString());
            
            response = Response.ok(createResponse.xmlText(), MediaType.APPLICATION_ATOM_XML).build();
            
            
        } catch (GetFeasibilityFault ex) {
            Logger.getLogger(FASOSSearchInterface.class.getName()).log(Level.SEVERE, null, ex);
            
            response = Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .type(MediaType.APPLICATION_XML)
                    .entity("GetFeasibilityFault : " + ex.getMessage()
                            + "\n" + "Contact the server administrator."
                        )
                    .build();
            
        }
        
        //Response.status(Response.Status.SERVICE_UNAVAILABLE).build()
        return response;
    }
}
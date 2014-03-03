/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eads.astrium.hmas.fas.os;

import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.multipart.FormDataMultiPart;
import java.io.IOException;
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
import net.eads.astrium.hmas.fas.conf.os.DescriptionDocumentLoader;

/**
 *
 * @author re-sulrich
 */
@Path("fas/os/description")
public class FASOSDescriptionInterface {

    
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
    public Response FASAdminPostRequestParser()
    {
        System.out.println("Java library path : " + System.getProperty("java.library.path") );
        
        
        Response response = null;
        DescriptionDocumentLoader loader = new DescriptionDocumentLoader("Sentinel1");
        
        String content;
        try {
            content = loader.getContent();
            response = Response.ok(content, "application/opensearchdescription+xml").build();
        } catch (IOException ex) {
            Logger.getLogger(FASOSDescriptionInterface.class.getName()).log(Level.SEVERE, null, ex);
            
            response = Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .type(MediaType.APPLICATION_XML)
                    .entity("Error parsing MXL : " + ex.getMessage())
                    .build();
        }
        
        return response;
    }
}
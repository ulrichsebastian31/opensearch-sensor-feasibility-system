/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eads.astrium.hmas.fas.rs.parsers;

import javax.ws.rs.core.MultivaluedMap;
import net.eads.astrium.hmas.rs.exceptions.MissingParameterException;

import net.opengis.swes.x21.ExtensibleRequestDocument;
import net.opengis.swes.x21.ExtensibleRequestType;

/**
 *
 * @author re-sulrich
 */
public class ExtensibleRequestParser {
    
    public static ExtensibleRequestDocument getExtensibleRequest(MultivaluedMap<String, String> params)
            throws MissingParameterException
    {
        String version = params.getFirst("version");
        String service = params.getFirst("service");
        String acceptFormat = params.getFirst("acceptFormat");
        
        ExtensibleRequestDocument doc = ExtensibleRequestDocument.Factory.newInstance();
        ExtensibleRequestType extensibleRequest = doc.addNewExtensibleRequest();
        
        extensibleRequest.setVersion(version);
        extensibleRequest.setAcceptFormat(acceptFormat);
        
        if (service != null)
            extensibleRequest.setService(service);
        else
        {
            if (acceptFormat != null)
                throw new MissingParameterException(
                        "Parameter service is missing. This parameter must be set.", 
                        acceptFormat
                    );
            else
                throw new MissingParameterException(
                        "Parameter service is missing. This parameter must be set."
                    );
        }
        
        return doc;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eads.astrium.hmas.fas.rs.parsers;

import javax.ws.rs.core.MultivaluedMap;
import net.eads.astrium.hmas.rs.exceptions.MissingParameterException;
import net.opengis.eosps.x20.DescribeTaskingDocument;
import net.opengis.sps.x21.DescribeTaskingType;
import net.opengis.swes.x21.ExtensibleRequestDocument;
import net.opengis.swes.x21.ExtensibleRequestType;

/**
 *
 * @author re-sulrich
 */
public class DescribeTaskingParser extends DreamRestGetParser{
    
    public DescribeTaskingDocument createXMLFromGetRequest(MultivaluedMap<String, String> params)
            throws MissingParameterException
    {
        //Parses the parameters that are the same for all requests (except GetCapabilities)
        ExtensibleRequestDocument extensibleRequestDocument = ExtensibleRequestParser.getExtensibleRequest(params);
        ExtensibleRequestType extensibleRequest = extensibleRequestDocument.getExtensibleRequest();
        
        //Parses DescribeTaskingDocument parameters
        String procedure = params.getFirst("procedure");
        
        //Create XML request
        DescribeTaskingDocument doc = DescribeTaskingDocument.Factory.newInstance();
        DescribeTaskingType desc = doc.addNewDescribeTasking();
        
        //Add extensibleRequest parameters
        desc.setService(extensibleRequest.getService());
        desc.setVersion(extensibleRequest.getVersion());
        desc.setAcceptFormat(extensibleRequest.getAcceptFormat());
        
        //Add DescribeTaskingDocument parameters
        desc.setProcedure(procedure);
        
        return doc;
    }
}
        

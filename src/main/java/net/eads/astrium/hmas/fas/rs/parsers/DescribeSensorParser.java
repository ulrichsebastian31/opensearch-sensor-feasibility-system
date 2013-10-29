/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eads.astrium.hmas.fas.rs.parsers;

import java.util.List;
import javax.ws.rs.core.MultivaluedMap;
import net.eads.astrium.hmas.rs.exceptions.MissingParameterException;
import net.opengis.eosps.x20.DescribeSensorDocument;
import net.opengis.swes.x21.DescribeSensorType;
import net.opengis.swes.x21.ExtensibleRequestDocument;
import net.opengis.swes.x21.ExtensibleRequestType;

/**
 *
 * @author re-sulrich
 */
public class DescribeSensorParser {
    
    public DescribeSensorDocument createXMLFromGetRequest( MultivaluedMap<String, String> params)
            throws MissingParameterException
    {
        //Parses the parameters that are the same for all requests (except GetCapabilities)
        ExtensibleRequestDocument extensibleRequestDocument = ExtensibleRequestParser.getExtensibleRequest(params);
        ExtensibleRequestType extensibleRequest = extensibleRequestDocument.getExtensibleRequest();
        
        //Parses DescribeSensorDocument parameters
        String procedure = params.getFirst("procedure");
        String procedureDescriptionFormat = params.getFirst("procedureDescriptionFormat");
        
        //Create XML request
        DescribeSensorDocument doc = DescribeSensorDocument.Factory.newInstance();
        DescribeSensorType describeSensor = doc.addNewDescribeSensor();
        
        //Add extensibleRequest parameters
        describeSensor.setService(extensibleRequest.getService());
        describeSensor.setVersion(extensibleRequest.getVersion());
        describeSensor.setAcceptFormat(extensibleRequest.getAcceptFormat());
        
        //Add DescribeSensorDocument parameters
        describeSensor.setProcedureDescriptionFormat(procedureDescriptionFormat);
        describeSensor.setProcedure(procedure);
        
        //TODO: valid time
        
        return doc;
    }
}

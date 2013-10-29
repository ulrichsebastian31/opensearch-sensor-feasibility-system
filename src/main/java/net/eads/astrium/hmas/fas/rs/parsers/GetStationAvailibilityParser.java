/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eads.astrium.hmas.fas.rs.parsers;

import javax.ws.rs.core.MultivaluedMap;
import net.eads.astrium.hmas.rs.exceptions.MissingParameterException;
import net.opengis.eosps.x20.GetStationAvailabilityDocument;
import net.opengis.eosps.x20.GetStationAvailabilityType;
import net.opengis.gml.x32.TimePeriodType;
import net.opengis.swes.x21.ExtensibleRequestDocument;
import net.opengis.swes.x21.ExtensibleRequestType;

/**
 *
 * @author re-sulrich
 */
public class GetStationAvailibilityParser {
    
    public GetStationAvailabilityDocument createXMLFromGetRequest(MultivaluedMap<String, String> params)
            throws MissingParameterException
    {
        //Parses the parameters that are the same for all requests (except GetCapabilities)
        ExtensibleRequestDocument extensibleRequestDocument = ExtensibleRequestParser.getExtensibleRequest(params);
        ExtensibleRequestType extensibleRequest = extensibleRequestDocument.getExtensibleRequest();
        
        //Parses GetSensorAvailabilityDocument parameters
        String station = params.getFirst("stationId");
        String begin = params.getFirst("begin");
        String end = params.getFirst("end");
        
        //Create XML request
        GetStationAvailabilityDocument doc = GetStationAvailabilityDocument.Factory.newInstance();
        GetStationAvailabilityType desc = doc.addNewGetStationAvailability();
        
        //Add extensibleRequest parameters
        desc.setService(extensibleRequest.getService());
        desc.setVersion(extensibleRequest.getVersion());
        desc.setAcceptFormat(extensibleRequest.getAcceptFormat());
        
        //Add GetSensorAvailabilityDocument parameters
        desc.setStation(station);
        TimePeriodType timePeriod = desc.addNewRequestPeriod().addNewTimePeriod();
        timePeriod.addNewBeginPosition().setStringValue(begin);
        timePeriod.addNewEndPosition().setStringValue(end);
        
        return doc;
    }
}

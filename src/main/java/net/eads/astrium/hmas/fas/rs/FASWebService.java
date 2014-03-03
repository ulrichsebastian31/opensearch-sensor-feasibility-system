/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eads.astrium.hmas.fas.rs;

import com.sun.jersey.api.core.HttpContext;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.core.Response;

import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;
import net.eads.astrium.dream.util.FormatHandler;
import net.eads.astrium.hmas.exceptions.DreamEoSpsException;
import net.eads.astrium.hmas.fas.FASWorker;
import net.eads.astrium.hmas.fas.conf.exceptions.FASNotFoundException;
import net.eads.astrium.hmas.rs.exceptions.RequestNotFoundException;
import net.eads.astrium.hmas.rs.exceptions.ServiceNotFoundException;
import net.eads.astrium.hmas.fas.rs.parsers.DescribeSensorParser;
import net.eads.astrium.hmas.fas.rs.parsers.DescribeTaskingParser;
import net.eads.astrium.hmas.fas.rs.parsers.GetCapabilitiesParser;
import net.eads.astrium.hmas.fas.rs.parsers.GetSensorAvailibilityParser;
import net.eads.astrium.hmas.fas.rs.parsers.GetStationAvailibilityParser;
import net.eads.astrium.hmas.rs.exceptions.MissingParameterException;
import net.eads.astrium.hmas.rs.exceptions.XMLParsingException;
import net.opengis.eosps.x20.DescribeSensorDocument;
import net.opengis.eosps.x20.DescribeSensorResponseDocument;
import net.opengis.eosps.x20.DescribeTaskingDocument;
import net.opengis.eosps.x20.DescribeTaskingResponseDocument;
import net.opengis.eosps.x20.GetSensorAvailabilityDocument;
import net.opengis.eosps.x20.GetSensorAvailabilityResponseDocument;
import net.opengis.eosps.x20.GetStationAvailabilityDocument;
import net.opengis.eosps.x20.GetStationAvailabilityResponseDocument;
import net.opengis.sps.x21.CapabilitiesDocument;
import net.opengis.sps.x21.GetCapabilitiesDocument;
import net.opengis.sps.x21.GetCapabilitiesType;

/**
 *
 * @author re-sulrich
 */
@Path("fas/{serviceId}")
public class FASWebService {

    
    
    
    /**
     * Map containing the URL parameters
     * through ui.getQueryParameters() and ui.getPathParameters()
     * Will be filled in automatically by the service when a request is received
     */ 
    @Context 
    public volatile UriInfo ui;
    
    @Context
    public volatile HttpContext httpContext;

    @GET
    public Response SpsXmlGetMethodsParser()
            throws RequestNotFoundException, MissingParameterException
    {
        return handleRequest(null);
    }
    
    @POST
    @Consumes("*/xml")
    public Response SpsXmlPostMethodsParser(String xmlRequest)
            throws RequestNotFoundException, MissingParameterException, XMLParsingException
    {
        return handleRequest(xmlRequest);
    }
    
    protected Response handleRequest(
            String postRequest      //Get parameters available through ui.getQueryParameters();
            ) 
            throws RequestNotFoundException, ServiceNotFoundException
    {
        Response response = null;
        
        MultivaluedMap<String, String> params = ui.getQueryParameters();
        params.putAll(ui.getPathParameters());
        
        String serviceId = params.getFirst("serviceId");
        String request = params.getFirst("request");
        
        System.out.println("Request :"
                + "\nServiceId is : " + serviceId + ""
                + "\nRequest is : " + request);
        
        FASWorker worker = null;
        try {
            worker = new FASWorker(serviceId);
        } catch (FASNotFoundException ex) {
            Logger.getLogger(FASWebService.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (worker == null)
        {
            throw new ServiceNotFoundException(serviceId);
        }
        
        try {
        
        if ("getcapabilities".equalsIgnoreCase(request))
        {
            GetCapabilitiesDocument doc = GetCapabilitiesParser.createXMLGetCapabilities(params);
            GetCapabilitiesType getCaps = doc.getGetCapabilities2();
            
            CapabilitiesDocument caps = worker.getCapabilities(doc);
            String[] formats = getCaps.getAcceptFormats().getOutputFormatArray();
            System.out.println("Formats size : " + formats.length);
            
            String format = null;
            if (formats.length > 0) {
                
                System.out.println("Format[0] : " + formats[0]);
                format = FormatHandler.getFirstSupportedType(
                               getCaps.getAcceptFormats().getOutputFormatArray()
                           );
            }
            else {
                format = "text/xml";
            }
            System.out.println("Format  : " + format);
            
            String responseText = null;
            if (format.contains("xml"))
                responseText = caps.xmlText();
            else
            {
                if (format.contains("json"))
                {
                    responseText = GetCapabilitiesParser.createJSONCapabilitiesResponse(caps.xmlText());
                }
                else  //if (format.equals("text/plain"))
                    responseText = "This page gives the capabilities of this service "
                            + "\n\rif requested in /xml or application/json formats.";
            
            }
            response = Response.ok(responseText, format).build();
        }
        
        if ("describetasking".equalsIgnoreCase(request))
        {
            DescribeTaskingDocument describeTasking = new DescribeTaskingParser().createXMLFromGetRequest(params);
            DescribeTaskingResponseDocument tasking = worker.describeTasking(describeTasking);
            
            String format = describeTasking.getExtensibleRequest().getAcceptFormat();
            
            String responseText = null;
            if (format.contains("xml"))
                responseText = tasking.xmlText();
            //if (format.equals("text/plain"))
            else
                responseText = "This page describe the tasking request to be used "
                        + "\n\rto task a given sensor if requested"
                        + "\n\r in /xml or application/json formats.";
            
            response = Response.ok(responseText, format).build();
        }
        
        if ("describesensor".equalsIgnoreCase(request))
        {
            DescribeSensorDocument describeSensor = new DescribeSensorParser().createXMLFromGetRequest(params);
            DescribeSensorResponseDocument sensorDescription = worker.describeSensor(describeSensor);
            
            String format = describeSensor.getExtensibleRequest().getAcceptFormat();
            
            String responseText = null;
            if (format.contains("xml"))
                responseText = sensorDescription.xmlText();
            
            //if (format.equals("text/plain"))
            else
                responseText = "This page gives a description of a given sensor"
                        + "\n\rif requested in /xml or application/json formats.";
            
            response = Response.ok(responseText, format).build();
        }
        
        if ("getsensoravailibility".equalsIgnoreCase(request))
        {
            GetSensorAvailabilityDocument getSensorAvailability = new GetSensorAvailibilityParser().createXMLFromGetRequest(params);
            GetSensorAvailabilityResponseDocument sensorAvailibilities = worker.getSensorAvailability(getSensorAvailability);
            
            String format = getSensorAvailability.getExtensibleRequest().getAcceptFormat();
            
            String responseText = null;
            if (format.contains("xml"))
                responseText = sensorAvailibilities.xmlText();
            //if (format.equals("text/plain"))
            else
                responseText = "This page gives the availibility of a sensor "
                        + "\n\rif requested in /xml or application/json formats.";
            
            response = Response.ok(responseText, format).build();
        }
        
        if ("getstationavailibility".equalsIgnoreCase(request))
        {
            GetStationAvailabilityDocument getStationAvailability = new GetStationAvailibilityParser().createXMLFromGetRequest(params);
            GetStationAvailabilityResponseDocument stationAvailibilities = worker.getStationAvailability(getStationAvailability);
            
            String format = getStationAvailability.getExtensibleRequest().getAcceptFormat();
            
            String responseText = null;
            if (format.contains("xml"))
                responseText = stationAvailibilities.xmlText();
            //if (format.equals("text/plain"))
            else
                responseText = "This page gives the availibility of a ground station "
                        + "\n\rif requested in /xml or application/json formats.";
            
            response = Response.ok(responseText, format).build();
        }
        
        } catch (DreamEoSpsException e) {
            response = Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .type(MediaType.APPLICATION_XML)
                    .entity(e.getFaultInfo().xmlText())
                    .build();
        }
        
        if (response == null)
            throw new RequestNotFoundException(
                    "Request " + request + " was not found on this service."
                );
        
        return response;
    }
}

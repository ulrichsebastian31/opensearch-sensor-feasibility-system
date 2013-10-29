package net.eads.astrium.hmas.fas.operations;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.eads.astrium.hmas.conf.exceptions.ConfFileNotFoundException;
import net.eads.astrium.hmas.conf.exceptions.ConfFileParsingException;
import net.eads.astrium.hmas.exceptions.DescribeSensorFault;
import net.eads.astrium.hmas.fas.configuration.FASConfFolder;
import net.eads.astrium.hmas.operations.DreamEOSPSOperation;

import net.opengis.eosps.x20.DescribeSensorDocument;
import net.opengis.eosps.x20.DescribeSensorResponseDocument;
import net.opengis.sensorML.x102.SensorMLDocument;
import net.opengis.sensorML.x102.SensorMLDocument.SensorML;
import net.opengis.swes.x21.DescribeSensorResponseType;
import net.opengis.swes.x21.DescribeSensorType;
import net.opengis.swes.x21.SensorDescriptionType;
import org.apache.xmlbeans.XmlException;


/**
 * @file DescribeSensorOperation.java
 * @author Sebastian Ulrich <sebastian_ulrich@hotmail.fr>
 * @version 1.0
 * 
 * @section LICENSE
 * 
 *          To be defined
 * 
 * @section DESCRIPTION
 * 
 *          The DescribeSensor Operation gives information about the way the sensor can be tasked.
 *          
 *          This class is not currently working as some part of the response creation are not yet available.
 *      
 *          Plus, a lot of parameters should be found in a config file and not hard-coded.
 */
public class DescribeSensorOperation extends DreamEOSPSOperation<FASConfFolder,DescribeSensorDocument,DescribeSensorResponseDocument, DescribeSensorFault> {

    /**
     * 
     * @param request
     */
    public DescribeSensorOperation(FASConfFolder confFolder, DescribeSensorDocument request){

            super(confFolder, request);
    }

    @Override
    public void validRequest() throws DescribeSensorFault {
    }

    @Override
    public void executeRequest() throws DescribeSensorFault {

        this.validRequest();
        
        DescribeSensorType req = this.getRequest().getDescribeSensor();
        String sensorId = req.getProcedure();
        String sensorDescriptionFormat = req.getProcedureDescriptionFormat();
        
        DescribeSensorResponseDocument responseDocument = DescribeSensorResponseDocument.Factory.newInstance();
        
        DescribeSensorResponseType resp = responseDocument.addNewDescribeSensorResponse2();
        resp.setProcedureDescriptionFormat("http://www.opengis.net/sensorml/1.0.2");
        try {
            SensorDescriptionType sensor = resp.addNewDescription().addNewSensorDescription();
            SensorDescriptionType.Data data = sensor.addNewData();
            
            data.set(loadSensorMLDescription(sensorId));
            
            
        } catch (IOException ex) {
            Logger.getLogger(DescribeSensorOperation.class.getName()).log(Level.SEVERE, null, ex);
            throw new DescribeSensorFault("IOException : parsing " + sensorId + " description.", ex);
            
        } catch (ConfFileNotFoundException ex) {
            Logger.getLogger(DescribeSensorOperation.class.getName()).log(Level.SEVERE, null, ex);
            throw new DescribeSensorFault("ConfFileNotExistsException : parsing " + sensorId + " description.", ex);
            
        } catch (ConfFileParsingException ex) {
            Logger.getLogger(DescribeSensorOperation.class.getName()).log(Level.SEVERE, null, ex);
            throw new DescribeSensorFault("ConfFileParsingException : parsing " + sensorId + " description.", ex);
            
        }
        
        //Old description swe
        //SensorDescriptionType description = response.addNewDescription().addNewSensorDescription();
        
        this.setResponse(responseDocument);
    }

    public SensorMLDocument loadSensorMLDescription(String sensorId) 
            throws IOException, ConfFileNotFoundException, ConfFileParsingException
    {
        SensorMLDocument doc = null;
        
        String content = this.getServiceConfiguration().getSensorDescription(sensorId, "sensorML");
        
        System.out.println("" + content);
        
        try {
            doc = SensorMLDocument.Factory.parse(content);
        } catch (XmlException ex) {
            Logger.getLogger(DescribeSensorOperation.class.getName()).log(Level.SEVERE, null, ex);
            
            throw new ConfFileParsingException(
                    this.getServiceConfiguration().getInstanceId() + File.separator + 
                    sensorId + File.separator + 
                    "sensorML");
        }
        
        return doc;
    }
}
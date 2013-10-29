/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eads.astrium.hmas.fas;

import java.io.IOException;
import net.eads.astrium.hmas.fas.configuration.FASConfFolder;
import net.eads.astrium.hmas.fas.configuration.exceptions.FASAlreadyExistsException;
import net.eads.astrium.hmas.fas.configuration.exceptions.FASNotFoundException;
import net.eads.astrium.hmas.exceptions.DescribeSensorFault;
import net.eads.astrium.hmas.exceptions.DescribeTaskingFault;
import net.eads.astrium.hmas.exceptions.GetCapabilitiesFault;
import net.eads.astrium.hmas.exceptions.GetFeasibilityFault;
import net.eads.astrium.hmas.exceptions.GetSensorAvailabilityFault;
import net.eads.astrium.hmas.exceptions.GetStationAvailabilityFault;
import net.eads.astrium.hmas.fas.operations.DescribeSensorOperation;
import net.eads.astrium.hmas.fas.operations.DescribeTaskingOperation;
import net.eads.astrium.hmas.fas.operations.GetCapabilitiesOperation;
import net.eads.astrium.hmas.fas.operations.GetFeasibilityOperation;
import net.eads.astrium.hmas.fas.operations.GetSensorAvailabilityOperation;
import net.eads.astrium.hmas.fas.operations.GetStationAvailabilityOperation;
import net.opengis.eosps.x20.DescribeSensorDocument;
import net.opengis.eosps.x20.DescribeSensorResponseDocument;
import net.opengis.eosps.x20.DescribeTaskingDocument;
import net.opengis.eosps.x20.DescribeTaskingResponseDocument;
import net.opengis.eosps.x20.GetFeasibilityDocument;
import net.opengis.eosps.x20.GetFeasibilityResponseDocument;
import net.opengis.eosps.x20.GetSensorAvailabilityDocument;
import net.opengis.eosps.x20.GetSensorAvailabilityResponseDocument;
import net.opengis.eosps.x20.GetStationAvailabilityDocument;
import net.opengis.eosps.x20.GetStationAvailabilityResponseDocument;
import net.opengis.sps.x21.CapabilitiesDocument;
import net.opengis.sps.x21.GetCapabilitiesDocument;

/**
 *
 * @author re-sulrich
 */
public class FASWorker {
    
    private FASConfFolder confFolder;

    public FASConfFolder getConfFolder() {
        return confFolder;
    }

    public FASWorker(String instanceId) throws FASNotFoundException
    {
        confFolder = FASConfFolder.loadFASConfFolder(instanceId);
    }
    
    public FASWorker(String instanceId, String name, String serverBaseURI) 
            throws FASAlreadyExistsException, IOException {
        
        confFolder = FASConfFolder.createFASConfFolder(instanceId, name, serverBaseURI);
    }
    
    public void destroy() {
        if (confFolder != null && confFolder.getConfFolder().exists())
        {
            confFolder.getConfFolder().delete();
        }
    }

    public CapabilitiesDocument getCapabilities(GetCapabilitiesDocument request) throws GetCapabilitiesFault {
        GetCapabilitiesOperation operation = new GetCapabilitiesOperation(confFolder, request);
        
        operation.executeRequest();
        
        return operation.getResponse();
    }

    public DescribeSensorResponseDocument describeSensor(DescribeSensorDocument request) throws DescribeSensorFault {
        
        DescribeSensorOperation operation = new DescribeSensorOperation(confFolder, request);

        operation.executeRequest();
        
        return operation.getResponse();
    }

    public DescribeTaskingResponseDocument describeTasking(DescribeTaskingDocument request) throws DescribeTaskingFault {
        
        DescribeTaskingOperation operation = new DescribeTaskingOperation(confFolder, request);
        
        operation.executeRequest();
        
        return operation.getResponse();
    }

    public GetSensorAvailabilityResponseDocument getSensorAvailability(GetSensorAvailabilityDocument request) throws GetSensorAvailabilityFault {
        GetSensorAvailabilityOperation operation = new GetSensorAvailabilityOperation(confFolder, request);
        
        operation.executeRequest();
        
        return operation.getResponse();
    }

    public GetStationAvailabilityResponseDocument getStationAvailability(GetStationAvailabilityDocument request) throws GetStationAvailabilityFault {
        GetStationAvailabilityOperation operation = new GetStationAvailabilityOperation(confFolder, request);
        
        operation.executeRequest();
        
        return operation.getResponse();
    }

    public GetFeasibilityResponseDocument getFeasibility(GetFeasibilityDocument request) throws GetFeasibilityFault {
        GetFeasibilityOperation operation = new GetFeasibilityOperation(confFolder, request);
        
        operation.executeRequest();
        
        return operation.getResponse();
    }
    
}

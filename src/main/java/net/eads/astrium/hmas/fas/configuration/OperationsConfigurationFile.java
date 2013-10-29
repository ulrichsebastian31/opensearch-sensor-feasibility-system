/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eads.astrium.hmas.fas.configuration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import net.eads.astrium.hmas.conf.DreamConfFileHandler;
import net.eads.astrium.hmas.conf.DreamConfFolder;
import net.eads.astrium.hmas.conf.exceptions.ConfFileAlreadyExistsException;
import net.eads.astrium.hmas.conf.exceptions.ConfFileNotFoundException;

/**
 *
 * @author re-sulrich
 */
public class OperationsConfigurationFile {
    
    private String filePath;

    public void createOperationsConfigurationFile(String serverBaseURI) throws ConfFileAlreadyExistsException, IOException
    {
        Map<String, String> operations = new HashMap<String, String>();
        operations.put("getoperations", 
                "GetCapabilities,"
                + "DescribeSensor,"
                + "DescribeTasking,"
                + "GetSensorAvailability,"
                + "GetStationAvailability"
            );
        
        operations.put("postoperations", "GetFeasibility");
        
        operations.put("serverbaseuri", serverBaseURI);
        
        DreamConfFileHandler.createConfigurationFile(filePath, operations);
    }
    
    public OperationsConfigurationFile(String instanceId) 
    {
        filePath = 
                DreamConfFolder.DREAM_WS_CONF_FOLDER + 
                DreamConfFolder.getFASConfTypePath() + 
                instanceId + File.separator + "operations";
    }
    
    public String[] getGetOperations() throws IOException, ConfFileNotFoundException
    {
        return DreamConfFileHandler.getProperties(filePath).get("getoperations").split(",");
    }
    public String[] getPostOperations() throws IOException, ConfFileNotFoundException
    {
        return DreamConfFileHandler.getProperties(filePath).get("postoperations").split(",");
    }
    
    public String getServerBaseURI() throws IOException, ConfFileNotFoundException
    {
        return DreamConfFileHandler.getProperties(filePath).get("serverbaseuri");
    }
}

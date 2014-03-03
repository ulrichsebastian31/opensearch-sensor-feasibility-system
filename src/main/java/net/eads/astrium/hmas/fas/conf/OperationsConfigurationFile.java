/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eads.astrium.hmas.fas.conf;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import net.eads.astrium.hmas.conf.ConfFileHandler;
import net.eads.astrium.hmas.conf.ConfFolder;
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
        
        ConfFileHandler.createConfigurationFile(filePath, operations);
    }
    
    public OperationsConfigurationFile(String instanceId) 
    {
        filePath = 
                ConfFolder.DREAM_WS_CONF_FOLDER + 
                ConfFolder.getFASConfTypePath() + 
                instanceId + File.separator + "operations";
    }
    
    public String[] getGetOperations() throws IOException, ConfFileNotFoundException
    {
        return ConfFileHandler.getProperties(filePath).get("getoperations").split(",");
    }
    public String[] getPostOperations() throws IOException, ConfFileNotFoundException
    {
        return ConfFileHandler.getProperties(filePath).get("postoperations").split(",");
    }
    
    public String getServerBaseURI() throws IOException, ConfFileNotFoundException
    {
        return ConfFileHandler.getProperties(filePath).get("serverbaseuri");
    }
}

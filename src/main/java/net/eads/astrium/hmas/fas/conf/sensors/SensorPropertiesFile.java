/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eads.astrium.hmas.fas.conf.sensors;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import net.eads.astrium.hmas.conf.ConfFileHandler;
import net.eads.astrium.hmas.conf.ConfFolder;
import net.eads.astrium.hmas.conf.exceptions.ConfFileAlreadyExistsException;
import net.eads.astrium.hmas.conf.exceptions.ConfFileNotFoundException;

/**
 *
 * @author re-sulrich
 * 
 * This class handles the file containing the sensor's basic properties
 */
class SensorPropertiesFile {
    
    private String filePath;
    private String sensorId;
    
    public static SensorPropertiesFile createSensorPropertiesFile(String instanceId, String sensorId, Map<String, String> properties) throws IOException, ConfFileAlreadyExistsException
    {
        SensorPropertiesFile file = new SensorPropertiesFile(instanceId, sensorId);
        
        file.setSensorProperties(properties);
        
        return file;
    }
    
    public SensorPropertiesFile(String instanceId, String sensorId) {
        
        filePath = 
                ConfFolder.DREAM_WS_CONF_FOLDER + 
                ConfFolder.getFASConfTypePath() + 
                instanceId + File.separator + 
                "sensors" + File.separator + 
                sensorId + File.separator + "properties.conf";
        
        this.sensorId = sensorId;
    }
        
    public Map<String, String> getSensorProperties()
            throws IOException, ConfFileNotFoundException
    {
        return ConfFileHandler.getProperties(filePath);
    }
    
    public void setSensorProperties(Map<String, String> sensorProperties)
            throws IOException, ConfFileAlreadyExistsException
    {
        ConfFileHandler.createConfigurationFile(filePath, sensorProperties);
    }
}
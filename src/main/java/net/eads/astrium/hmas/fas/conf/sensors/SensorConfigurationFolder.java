/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eads.astrium.hmas.fas.conf.sensors;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import net.eads.astrium.hmas.conf.ConfFolder;
import net.eads.astrium.hmas.conf.exceptions.ConfFileAlreadyExistsException;
import net.eads.astrium.hmas.fas.conf.exceptions.SensorAlreadyExistsException;
import net.eads.astrium.hmas.fas.conf.exceptions.SensorNotFoundException;

/**
 *
 * @author re-sulrich
 */
class SensorConfigurationFolder {
    
    private String folderPath;
    private SensorDescriptionFiles descriptions;
    private SensorPropertiesFile properties;

    public SensorDescriptionFiles getDescriptions() {
        return descriptions;
    }

    public SensorPropertiesFile getProperties() {
        return properties;
    }
    
    SensorConfigurationFolder(String instanceId, String sensorId) {
        
        folderPath = 
                ConfFolder.DREAM_WS_CONF_FOLDER + 
                ConfFolder.getFASConfTypePath() + 
                instanceId + File.separator + 
                "sensors" + File.separator + 
                sensorId + File.separator;
    }
    
    public static SensorConfigurationFolder createSensorConfigurationFolder(
            String instanceId, String sensorId, Map<String, String> properties) 
        throws SensorAlreadyExistsException, IOException, ConfFileAlreadyExistsException
    {
        SensorConfigurationFolder conf = new SensorConfigurationFolder(instanceId, sensorId);
        
        File folder = new File(conf.folderPath);
        
        if (folder.exists() && folder.isDirectory())
        {
            throw new SensorAlreadyExistsException(instanceId, sensorId);
        }
        
        folder.mkdirs();
        conf.properties = SensorPropertiesFile.createSensorPropertiesFile(instanceId, sensorId, properties);
        conf.descriptions = new SensorDescriptionFiles(instanceId, sensorId);
        
        return conf;
    }
    
    public static SensorConfigurationFolder loadSensorConfigurationFolder(String instanceId, String sensorId) 
        throws SensorNotFoundException
    {
        SensorConfigurationFolder conf = new SensorConfigurationFolder(instanceId, sensorId);
        
        File folder = new File(conf.folderPath);
        
        if (!folder.exists() || !folder.isDirectory())
        {
            throw new SensorNotFoundException(instanceId, sensorId);
        }
        conf.properties = new SensorPropertiesFile(instanceId, sensorId);
        conf.descriptions = new SensorDescriptionFiles(instanceId, sensorId);
        
        return conf;
    }
}

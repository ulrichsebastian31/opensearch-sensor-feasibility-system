/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eads.astrium.hmas.fas.configuration.sensors;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.eads.astrium.hmas.conf.DreamConfFolder;
import net.eads.astrium.hmas.conf.exceptions.ConfFileAlreadyExistsException;
import net.eads.astrium.hmas.conf.exceptions.ConfFileNotFoundException;
import net.eads.astrium.hmas.fas.configuration.exceptions.SensorAlreadyExistsException;
import net.eads.astrium.hmas.fas.configuration.exceptions.SensorNotFoundException;

/**
 *
 * @author re-sulrich
 * 
 * This class is the interface to create, modify and read sensor configurations for the FAS
 */
public class SensorsConfigurationsFolder {
    
    private Map<String, SensorConfigurationFolder> sensors;
    private String folderPath;
    private String instanceId;

    public SensorsConfigurationsFolder(String instanceId) {
        
        this.instanceId = instanceId;
        this.sensors = new HashMap<String, SensorConfigurationFolder>();
        
        this.folderPath = 
                DreamConfFolder.DREAM_WS_CONF_FOLDER + 
                DreamConfFolder.getFASConfTypePath() + 
                instanceId + File.separator + "sensors" + File.separator;
        
        File folder = new File(folderPath);
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            for (int i = 0; i < files.length; i++) {
                String sensorId = files[i].getName();
                
                System.out.println("Loading " + sensorId);
                //TODO: Exception
                try {
                    sensors.put(sensorId, SensorConfigurationFolder.loadSensorConfigurationFolder(instanceId, sensorId));
                } catch (SensorNotFoundException ex) {
                    Logger.getLogger(SensorsConfigurationsFolder.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        else
        {
            folder.mkdirs();
        }
    }
    
    
    
    
    /**
     * Setters
     */
    public void addSensor(String sensorId, Map<String, String> properties) 
        throws SensorAlreadyExistsException, IOException, ConfFileAlreadyExistsException
    {
        SensorConfigurationFolder conf = SensorConfigurationFolder.createSensorConfigurationFolder(instanceId, sensorId, properties);
        
        this.sensors.put(sensorId, conf);
    }
    public void addSensorDescription(String sensorId, String sensorDescriptionType, String content) 
            throws ConfFileAlreadyExistsException, IOException
    {
        SensorConfigurationFolder conf = this.sensors.get(sensorId);
        
        conf.getDescriptions().createSensorDescriptionFile(sensorDescriptionType, content);
    }
    
    /**
     * Getters
     */
    public String getSensorDescription(String sensorId, String sensorDescriptionType) 
            throws IOException
    {
        SensorConfigurationFolder conf = this.sensors.get(sensorId);
        SensorDescriptionFiles descs = conf.getDescriptions();
        String desc = descs.getSensorDescriptionFile(sensorDescriptionType);
        return desc;
    }
    
    public Map<String, String> getSensorProperties(String sensorId) 
            throws IOException, ConfFileNotFoundException
    {
        SensorConfigurationFolder conf = this.sensors.get(sensorId);
        
        return conf.getProperties().getSensorProperties();
    }
    
    public Set<String> getSensors()
    {
        return this.sensors.keySet();
    }
}

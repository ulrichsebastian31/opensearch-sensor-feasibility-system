/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eads.astrium.hmas.fas.configuration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.eads.astrium.dream.util.structures.SatellitePlatform;
import net.eads.astrium.hmas.conf.DreamConfFolder;
import net.eads.astrium.hmas.conf.exceptions.ConfFileAlreadyExistsException;
import net.eads.astrium.hmas.conf.exceptions.ConfFileNotFoundException;
import net.eads.astrium.hmas.conf.exceptions.ConfFileParsingException;
import net.eads.astrium.hmas.conf.exceptions.ConfFolderAlreadyExistsException;
import net.eads.astrium.hmas.conf.exceptions.ConfFolderNotFoundException;
import net.eads.astrium.hmas.fas.configuration.exceptions.FASAlreadyExistsException;
import net.eads.astrium.hmas.fas.configuration.exceptions.FASNotFoundException;
import net.eads.astrium.hmas.fas.configuration.exceptions.SensorAlreadyExistsException;
import net.eads.astrium.hmas.fas.configuration.sensors.SensorsConfigurationsFolder;
import net.opengis.sensorML.x102.SensorMLDocument;
import org.apache.xmlbeans.XmlException;

/**
 *
 * @author re-sulrich
 */
public class FASConfFolder extends DreamConfFolder {
    
    
    //---------------------------------------------------------------------------------------
    //Static functions
    //---------------------------------------------------------------------------------------
    
    
    /**
     * 
     * Creates the FAS conf folder for the given instance
     * @param instanceId    the Id of the instance to be created
     * @param name          the full name of the satellite of this FAS
     * @return 
     * @throws FASAlreadyExistsException        if instance exists
     * @throws IOException                      File handling exceptions                                     
     */
    public static FASConfFolder createFASConfFolder(String instanceId, String satelliteName, String serverBaseURI) 
            throws FASAlreadyExistsException, IOException
    {
        FASConfFolder fasConf = new FASConfFolder(instanceId);
        try {
            DreamConfFolder.createConfFolder(ConfFolderType.fas, instanceId);
        } catch (ConfFolderAlreadyExistsException ex) {
            Logger.getLogger(FASConfFolder.class.getName()).log(Level.SEVERE, null, ex);
            throw new FASAlreadyExistsException(instanceId);
        }
        
        System.out.println("Creating conf files");
        
        try {
            System.out.println("Satellite");
          
            Map<String, String> satelliteParameters = new HashMap<String, String>();
            satelliteParameters.put("instanceId", instanceId);
            satelliteParameters.put("satelliteName", satelliteName);
            satelliteParameters.put("serverBaseURI", serverBaseURI);
            fasConf.satellite = new SatelliteConfigurationFile(instanceId);
            fasConf.satellite.createSatelliteConfigurationFile(satelliteParameters);

            System.out.println("operations");
          
            fasConf.operations = new OperationsConfigurationFile(instanceId);
            fasConf.operations.createOperationsConfigurationFile(serverBaseURI);

            System.out.println("sensors");
            fasConf.sensors = new SensorsConfigurationsFolder(instanceId);
            
        
        } catch (ConfFileAlreadyExistsException ex) {
            //Never thrown, because InstanceAlreadyExistsException would be thrown before
            Logger.getLogger(FASConfFolder.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return fasConf;
    }
    
    /**
     * This function permits to get the configuration folder for an existing instance
     * @param instanceId    the Id of the instance to be loaded
     * @return
     * @throws InstanceNotFoundException    if this instance doesn't exist
     */
    public static FASConfFolder loadFASConfFolder(String instanceId) throws FASNotFoundException
    {
        FASConfFolder conf = new FASConfFolder(instanceId);
        try {
            DreamConfFolder.loadConfFolder(ConfFolderType.fas, instanceId);
        } catch (ConfFolderNotFoundException ex) {
            Logger.getLogger(FASConfFolder.class.getName()).log(Level.SEVERE, null, ex);
            throw new FASNotFoundException(instanceId);
        }
        
        conf.satellite = new SatelliteConfigurationFile(instanceId);
        conf.operations = new OperationsConfigurationFile(instanceId);
        conf.sensors = new SensorsConfigurationsFolder(instanceId);
        
        return conf;
    }
    //---------------------------------------------------------------------------------------
    //End of static functions
    //---------------------------------------------------------------------------------------
    
    /**
     * Handles the operations configuration file
     */
    private OperationsConfigurationFile operations;
    /**
     * Handles the satellite configuration file
     */
    private SatelliteConfigurationFile satellite;
    /**
     * Handles the sensors configuration files
     */
    private SensorsConfigurationsFolder sensors;
    
    FASConfFolder(String instanceId)
    {
        super(ConfFolderType.fas, instanceId);
    }

    /**
     * Functions that permit to get and set properties
     * through the appropriate configuration Objects
     */
    
    public String[] getGetOperations() throws IOException, ConfFileNotFoundException {
        return this.operations.getGetOperations();
    }

    public String getServerBaseURI() throws IOException, ConfFileNotFoundException {
        return this.operations.getServerBaseURI();
    }

    public String[] getPostOperations() throws IOException, ConfFileNotFoundException {
        return this.operations.getPostOperations();
    }

    public Map<String, String> getSensorProperties(String sensorId) throws IOException, ConfFileNotFoundException {
        return this.sensors.getSensorProperties(sensorId);
    }
    
    /**
     * This function reads the content of the description file
     * then puts it in the 
     * @param sensorId
     * @param sensorDescriptionFormat
     * @return
     * @throws IOException
     * @throws ConfFileNotFoundException
     * @throws ConfFileParsingException 
     */
    public String getSensorDescription(String sensorId, String sensorDescriptionFormat) 
            throws IOException, ConfFileNotFoundException, ConfFileParsingException
    {
        String content = this.sensors.getSensorDescription(sensorId, sensorDescriptionFormat);
        
        return content;
    }

    public Set<String> getSensors() {
        return this.sensors.getSensors();
    }

    public void addSensorConfiguration(String sensorId, Map<String, String> map) 
            throws IOException, ConfFileAlreadyExistsException, SensorAlreadyExistsException {
        this.sensors.addSensor(sensorId, map);
    }
    
    public SatellitePlatform getSatellitePlatform() throws IOException, ConfFileNotFoundException {
        return this.satellite.getSatellitePlatform();
    }
}
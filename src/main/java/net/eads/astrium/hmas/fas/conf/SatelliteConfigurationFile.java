/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eads.astrium.hmas.fas.conf;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import net.eads.astrium.dream.util.structures.Orbit;
import net.eads.astrium.dream.util.structures.SatellitePlatform;
import net.eads.astrium.hmas.conf.ConfFileHandler;
import net.eads.astrium.hmas.conf.ConfFolder;
import net.eads.astrium.hmas.conf.exceptions.ConfFileAlreadyExistsException;
import net.eads.astrium.hmas.conf.exceptions.ConfFileNotFoundException;

/**
 *
 * @author re-sulrich
 */
public class SatelliteConfigurationFile {
    private final String filePath;

    
    public void createSatelliteConfigurationFile(Map<String, String> properties) 
            throws ConfFileAlreadyExistsException, IOException
    {
        ConfFileHandler.createConfigurationFile(filePath, properties);
    }
    
    
    
    public SatelliteConfigurationFile(String instanceId) {
        
        filePath = 
                ConfFolder.DREAM_WS_CONF_FOLDER + 
                ConfFolder.getFASConfTypePath() + 
                instanceId + File.separator + "satellite.conf";
    }
    
    public Map<String, String> getProperties() 
            throws IOException, ConfFileNotFoundException
    {
        return ConfFileHandler.getProperties(filePath);
    }
    
    public SatellitePlatform getSatellitePlatform() 
            throws IOException, ConfFileNotFoundException {
        
        Map<String, String> properties = ConfFileHandler.getProperties(filePath);
        
        String id = null;
        String noradName = null;
        String name = null;
        String desc = null;
        String orbitType = null;
        String href = null;
        if (properties.containsKey("satelliteId")) {
            id = properties.get("satelliteId");
        }
        if (properties.containsKey("noradName")) {
            noradName = properties.get("noradName");
        }
        if (properties.containsKey("satelliteName")) {
            name = properties.get("satelliteName");
        }
        if (properties.containsKey("description")) {
            desc = properties.get("description");
        }
        if (properties.containsKey("href")) {
            href = properties.get("href");
        }
        if (properties.containsKey("orbitType")) {
            orbitType = properties.get("orbitType");
        }
        
        SatellitePlatform platform = new SatellitePlatform(id, "", "", noradName, name, desc, href, new Orbit(orbitType, "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
        
        return platform;
    }
}

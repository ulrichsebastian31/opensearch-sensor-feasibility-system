/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eads.astrium.hmas.fas.configuration.sensors;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import net.eads.astrium.hmas.conf.DreamConfFileHandler;
import net.eads.astrium.hmas.conf.DreamConfFolder;

/**
 *
 * @author re-sulrich
 */
public class SensorTaskingDescriptionFile {
    
    private String filePath;
    
    /**
     * Creates an object that will permit to read the sensor tasking description file
     *
     * @param sensorFolderPath The path of the file called tasking
     */
    public SensorTaskingDescriptionFile(String satelliteId, String sensorId) {

        this.filePath = 
                DreamConfFolder.DREAM_WS_CONF_FOLDER + 
                DreamConfFolder.getFASConfTypePath() + 
                satelliteId + File.separator + 
                "sensors" + File.separator + 
                sensorId + File.separator + 
                "tasking.xml";
        
        System.out.println("SensorTaskingDescriptionFiles : " + filePath);
        
    }

    public String getSensorTaskingDescriptionFile() throws IOException
    {
        String content = null;
        
        System.out.println("" + filePath);
        if (new File(filePath).isFile())
            content = DreamConfFileHandler.getContent(filePath);
        else
            throw new FileNotFoundException(filePath);
        
        return content;
    }

}

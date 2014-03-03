/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eads.astrium.hmas.fas.conf.sensors;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import net.eads.astrium.hmas.conf.ConfFileHandler;
import net.eads.astrium.hmas.conf.ConfFolder;

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
                ConfFolder.DREAM_WS_CONF_FOLDER + 
                ConfFolder.getFASConfTypePath() + 
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
            content = ConfFileHandler.getContent(filePath);
        else
            throw new FileNotFoundException(filePath);
        
        return content;
    }

}

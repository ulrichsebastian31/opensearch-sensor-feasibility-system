/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eads.astrium.hmas.fas.configuration.sensors;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.eads.astrium.hmas.conf.DreamConfFileHandler;
import net.eads.astrium.hmas.conf.DreamConfFolder;
import net.eads.astrium.hmas.conf.exceptions.ConfFileAlreadyExistsException;

/**
 *
 * @author re-sulrich
 */
class SensorDescriptionFiles {

    private String folderPath;
    private String satelliteId;
    private String sensorId;
    /**
     * Map : - key : sensorDescriptionType - value : filePath
     */
    private List<String> descriptions;

    /**
     * Creates an object that will permit to read the different sensor descriptions 
     * for this sensor
     *
     * @param sensorFolderPath The path of the folder called
     * "sensors"
     */
    public SensorDescriptionFiles(String satelliteId, String sensorId) {

        this.folderPath = 
                DreamConfFolder.DREAM_WS_CONF_FOLDER + 
                DreamConfFolder.getFASConfTypePath() + 
                satelliteId + File.separator + 
                "sensors" + File.separator + 
                sensorId + File.separator + 
                "descriptions";
        
        System.out.println("SensorDescriptionFiles : " + folderPath);
        
        this.satelliteId = satelliteId;
        this.sensorId = sensorId;
        
        this.descriptions = new ArrayList<String>();

        File folder = new File(folderPath);
        
        
        System.out.println("ex ? " + folder.exists() + "\ndir : " + folder.isDirectory());
        
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            System.out.println("nb f : " + files.length);
            for (int i = 0; i < files.length; i++) {
                
                System.out.println("f " + i + "  : " + files[i].getName());
                this.descriptions.add(files[i].getName());
            }
        }
        else
        {
            folder.mkdirs();
        }
    }

    public void createSensorDescriptionFile(String sensorDescriptionType, String content) 
            throws ConfFileAlreadyExistsException, IOException {

        DreamConfFileHandler.createConfigurationFile(
                folderPath + File.separator + sensorDescriptionType, 
                content);
        
        descriptions.add(sensorDescriptionType);
    }
    
    public String getSensorDescriptionFile(String sensorDescriptionType) throws IOException
    {
        String content = null;
        System.out.println("nbdescs :" + descriptions.size());
        for (String desc : descriptions) {
            if (desc.equalsIgnoreCase(sensorDescriptionType))
            {
                System.out.println("" + folderPath + File.separator + desc);
                
                content = DreamConfFileHandler.getContent(folderPath + File.separator + desc);
            }
        }
        
        return content;
    }

    public List<String> getSensorDescriptionFormats() {
        return this.descriptions;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eads.astrium.dream.fas;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.eads.astrium.hmas.conf.exceptions.ConfFileNotFoundException;
import net.eads.astrium.hmas.fas.conf.sensors.SensorsConfigurationsFolder;
import org.junit.Test;

/**
 *
 * @author re-sulrich
 */
public class TestLoadSensorDescriptions {
    
    @Test
    public void loadSensorProps()
    {
        String folderpath = "C:\\Users\\re-sulrich\\.dream\\fas\\s1-fas\\sensors\\";
        SensorsConfigurationsFolder descLoader = new SensorsConfigurationsFolder("s1-fas");
        
        try {
            Map<String, String> desc = descLoader.getSensorProperties("s1-sar");
            for (String string : desc.keySet()) {
                System.out.println("" + string + " : " + desc.get(string));
            }
        } catch (IOException ex) {
            Logger.getLogger(TestLoadSensorDescriptions.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ConfFileNotFoundException ex) {
            Logger.getLogger(TestLoadSensorDescriptions.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    public void loadSensorMLDesc()
    {
        String folderpath = "C:\\Users\\re-sulrich\\.dream\\fas\\s1-fas\\sensors\\";
        SensorsConfigurationsFolder descLoader = new SensorsConfigurationsFolder("s1-fas");
        
        try {
            String desc = descLoader.getSensorDescription("s1-sar", "SensorML");
            
            System.out.println("" + desc);
            
        } catch (IOException ex) {
            Logger.getLogger(TestLoadSensorDescriptions.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

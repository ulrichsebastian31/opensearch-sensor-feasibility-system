/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eads.astrium.hmas.fas.conf.os;

import java.io.File;
import java.io.IOException;
import net.eads.astrium.hmas.conf.ConfFileHandler;
import net.eads.astrium.hmas.conf.ConfFolder;

/**
 *
 * @author re-sulrich
 */
public class DescriptionDocumentLoader {
    
    private String filePath;

    public DescriptionDocumentLoader(String instanceId) 
    {
        filePath = 
                ConfFolder.DREAM_WS_CONF_FOLDER + 
                ConfFolder.getFASConfTypePath() + 
                instanceId + File.separator + "description.xml";
    }
    
    public String getContent() throws IOException {
        
        return ConfFileHandler.getContent(filePath);
    }
}

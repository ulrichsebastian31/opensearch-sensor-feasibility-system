/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eads.astrium.hmas.fas.configuration.os;

import java.io.File;
import java.io.IOException;
import net.eads.astrium.hmas.conf.DreamConfFileHandler;
import net.eads.astrium.hmas.conf.DreamConfFolder;

/**
 *
 * @author re-sulrich
 */
public class DescriptionDocumentLoader {
    
    private String filePath;

    public DescriptionDocumentLoader(String instanceId) 
    {
        filePath = 
                DreamConfFolder.DREAM_WS_CONF_FOLDER + 
                DreamConfFolder.getFASConfTypePath() + 
                instanceId + File.separator + "description.xml";
    }
    
    public String getContent() throws IOException {
        
        return DreamConfFileHandler.getContent(filePath);
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eads.astrium.hmas.conf;

import java.io.File;
import net.eads.astrium.hmas.conf.exceptions.ConfFolderAlreadyExistsException;
import net.eads.astrium.hmas.conf.exceptions.ConfFolderNotFoundException;

/**
 *
 * @author re-sulrich
 */
public class ConfFolder {
    
    
    /**
     * Static variable that defines the conf folder for all services
     */
    public static String DREAM_WS_CONF_FOLDER = 
            System.getProperty("user.home") + File.separator + ".dream" + File.separator;
    
    public static enum ConfFolderType { fas, mmfas};
    public static String getFASConfTypePath()
    {
        return "fas" + File.separator;
    }
    public static String getMMFASConfTypePath()
    {
        return "mmfas" + File.separator;
    }
    public static String getConfTypePath(ConfFolderType type)
    {
        String path = "";
        if (type == ConfFolderType.fas)
            path = getFASConfTypePath();
        if (type == ConfFolderType.mmfas)
            path = getMMFASConfTypePath();
        
        return path;
    }
    
    
    
    
    /**
     * 
     * Creates the conf folder for the given instance
     * @param instanceId    the Id of the instance to be created
     * @param name          the full name of the satellite of this FAS
     * @return 
     * @throws FASAlreadyExistsException if instance exists
     */
    public static void createConfFolder(ConfFolderType type, String instanceId) 
            throws ConfFolderAlreadyExistsException 
    {
        ConfFolder conf = new ConfFolder(type, instanceId);
        
        if (conf.getConfFolder().exists() && conf.getConfFolder().isDirectory())
        {
            throw new ConfFolderAlreadyExistsException(type.name() + File.separator + conf.getInstanceId());
        }
        
        conf.getConfFolder().mkdirs();
    }
    
    /**
     * Loads the existing configuration of the given instance
     * @param instanceId
     * @param name
     * @return
     */
    public static ConfFolder loadConfFolder(ConfFolderType type, String instanceId) 
            throws ConfFolderNotFoundException 
    {
        ConfFolder conf = new ConfFolder(type, instanceId);
        
        if (!conf.getConfFolder().exists() || !conf.getConfFolder().isDirectory())
        {
            throw new ConfFolderNotFoundException(type.name() + File.separator + conf.getInstanceId());
        }
        return conf;
    }
    
    protected String instanceId;
    public String getInstanceId() {
        return instanceId;
    }
    
    private File confFolder;
    public File getConfFolder() {
        return confFolder;
    }


    protected ConfFolder(ConfFolderType type, String instanceId) {
        
        this.confFolder = new File(DREAM_WS_CONF_FOLDER + getConfTypePath(type) + instanceId + File.separator);
        this.instanceId = instanceId;
    }
    
}

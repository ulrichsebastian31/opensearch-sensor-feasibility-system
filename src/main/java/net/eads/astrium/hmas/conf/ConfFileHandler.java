/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eads.astrium.hmas.conf;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import net.eads.astrium.hmas.conf.exceptions.ConfFileAlreadyExistsException;
import net.eads.astrium.hmas.conf.exceptions.ConfFileNotFoundException;

/**
 *
 * @author re-sulrich
 */
public class ConfFileHandler {

    
    public static String getContent(String filePath) throws IOException
    {
        String content = "";
        
        FileInputStream fis = new FileInputStream(filePath);
        BufferedReader br = new BufferedReader(new InputStreamReader(fis, Charset.forName("UTF-8")));
        
        String line = null;
        while ((line = br.readLine()) != null) {
            
            content += line + "\n";
        }

        // Done with the file
        br.close();
        br = null;
        fis = null;
        
        return content;
    }
    
    public static File createConfigurationFile(
            String path,
            String content
        ) throws ConfFileAlreadyExistsException, IOException
    {
        File configurationFile = new File(path);
        
        if (configurationFile.exists())
        {
            throw new ConfFileAlreadyExistsException(path);
        }
        System.out.println("Creating file");
        configurationFile.createNewFile();
        
        PrintWriter w = new PrintWriter(configurationFile);
        BufferedWriter bf = new BufferedWriter(w);

        bf.write(content);
        bf.newLine();
        bf.flush();
        
        bf.close();
        w.close();
        
        return configurationFile;
    }

    public static Map<String, String> getProperties(String filePath) 
            throws IOException, ConfFileNotFoundException
    {
        if (!new File(filePath).exists())
            throw new ConfFileNotFoundException(filePath);
        
        Map<String, String> properties = new HashMap<String, String>();
        FileInputStream fis = new FileInputStream(filePath);
        BufferedReader br = new BufferedReader(new InputStreamReader(fis, Charset.forName("UTF-8")));
        
        String line = null;
        while ((line = br.readLine()) != null) {
            
            String[] prop = line.split(":", 2);
            
            if (prop.length > 1)
            {
                properties.put(prop[0], prop[1]);
            }
        }

        // Done with the file
        br.close();
        br = null;
        fis = null;
        
        return properties;
    }
    
    public static File createConfigurationFile(
            String path,
            Map<String, String> properties
        ) throws ConfFileAlreadyExistsException, IOException
    {
        File configurationFile = new File(path);
        
        if (configurationFile.exists())
        {
            throw new ConfFileAlreadyExistsException(path);
        }
        System.out.println("Creating file");
        configurationFile.createNewFile();
        
        System.out.println("Writing " + properties.keySet().size() + " properties.");

        PrintWriter w = new PrintWriter(configurationFile);
        BufferedWriter bf = new BufferedWriter(w);

        for (String aProperty : properties.keySet())
        {
            bf.write(aProperty + ":" + properties.get(aProperty) + "\n");
//            bf.newLine();
            bf.flush();
        }
        
        bf.close();
        w.close();
        
        return configurationFile;
    }
}

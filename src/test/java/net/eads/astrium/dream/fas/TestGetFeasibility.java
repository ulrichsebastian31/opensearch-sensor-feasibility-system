/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eads.astrium.dream.fas;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import javax.ws.rs.core.MultivaluedMap;
import org.junit.Test;

/**
 *
 * @author re-sulrich
 */
public class TestGetFeasibility {
    
    
    @Test
    public void test() throws FileNotFoundException, UnsupportedEncodingException, IOException {
        
        ClientConfig config = new DefaultClientConfig();
        Client client = Client.create(config);
        
        String request = "http://127.0.0.1:8080/HMAS-FAS-1.0-SNAPSHOT/hmas/fas/os/search";
        
        WebResource webResource = client.resource(request);
        
        MultivaluedMap queryParams = new MultivaluedMapImpl();
        
        queryParams.add("instrument", "S1SAR");
//        queryParams.add("box", "23.32,54.65,23.43,34.23");
        queryParams.add("box", "0.0,60.0,90.0,90.0");
        queryParams.add("start", "2002-03-01T09:20:02Z");
        queryParams.add("end", "2002-03-01T20:58:46Z");
        
        String s = webResource
                .queryParams(queryParams)
                .accept("application/xml")
                .get(String.class);
        
//        System.out.println(s);
//        new File("C:\\Users\\re-sulrich\\.dream\\fas\\Sentinel1\\feasibility2").createNewFile();
        
        PrintWriter writer = new PrintWriter("C:\\Users\\re-sulrich\\.dream\\fas\\Sentinel1\\feasibility321", "UTF-8");
        writer.print(s);
        writer.close();
    }
}

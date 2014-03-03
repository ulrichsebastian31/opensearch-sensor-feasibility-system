/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eads.astrium.dream.fas;

import java.io.IOException;
import net.eads.astrium.hmas.fas.conf.os.DescriptionDocumentLoader;
import org.junit.Test;

/**
 *
 * @author re-sulrich
 */
public class TestReadDescriptionDocument {
    
    @Test
    public void test() throws IOException {
        DescriptionDocumentLoader loader = new DescriptionDocumentLoader("Sentinel1");
        
        
        
        System.out.println("" + loader.getContent());
        
        
    }
}

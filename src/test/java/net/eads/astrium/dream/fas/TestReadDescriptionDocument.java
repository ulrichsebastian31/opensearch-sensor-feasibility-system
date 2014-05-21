/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eads.astrium.dream.fas;

import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import net.eads.astrium.hmas.fas.conf.os.DescriptionDocumentLoader;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 *
 * @author re-sulrich
 */
public class TestReadDescriptionDocument {
    
    
    
    @Test
    public void testAddURL() throws ParserConfigurationException, SAXException, IOException {
        
        DescriptionDocumentLoader loader = new DescriptionDocumentLoader("Sentinel1");
        
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

        Document doc = documentBuilder.parse(loader.getFilePath());

        Element root = doc.getDocumentElement();

        System.out.println("Root : " + root.getNodeName());

        for (int i = 0; i < root.getChildNodes().getLength(); i++) {
            Node node = root.getChildNodes().item(i);
            
            if (node.getNodeName().equals("Url")) {
                String temp = node.getAttributes().getNamedItem("template").getTextContent();
                
                Node temps = node.getAttributes().removeNamedItem("template");
                
                temp = temp.replace("http://127.0.0.1:8080/HMAS-FAS-1.0-SNAPSHOT/hmas", "http://newUrl");
                
                temps.setTextContent(temp);
                
                node.getAttributes().setNamedItem(temps);
                
                System.out.println("" + temp);
                System.out.println("" + node);
                
                for (int j = 0; j < node.getAttributes().getLength(); j++) {
                    Node attr = node.getAttributes().item(j);
                    System.out.println("" + attr.getNodeName() + " : " + attr.getTextContent());
                }
            }
        }
    }
    
//    @Test
    public void test() throws IOException {
        DescriptionDocumentLoader loader = new DescriptionDocumentLoader("Sentinel1");
        
        
        
        System.out.println("" + loader.getContent());
        
        
    }
}

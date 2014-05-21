/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eads.astrium.hmas.fas.os;

import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.multipart.FormDataMultiPart;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import net.eads.astrium.hmas.fas.conf.os.DescriptionDocumentLoader;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 *
 * @author re-sulrich
 */
@Path("fas/os/description")
public class FASOSDescriptionInterface {

    
    /**
     * Map containing the URL parameters
     */ 
    @Context 
    volatile UriInfo ui;
    
    @Context
    volatile HttpContext httpContext;
    
//    private URI admin = httpContext.getUriInfo().getBaseUri();//.resolve("fasadmin/");
    
    
//    @POST
//    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @GET
    public Response FASAdminPostRequestParser()
    {
        System.out.println("Java library path : " + System.getProperty("java.library.path") );
        
        URI baseURI = httpContext.getUriInfo().getAbsolutePath();
        
        String serversAdress = null;
        if (baseURI.getPort() == -1) {
            serversAdress = "http://" + baseURI.getHost() + baseURI.getPath();
        }
        else {
            serversAdress = "http://" + baseURI.getHost() + ":" + baseURI.getPort() + baseURI.getPath();
        }
                
        if (serversAdress.contains("/fas/os/description"))
        {
            serversAdress = serversAdress.substring(0, serversAdress.lastIndexOf("/fas/os/description"));
        }
        if (!serversAdress.endsWith("/")) serversAdress += "/";
        
        
        Response response = null;
        DescriptionDocumentLoader loader = new DescriptionDocumentLoader("Sentinel1");
        
        String content;
        try {
//            content = loader.getContent();
            
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            Document document = documentBuilder.parse(loader.getFilePath());

            Element root = document.getDocumentElement();

            System.out.println("Root : " + root.getNodeName());

            for (int i = 0; i < root.getChildNodes().getLength(); i++) {
                Node node = root.getChildNodes().item(i);

                if (node.getNodeName().equals("Url")) {
                    String temp = node.getAttributes().getNamedItem("template").getTextContent();

                    Node temps = node.getAttributes().removeNamedItem("template");

                    temp = temp.replace("http://127.0.0.1:8080/HMAS-FAS-1.0-SNAPSHOT/hmas/", serversAdress);

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
            
            String resp = null;
            
            OutputFormat format = new OutputFormat(document);
            format.setIndenting(true);

            StringWriter sw = new StringWriter();

            XMLSerializer serializer = new XMLSerializer(sw, format);
            serializer.serialize(document);

            resp = sw.toString();
            
            response = Response.ok(resp, "application/opensearchdescription+xml").build();
        } catch (SAXException|ParserConfigurationException|IOException ex) {
            Logger.getLogger(FASOSDescriptionInterface.class.getName()).log(Level.SEVERE, null, ex);
            
            response = Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .type(MediaType.APPLICATION_XML)
                    .entity("Error parsing MXL : " + ex.getMessage())
                    .build();
        }
        
        return response;
    }
}
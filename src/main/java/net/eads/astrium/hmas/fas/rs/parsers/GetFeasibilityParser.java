/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eads.astrium.hmas.fas.rs.parsers;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.MultivaluedMap;
import net.eads.astrium.hmas.rs.exceptions.MissingParameterException;
import net.eads.astrium.hmas.rs.exceptions.XMLParsingException;
import net.opengis.eosps.x20.GetFeasibilityDocument;
import org.apache.xmlbeans.XmlException;

/**
 *
 * @author re-sulrich
 */
public class GetFeasibilityParser {

    public static GetFeasibilityDocument createXMLGetFeasibility(String xmlRequest)
            throws MissingParameterException, XMLParsingException
    {
        GetFeasibilityDocument doc = null;
        try {
            doc = GetFeasibilityDocument.Factory.parse(xmlRequest);
        } catch (XmlException ex) {
            Logger.getLogger(GetFeasibilityParser.class.getName()).log(Level.SEVERE, null, ex);
            
            throw new XMLParsingException(
                    "Failed parsing XML request : " + ex.getMessage(), 
                    "text/xml");
        }
        
        return doc;
    }
}

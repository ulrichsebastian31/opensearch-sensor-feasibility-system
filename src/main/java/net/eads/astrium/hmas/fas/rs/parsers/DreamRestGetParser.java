/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eads.astrium.hmas.fas.rs.parsers;

import javax.ws.rs.core.MultivaluedMap;
import net.eads.astrium.hmas.rs.exceptions.MissingParameterException;

import org.apache.xmlbeans.XmlObject;

/**
 *
 * @author re-sulrich
 */
public abstract class DreamRestGetParser {
    
    
    public abstract XmlObject createXMLFromGetRequest( MultivaluedMap<String, String> params)
            throws MissingParameterException;
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eads.astrium.hmas.conf.exceptions;

/**
 *
 * @author re-sulrich
 */
public class ConfFileParsingException extends Exception {
    
    public ConfFileParsingException(String relativPath)
    {
        super("FAS " + relativPath + ":  doesn't exists.");
    }
}

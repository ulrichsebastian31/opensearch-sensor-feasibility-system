/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eads.astrium.hmas.conf.exceptions;

/**
 *
 * @author re-sulrich
 */
public class ConfFileAlreadyExistsException extends Exception {
    
    public ConfFileAlreadyExistsException(String relativPath)
    {
        super("File " + relativPath + " already exists.");
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eads.astrium.hmas.conf.exceptions;

/**
 *
 * @author re-sulrich
 */
public class ConfFolderAlreadyExistsException extends Exception {
    
    public ConfFolderAlreadyExistsException(String relativPath)
    {
        super("File " + relativPath + " already exists.");
    }
}

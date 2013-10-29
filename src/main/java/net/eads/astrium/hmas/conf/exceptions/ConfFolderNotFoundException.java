/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eads.astrium.hmas.conf.exceptions;

/**
 *
 * @author re-sulrich
 */
public class ConfFolderNotFoundException extends Exception {
    
    public ConfFolderNotFoundException(String relativPath)
    {
        super("File " + relativPath + ":  doesn't exists.");
    }
}

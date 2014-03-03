/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eads.astrium.hmas.fas.conf.exceptions;

/**
 *
 * @author re-sulrich
 */
public class FASNotFoundException extends Exception {
    
    public FASNotFoundException(String instanceId)
    {
        super("FAS " + instanceId + " doesn't exists.");
    }
}

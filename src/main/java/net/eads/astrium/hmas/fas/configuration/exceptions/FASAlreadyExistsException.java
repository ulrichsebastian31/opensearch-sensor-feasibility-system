/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eads.astrium.hmas.fas.configuration.exceptions;

/**
 *
 * @author re-sulrich
 */
public class FASAlreadyExistsException extends Exception {
    
    public FASAlreadyExistsException(String instanceId)
    {
        super("FAS " + instanceId + " already exists.");
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eads.astrium.hmas.fas.configuration.exceptions;

/**
 *
 * @author re-sulrich
 */
public class SensorAlreadyExistsException extends Exception {
    
    public SensorAlreadyExistsException(String instanceId, String sensorId)
    {
        super("Sensor " + sensorId + " already exists in FAS " + instanceId);
    }
}

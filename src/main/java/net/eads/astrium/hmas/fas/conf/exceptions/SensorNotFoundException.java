/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eads.astrium.hmas.fas.conf.exceptions;

/**
 *
 * @author re-sulrich
 */
public class SensorNotFoundException extends Exception {
    
    public SensorNotFoundException(String instanceId, String sensorId)
    {
        super("Sensor " + sensorId + " doesn't exists in FAS " + instanceId);
    }
}

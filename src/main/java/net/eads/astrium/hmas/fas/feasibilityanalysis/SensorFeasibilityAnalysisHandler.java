/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eads.astrium.hmas.fas.feasibilityanalysis;

import EECFI.CfiError;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;
import net.eads.astrium.dream.eocfihandler.dreameocfihandler.EoCfiHndlrError;
import net.eads.astrium.dream.util.structures.tasking.Segment;
import net.eads.astrium.dream.util.structures.tasking.Status;

/**
 *
 * @author re-sulrich
 */
public abstract class SensorFeasibilityAnalysisHandler {
    
    final String satelliteId;
    final String sensorType;
    final String sensorId;
    
    String taskId;
    int estimatedCost;
    String currency;
    
    Status status;
    List<Segment> segments;

    public String getTaskId() {
        return taskId;
    }

    public int getEstimatedCost() {
        return estimatedCost;
    }

    public String getCurrency() {
        return currency;
    }

    public Status getStatus() {
        return status;
    }

    public List<Segment> getSegments() {
        return segments;
    }

    
    public SensorFeasibilityAnalysisHandler(
            String satelliteId, String sensorType, String sensorId) {
        
        this.satelliteId = satelliteId;
        this.sensorType = sensorType;
        this.sensorId = sensorId;
        
        this.status = new Status("1", 0, "", Calendar.getInstance().getTime(), Calendar.getInstance().getTime());
    }
    
    public abstract void doFeasibility() throws SQLException, EoCfiHndlrError;
    
    public abstract void doFakeFeasibility() throws SQLException;
    
}

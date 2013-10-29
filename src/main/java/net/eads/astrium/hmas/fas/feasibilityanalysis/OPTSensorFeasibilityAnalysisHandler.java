/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eads.astrium.hmas.fas.feasibilityanalysis;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import net.eads.astrium.dream.eocfihandler.dreameocfihandler.EoCfiHndlrError;
import net.eads.astrium.dream.util.structures.tasking.OPTTaskingParameters;
import net.eads.astrium.dream.util.structures.tasking.Point;
import net.eads.astrium.dream.util.structures.tasking.Polygon;
import net.eads.astrium.dream.util.structures.tasking.Segment;
import net.eads.astrium.dream.util.structures.tasking.SegmentVisGS;
import net.eads.astrium.dream.util.structures.tasking.Status;

/**
 *
 * @author re-sulrich
 */
public class OPTSensorFeasibilityAnalysisHandler extends SensorFeasibilityAnalysisHandler {
    
    private final OPTTaskingParameters optParameters;

    public OPTSensorFeasibilityAnalysisHandler(OPTTaskingParameters optParameters, String satelliteId, String sensorId) {
        super(satelliteId, "OPT", sensorId);
        this.optParameters = optParameters;
    }
    
    @Override
    public void doFeasibility() throws SQLException, EoCfiHndlrError {
        
//        taskId = dbHandler.createOPTFeasibilityTask(
//                    optParameters);
        //TODO: do feasibility
    }

    @Override
    public void doFakeFeasibility() throws SQLException {
        
        Calendar cstart = Calendar.getInstance();cstart.add(Calendar.HOUR_OF_DAY, 1);
        Calendar cend = Calendar.getInstance();cend.add(Calendar.HOUR_OF_DAY, 2);
        
        this.estimatedCost = 1;
        this.currency = "Euros";
        
        this.taskId = "125";
        
        status = new Status(
                "FEASIBILITY COMPLETE", 
                100, 
                "The task is completed", 
                Calendar.getInstance().getTime(), 
                new Date(2013, 11, 21, 0, 0, 0));
        
        segments = new ArrayList<>();
        segments.add(
                new Segment(
                        "21", 
                        new Polygon( Arrays.asList(new Point[]{
                            new Point(125,37, 0.0), 
                            new Point(127,35, 0.0), 
                            new Point(126,34, 0.0), 
                            new Point(124,36, 0.0), 
                            new Point(125,37, 0.0)})), 
                        (long)1243, (long)12, status, 
                        Calendar.getInstance(), Calendar.getInstance(), 100.00,
                        new SegmentVisGS("GHATOJHX", "Kumamoto (JAPAN)", cstart, cend)
                    )
            );
        
        segments.add(
                new Segment(
                        "22", 
                        new Polygon( Arrays.asList(new Point[]{
                            new Point(129,41, 0.0), 
                            new Point(131,39, 0.0), 
                            new Point(130,38, 0.0), 
                            new Point(128,40, 0.0), 
                            new Point(129,41, 0.0)})), 
                        (long)1243, (long)12, status, 
                        Calendar.getInstance(), Calendar.getInstance(),  100.00,
                        new SegmentVisGS("GHATOJHX", "Kumamoto (JAPAN)", cstart, cend)
                    )
            );
        
        
    }
}

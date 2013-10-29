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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.eads.astrium.dream.eocfihandler.dreameocfihandler.EOCFIHandling;
import net.eads.astrium.dream.eocfihandler.dreameocfihandler.EoCfiHndlrError;
import net.eads.astrium.dream.util.structures.tasking.Point;
import net.eads.astrium.dream.util.structures.tasking.Polygon;
import net.eads.astrium.dream.util.structures.tasking.SARTaskingParameters;
import net.eads.astrium.dream.util.structures.tasking.Segment;
import net.eads.astrium.dream.util.structures.tasking.SegmentVisGS;
import net.eads.astrium.dream.util.structures.tasking.Status;

/**
 *
 * @author re-sulrich
 */
public class SARSensorFeasibilityAnalysisHandler extends SensorFeasibilityAnalysisHandler {
    
    private final SARTaskingParameters sarParameters;

    public SARSensorFeasibilityAnalysisHandler(SARTaskingParameters sarParameters, String satelliteId, String sensorId) {
        super(satelliteId, "SAR", sensorId);
        this.sarParameters = sarParameters;
    }
    
    @Override
    public void doFeasibility() throws SQLException, EoCfiHndlrError {
        
        EOCFIHandling handler = EOCFIHandling.Factory.newInstance(satelliteId);

        segments = handler.getSARFeasibility(sensorId, sarParameters, 1.0);

        //Figure out the last segment to be acquired to put its end date as AcquisitionEndDate
        Calendar estimatedToC = Calendar.getInstance();
        if (segments != null)
            for (Segment segment : segments)
                if (estimatedToC.before(segment.getEndOfAcquisition()))
                    estimatedToC.setTime(segment.getEndOfAcquisition().getTime());

        this.status = new Status("" + segments.size(), 100, "Feasibility analysis complete", Calendar.getInstance().getTime(), estimatedToC.getTime());
        
    }

    @Override
    public void doFakeFeasibility() throws SQLException {
        
        Calendar cstart = Calendar.getInstance();cstart.add(Calendar.HOUR_OF_DAY, 1);
        Calendar cend = Calendar.getInstance();cend.add(Calendar.HOUR_OF_DAY, 2);
        
        this.estimatedCost = 1;
        this.currency = "Euros";
        this.taskId = "123";
        
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
                            new Point(125,35, 0.0), 
                            new Point(127,37, 0.0), 
                            new Point(128,36, 0.0), 
                            new Point(126,34, 0.0), 
                            new Point(125,35, 0.0)})), 
                        (long)1243, (long)12, status, 
                        Calendar.getInstance(), Calendar.getInstance(),  100.00,
                        new SegmentVisGS("GHATOJHX", "Kumamoto (JAPAN)", cstart, cend)
                    )
            );
        
        segments.add(
                new Segment(
                        "22", 
                        new Polygon( Arrays.asList(new Point[]{
                            new Point(129,39, 0.0), 
                            new Point(131,41, 0.0), 
                            new Point(132,40, 0.0), 
                            new Point(130,38, 0.0), 
                            new Point(129,39, 0.0)})), 
                        (long)1243, (long)12, status, 
                        Calendar.getInstance(), Calendar.getInstance(),  100.00,
                        new SegmentVisGS("GHATOJHX", "Kumamoto (JAPAN)", cstart, cend)
                    )
            );
        
        
        
    }
}

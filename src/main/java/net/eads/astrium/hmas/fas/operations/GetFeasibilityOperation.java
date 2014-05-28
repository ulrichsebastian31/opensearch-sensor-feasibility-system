package net.eads.astrium.hmas.fas.operations;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.eads.astrium.dream.eocfihandler.dreameocfihandler.EoCfiHndlrError;
import net.eads.astrium.dream.util.DateHandler;
import net.eads.astrium.dream.util.structures.SatellitePlatform;
import net.eads.astrium.dream.util.structures.TimePeriod;
import net.eads.astrium.dream.util.structures.tasking.geometries.Point;
import net.eads.astrium.dream.util.structures.tasking.geometries.Polygon;
import net.eads.astrium.dream.util.structures.tasking.SARTaskingParameters;
import net.eads.astrium.dream.util.structures.tasking.Segment;
import net.eads.astrium.dream.util.structures.tasking.Status;
import net.eads.astrium.hmas.conf.ConfFolder;
import net.eads.astrium.hmas.conf.exceptions.ConfFileNotFoundException;
import net.eads.astrium.hmas.operations.DreamEOSPSOperation;
import net.eads.astrium.hmas.exceptions.GetFeasibilityFault;
import net.eads.astrium.hmas.fas.conf.FASConfFolder;
import net.eads.astrium.hmas.fas.feasibilityanalysis.SARSensorFeasibilityAnalysisHandler;
import net.eads.astrium.hmas.fas.feasibilityanalysis.SensorFeasibilityAnalysisHandler;

import net.opengis.eosps.x20.AcquisitionAngleType;
import net.opengis.eosps.x20.AcquisitionParametersSARType;
import net.opengis.eosps.x20.CoverageProgrammingRequestType;
import net.opengis.eosps.x20.FeasibilityStudyDocument;
import net.opengis.eosps.x20.FeasibilityStudyType;
import net.opengis.eosps.x20.GetFeasibilityDocument;
import net.opengis.eosps.x20.GetFeasibilityResponseDocument;
import net.opengis.eosps.x20.GetFeasibilityResponseType;
import net.opengis.eosps.x20.GetFeasibilityType;
import net.opengis.eosps.x20.IncidenceRangeType;
import net.opengis.eosps.x20.MonoscopicAcquisitionType;
import net.opengis.eosps.x20.PointingRangeType;
import net.opengis.eosps.x20.SegmentPropertyType;
import net.opengis.eosps.x20.SegmentType;
import net.opengis.eosps.x20.StatusReportType;
import net.opengis.eosps.x20.TaskingParametersType;
import net.opengis.eosps.x20.ValidationParametersSARType;
import net.opengis.gml.x32.CodeType;
import net.opengis.gml.x32.CoordinatesType;
import net.opengis.gml.x32.LinearRingType;
import net.opengis.gml.x32.PolygonType;
import net.opengis.gml.x32.TimePeriodType;
import xint.esa.earth.eop.EarthObservationEquipmentType;
import xint.esa.earth.eop.OrbitTypeValueType;
import xint.esa.earth.eop.PlatformType;

/**
 * @file GetFeasibilityOperation.java
 * @author Benjamin VADANT <benjaminvadant@gmail.com>
 * @version 1.0
 *
 * @section LICENSE
 *
 * To be defined
 *
 * @section DESCRIPTION
 *
 * The GetFeasibility Operation realizes a feasibility analysis of a given
 * acquisition.
 *
 * This class is not currently working as some part of the request analysis are
 * not yet available.
 *
 * It is now always sending back the same analysis results defined in the
 * getFeasibility method.
 */
public class GetFeasibilityOperation extends DreamEOSPSOperation<FASConfFolder, GetFeasibilityDocument, GetFeasibilityResponseDocument, GetFeasibilityFault> {

    
    private TaskingParametersType taskingParameters;
    
    private SatellitePlatform platform;
    private String sensorId;
    private String sensorType;
    
    
    private SensorFeasibilityAnalysisHandler feasibilityHandler;
    
    /**
     *
     * @param request
     */
    public GetFeasibilityOperation(FASConfFolder loader, GetFeasibilityDocument request) {

        super(loader, request);
    }

    @Override
    public void validRequest() throws GetFeasibilityFault {
        try {
            platform = this.getServiceConfiguration().getSatellitePlatform();
        } catch (IOException|ConfFileNotFoundException ex) {
            Logger.getLogger(GetFeasibilityOperation.class.getName()).log(Level.SEVERE, null, ex);
            
            throw new GetFeasibilityFault("Configuration not found.");
        }
        
        
        String folderName = ConfFolder.DREAM_WS_CONF_FOLDER + File.separator + 
                "fas" + File.separator + 
                "Sentinel1" + File.separator + 
                "feasibilityRequests" + File.separator;
        
        File folder = new File(folderName);
        if (!folder.exists() || !folder.isDirectory()) {
            folder.mkdirs();
        }
        File[] files = folder.listFiles();
        int nbFiles = 0;
        if (files != null && files.length > 0) {
            nbFiles = files.length;
        }
        
        
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(
                    folderName + nbFiles, 
                    "UTF-8");
            writer.print(getRequest().xmlText());
            writer.flush();
            writer.close();
            
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            Logger.getLogger(GetFeasibilityOperation.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (writer != null)
                writer.close();
        }
        
        
        
        
        //----------------------------------------------------------------------------
        // Decoding request
        //----------------------------------------------------------------------------
        GetFeasibilityType feasibility = this.getRequest().getGetFeasibility();

        String procedure = feasibility.getProcedure();
        if (procedure == null || procedure.equals("")) {
            throw new GetFeasibilityFault("Please choose sensor or sensor type to task through the 'procedure' parameter.");
        }

        taskingParameters = feasibility.getEoTaskingParameters();
        if (taskingParameters == null || taskingParameters.getCoverageProgrammingRequest() == null) {
            throw new GetFeasibilityFault("Please fill in a TaskingParameter:CoverageProgrammingRequest structure to task a (set of) sensor(s).");
        }
        CoverageProgrammingRequestType coverage = taskingParameters.getCoverageProgrammingRequest();

        if (procedure.equalsIgnoreCase("sar")) {
            procedure = "S1SAR";
        }
        
        if (!procedure.equals("S1SAR"))
            throw new GetFeasibilityFault("Sensor" + procedure + " is not handled by this server.");
        
        sensorId = procedure;

        sensorType = "SAR";

        double minIncAz = -90;
        double maxIncAz = 90;
        double minIncEl = -90;
        double maxIncEl = 90;
        double minPoiAc = -90;
        double maxPoiAc = 90;
        double minPoiAl = -90;
        double maxPoiAl = 90;

        Polygon coords = null;
        String roiType = null;
        if (coverage.getRegionOfInterest() != null &&
                coverage.getRegionOfInterest().getPolygonArray() != null &&
                coverage.getRegionOfInterest().getPolygonArray(0).getExterior() != null) {

            CoordinatesType c = ((LinearRingType)
                    coverage.getRegionOfInterest().getPolygonArray(0).getExterior().getAbstractRing()).getCoordinates();

            String dec = c.getDecimal();
            String cs = c.getCs();
            String ts = c.getTs();

            String[] p = c.getStringValue().split(ts);

            List<Point> points = null;

            if (p != null && p.length > 0 && !p[0].equals("")) {

                points = new ArrayList<>();
                for (int i = 0; i < p.length; i++) {

                    String[] coord = p[i].split(cs);

                    double longitude = Double.valueOf(coord[1].replace(dec, "."));
                    double latitude = Double.valueOf(coord[0].replace(dec, "."));

                    points.add(new Point(longitude, latitude, 0.0));
                }
            }

            coords = new Polygon(points);
            roiType = "POLYGON";
        }

        Date begin = null;
        Date end = null;

        if (coverage.getTimeOfInterest() != null &&
                coverage.getTimeOfInterest().getSurveyPeriodArray() != null &&
                coverage.getTimeOfInterest().getSurveyPeriodArray(0).getTimePeriod() != null)
        {
            TimePeriodType toi = coverage.getTimeOfInterest().getSurveyPeriodArray(0).getTimePeriod();

            String b = toi.getBeginPosition().getStringValue();
            String e = toi.getEndPosition().getStringValue();

            System.out.println("UPdatesddsdgvsdtu");

            if (b != null) {
                try {
                    begin = DateHandler.parseDate(b);
                } catch (ParseException ex) {
                    Logger.getLogger(GetFeasibilityOperation.class.getName()).log(Level.SEVERE, null, ex);
                    throw new GetFeasibilityFault("Error parsing date : " + b);
                } catch (NullPointerException ex) {
                    Logger.getLogger(GetFeasibilityOperation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (e != null) {
                try {
                    end = DateHandler.parseDate(e);
                } catch (ParseException ex) {
                    Logger.getLogger(GetFeasibilityOperation.class.getName()).log(Level.SEVERE, null, ex);
                    throw new GetFeasibilityFault("Error parsing date : " + e);
                } catch (NullPointerException ex) {
                    Logger.getLogger(GetFeasibilityOperation.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        MonoscopicAcquisitionType monoAcq = coverage.getAcquisitionType().getMonoscopicAcquisitionArray(0);

        if (monoAcq != null) {

            AcquisitionAngleType acqAngle = monoAcq.getAcquisitionAngle();
            if (acqAngle != null) {
                IncidenceRangeType inc = acqAngle.getIncidenceRange();
                PointingRangeType poi = acqAngle.getPointingRange();
                if (inc != null) {
                    if (inc.getAzimuthAngle() != null)
                    {
                        try {
                            minIncAz = Double.valueOf(inc.getAzimuthAngle().getValue().get(0).toString());
                            maxIncAz = Double.valueOf(inc.getAzimuthAngle().getValue().get(1).toString());
                        } catch (NumberFormatException | NullPointerException e) { }
                    }
                    if (inc.getElevationAngle() != null)
                    {
                        try {
                            minIncEl = Double.valueOf(inc.getElevationAngle().getValue().get(0).toString());
                            maxIncEl = Double.valueOf(inc.getElevationAngle().getValue().get(1).toString());
                        } catch (NumberFormatException | NullPointerException e) { }
                    }
                }
                if (poi != null) {

                    if (poi.getAcrossTrackAngle() != null)
                    {
                        try {
                            minPoiAc = Double.valueOf(poi.getAcrossTrackAngle().getValue().get(0).toString());
                            maxPoiAc = Double.valueOf(poi.getAcrossTrackAngle().getValue().get(1).toString());
                        } catch (NumberFormatException | NullPointerException e) { }
                    }

                    if (poi.getAlongTrackAngle() != null)
                    {
                        try {
                            minPoiAl = Double.valueOf(poi.getAlongTrackAngle().getValue().get(0).toString());
                            maxPoiAl = Double.valueOf(poi.getAlongTrackAngle().getValue().get(1).toString());
                        } catch (NumberFormatException | NullPointerException e) { }
                    }
                }
            }

            //Sensor specific parameters
            if (sensorType.equalsIgnoreCase("SAR")) {

                AcquisitionParametersSARType sarAcqParams = monoAcq.getAcquisitionParameters().getAcquisitionParametersSAR();

                int minGR = 0;
                int maxGR = 100;
                List<String> instModes = null;
                boolean fusionAccepted = true;
                List<String> polModes = null;

                double maxNoiseLevel = 0.0;
                double maxAmbiguityLevel = 0.0;

                if (sarAcqParams != null) {

                    if (sarAcqParams.getGroundResolution() != null) {

                        List gr = sarAcqParams.getGroundResolution().getValue();
                        if (gr.get(0) != null)
                            try {
                                minGR = Integer.valueOf(gr.get(0).toString());
                            } catch (NumberFormatException e) {}
                        if (gr.size() > 1 && gr.get(1) != null)
                            try {
                                maxGR = Integer.valueOf(gr.get(1).toString());
                            } catch (NumberFormatException e) {}
                    }

                    if (sarAcqParams.getInstrumentMode() != null)
                        instModes = Arrays.asList(new String[]{sarAcqParams.getInstrumentMode().getValue()});

                    if (sarAcqParams.getFusionAccepted() != null)
                        fusionAccepted = sarAcqParams.getFusionAccepted().getValue();

                    if (sarAcqParams.getPolarizationMode() != null)                        
                        polModes = Arrays.asList(new String[]{sarAcqParams.getPolarizationMode().getValue()});
                }

                ValidationParametersSARType sarValParam = coverage.getValidationParameters().getValidationParametersSAR();

                if (sarValParam != null)
                {
                    if (sarValParam.getMaxNoiseLevel() != null)
                        maxNoiseLevel = sarValParam.getMaxNoiseLevel().getValue();

                    if (sarValParam.getMaxAmbiguityLevel() != null)
                        maxAmbiguityLevel = sarValParam.getMaxAmbiguityLevel().getValue();
                }
                SARTaskingParameters sarParameters = new SARTaskingParameters(
                        maxNoiseLevel, 
                        maxAmbiguityLevel, 
                        fusionAccepted, 
                        polModes, 
                        minIncAz, maxIncAz, minIncEl, maxIncEl, minPoiAc, maxPoiAc, minPoiAl, maxPoiAl, 
                        coords, roiType, 
                        Arrays.asList(new TimePeriod[]{new TimePeriod(begin, end)}), 
                        minGR, maxGR, 
                        instModes);

                this.feasibilityHandler = new SARSensorFeasibilityAnalysisHandler(
                        sarParameters, 
                        "Sentinel1",
                        sensorId);
            }
        }
    }

    @Override
    public void executeRequest() throws GetFeasibilityFault {

        //Handling request
        this.validRequest();
        
        try {
            this.feasibilityHandler.doFeasibility();
        } catch (SQLException ex) {
            Logger.getLogger(GetFeasibilityOperation.class.getName()).log(Level.SEVERE, null, ex);
            
            throw new GetFeasibilityFault("SQLException", ex);
        } catch (EoCfiHndlrError ex) {
            Logger.getLogger(GetFeasibilityOperation.class.getName()).log(Level.SEVERE, null, ex);
            
            throw new GetFeasibilityFault("EOCFI error", ex);
        }
        
        // Creating response
        this.setResponse(this.createResponse());
    }
    
    /**
     * Generates the XML response from the data contained in the feasibilityHandler
     * NB : this function is only to be used after the feasibilityHandler has performed the doFeasibility method
     * @return the XML response to send back to the client
     */
    private GetFeasibilityResponseDocument createResponse() {
        
        Status status = this.feasibilityHandler.getStatus();
        
        GetFeasibilityResponseDocument response = GetFeasibilityResponseDocument.Factory.newInstance();
        GetFeasibilityResponseType resp = response.addNewGetFeasibilityResponse();
        
        //------------------------------------------------------------
        //Status report
        //------------------------------------------------------------
        StatusReportType statusReport = resp.addNewResult().addNewStatusReport();
        
        statusReport.setProcedure(this.sensorId);
        
        statusReport.setTask(this.feasibilityHandler.getTaskId());
        
        statusReport.setIdentifier(status.getIdentifier());
        statusReport.setUpdateTime(DateHandler.getCalendar(status.getUpdateTime()));
        statusReport.setEstimatedToC(DateHandler.getCalendar(status.getEstimatedTimeOfCompletion()));
        statusReport.setPercentCompletion(status.getPercentCompletion());
        statusReport.addNewStatusMessage().setStringValue(status.getMessage());

        //Add the parameters of the request to the response (informative)
        statusReport.addNewEoTaskingParameters().set(
                taskingParameters
            );
        //------------------------------------------------------------
        //Feasibility study
        //------------------------------------------------------------
        
        FeasibilityStudyDocument study = this.getFeasibilityStudy();
        resp.addNewExtension().set(study);
        
        
        String folderName = ConfFolder.DREAM_WS_CONF_FOLDER + File.separator + 
                "fas" + File.separator + 
                "Sentinel1" + File.separator + 
                "feasibilityResponses" + File.separator;
        
        
        File folder = new File(folderName);
        if (!folder.exists() || !folder.isDirectory()) {
            folder.mkdirs();
        }
        File[] files = folder.listFiles();
        int nbFiles = 0;
        if (files != null && files.length > 0) {
            nbFiles = files.length;
        }
        
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(
                    folderName + nbFiles, 
                    "UTF-8");
            writer.print(response.xmlText());
            writer.flush();
            writer.close();
            
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            Logger.getLogger(GetFeasibilityOperation.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (writer != null)
                writer.close();
        }
        
        return response;
    }
    
    
    /**
     * Creates the FeasibilityStudyDocument from the data of the SensorFeasibilityAnalysisHandler
     * @return 
     */
    private FeasibilityStudyDocument getFeasibilityStudy() {
        
        FeasibilityStudyDocument studyDoc = FeasibilityStudyDocument.Factory.newInstance();
        FeasibilityStudyType study = studyDoc.addNewFeasibilityStudy();
        
        study.addNewEstimatedCost().setDoubleValue(this.feasibilityHandler.getEstimatedCost());
        study.getEstimatedCost().setUom(this.feasibilityHandler.getCurrency());
        study.setId(this.feasibilityHandler.getTaskId());
        
        study.setSegmentArray(this.addSegments());
        
        return studyDoc;
    }
    
    /**
     * Creates the Segments response structure from the data of the SensorFeasibilityAnalysisHandler
     * @return 
     */
    private SegmentPropertyType[] addSegments() {
        
        List<Segment> segments = this.feasibilityHandler.getSegments();
        
        System.out.println("null ? " + (segments == null) + "\nsize ? " + segments.size());
        
        SegmentPropertyType[] ss = new SegmentPropertyType[segments.size()];
        
        int i = 0;
        for (Segment segment : segments) {
            
            SegmentPropertyType s = SegmentPropertyType.Factory.newInstance();
            
            SegmentType seg = s.addNewSegment();
            
            //Times
            seg.setAcquisitionStartTime(segment.getStartOfAcquisition());
            seg.setAcquisitionStopTime(segment.getEndOfAcquisition());

            seg.setId(segment.getSegmentId());

            //EOP
            EarthObservationEquipmentType eoEquipment = seg.addNewAcquisitionMethod().addNewEarthObservationEquipment();
            //Sensor
            CodeType st = eoEquipment.addNewSensor().addNewSensor().addNewSensorType();
            st.setCodeSpace("urn:ogc:def:property:OGC:sensorType");
            st.setStringValue(sensorType);
            
            eoEquipment.addNewInstrument().addNewInstrument().setShortName(sensorId);
            //Platform
            PlatformType plat = eoEquipment.addNewPlatform().addNewPlatform();
            plat.setSerialIdentifier(platform.getId());
            plat.setOrbitType(OrbitTypeValueType.Enum.forString(platform.getOrbit().getOrbitType()));
            plat.setShortName(platform.getName());

            eoEquipment.addNewIdentifier().setStringValue(platform.getId() + " - " + sensorId);

            eoEquipment.addNewDescription().setStringValue(platform.getDescription());

            //Polygon
            PolygonType polygon = seg.addNewFootprint().addNewPolygon();

            CoordinatesType coords = CoordinatesType.Factory.newInstance();
            coords.setDecimal(".");
            coords.setCs(",");
            coords.setTs(" ");

            coords.setStringValue(segment.getPolygon().printCoordinatesGML());

            LinearRingType lineRing = LinearRingType.Factory.newInstance();
            lineRing.setCoordinates(coords);

            polygon.addNewExterior().setAbstractRing(lineRing);

            ss[i] = s;
            i++;
        }
        
        return ss;
    }
    
    
}
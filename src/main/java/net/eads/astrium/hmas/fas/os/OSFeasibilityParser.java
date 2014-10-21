/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               OSFeasibilityParser.java
 *   File Type                                          :               Source Code
 *   Description                                        :                *
 * --------------------------------------------------------------------------------------------------------
 *
 * =================================================================
 *             (coffee) COPYRIGHT EADS ASTRIUM LIMITED 2013. All Rights Reserved
 *             This software is supplied by EADS Astrium Limited on the express terms
 *             that it is to be treated as confidential and that it may not be copied,
 *             used or disclosed to others for any purpose except as authorised in
 *             writing by this Company.
 * --------------------------------------------------------------------------------------------------------
 *//*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eads.astrium.hmas.fas.os;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.MultivaluedMap;
import net.eads.astrium.dream.util.DateHandler;
import net.eads.astrium.dream.util.structures.tasking.geometries.Circle;
import net.eads.astrium.dream.util.structures.tasking.geometries.Geometry;
import net.eads.astrium.dream.util.structures.tasking.geometries.Point;
import net.eads.astrium.dream.util.structures.tasking.geometries.Polygon;
import net.opengis.eosps.x20.AcquisitionAngleType;
import net.opengis.eosps.x20.AcquisitionParametersOPTType;
import net.opengis.eosps.x20.AcquisitionParametersSARType;
import net.opengis.eosps.x20.AcquisitionParametersType;
import net.opengis.eosps.x20.CoverageProgrammingRequestType;
import net.opengis.eosps.x20.GetFeasibilityDocument;
import net.opengis.eosps.x20.GetFeasibilityType;
import net.opengis.eosps.x20.IncidenceRangeType;
import net.opengis.eosps.x20.MonoscopicAcquisitionType;
import net.opengis.eosps.x20.PointingRangeType;
import net.opengis.eosps.x20.RegionOfInterestType;
import net.opengis.eosps.x20.SurveyPeriodDocument;
import net.opengis.eosps.x20.TaskingParametersType;
import net.opengis.eosps.x20.TimeOfInterestType;
import net.opengis.eosps.x20.ValidationParametersOPTType;
import net.opengis.eosps.x20.ValidationParametersSARType;
import net.opengis.gml.x32.AbstractRingPropertyType;
import net.opengis.gml.x32.CoordinatesType;
import net.opengis.gml.x32.LinearRingType;
import net.opengis.gml.x32.TimePeriodType;
import net.opengis.gml.x32.TimePositionType;
import net.opengis.swe.x20.CategoryType;
import net.opengis.swe.x20.QuantityRangeType;
import net.opengis.swe.x20.QuantityType;

/**
 *
 * @author re-sulrich
 */
public class OSFeasibilityParser {

    private static List<String> OPT_PARAMS = Arrays.asList(new String[]{"minLum", "cloudCover", "snowCover", "hazeAccepted", "sandWindAccepted", "maxSunGlint"});
    private static List<String> SAR_PARAMS = Arrays.asList(new String[]{"polarisationMode", "maxNoiseLevel", "maxAmbiguityLevel"});

    /**
     *
     * @param loader The DatabaseLoader that will permit to get the SensorType
     * of a given sensor
     * @param configuration The Map that contains the parameters of the GET
     * method sent by the client
     * @return A proper EOSPS GetFeasibilityDocument to be passed to the
     * GetFeasibilityOperation
     * @throws ParseException
     * @throws SQLException
     */
    public static GetFeasibilityDocument parseGetFeasibilityGetMethod(
            MultivaluedMap<String, String> configuration) throws ParseException {

        //Copy the input map to a local map
        //That way, we can delete the used parameters (from the local map)
        //To keep only the OPT/SAR specific ones
        Map<String, String> conf = new HashMap<>();
        for (String parameter : configuration.keySet()) {

            System.out.println("Putting " + parameter + ", value is : " + configuration.getFirst(parameter));
            
            conf.put(parameter, configuration.getFirst(parameter));
        }

        //Global parameters
        String format = "text/xml";
        String version = "2.0";
        String service = "EOSPS";

        //Region Of Interest

        
        
        
        
        
        //Box (13-039) is two points
        //To be changed to polygon (EOSPS) = parseable string : lat.1,lon.1 lat.2,lon.2 ... lat.N,lon.N
        
        Geometry geometry = null;
        
        if (conf.get("box") != null && !conf.get("box").equals("")) {
            String[] box = null;
        
            box = conf.remove("box").split(",");
            if (box.length < 4) {
                throw new ParseException("box", -1);
            }
            
            List<Point> points = new ArrayList<>();
            points.add(new Point(Double.valueOf(box[0]), Double.valueOf(box[1]), 0.0));
            points.add(new Point(Double.valueOf(box[0]), Double.valueOf(box[3]), 0.0));
            points.add(new Point(Double.valueOf(box[2]), Double.valueOf(box[3]), 0.0));
            points.add(new Point(Double.valueOf(box[2]), Double.valueOf(box[1]), 0.0));
            points.add(new Point(Double.valueOf(box[0]), Double.valueOf(box[1]), 0.0));
            
            geometry = new Polygon(points);
        }
        
        if (conf.get("radius") != null && !conf.get("radius").equals("")) {
            double lon = Double.valueOf(conf.remove("lon"));
            double lat = Double.valueOf(conf.remove("lat"));
            double radius = Double.valueOf(conf.remove("radius"));
            
            geometry = new Circle(new Point(lon, lat, 0.0), radius);
        }
        
        if (conf.get("geometry") != null && !conf.get("geometry").equals("")) {
            
            geometry = parseGeometry(conf.remove("geometry"));
        }
        if (geometry != null)
            System.out.println("Geometry : " + geometry.getClass().getName() + " : " + geometry.printCoordinatesGML());
        else {
            System.out.println("Geometry null");
            throw new ParseException("Could not read the Area of Interest", 0);
        }
        
        
        
        //TOI
        String begin = conf.remove("start"); //"2013-06-30T00:00:00Z";
        String end = conf.remove("end"); //"2013-07-20T00:00:00Z";

        if (begin == null || begin.equals("")) {
            begin = DateHandler.formatDate(Calendar.getInstance().getTime());
            System.out.println("Setting begin time to now");
        }
        if (end == null || end.equals("")) {
            throw new ParseException("Date parameters could not be parsed", -1);
        }
        
        //Acquisition angles
        String incidenceAz = conf.remove("incAz");
        String incidenceEl = conf.remove("incEl");
        String pointingAc = conf.remove("pointAc");
        String pointingAl = conf.remove("pointAl");

        double minIncidenceAzimuth = 0.0;
        double maxIncidenceAzimuth = 0.0;
        double minIncidenceElevation = 0.0;
        double maxIncidenceElevation = 0.0;
        double minPointingAcross = 0.0;
        double maxPointingAcross = 0.0;
        double minPointingAlong = 0.0;
        double maxPointingAlong = 0.0;

        if (incidenceAz != null && !incidenceAz.equals("")) {
            try {
                minIncidenceAzimuth = Double.valueOf(incidenceAz.split(",")[0]);
            } catch (Exception e) {
            }

            try {
                maxIncidenceAzimuth = Double.valueOf(incidenceAz.split(",")[1]);
            } catch (Exception e) {
            }
        }
        
        if (incidenceEl != null && !incidenceEl.equals("")) {
            try {
                minIncidenceElevation = Double.valueOf(incidenceEl.split(",")[0]);
            } catch (Exception e) {
            }

            try {
                maxIncidenceElevation = Double.valueOf(incidenceEl.split(",")[1]);
            } catch (Exception e) {
            }
        }

        if (pointingAc != null && !pointingAc.equals("")) {
            try {
                minPointingAcross = Double.valueOf(pointingAc.split(",")[0]);
            } catch (Exception e) {
            }

            try {
                maxPointingAcross = Double.valueOf(pointingAc.split(",")[1]);
            } catch (Exception e) {
            }
        }
        
        if (pointingAl != null && !pointingAl.equals("")) {
            try {
                minPointingAlong = Double.valueOf(pointingAl.split(",")[0]);
            } catch (Exception e) {
            }

            try {
                maxPointingAlong = Double.valueOf(pointingAl.split(",")[1]);
            } catch (Exception e) {
            }
        }

        //Satellite / Sensor
        String platform = conf.remove("platform");
        String sensorType = conf.remove("sensorType");
        String instrument = conf.remove("instrument");

        if (instrument != null && instrument.equals(""))
            instrument = null;
        
        //Setting the sensor type if is not null
        //Only Sentinel 1 implemented so sensor type is always SAR
        if (sensorType == null) {
            sensorType = "SAR";
        }

        //Parameters (are in opt/sar specific structures but are in both => common)
        //Fusion accepted (EOSPS) == compositeType(13-039)
        boolean fusionAccepted = false;
        String fa = conf.remove("compositeType");
        if (fa != null && fa.equalsIgnoreCase("true")) {
            fusionAccepted = true;
        }

        String coverageType = conf.remove("coverageType");
        
        String instrumentMode = conf.remove("sensorMode");
        
        if (instrumentMode == null || instrumentMode.equals("")) {
            instrumentMode = "SM";
        }

        String[] resolutions = null;
        if (conf.get("resolution") != null) {
            resolutions = conf.remove("resolution").split(",");
        }

        //Creating the basic structure to be returned
        GetFeasibilityDocument doc = GetFeasibilityDocument.Factory.newInstance();
        GetFeasibilityType feasibilityRequest = doc.addNewGetFeasibility();

        feasibilityRequest.setAcceptFormat(format);
        feasibilityRequest.setVersion(version);
        feasibilityRequest.setService(service);

        if (instrument != null) {

            feasibilityRequest.setProcedure(instrument);
        } else {
            if (sensorType != null) {

                feasibilityRequest.setProcedure(sensorType);
            }
            else {
                System.out.println("Sensor type null");
            }
        }
        
        TaskingParametersType eotp = feasibilityRequest.addNewEoTaskingParameters();
        CoverageProgrammingRequestType cov = eotp.addNewCoverageProgrammingRequest();

        RegionOfInterestType roi = cov.addNewRegionOfInterest();
        AbstractRingPropertyType ext = roi.addNewPolygon().addNewExterior();

        CoordinatesType coords = CoordinatesType.Factory.newInstance();
        coords.setDecimal(".");
        coords.setCs(",");
        coords.setTs(" ");
        coords.setStringValue(
                geometry.printCoordinatesGML() );
        LinearRingType lineRing = LinearRingType.Factory.newInstance();
        lineRing.setCoordinates(coords);
        ext.setAbstractRing(lineRing);

        TimeOfInterestType toi = cov.addNewTimeOfInterest();
        SurveyPeriodDocument.SurveyPeriod surveyPeriod = toi.addNewSurveyPeriod();
        TimePeriodType tp = surveyPeriod.addNewTimePeriod();
        TimePositionType beginPos = tp.addNewBeginPosition();
        beginPos.setStringValue(begin);
        TimePositionType endPos = tp.addNewEndPosition();
        endPos.setStringValue(end);




        MonoscopicAcquisitionType mono = cov.addNewAcquisitionType().addNewMonoscopicAcquisition();

        if (coverageType != null && !coverageType.equals(""))
            mono.addNewCoverageType().setValue(coverageType);

        AcquisitionAngleType acqAngle = mono.addNewAcquisitionAngle();

        IncidenceRangeType incAng = acqAngle.addNewIncidenceRange();
        QuantityRangeType az = incAng.addNewAzimuthAngle();
        QuantityRangeType el = incAng.addNewElevationAngle();
        az.addNewUom().setCode("deg");
        el.addNewUom().setCode("deg");

        List l1 = new ArrayList<Double>();
        l1.add(minIncidenceAzimuth);
        l1.add(maxIncidenceAzimuth);
        az.setValue(l1);

        List l2 = new ArrayList<Double>();
        l2.add(minIncidenceElevation);
        l2.add(maxIncidenceElevation);
        el.setValue(l2);

        PointingRangeType poiAng = acqAngle.addNewPointingRange();
        QuantityRangeType ac = poiAng.addNewAcrossTrackAngle();
        QuantityRangeType al = poiAng.addNewAlongTrackAngle();

        List l3 = new ArrayList<Double>();
        l3.add(minPointingAcross);
        l3.add(maxPointingAcross);
        ac.setValue(l3);

        List l4 = new ArrayList<Double>();
        l4.add(minPointingAlong);
        l4.add(maxPointingAlong);
        al.setValue(l4);

        if (coverageType != null && !coverageType.equals("")) {
            CategoryType covType = mono.addNewCoverageType();
            covType.setValue(coverageType);
        }
        
//        mono.addNewMaxCoupleDelay();
//        mono.addNewBHRatio();

        AcquisitionParametersType params = mono.addNewAcquisitionParameters();


        if (sensorType != null) {
            //OPT Specific
            if (sensorType.equalsIgnoreCase("opt")) {

                //OPT acquisition parameters
                AcquisitionParametersOPTType optap = params.addNewAcquisitionParametersOPT();
                optap.addNewFusionAccepted().setValue(fusionAccepted);

                optap.addNewInstrumentMode().setValue(instrumentMode);

                if (resolutions != null && resolutions.length > 0 && resolutions[0] != null && !resolutions[0].equals("")) {
                    
                    QuantityRangeType gr = optap.addNewGroundResolution();
                    List l5 = new ArrayList<Double>();
                    l5.add(resolutions[0]);
                    if (resolutions.length > 1) {
                        l5.add(resolutions[1]);
                    }
                    gr.setValue(l5);
                }
                
                double minLuminosity = 0.0;
                try {
                    String minLum =conf.get("minLum");
                    if (minLum != null && !minLum.equals("")) {
                        minLuminosity = Double.valueOf(minLum);
                        
                        QuantityType minLumi = optap.addNewMinLuminosity();
                        minLumi.addNewUom().setCode("W");
                        minLumi.setValue(minLuminosity);
                    }
                
                } catch (NumberFormatException e) {
                }
                
                ValidationParametersOPTType optvp = cov.addNewValidationParameters().addNewValidationParametersOPT();
                
                //OPT validation parameters
                String cC = conf.get("cloudCover");
                double cloudCover = 0.0;
                try {
                    if (cC != null && !cC.equals("")) {
                        cloudCover = Double.valueOf(cC);
                        QuantityType cc = optvp.addNewMaxCloudCover();
                        cc.addNewUom().setCode("%");
                        cc.setValue(cloudCover);
                    }
                } catch (NumberFormatException e) {
                }

                String sC = conf.get("snowCover");
                double snowCover = 0.0;
                try {
                    if (sC != null && !sC.equals("")) {
                        snowCover = Double.valueOf(sC);
                        QuantityType sc = optvp.addNewMaxSnowCover();
                        sc.addNewUom().setCode("%");
                        sc.setValue(snowCover);
                    }
                } catch (NumberFormatException e) {
                }

                boolean sandWindAccepted = false;
                String swa = conf.get("sandWindAccepted");
                if (swa != null && swa.equalsIgnoreCase("true")) {
                    sandWindAccepted = true;
                    optvp.addNewSandWindAccepted().setValue(sandWindAccepted);
                }

                boolean hazeAccepted = false;
                String ha = conf.get("hazeAccepted");
                if (ha != null && ha.equalsIgnoreCase("true")) {
                    hazeAccepted = true;                    
                    optvp.addNewHazeAccepted().setValue(hazeAccepted);
                }

                String mSG = conf.get("maxSunGlint");
                double maxSunGlint = 0.0;
                try {
                    if (mSG != null && !mSG.equals("")) {
                        maxSunGlint = Double.valueOf(mSG);
                        QuantityType msg = optvp.addNewMaxSunGlint();
                        msg.addNewUom().setCode("%");
                        msg.setValue(maxSunGlint);
                    }

                } catch (NumberFormatException e) {
                }
            }
            //SAR Specific
            if (sensorType.equalsIgnoreCase("sar")) {

                //SAR acquisition parameters
                AcquisitionParametersSARType sarap = params.addNewAcquisitionParametersSAR();

                sarap.addNewInstrumentMode().setValue(instrumentMode);

                sarap.addNewFusionAccepted().setValue(fusionAccepted);

                if (resolutions != null && resolutions.length > 0 && resolutions[0] != null && !resolutions[0].equals("")) {
                    
                    QuantityRangeType gr = sarap.addNewGroundResolution();
                    List l5 = new ArrayList<>();
                    l5.add(resolutions[0]);
                    if (resolutions.length > 1) {
                        l5.add(resolutions[1]);
                    }   
                    gr.setValue(l5);
                }
                
                String polarisationMode = conf.get("polMode");
                if (polarisationMode != null && !polarisationMode.equals(""))
                sarap.addNewPolarizationMode().setValue(polarisationMode);
                
                
                ValidationParametersSARType sarvp = cov.addNewValidationParameters().addNewValidationParametersSAR();
                
                //SAR validation parameters
                String mNL = conf.get("maxNoiseLevel");
                double noise = 0.0;
                try {
                    if (mNL != null && !mNL.equals(""))
                        noise = Double.valueOf(mNL);
                        QuantityType n = sarvp.addNewMaxNoiseLevel();
                        n.addNewUom().setCode("dB");
                        n.setValue(noise);
                } catch (NumberFormatException e) {
                }
                String mAL = conf.get("maxAmbiguityLevel");
                double ambiguity = 0.0;
                try {
                    if (mAL != null && !mAL.equals(""))
                        ambiguity = Double.valueOf(mAL);
                        QuantityType a = sarvp.addNewMaxAmbiguityLevel();
                        a.addNewUom().setCode("dB");
                        a.setValue(ambiguity);
                } catch (NumberFormatException e) {
                }
            }
        }

        return doc;
    }
    
    public static Geometry parseGeometry(String geometryString) {
        
        Geometry geometry = null;
        
        System.out.println("" + geometryString);
        
        String[] geom = geometryString.split("\\(", 2);
            
        String geomType = geom[0];
        String coords = geom[1].substring(0,geom[1].length() - 1);

        System.out.println("" + geomType + "\n" + coords + "\n" + coords.substring(1, coords.length() - 1));
        
        switch (geomType) {
//            POINT(6 10)
            case "POINT":

            break;
//            LINESTRING(3 4,10 50,20 25)
            case "LINESTRING":

            break;
//            POLYGON((1 1,5 1,5 5,1 5,1 1),(2 2,2 3,3 3,3 2,2 2))
            case "POLYGON":
                List<Point> points = new ArrayList<>();
                String[] pols = coords.substring(1, coords.length() - 1).split("\\),\\(");
                String exterior = pols[0];
                String[] pointsCoords = exterior.split(",");
                for (int i = 0; i < pointsCoords.length; i++) {
                    String[] pointCoords = pointsCoords[i].split(" ");
                    points.add(new Point(//Long, lat
                            Double.valueOf(pointCoords[0]), 
                            Double.valueOf(pointCoords[1]), 
                            0.0));
                }

                geometry = new Polygon(points);

            break;
//            MULTIPOINT(3.5 5.6, 4.8 10.5)
            case "MULTIPOINT":

            break;
//            MULTILINESTRING((3 4,10 50,20 25),(-5 -8,-10 -8,-15 -4))
            case "MULTILINESTRING":

            break;
//            MULTIPOLYGON(
            //(
            //(1 1,5 1,5 5,1 5,1 1),(2 2,2 3,3 3,3 2,2 2))
            //,
            //((6 3,9 2,9 4,6 3))
            //)
            case "MULTIPOLYGON":

            break;
        }

        return geometry;
    }
}

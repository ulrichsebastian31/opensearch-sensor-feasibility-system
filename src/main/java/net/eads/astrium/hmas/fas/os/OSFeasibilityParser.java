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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.MultivaluedMap;
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

            conf.put(parameter, configuration.getFirst(parameter));
        }

        //Global parameters
        String format = "text/xml";
        String version = "2.0";
        String service = "EOSPS";

        //Region Of Interest

        //Box (13-039) is two points
        //To be changed to polygon (EOSPS) = parseable string : lat.1,lon.1 lat.2,lon.2 ... lat.N,lon.N
        String[] box = null;
        String coordinates = "";
        if (conf.get("box") != null) {
            box = conf.remove("box").split(",");
            if (box.length < 4) {
                throw new ParseException("box", -1);
            }

            coordinates = ""
                    + box[0] + "," + box[1] + " "
                    + box[2] + "," + box[1] + " "
                    + box[2] + "," + box[3] + " "
                    + box[0] + "," + box[3] + " "
                    + box[0] + "," + box[1] + "";
        }

        //TOI
        String begin = conf.remove("start"); //"2013-06-30T00:00:00Z";
        String end = conf.remove("end"); //"2013-07-20T00:00:00Z";

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

        try {
            minIncidenceAzimuth = Double.valueOf(incidenceAz.split(",")[0]);
        } catch (Exception e) {
        }

        try {
            maxIncidenceAzimuth = Double.valueOf(incidenceAz.split(",")[1]);
        } catch (Exception e) {
        }

        try {
            minIncidenceElevation = Double.valueOf(incidenceEl.split(",")[0]);
        } catch (Exception e) {
        }

        try {
            maxIncidenceElevation = Double.valueOf(incidenceEl.split(",")[1]);
        } catch (Exception e) {
        }

        try {
            minPointingAcross = Double.valueOf(pointingAc.split(",")[0]);
        } catch (Exception e) {
        }

        try {
            maxPointingAcross = Double.valueOf(pointingAc.split(",")[1]);
        } catch (Exception e) {
        }

        try {
            minPointingAlong = Double.valueOf(pointingAl.split(",")[0]);
        } catch (Exception e) {
        }

        try {
            maxPointingAlong = Double.valueOf(pointingAl.split(",")[1]);
        } catch (Exception e) {
        }

        //Satellite / Sensor
        String platform = conf.remove("platform");
        String sensorType = conf.remove("sensorType");
        String instrument = conf.remove("instrument");

        if (instrument != null && instrument.equals(""))
            instrument = null;
        
        //Setting the sensor type if is not null
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
        coords.setStringValue(coordinates);
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

        CategoryType covType = mono.addNewCoverageType();
        covType.setValue(coverageType);
        
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

                if (resolutions != null && resolutions.length > 0) {

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
                    if (conf.get("minLum") != null)
                        minLuminosity = Double.valueOf(conf.get("minLum"));
                } catch (NumberFormatException e) {
                }

                QuantityType minLumi = optap.addNewMinLuminosity();
                minLumi.addNewUom().setCode("W");
                minLumi.setValue(minLuminosity);

                //OPT validation parameters
                String cC = conf.get("cloudCover");
                double cloudCover = 0.0;
                try {
                    if (cC != null)
                        cloudCover = Double.valueOf(cC);
                } catch (NumberFormatException e) {
                }

                String sC = conf.get("snowCover");
                double snowCover = 0.0;
                try {
                    if (sC != null)
                    snowCover = Double.valueOf(sC);
                } catch (NumberFormatException e) {
                }

                boolean sandWindAccepted = false;
                String swa = conf.get("sandWindAccepted");
                if (swa != null && swa.equalsIgnoreCase("true")) {
                    sandWindAccepted = true;
                }

                boolean hazeAccepted = false;
                String ha = conf.get("hazeAccepted");
                if (ha != null && ha.equalsIgnoreCase("true")) {
                    hazeAccepted = true;
                }

                String mSG = conf.get("maxSunGlint");
                double maxSunGlint = 0.0;
                try {
                    if (mSG != null)
                        maxSunGlint = Double.valueOf(mSG);
                } catch (NumberFormatException e) {
                }

                ValidationParametersOPTType optvp = cov.addNewValidationParameters().addNewValidationParametersOPT();
                QuantityType cc = optvp.addNewMaxCloudCover();
                cc.addNewUom().setCode("%");
                cc.setValue(cloudCover);
                QuantityType sc = optvp.addNewMaxSnowCover();
                sc.addNewUom().setCode("%");
                sc.setValue(snowCover);
                QuantityType msg = optvp.addNewMaxSunGlint();
                msg.addNewUom().setCode("%");
                msg.setValue(maxSunGlint);

                optvp.addNewHazeAccepted().setValue(hazeAccepted);
                optvp.addNewSandWindAccepted().setValue(sandWindAccepted);

            }
            //SAR Specific
            if (sensorType.equalsIgnoreCase("sar")) {

                //SAR acquisition parameters
                AcquisitionParametersSARType sarap = params.addNewAcquisitionParametersSAR();

                sarap.addNewInstrumentMode().setValue(instrumentMode);

                sarap.addNewFusionAccepted().setValue(fusionAccepted);

                
                if (resolutions != null && resolutions.length > 0) {
                    
                    QuantityRangeType gr = sarap.addNewGroundResolution();
                    List l5 = new ArrayList<>();
                    l5.add(resolutions[0]);
                    if (resolutions.length > 1) {
                        l5.add(resolutions[1]);
                    }   
                    gr.setValue(l5);
                }
                
                String polarisationMode = conf.get("polMode");
                sarap.addNewPolarizationMode().setValue(polarisationMode);
                
                String mNL = conf.get("maxNoiseLevel");
                String mAL = conf.get("maxAmbiguityLevel");
                
                //SAR validation parameters
                double noise = 0.0;
                try {
                    if (mNL != null)
                        noise = Double.valueOf(mNL);
                } catch (NumberFormatException e) {
                }
                double ambiguity = 0.0;
                try {
                    if (mAL != null)
                        ambiguity = Double.valueOf(mAL);
                } catch (NumberFormatException e) {
                }

                ValidationParametersSARType sarvp = cov.addNewValidationParameters().addNewValidationParametersSAR();
                QuantityType n = sarvp.addNewMaxNoiseLevel();
                n.addNewUom().setCode("dB");
                n.setValue(noise);
                QuantityType a = sarvp.addNewMaxAmbiguityLevel();
                a.addNewUom().setCode("dB");
                a.setValue(ambiguity);


            }
        }

        return doc;
    }
}

package net.eads.astrium.hmas.fas.operations;

import net.eads.astrium.hmas.operations.DreamEOSPSOperation;
import java.util.ArrayList;
import net.eads.astrium.hmas.exceptions.DescribeTaskingFault;
import net.eads.astrium.hmas.fas.conf.FASConfFolder;

import net.opengis.eosps.x20.AcquisitionAngleType;
import net.opengis.eosps.x20.AcquisitionParametersOPTType;
import net.opengis.eosps.x20.AcquisitionParametersSARType;
import net.opengis.eosps.x20.AcquisitionParametersType;
import net.opengis.eosps.x20.AcquisitionTypeType;
import net.opengis.eosps.x20.CoverageProgrammingRequestType;
import net.opengis.eosps.x20.DescribeTaskingDocument;
import net.opengis.eosps.x20.DescribeTaskingResponseDocument;
import net.opengis.eosps.x20.DescribeTaskingResponseType;
import net.opengis.eosps.x20.IncidenceRangeType;
import net.opengis.eosps.x20.MonoscopicAcquisitionType;
import net.opengis.eosps.x20.RegionOfInterestType;
import net.opengis.eosps.x20.StereoscopicAcquisitionType;
import net.opengis.eosps.x20.SurveyPeriodDocument;
import net.opengis.eosps.x20.TaskingParametersType;
import net.opengis.eosps.x20.TimeOfInterestType;
import net.opengis.gml.x32.AbstractRingPropertyType;
import net.opengis.gml.x32.CircleType;
import net.opengis.gml.x32.CoordinatesType;
import net.opengis.gml.x32.LinearRingType;
import net.opengis.gml.x32.PolygonType;
import net.opengis.gml.x32.StringOrRefType;
import net.opengis.swe.x20.AllowedTokensPropertyType;
import net.opengis.swe.x20.AllowedTokensType;
import net.opengis.swe.x20.AllowedValuesPropertyType;
import net.opengis.swe.x20.AllowedValuesType;
import net.opengis.swe.x20.CategoryType;
import net.opengis.swe.x20.QuantityRangeType;
import net.opengis.swe.x20.Reference;
import net.opengis.swe.x20.UnitReference;


/**
 * @file DescribeTaskingOperation.java
 * @author Sebastian Ulrich <sebastian_ulrich@hotmail.fr>
 * @version 1.0
 * 
 * @section LICENSE
 * 
 *          To be defined
 * 
 * @section DESCRIPTION
 * 
 *          The DescribeTasking Operation gives information about the way the sensor can be tasked.
 *          
 *          This class is not currently working as some part of the response creation are not yet available.
 *          Plus, a lot of parameters should be found in a config file and not hard-coded.
 */
public class DescribeTaskingOperation extends DreamEOSPSOperation<FASConfFolder,DescribeTaskingDocument,DescribeTaskingResponseDocument, DescribeTaskingFault> {

	/**
	 * 
	 * @param request
	 */
	public DescribeTaskingOperation(FASConfFolder confFolder, DescribeTaskingDocument request){

		super(confFolder, request);
	}

	@Override
	public void validRequest() throws DescribeTaskingFault {
	}

	@Override
	public void executeRequest() throws DescribeTaskingFault {
		
		this.validRequest();
		
		DescribeTaskingResponseDocument responseDocument = DescribeTaskingResponseDocument.Factory.newInstance();
		
		DescribeTaskingResponseType response = responseDocument.addNewDescribeTaskingResponse2();
                
                TaskingParametersType taskingParam = response.addNewEoTaskingParameters();
                
		CoverageProgrammingRequestType coverage = this.getCoverageProgrammingRequest();
		
		taskingParam.setCoverageProgrammingRequest(coverage);

		this.setResponse(responseDocument);
	}

	/**
	 * Creates a coverage Programming request and fills it in with :
	 *  - an AcquisitionType
	 *  - a Region of Interest
	 *  - a Time of Interest
	 * @return
	 */
	public CoverageProgrammingRequestType getCoverageProgrammingRequest()
	{
            CoverageProgrammingRequestType coverage = CoverageProgrammingRequestType.Factory.newInstance();

            coverage.setAcquisitionType(this.getAcquisitionType());

            coverage.setRegionOfInterest(this.getRegionOfInterest());

            coverage.setTimeOfInterest(this.getTimeOfInterest());
            
            return coverage;
	}
	
	
	/**
	 * Creates a Time of Interest type
	 * @return
	 */
	private TimeOfInterestType getTimeOfInterest() {
		
		TimeOfInterestType toiType = TimeOfInterestType.Factory.newInstance();
                SurveyPeriodDocument.SurveyPeriod surveyPeriod = toiType.addNewSurveyPeriod();
		surveyPeriod.addNewTimePeriod();
                        
//		surveyPeriod.setLabel("Survey Period");
//		
//		
//		AllowedTimesType times = surveyPeriod.addNewConstraint().addNewAllowedTimes();
//		
//		
//		
//		TimePosition pos= times.addNewValue();
//		pos.setStringValue("<swe:timePosition>0000-00-00T00:00:00Z</swe:timePosition> ");
//		
//		
//		UnitReference ref = surveyPeriod.addNewUom();
//		ref.setHref("urn:ogc:def:unit:ISO-8601::DateTime");
//		
//		surveyPeriod.setUom(ref);
		
		return toiType;
	}

	/**
	 * Creates a Region of Interest type
	 * @return
	 */
	private RegionOfInterestType getRegionOfInterest() {
		

		CoordinatesType coords = CoordinatesType.Factory.newInstance();
		coords.setDecimal(".");
		coords.setCs(",");
		coords.setTs(" ");
		
		RegionOfInterestType roiType = RegionOfInterestType.Factory.newInstance();
		
		PolygonType polygon = roiType.addNewPolygon();
		
		StringOrRefType def = StringOrRefType.Factory.newInstance();
		def.setStringValue("" +
				"A Polygon is a special surface that is defined by a single surface patch (see D.3.6). " +
				"The boundary of this patch is coplanar and the polygon uses planar interpolation in its interior." +
				"The elements exterior and interior describe the surface boundary of the polygon.");
		
		polygon.setDescription(def);
		
		AbstractRingPropertyType exterior = polygon.addNewExterior();
		//TODO : Complete
		LinearRingType lineRing = LinearRingType.Factory.newInstance();
		lineRing.setCoordinates(coords);
		
		
		exterior.setAbstractRing(lineRing);

		//TODO : Complete
		CircleType circle = roiType.addNewCircle();
		
		circle.setCoordinates(coords);
		//Complete polygon and circle structures here

		return roiType;
		
		
		// We don't have the right polygon structure
		
//		<swe:item name="Polygon">
//        <swe:DataRecord
//          definition="http://www.opengis.net/def/objectType/ISO-19107/2003/GM_Polygon">
//          <swe:field name="Exterior">
//            <swe:DataArray
//              definition="http://www.opengis.net/def/objectType/ISO-19107/2003/GM_Ring">
//              <swe:elementCount>
//                <swe:Count />
//              </swe:elementCount>
//              <swe:elementType name="Point">
//                <swe:Vector referenceFrame="http://www.opengis.net/def/crs/EPSG/0/4326">
//                  <swe:coordinate name="Lat">
//                    <swe:Quantity
//                      definition="http://www.opengis.net/def/property/OGC/0/GeodeticLatitude"
//                      axisID="Lat">
//                      <swe:uom code="deg" />
//                    </swe:Quantity>
//                  </swe:coordinate>
//                  <swe:coordinate name="Lon">
//                    <swe:Quantity
//                      definition="http://www.opengis.net/def/property/OGC/0/Longitude"
//                      axisID="Long">
//                      <swe:uom code="deg" />
//                    </swe:Quantity>
//                  </swe:coordinate>
//                </swe:Vector>
//              </swe:elementType>
//            </swe:DataArray>
//          </swe:field>
//        </swe:DataRecord>
//      </swe:item>
		
		
		
		
		//And we don't have the right Circle structure either
		
//      <swe:item name="Circle">
//        <swe:DataRecord
//          definition="http://www.opengis.net/def/objectType/ISO-19107/2003/GM_Circle">
//          <swe:field name="Center">
//            <swe:Vector referenceFrame="http://www.opengis.net/def/crs/EPSG/0/4326">
//              <swe:coordinate name="Lat">
//                <swe:Quantity
//                  definition="http://www.opengis.net/def/property/OGC/0/GeodeticLatitude"
//                  axisID="Lat">
//                  <swe:uom code="deg" />
//                </swe:Quantity>
//              </swe:coordinate>
//              <swe:coordinate name="Lon">
//                <swe:Quantity
//                  definition="http://www.opengis.net/def/property/OGC/0/Longitude"
//                  axisID="Long">
//                  <swe:uom code="deg" />
//                </swe:Quantity>
//              </swe:coordinate>
//            </swe:Vector>
//          </swe:field>
//          <swe:field name="Radius">
//            <swe:Quantity
//              definition="http://www.opengis.net/def/property/OGC-EO/0/Radius">
//              <swe:uom code="km" />
//            </swe:Quantity>
//          </swe:field>
//        </swe:DataRecord>
//      </swe:item>
		
		
	}

	/**
	 * Creates a Acquisition type
	 * @return
	 */
	public AcquisitionTypeType getAcquisitionType()
	{
		AcquisitionTypeType acquisitionType = AcquisitionTypeType.Factory.newInstance();
		
                //TODO : Stereoscopic acquisitions
                
		StereoscopicAcquisitionType stereo = acquisitionType.addNewStereoscopicAcquisition();
		
		MonoscopicAcquisitionType mono = acquisitionType.addNewMonoscopicAcquisition();
		
                //Fill in AcquisitionAngle type
		AcquisitionAngleType acquisitionAngle = mono.addNewAcquisitionAngle();
		
		IncidenceRangeType incidence = acquisitionAngle.addNewIncidenceRange();
                
		QuantityRangeType elevation = incidence.addNewElevationAngle();
		
		elevation.setDescription("Range of acceptable elevation incidence angles");
		elevation.setLabel("Elevation Incidence Angle Range");
		
		UnitReference ref = UnitReference.Factory.newInstance();
		ref.setCode("deg");
		
		elevation.setUom(ref);
		
		ArrayList<String> values = new ArrayList<String>();
		values.add("+10");																					//config files
		values.add("+50");																					//config files
		
		elevation.setValue(values);
		
		AllowedValuesPropertyType allowedValues = AllowedValuesPropertyType.Factory.newInstance();
		AllowedValuesType allowed = allowedValues.addNewAllowedValues();
		allowed.addInterval(values);
		
		elevation.setConstraint(allowedValues);
		
		//Fill in AcquisitionParameters type
		AcquisitionParametersType acquisitionParameters = mono.addNewAcquisitionParameters();
                AcquisitionParametersOPTType optAcquisitionParameters = acquisitionParameters.addNewAcquisitionParametersOPT();
                
		AcquisitionParametersSARType sarAcquisitionParameters = acquisitionParameters.addNewAcquisitionParametersSAR();
//		sarAcquisitionParameters.a
		//Fill in InstrumentMode type in AcquisitionParameters type
		CategoryType instrumentMode = sarAcquisitionParameters.addNewInstrumentMode();
		
		instrumentMode.setLabel("Instrument Mode");
		
		Reference instrumentCodeSpace = instrumentMode.addNewCodeSpace();
		instrumentCodeSpace.setHref("http://www.esa.int/registry/ASARModes");
		
		AllowedTokensPropertyType instrumentConstraint = instrumentMode.addNewConstraint();
		AllowedTokensType instrumentAllowedTokens = instrumentConstraint.addNewAllowedTokens();
		instrumentAllowedTokens.addValue("SM");																					//config files
		instrumentAllowedTokens.addValue("EW");																					//config files
		instrumentAllowedTokens.addValue("IW");																					//config files
		instrumentAllowedTokens.addValue("WV");																					//config files
		
		instrumentMode.setValue("SM");																							//config files
		
		//Fill in PolarizationMode type in AcquisitionParameters type
		CategoryType polarizationMode = sarAcquisitionParameters.addNewPolarizationMode();
		
		polarizationMode.setLabel("Polarization Mode");
		
		Reference polarizationCodeSpace = polarizationMode.addNewCodeSpace();
		polarizationCodeSpace.setHref("http://www.opengis.net/def/property/OGC-EO/0/sar/PolarizationModes");
		
		AllowedTokensPropertyType polarizationConstraint = polarizationMode.addNewConstraint();
		AllowedTokensType polarizationAllowedTokens = polarizationConstraint.addNewAllowedTokens();
		polarizationAllowedTokens.addValue("HH");																					//config files
		polarizationAllowedTokens.addValue("VV");																					//config files
		polarizationAllowedTokens.addValue("HH/HV");																				//config files
		polarizationAllowedTokens.addValue("VV/VH");																				//config files
		
		polarizationMode.setValue("HH");																							//config files
		
		return acquisitionType;
	}
	
}
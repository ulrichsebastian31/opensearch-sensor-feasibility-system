package net.eads.astrium.hmas.fas.operations;

import net.eads.astrium.hmas.exceptions.GetSensorAvailabilityFault;
import net.eads.astrium.hmas.fas.conf.FASConfFolder;
import net.eads.astrium.hmas.operations.DreamEOSPSOperation;

import net.opengis.eosps.x20.GetSensorAvailabilityDocument;
import net.opengis.eosps.x20.GetSensorAvailabilityResponseDocument;
import net.opengis.eosps.x20.GetSensorAvailabilityResponseType;
import net.opengis.eosps.x20.GetSensorAvailabilityResponseType.ResponsePeriod;
import net.opengis.gml.x32.TimePeriodType;
import net.opengis.gml.x32.TimePositionType;

/**
 * @file GetSensorAvailabilityOperation.java
 * @author Sebastian Ulrich <sebastian_ulrich@hotmail.fr>
 * @version 1.0
 * 
 * @section LICENSE
 * 
 *          To be defined
 * 
 * @section DESCRIPTION
 * 
 *          The GetSensorAvailability Operation indicates if the sensor is available at a given time period.
 */
public class GetSensorAvailabilityOperation extends DreamEOSPSOperation<FASConfFolder,GetSensorAvailabilityDocument,GetSensorAvailabilityResponseDocument, GetSensorAvailabilityFault> {

	private String sensorName;
	/**
	 * 
	 * @param request
	 */
	
	public GetSensorAvailabilityOperation(FASConfFolder confFolder, GetSensorAvailabilityDocument request){

		super(confFolder, request);
	}

	@Override
	public void validRequest() throws GetSensorAvailabilityFault {
		
	}

	@Override
	public void executeRequest() throws GetSensorAvailabilityFault {
		
            
            GetSensorAvailabilityResponseDocument responseDocument = GetSensorAvailabilityResponseDocument.Factory.newInstance();
            GetSensorAvailabilityResponseType response = responseDocument.addNewGetSensorAvailabilityResponse();

            //Complete response document
            

            //--------------------------------------------------------------
            //Start setting responsePeriod
            //--------------------------------------------------------------
            ResponsePeriod responsePeriod = response.addNewResponsePeriod();

            TimePeriodType responseTimePeriod = responsePeriod.addNewTimePeriod();

            TimePositionType responseTimePeriodBegin = responseTimePeriod.addNewBeginPosition();
            TimePositionType responseTimePeriodEnd = responseTimePeriod.addNewEndPosition();

            String begin = "2013-06-30T00:00:00Z";
            String m1 = "2013-07-08T00:00:00Z";
            String m2 = "2013-07-09T00:00:00Z";
            String end = "2013-07-20T00:00:00Z";

            //req : begin=2013-06-30T00:00:00Z&end=2013-07-20T00:00:00Z
            responseTimePeriodBegin.setStringValue(begin);
            responseTimePeriodEnd.setStringValue(end);



            TimePeriodType a1 = response.addNewAvailabilityPeriod().addNewTimePeriod();

            a1.addNewBeginPosition().setStringValue(begin);
            a1.addNewEndPosition().setStringValue(m1);

            TimePeriodType a2 = response.addNewAvailabilityPeriod().addNewTimePeriod();

            a2.addNewBeginPosition().setStringValue(m2);
            a2.addNewEndPosition().setStringValue(end);
            
            
            this.setResponse(responseDocument);
            
            
            
            
            
            
            
            
            
            
            
//		this.validRequest();
//
//		//Get request
//		GetSensorAvailabilityDocument request = this.getRequest();
//		GetSensorAvailabilityType sensorRequest = request.getGetSensorAvailability();
//		
//		
//		
//		
//		//Get request time span
//		RequestPeriod period = sensorRequest.getRequestPeriod();
//		
//		
//		
//		String begin = period.getTimePeriod().getBeginPosition().getStringValue();
//		String end = period.getTimePeriod().getEndPosition().getStringValue();
//
////		String[] begins = begin.split("-");
////		int[] beg = new int[3];
////		for (int i = 0; i < 3; i++)
////		{
////			beg[i] = Integer.valueOf(begins[i]);
////		}
////		String[] ends = end.split("-");
////		int[] en = new int[3];
////		for (int i = 0; i < 3; i++)
////		{
////			en[i] = Integer.valueOf(ends[i]);
////		}
//		
////		Date beginDate = new Date(beg[0] - 1900, beg[1] - 1, beg[2]);
////		Date endDate = new Date(en[0] - 1900, en[1] - 1, en[2]);
//		
//		Date beginDate = new Date(1970, 01, 01, 00, 00,00);
//		Date endDate = new Date(2970,01,01,00,00,00);
//		
//			try {
//				beginDate=SynchronousOperation.dateFormat.parse(begin);
//				endDate=SynchronousOperation.dateFormat.parse(end);
//			} catch (ParseException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//
//		ResourceBundle databaseResourceBundle = ResourceBundle.getBundle(DataBaseHandler.getParametersPath());
//				
//		String dataBaseName = databaseResourceBundle.getString("DATABASE_NAME");
//		String userName = databaseResourceBundle.getString("DATABASE_USERNAME");
//		String password = databaseResourceBundle.getString("DATABASE_PASSWORD");
//		
//		//Search for entries where dates are in between begin and end
//		
//		//Get sensor unavailibilities in database
//		UnavailibilityDatabaseHandler dbhandler = 
//				new UnavailibilityDatabaseHandler(dataBaseName, userName, password);
//		
//		List<Unavailibility> unavailibilities = 
//				dbhandler.getUnavailibilities(sensorName, beginDate, endDate);
//
//		List<Availibility> availibilities = 
//				dbhandler.getAvailibilities(beginDate, endDate, unavailibilities);
//		
//		dbhandler.closeConnection();
//		
//		//--------------------------------------------------------------
//		//Start creating response
//		//--------------------------------------------------------------
//
//		
//		//Create response document
//		GetSensorAvailabilityResponseDocument responseDocument = GetSensorAvailabilityResponseDocument.Factory.newInstance();
//		GetSensorAvailabilityResponseType response = responseDocument.addNewGetSensorAvailabilityResponse();
//		
//		//Complete response document
//		
//		//--------------------------------------------------------------
//		//Start setting responsePeriod
//		//--------------------------------------------------------------
//		ResponsePeriod responsePeriod = response.addNewResponsePeriod();
//		
//		TimePeriodType responseTimePeriod = responsePeriod.addNewTimePeriod();
//		
//		TimePositionType responseTimePeriodBegin = responseTimePeriod.addNewBeginPosition();
//		TimePositionType responseTimePeriodEnd = responseTimePeriod.addNewEndPosition();
//
//		responseTimePeriodBegin.setStringValue(begin);
//		responseTimePeriodEnd.setStringValue(end);
//
//		//--------------------------------------------------------------
//		//Start setting availibilitiesPeriods
//		//--------------------------------------------------------------
//		for (Availibility anAvailibility : availibilities)
//		{
//			AvailabilityPeriod tmp = response.addNewAvailabilityPeriod();
//			
//			TimePeriodType tmpPeriod = tmp.addNewTimePeriod();
//
//			TimePositionType tmpBegin = tmpPeriod.addNewBeginPosition();
//			tmpBegin.setStringValue(anAvailibility.getBegin().toString());//** attention format
//			
//			TimePositionType tmpEnd = tmpPeriod.addNewEndPosition();
//			tmpEnd.setStringValue(anAvailibility.getEnd().toString());//** attention format
//		}
//		
//		
//		this.setResponse(responseDocument);
	}
}


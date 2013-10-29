package net.eads.astrium.hmas.fas.operations;

import net.eads.astrium.hmas.operations.DreamEOSPSOperation;
import java.util.List;
import net.eads.astrium.hmas.exceptions.GetStationAvailabilityFault;
import net.eads.astrium.hmas.fas.configuration.FASConfFolder;

import net.opengis.eosps.x20.GetStationAvailabilityDocument;
import net.opengis.eosps.x20.GetStationAvailabilityResponseDocument;
import net.opengis.eosps.x20.GetStationAvailabilityResponseType;
import net.opengis.eosps.x20.GetStationAvailabilityResponseType.ResponsePeriod;
import net.opengis.gml.x32.TimePeriodType;
import net.opengis.gml.x32.TimePositionType;

/**
 * @file GetStationAvailabilityOperation.java
 * @author Sebastian Ulrich <sebastian_ulrich@hotmail.fr>
 * @version 1.0
 * 
 * @section LICENSE
 * 
 *          To be defined
 * 
 * @section DESCRIPTION
 * 
 *          The GetStationAvailability Operation indicates if a station is available at a given time period.
 */
public class GetStationAvailabilityOperation extends DreamEOSPSOperation<FASConfFolder,GetStationAvailabilityDocument,GetStationAvailabilityResponseDocument, GetStationAvailabilityFault> {

	
	private List<String> stations;
	
	/**
	 * 
	 * @param request
	 */
	public GetStationAvailabilityOperation(FASConfFolder confFolder, GetStationAvailabilityDocument request){

		super(confFolder, request);
	}

	@Override
	public void validRequest() throws GetStationAvailabilityFault {
		
	}

	@Override
	public void executeRequest() throws GetStationAvailabilityFault {

            
            GetStationAvailabilityResponseDocument responseDocument = 
				GetStationAvailabilityResponseDocument.Factory.newInstance();
            GetStationAvailabilityResponseType response = 
                            responseDocument.addNewGetStationAvailabilityResponse();
		


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
//		ResourceBundle resourceBundle = ResourceBundle
//				.getBundle(DataBaseHandler.getParametersPath());
//				
//		String dataBaseName = resourceBundle.getString("DATABASE_NAME");
//		String userName = resourceBundle.getString("DATABASE_USERNAME");
//		String password = resourceBundle.getString("DATABASE_PASSWORD");
//		
//
//		//Get request
//		GetStationAvailabilityDocument request = this.getRequest();
//		GetStationAvailabilityType stationRequest = request.getGetStationAvailability();
//		
//		//Get request station for database query
//		String requestStation = this.getRequest().getGetStationAvailability().getStation();
//
//		//Get request time span
//		RequestPeriod period = stationRequest.getRequestPeriod();
//		String begin = period.getTimePeriod().getBeginPosition().getStringValue();
//		String end = period.getTimePeriod().getEndPosition().getStringValue();
//
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
////		
////		Date beginDate = new Date(beg[0] - 1900, beg[1] - 1, beg[2]);
////		Date endDate = new Date(en[0] - 1900, en[1] - 1, en[2]);
//		
//		
//		//Search for entries where dates are in between begin and end
//		
//		//Get sensor unavailibilities in database
//		UnavailibilityDatabaseHandler dbhandler = 
//				new UnavailibilityDatabaseHandler(dataBaseName, userName, password);
//		
//		List<Unavailibility> unavailibilities = 
//				dbhandler.getUnavailibilities(requestStation, beginDate, endDate);
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
//		GetStationAvailabilityResponseDocument responseDocument = 
//				GetStationAvailabilityResponseDocument.Factory.newInstance();
//		GetStationAvailabilityResponseType response = 
//				responseDocument.addNewGetStationAvailabilityResponse();
//		
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
//			tmpBegin.setStringValue(anAvailibility.getBegin().toString());
//			
//			TimePositionType tmpEnd = tmpPeriod.addNewEndPosition();
//			tmpEnd.setStringValue(anAvailibility.getEnd().toString());
//		}
//		
//		this.setResponse(responseDocument);
	}

}
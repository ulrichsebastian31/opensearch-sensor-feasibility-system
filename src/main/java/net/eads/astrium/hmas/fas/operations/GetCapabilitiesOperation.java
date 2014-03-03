package net.eads.astrium.hmas.fas.operations;

import java.io.IOException;
import net.eads.astrium.hmas.operations.DreamEOSPSOperation;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.eads.astrium.hmas.conf.exceptions.ConfFileNotFoundException;
import net.eads.astrium.hmas.exceptions.GetCapabilitiesFault;
import net.eads.astrium.hmas.fas.conf.FASConfFolder;

import org.apache.xmlbeans.GDuration;
import org.oasisOpen.docs.wsn.t1.TopicSetType;

import net.opengis.gml.x32.AbstractRingPropertyType;
import net.opengis.gml.x32.DirectPositionListType;
import net.opengis.gml.x32.LinearRingType;
import net.opengis.gml.x32.PolygonType;
import net.opengis.ows.x11.DCPDocument.DCP;
import net.opengis.ows.x11.GetCapabilitiesType;
import net.opengis.ows.x11.HTTPDocument.HTTP;
import net.opengis.ows.x11.OperationDocument.Operation;
import net.opengis.ows.x11.OperationsMetadataDocument.OperationsMetadata;
import net.opengis.ows.x11.RequestMethodType;
import net.opengis.ows.x11.ServiceIdentificationDocument.ServiceIdentification;
import net.opengis.sps.x21.CapabilitiesDocument;
import net.opengis.sps.x21.CapabilitiesType;
import net.opengis.sps.x21.CapabilitiesType.Contents;
import net.opengis.sps.x21.CapabilitiesType.Notifications;
import net.opengis.sps.x21.GetCapabilitiesDocument;
import net.opengis.sps.x21.SPSContentsType;
import net.opengis.sps.x21.SensorOfferingType;
import net.opengis.sps.x21.SensorOfferingType.ObservableArea;
import net.opengis.sps.x21.SensorOfferingType.ObservableArea.ByPolygon;
import net.opengis.sps.x21.TaskDocument;
import net.opengis.sps.x21.TaskType;
import net.opengis.swes.x21.AbstractContentsType.Offering;
import net.opengis.swes.x21.FilterDialectMetadataType;
import net.opengis.swes.x21.NotificationProducerMetadataType;
import net.opengis.swes.x21.NotificationProducerMetadataType.ProducerEndpoint;
import net.opengis.swes.x21.NotificationProducerMetadataType.ServedTopics;
import net.opengis.swes.x21.NotificationProducerMetadataType.SupportedDialects;

/**
 * @file GetCapabilitiesOperation.java
 * @author Sebastian Ulrich <sebastian_ulrich@hotmail.fr>
 * @version 1.0
 * 
 * @section LICENSE
 * 
 *          To be defined
 * 
 * @section DESCRIPTION
 * 
 *          The GetCapabilities Operation gives information about what the sensor is capable of, in term of available operations essentialy.
 *          
 *          This class is not currently working as some part of the response creation are not yet available.
 *          Plus, a lot of parameters should be found in a config file and not hard-coded.
 */
public class GetCapabilitiesOperation extends DreamEOSPSOperation<FASConfFolder,GetCapabilitiesDocument,CapabilitiesDocument, GetCapabilitiesFault> {

    /**
     * 
     * @param request
     */
    public GetCapabilitiesOperation(FASConfFolder confFolder, GetCapabilitiesDocument request){

            super(confFolder, request);
    }

    @Override
    public void validRequest() throws GetCapabilitiesFault {

    }

    @Override
    public void executeRequest() throws GetCapabilitiesFault {

            this.validRequest();

            GetCapabilitiesDocument req = this.getRequest();

            GetCapabilitiesType caps = req.getGetCapabilities();

//		String[] sections = caps.getSections().getSectionArray();
//		
//		if (sections != null)
//		{
//			if (sections.length > 0)
//			{
//				for (int i = 0; i < sections.length; i++)
//				{
//					System.out.println(sections[i].toString());
//				}
//			}
//			else
//			{
//				System.out.println("sections empty");
//			}
//		}
//		else
//		{
//			System.out.println("sections null");
//		}
            System.out.println("Read request...");

            CapabilitiesDocument capabilitiesDocument = CapabilitiesDocument.Factory.newInstance();

            CapabilitiesType capabilities = capabilitiesDocument.addNewCapabilities();

            System.out.println("Service :");

            ServiceIdentification serviceId = capabilities.addNewServiceIdentification();

            serviceId.addNewTitle().setStringValue("FAS Sensor Planning Service");
            serviceId.addNewAbstract().setStringValue("FAS sps developed by Astrium LTD");
            serviceId.addNewServiceType().setStringValue("SPS");
            serviceId.addNewServiceTypeVersion().setStringValue("2.0.0");
            serviceId.addNewAccessConstraints().setStringValue("none");

            System.out.println("Service done...");
            
            System.out.println("Operation metadata :");
            capabilities.setOperationsMetadata(this.getOperationsMetadata());
            System.out.println("Operation metadata done...");

            System.out.println("Notifications :");
            capabilities.setNotifications(this.getNotifications());
            System.out.println("Notifications done...");

            System.out.println("Contents :");
            capabilities.setContents(this.getContents());
            System.out.println("Contents done...");

            System.out.println("Writing Response");

            this.setResponse(capabilitiesDocument);
    }


    public Contents getContents() throws GetCapabilitiesFault
    {
        Contents contents = Contents.Factory.newInstance();
        SPSContentsType spsContents = contents.addNewSPSContents();
        spsContents.addNewProcedureDescriptionFormat().setStringValue("http://www.opengis.net/sensorML/1.0");
        spsContents.addNewObservableProperty().setStringValue("http://sweet.jpl.nasa.gov/2.0/physRadiation.owl#Radiance");
        spsContents.setMinStatusTime(new GDuration("PT48H"));

        spsContents.addNewSupportedEncoding().setStringValue("http://www.opengis.net/swe/2.0/TextEncoding");
        spsContents.addNewSupportedEncoding().setStringValue("http://www.opengis.net/swe/2.0/XMLEncoding");

        try {
            Set<String> sensors = this.getServiceConfiguration().getSensors();

            Offering[] offerings = new Offering[sensors.size()];

            int i = 0;
            for (String aSensor : sensors)
            {
                offerings[i] = this.getOffering(
                        this.getServiceConfiguration().getSensorProperties(aSensor)
                    );
            }

            spsContents.setOfferingArray(offerings);

        } catch (IOException ex) {
            Logger.getLogger(GetCapabilitiesOperation.class.getName()).log(Level.SEVERE, null, ex);
            throw new GetCapabilitiesFault("IOException reading sensors informations.", ex);
            
        } catch (ConfFileNotFoundException ex) {
            Logger.getLogger(GetCapabilitiesOperation.class.getName()).log(Level.SEVERE, null, ex);
            throw new GetCapabilitiesFault("IOException reading sensors informations.", ex);
        }

        return contents;
    }

    /**
     * Old version to get sensor information through configuration files
     * @param sensorProperties
     * @return O
     */
    public Offering getOffering(Map<String, String> sensorProperties)
    {
        String sensorId = sensorProperties.get("sensorId");
        String sensorName = sensorProperties.get("sensorName");

        Offering offering = Offering.Factory.newInstance();

        SensorOfferingType sensorOffering = SensorOfferingType.Factory.newInstance();

        sensorOffering.setDescription("Programming service for "+sensorName+" only");
        sensorOffering.setIdentifier(sensorName);
        sensorOffering.addNewExtension();
        sensorOffering.setProcedure(""+sensorId);
        
        ObservableArea observableArea = sensorOffering.addNewObservableArea();

        ByPolygon byPolygon = observableArea.addNewByPolygon();
        PolygonType polygon = byPolygon.addNewPolygon();
        polygon.setId("gid0");

        AbstractRingPropertyType exterior = polygon.addNewExterior();

        LinearRingType linearRing = LinearRingType.Factory.newInstance();
        DirectPositionListType posList = linearRing.addNewPosList();
        posList.setSrsName("urn:ogc:def:crs:EPSG:6.17:4326");
        posList.setStringValue("-180 -80 +180 -80 +180 +80 -180 +80");																					//config files

        exterior.setAbstractRing(linearRing);


        offering.setAbstractOffering(sensorOffering);

        return offering;
    }

    public Notifications getNotifications() throws GetCapabilitiesFault
    {
        Notifications notifications = Notifications.Factory.newInstance();
        NotificationProducerMetadataType notificationProdMD = notifications.addNewNotificationProducerMetadata();

        String serverBaseURI = "";

        try {	
            serverBaseURI = this.getServiceConfiguration().getServerBaseURI();
        } catch (IOException ex) {
            Logger.getLogger(GetCapabilitiesOperation.class.getName()).log(Level.SEVERE, null, ex);
            serverBaseURI = "http://127.0.0.1:8080/DreamFASServices-1.0-SNAPSHOT/dream/fas/";
        } catch (ConfFileNotFoundException ex) {
            Logger.getLogger(GetCapabilitiesOperation.class.getName()).log(Level.SEVERE, null, ex);
        }

        ProducerEndpoint producerEndpoint = notificationProdMD.addNewProducerEndpoint();
        producerEndpoint
                .addNewEndpointReference()
                .addNewAddress()
                .setStringValue(serverBaseURI);																			//config files

        //Supported dialects
        SupportedDialects supportedDialects = notificationProdMD.addNewSupportedDialects();
        FilterDialectMetadataType filterDialect = supportedDialects.addNewFilterDialectMetadata();
        filterDialect.addNewTopicExpressionDialect().setStringValue("http://docs.oasis-open.org/wsn/t-1/TopicExpression/Simple");
        filterDialect.addNewTopicExpressionDialect().setStringValue("http://docs.oasis-open.org/wsn/t-1/TopicExpression/Concrete");
        filterDialect.addMessageContentDialect("http://www.w3.org/TR/1999/REC-xpath-19991116");
        //End Supported Dialects

        notificationProdMD.setFixedTopicSet(true);

        //Served Topics
        ServedTopics servedTopics = notificationProdMD.addNewServedTopics();

        TopicSetType topicSet = servedTopics.addNewTopicSet();

        TaskDocument taskEvent = TaskDocument.Factory.newInstance();

        TaskType task = taskEvent.addNewTask();


        // We don't have the right structure in topicSet

//		<swes:servedTopics>
//			<wstop:TopicSet>
//				<sps:TaskEvent>
//					<sps:TaskFailure wstop:topic="true"/>
//					<sps:TaskCancellation wstop:topic="true"/>
//					<sps:TaskCompletion wstop:topic="true"/>
//					<sps:TaskUpdate wstop:topic="true"/>
//					<sps:DataPublication wstop:topic="true"/>
//					<sps:TaskSubmission wstop:topic="true"/>
//				</sps:TaskEvent>
//				<sps:TaskingRequestEvent>
//					<sps:TaskingRequestAcceptance wstop:topic="true"/>
//					<sps:TaskingRequestRejection wstop:topic="true"/>
//				</sps:TaskingRequestEvent>
//				<eo:EOEvent>
//					<eo:SegmentPlanned wstop:topic="true"/>
//					<eo:SegmentAcquired wstop:topic="true"/>
//					<eo:SegmentValidated wstop:topic="true"/>
//					<eo:SegmentCancelled wstop:topic="true"/>
//					<eo:SegmentFailed wstop:topic="true"/>
//				</eo:EOEvent>
//			</wstop:TopicSet>
//		</swes:servedTopics>

        return notifications;
    }


    public OperationsMetadata getOperationsMetadata() throws GetCapabilitiesFault
    {
        OperationsMetadata operationMetadata = OperationsMetadata.Factory.newInstance();

        try {
            String serverBaseURI = this.getServiceConfiguration().getServerBaseURI();

            String[] getOperations = this.getServiceConfiguration().getGetOperations();
            int nbGetOperations = getOperations.length;
            for (int i = 1; i < nbGetOperations;i++) {

                String anOperation = getOperations[i];

                Operation operation = operationMetadata.addNewOperation();
                operation.setName( anOperation );

                DCP dcp = operation.addNewDCP();
                HTTP http = dcp.addNewHTTP();

                RequestMethodType get = http.addNewGet();
                get.setHref(serverBaseURI + "?service=SPS&request=" + anOperation);	
            }

            String[] postOperations = this.getServiceConfiguration().getPostOperations();
            int nbPostOperations = postOperations.length;
            for (int i = 1; i < nbPostOperations;i++) {

                String anOperation = postOperations[i];

                Operation operation = operationMetadata.addNewOperation();
                operation.setName( anOperation );

                DCP dcp = operation.addNewDCP();
                HTTP http = dcp.addNewHTTP();

                RequestMethodType post = http.addNewPost();
                post.setHref(serverBaseURI + "?service=SPS&request=" + anOperation);	
            }

        } catch (IOException ex) {
            Logger.getLogger(GetCapabilitiesOperation.class.getName()).log(Level.SEVERE, null, ex);
            throw new GetCapabilitiesFault("IOException reading operations informations.", ex);
        } catch (ConfFileNotFoundException ex) {
            Logger.getLogger(GetCapabilitiesOperation.class.getName()).log(Level.SEVERE, null, ex);
            throw new GetCapabilitiesFault("ConfFileNotExistsException reading operations informations.", ex);
        }
        return operationMetadata;
    }
	
}






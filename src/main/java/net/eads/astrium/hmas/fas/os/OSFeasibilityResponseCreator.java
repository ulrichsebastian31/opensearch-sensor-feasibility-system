/**
 * --------------------------------------------------------------------------------------------------------
 *   Project                                            :               DREAM
 * --------------------------------------------------------------------------------------------------------
 *   File Name                                          :               OSFeasibilityResponseCreator.java
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

import java.util.Date;
import net.opengis.eosps.x20.GetFeasibilityResponseDocument;
import net.opengis.eosps.x20.TaskingParametersType;
import net.opengis.eosps.x20.TaskingResponseType;
import org.w3.x2005.atom.FeedDocument;

/**
 *
 * @author re-sulrich
 */
public class OSFeasibilityResponseCreator {
    
    
    public static FeedDocument createResponse(GetFeasibilityResponseDocument responseDocument, String serverBaseURL) {
        
        FeedDocument doc = FeedDocument.Factory.newInstance();
        FeedDocument.Feed feed = doc.addNewFeed();
        
        TaskingResponseType.Result result = responseDocument.getGetFeasibilityResponse().getResult();
        
        
//        System.out.println("" + responseDocument.xmlText());
        
        TaskingParametersType tp = result
                .getStatusReport()
                .getEoTaskingParameters();
        
        if (tp != null) {
            
            System.out.println("" + tp.xmlText());

            String id = ""
                    + result.getStatusReport().getProcedure()
                    + " - ";

            id += result
                    .getStatusReport()
                    .getTask();

            feed.setId(id);

            String title = "EO feasibility for " 
                    + result
                            .getStatusReport()
                            .getProcedure()
                    + " beginning " + new Date().toString();

            feed.setTitle(title
                    );

            FeedDocument.Feed.Entry entry = feed.addNewEntry();

            entry.setId(id);
            entry.setTitle(title);

            FeedDocument.Feed.Entry.Link link = entry.addNewLink();
            link.setRel("enclosure");
            
            String url = serverBaseURL.substring(0, (serverBaseURL.lastIndexOf("hmas") + "hmas".length()));
            String task = "1";
            try {
                task = responseDocument.getGetFeasibilityResponse().getResult().getStatusReport().getTask();
            } catch (NullPointerException e) {
                
            }
            link.setHref("" + url + "/fas/Sentinel1?service=eosps&request=GetTask&acceptFormat=application/xml&task=" + task);
            
            entry.setDate(new Date().toString());

            entry.setGetFeasibilityResponse(responseDocument.getGetFeasibilityResponse());

        }
//        entry.addNewWhere().setPolygon(
//                responseDocument
//                        .getGetFeasibilityResponse()
//                        .getResult()
//                        .getStatusReport()
//                        .getEoTaskingParameters()
//                        .getCoverageProgrammingRequest()
//                        .getRegionOfInterest()
//                        .getPolygonArray(0)
//            );
        
        
        
        
        
        
        
        
        return doc;
        
        
        
    }
}

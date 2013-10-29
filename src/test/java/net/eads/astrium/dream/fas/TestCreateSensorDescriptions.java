/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.eads.astrium.dream.fas;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.eads.astrium.hmas.conf.exceptions.ConfFileAlreadyExistsException;
import net.eads.astrium.hmas.fas.configuration.sensors.SensorsConfigurationsFolder;
import org.junit.Test;

/**
 *
 * @author re-sulrich
 */
public class TestCreateSensorDescriptions {
    
    @Test
    public void createSensorMLDesc()
    {
        String folderpath = "C:\\Users\\re-sulrich\\.dream\\fas\\s1-fas\\sensors\\";
        SensorsConfigurationsFolder descLoader = new SensorsConfigurationsFolder("s1-fas");
        
        String content = "<sml:SensorML xmlns:sml=\"http://www.opengis.net/sensorML/1.0.2\"\n" +
"	xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:gml=\"http://www.opengis.net/gml\"\n" +
"	xmlns:swe=\"http://www.opengis.net/swe/1.0.1\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
"	xsi:schemaLocation=\"http://www.opengis.net/sensorML/1.0.2 http://schemas.opengis.net/sensorML/1.0.2/sensorML.xsd\"\n" +
"	version=\"1.0.1\">\n" +
"	<!-- -->\n" +
"	<sml:member\n" +
"		xlink:role=\"urn:x-ogc:def:dictionary:ESA:documentRoles:v01#system_summary\">\n" +
"		<!-- -->\n" +
"		<sml:System gml:id=\"Deimos_10M_COLOR\">\n" +
"			<!-- ================================================= -->\n" +
"			<!--           System Description           -->\n" +
"			<!-- ================================================= -->\n" +
"			<gml:description> Configuration of the Deimos constellation for 10m\n" +
"		Color Imagery </gml:description>\n" +
"			<!-- ================================================= -->\n" +
"			<!--           System Identifiers         -->\n" +
"			<!-- ================================================= -->\n" +
"			<sml:identification>\n" +
"				<sml:IdentifierList>\n" +
"					<sml:identifier name=\"System UID\">\n" +
"						<sml:Term definition=\"urn:x-ogc:def:identifier:OGC:uuid\">\n" +
"							<sml:value>urn:x-ogc:object:platform:ESA:Deimos:10mColor:v01</sml:value>\n" +
"						</sml:Term>\n" +
"					</sml:identifier>\n" +
"					<sml:identifier name=\"Short Name\">\n" +
"						<sml:Term definition=\"urn:x-ogc:def:identifier:OGC:shortName\">\n" +
"							<sml:value>Deimos 10m Color</sml:value>\n" +
"						</sml:Term>\n" +
"					</sml:identifier>\n" +
"				</sml:IdentifierList>\n" +
"			</sml:identification>\n" +
"			<!-- ================================================= -->\n" +
"			<!--          System Capabilities          -->\n" +
"			<!-- ================================================= -->\n" +
"			<sml:capabilities>\n" +
"				<swe:DataRecord>\n" +
"					<swe:field name=\"Band Type\">\n" +
"						<swe:Category definition=\"urn:x-ogc:def:classifier:OGC:bandType\">\n" +
"							<swe:codeSpace xlink:href=\"urn:x-ogc:def:dictionary:ESA:bandTypes:v01\" />\n" +
"							<swe:value>COLOR</swe:value>\n" +
"						</swe:Category>\n" +
"					</swe:field>\n" +
"					<swe:field name=\"Ground Resolution\">\n" +
"						<swe:Quantity definition=\"urn:x-ogc:def:phenomenon:ESA:groundResolution\">\n" +
"							<swe:uom code=\"m\" />\n" +
"							<swe:value>10</swe:value>\n" +
"						</swe:Quantity>\n" +
"					</swe:field>\n" +
"					<swe:field name=\"Latitude Coverage\">\n" +
"						<swe:QuantityRange definition=\"urn:x-ogc:def:phenomenon:OGC:latitude\">\n" +
"							<swe:uom code=\"deg\" />\n" +
"							<swe:value>-85 +85</swe:value>\n" +
"						</swe:QuantityRange>\n" +
"					</swe:field>\n" +
"					<swe:field name=\"Longitude Coverage\">\n" +
"						<swe:QuantityRange definition=\"urn:x-ogc:def:phenomenon:OGC:longitude\">\n" +
"							<swe:uom code=\"deg\" />\n" +
"							<swe:value>-180 +180</swe:value>\n" +
"						</swe:QuantityRange>\n" +
"					</swe:field>\n" +
"				</swe:DataRecord>\n" +
"			</sml:capabilities>\n" +
"			<!-- ================================================= -->\n" +
"			<!--           System Components             -->\n" +
"			<!-- ================================================= -->\n" +
"			<sml:components>\n" +
"				<sml:ComponentList>\n" +
"					<sml:component name=\"Deimos4-HRVIR1\">\n" +
"						<sml:System>\n" +
"							<sml:identification>\n" +
"								<sml:IdentifierList>\n" +
"									<sml:identifier name=\"System UID\">\n" +
"										<sml:Term definition=\"urn:x-ogc:def:identifier:OGC:uuid\">\n" +
"											<sml:value>urn:x-ogc:object:instrument:ESA:Deimos4:HRVIR1:v01</sml:value>\n" +
"										</sml:Term>\n" +
"									</sml:identifier>\n" +
"									<sml:identifier name=\"Platform UID\">\n" +
"										<sml:Term definition=\"urn:x-ogc:def:identifier:OGC:uuid\">\n" +
"											<sml:value>urn:x-ogc:object:instrument:ESA:Deimos4:v01</sml:value>\n" +
"										</sml:Term>\n" +
"									</sml:identifier>\n" +
"									<sml:identifier name=\"Short Name\">\n" +
"										<sml:Term definition=\"urn:x-ogc:def:identifier:OGC:shortName\">\n" +
"											<sml:value>Deimos4 HRVIR1</sml:value>\n" +
"										</sml:Term>\n" +
"									</sml:identifier>\n" +
"								</sml:IdentifierList>\n" +
"							</sml:identification>\n" +
"						</sml:System>\n" +
"					</sml:component>\n" +
"					<sml:component name=\"Deimos4-HRVIR2\">\n" +
"						<sml:System>\n" +
"							<sml:identification>\n" +
"								<sml:IdentifierList>\n" +
"									<sml:identifier name=\"System UID\">\n" +
"										<sml:Term definition=\"urn:x-ogc:def:identifier:OGC:uuid\">\n" +
"											<sml:value>urn:x-ogc:object:instrument:ESA:Deimos4:HRVIR2:v01</sml:value>\n" +
"										</sml:Term>\n" +
"									</sml:identifier>\n" +
"									<sml:identifier name=\"Platform UID\">\n" +
"										<sml:Term definition=\"urn:x-ogc:def:identifier:OGC:uuid\">\n" +
"											<sml:value>urn:x-ogc:object:instrument:ESA:Deimos4:v01</sml:value>\n" +
"										</sml:Term>\n" +
"									</sml:identifier>\n" +
"									<sml:identifier name=\"Short Name\">\n" +
"										<sml:Term definition=\"urn:x-ogc:def:identifier:OGC:shortName\">\n" +
"											<sml:value>Deimos4 HRVIR2</sml:value>\n" +
"										</sml:Term>\n" +
"									</sml:identifier>\n" +
"								</sml:IdentifierList>\n" +
"							</sml:identification>\n" +
"						</sml:System>\n" +
"					</sml:component>\n" +
"					<sml:component name=\"Deimos5-HRG1\">\n" +
"						<sml:System>\n" +
"							<sml:identification>\n" +
"								<sml:IdentifierList>\n" +
"									<sml:identifier name=\"System UID\">\n" +
"										<sml:Term definition=\"urn:x-ogc:def:identifier:OGC:uuid\">\n" +
"											<sml:value>urn:x-ogc:object:instrument:ESA:Deimos5:HRG1:v01</sml:value>\n" +
"										</sml:Term>\n" +
"									</sml:identifier>\n" +
"									<sml:identifier name=\"Platform UID\">\n" +
"										<sml:Term definition=\"urn:x-ogc:def:identifier:OGC:uuid\">\n" +
"											<sml:value>urn:x-ogc:object:instrument:ESA:Deimos5:v01</sml:value>\n" +
"										</sml:Term>\n" +
"									</sml:identifier>\n" +
"									<sml:identifier name=\"Short Name\">\n" +
"										<sml:Term definition=\"urn:x-ogc:def:identifier:OGC:shortName\">\n" +
"											<sml:value>Deimos5 HRG1</sml:value>\n" +
"										</sml:Term>\n" +
"									</sml:identifier>\n" +
"								</sml:IdentifierList>\n" +
"							</sml:identification>\n" +
"						</sml:System>\n" +
"					</sml:component>\n" +
"					<sml:component name=\"Deimos5-HRG2\">\n" +
"						<sml:System>\n" +
"							<sml:identification>\n" +
"								<sml:IdentifierList>\n" +
"									<sml:identifier name=\"System UID\">\n" +
"										<sml:Term definition=\"urn:x-ogc:def:identifier:OGC:uuid\">\n" +
"											<sml:value>urn:x-ogc:object:instrument:ESA:Deimos5:HRG2:v01</sml:value>\n" +
"										</sml:Term>\n" +
"									</sml:identifier>\n" +
"									<sml:identifier name=\"Platform UID\">\n" +
"										<sml:Term definition=\"urn:x-ogc:def:identifier:OGC:uuid\">\n" +
"											<sml:value>urn:x-ogc:object:instrument:ESA:Deimos5:v01</sml:value>\n" +
"										</sml:Term>\n" +
"									</sml:identifier>\n" +
"									<sml:identifier name=\"Short Name\">\n" +
"										<sml:Term definition=\"urn:x-ogc:def:identifier:OGC:shortName\">\n" +
"											<sml:value>Deimos5 HRG2</sml:value>\n" +
"										</sml:Term>\n" +
"									</sml:identifier>\n" +
"								</sml:IdentifierList>\n" +
"							</sml:identification>\n" +
"						</sml:System>\n" +
"					</sml:component>\n" +
"				</sml:ComponentList>\n" +
"			</sml:components>\n" +
"		</sml:System>\n" +
"	</sml:member>\n" +
"</sml:SensorML>";
        
        try {
            descLoader.addSensorDescription("s1-sar", "SensorML", content);
            
            String desc = descLoader.getSensorDescription("s1-sar", "sensorML");
            
            System.out.println("" + desc);
            
        } catch (IOException ex) {
            Logger.getLogger(TestCreateSensorDescriptions.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ConfFileAlreadyExistsException ex) {
            Logger.getLogger(TestCreateSensorDescriptions.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

<?xml version="1.0" encoding="UTF-8"?>
<ctl:package xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ctl="http://www.occamlab.com/ctl" xmlns:xi="http://www.w3.org/2001/XInclude" xmlns:eosp="http://a9.com/-/opensearch/extensions/eosp/1.0/">
	<!-- ************************************************************************************************* -->
	<!-- INCLUDING MODULES -->
	<xi:include href="functions.ctl"/>
	<xi:include href="geo_functions.ctl"/>
	<xi:include href="tests_core.ctl"/>
	<xi:include href="tests_ext.ctl"/>
	<!-- ************************************************************************************************* -->
	<ctl:suite name="eosp:compliance-suite">
		<ctl:title>OGC 13-039 compliance test suite</ctl:title>
		<ctl:description>Validates an implementation claiming compliance to the "OGC OpenSearch Extension for Earth Observation Satellite Tasking" Specification</ctl:description>
		<ctl:starting-test>eosp:compliance-tests</ctl:starting-test>
	</ctl:suite>
	<ctl:test name="eosp:compliance-tests">
		<ctl:assertion>Run the tests on an OGC 13-039 ATS module basis</ctl:assertion>
		<ctl:code>
			<xsl:variable name="form-values">
				<ctl:form height="500" width="800">
					<body>
						<h2 align="center">Compliance Test Suite of the OGC OpenSearch Extension for Earth Observation Satellite Tasking</h2>
						<h3 align="center">Input Parameters for the Executable Test Suite</h3>
						<table border="0" cellpadding="2" cellspacing="2" bgcolor="#E9CFEC">
							<tr>
								<td align="left">Endpoint Reference of the general OSDD Service</td>
								<td align="left">
									<input name="stringvalue.endpoint.url.osdd.gen" size="70" type="text" value="http://hma-demo-opensearch.spacebel.be/HMAS-FAS-1.0-SNAPSHOT/hmas/fas/os/description"/>
								</td>
							</tr>
							<tr>
								<td align="left">Endpoint Reference of the OSDD Service for OPT sensors</td>
								<td align="left">
									<input name="stringvalue.endpoint.url.osdd.opt" size="70" type="text" value="http://hma-demo-opensearch.spacebel.be/HMAS-FAS-1.0-SNAPSHOT/hmas/fas/os/description?sensorType=OPT"/>
								</td>
							</tr>
							<tr>
								<td align="left">Endpoint Reference of the OSDD Service for SAR sensors</td>
								<td align="left">
									<input name="stringvalue.endpoint.url.osdd.sar" size="70" type="text" value="http://hma-demo-opensearch.spacebel.be/HMAS-FAS-1.0-SNAPSHOT/hmas/fas/os/description?sensorType=SAR"/>
								</td>
							</tr>
						</table>
						<input style="text-align:center" type="submit" value="Start Test"/>
					</body>
				</ctl:form>
			</xsl:variable>
			<!-- ************************************************************************************************* -->
			<!-- DEFINITION OF COMMON PARAMETERS -->
			<xsl:variable name="endpoint.url.osdd.gen" select="$form-values/values/value[@key='stringvalue.endpoint.url.osdd.gen' ]"/>
			<xsl:variable name="endpoint.url.osdd.opt" select="$form-values/values/value[@key='stringvalue.endpoint.url.osdd.opt' ]"/>
			<xsl:variable name="endpoint.url.osdd.sar" select="$form-values/values/value[@key='stringvalue.endpoint.url.osdd.sar' ]"/>
			<!-- ************************************************************************************************* -->
			
			<!--
				BEGIN CALLS TO ATS TEST MODULES
			-->
			<ctl:call-test name="eosp:tests_core">
				<ctl:with-param name="endpoint.url.osdd.gen" select="$endpoint.url.osdd.gen"/>
			</ctl:call-test>
			<ctl:call-test name="eosp:tests_ext">
				<ctl:with-param name="endpoint.url.osdd.opt" select="$endpoint.url.osdd.opt"/>
				<ctl:with-param name="endpoint.url.osdd.sar" select="$endpoint.url.osdd.sar"/>
			</ctl:call-test>
		</ctl:code>
	</ctl:test>
</ctl:package>

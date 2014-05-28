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
			<!-- ************************************************************************************************* -->
			<!-- DEFINITION OF COMMON PARAMETERS -->
			<xsl:variable name="endpoint.url.osdd.gen">http://smarcos.pisa.intecs.it/DreamServices/dream/mmfas/gmes-mmfas/os/description</xsl:variable>
			<xsl:variable name="endpoint.url.osdd.opt">http://smarcos.pisa.intecs.it/DreamServices/dream/mmfas/gmes-mmfas/os/description?sensorType=OPT</xsl:variable>
			<xsl:variable name="endpoint.url.osdd.sar">http://smarcos.pisa.intecs.it/DreamServices/dream/mmfas/gmes-mmfas/os/description?sensorType=SAR</xsl:variable>
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

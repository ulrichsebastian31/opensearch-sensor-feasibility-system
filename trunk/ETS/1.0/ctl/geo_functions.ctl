<?xml version="1.0" encoding="UTF-8"?>
<ctl:package xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ctl="http://www.occamlab.com/ctl" xmlns:ctlp="http://www.occamlab.com/te/parsers" xmlns:os="http://a9.com/-/spec/opensearch/1.1/" xmlns:eo="http://a9.com/-/opensearch/extensions/eo/1.0/" xmlns:eosp="http://a9.com/-/opensearch/extensions/eosp/1.0/" xmlns:param="http://a9.com/-/spec/opensearch/extensions/parameters/1.0/" xmlns:gmlReader="java:com.vividsolutions.jts.io.gml2.GMLReader" xmlns:wktReader="java:com.vividsolutions.jts.io.WKTReader" xmlns:geometry="java:com.vividsolutions.jts.geom.Geometry" xmlns:rectIntersects="java:com.vividsolutions.jts.operation.predicate.RectangleIntersects" xmlns:javatype="http://saxon.sf.net/java-type" xmlns:javacollection="java:java.util.ArrayList" xmlns:saxon="http://saxon.sf.net/" xmlns:xs="http://www.w3.org/2001/XMLSchema">
	<!-- ************************************************************************************************* -->
	<!-- The following function checks the geographic match-->
	<ctl:function name="eosp:geographic_match">
		<ctl:param name="retrieved">retrieved parameter</ctl:param>
		<ctl:param name="provided">provided parameter in WKT</ctl:param>
		<ctl:description>Check if the geographic extension of the task segment intersects the provided polygon</ctl:description>
		<ctl:code>
			<xsl:variable name="retrieved_typed" as="xs:string" select="eosp:fromGML3toGML2string($retrieved)"/>
			<!--
			<ctl:message>Geometry:</ctl:message>
			<ctl:message select="$retrieved_typed"/>	
			-->
			<xsl:variable name="provided_typed" as="xs:string">
				<xsl:value-of select="string($provided)"/>
			</xsl:variable>
			<xsl:variable name="jts_geom_factory" as="javatype:com.vividsolutions.jts.geom.GeometryFactory" select="factory:new()" xmlns:factory="java:com.vividsolutions.jts.geom.GeometryFactory"/>
			<xsl:variable name="jts_WKTReader" as="javatype:com.vividsolutions.jts.io.WKTReader" select="wktReader:new($jts_geom_factory)"/>
			<xsl:variable name="jts_ls1" as="javatype:com.vividsolutions.jts.geom.Polygon" select="wktReader:read($jts_WKTReader, $provided_typed)"/>
			<xsl:variable name="jts_ls2" as="javatype:com.vividsolutions.jts.geom.Geometry" select="gmlReader:read(gmlReader:new(), $retrieved_typed, $jts_geom_factory)"/>
			<xsl:variable name="intersects" as="xs:boolean" select="rectIntersects:intersects($jts_ls1, $jts_ls2)"/>
			<xsl:value-of select="$intersects"/>
		</ctl:code>
	</ctl:function>
	<!-- The following function returns a GML2 geometry as expetced by the JTS GMLReader; for the sake of simplicity, it is returned in serialized form -->
	<ctl:function name="eosp:fromGML3toGML2string">
		<ctl:param name="coordinates"/>
		<ctl:code>
			<xsl:variable name="geometry-1" select=" '&lt;Polygon&gt;&lt;outerBoundaryIs&gt;&lt;LinearRing&gt;&lt;coordinates&gt;' "/>
			<xsl:variable name="geometry-2" select="normalize-space($coordinates)"/>
			<xsl:variable name="geometry-3" select=" '&lt;/coordinates&gt;&lt;/LinearRing&gt;&lt;/outerBoundaryIs&gt;&lt;/Polygon&gt;' "/>
			<xsl:variable name="geometry" select="concat($geometry-1, $geometry-2, $geometry-3)"/>
			<xsl:copy-of select="$geometry"/>
		</ctl:code>
	</ctl:function>
	<!-- The following function checks the temporal match-->
	<ctl:function name="eosp:temporal_match">
		<ctl:param name="retrieved_start">retrieved start parameter</ctl:param>
		<ctl:param name="retrieved_end">retrieved end parameter</ctl:param>
		<ctl:param name="provided_start">provided start parameter</ctl:param>
		<ctl:param name="provided_end">provided end parameter</ctl:param>
		<ctl:description>Check if the temporal extension of the task segment matches the provided polygon</ctl:description>
		<ctl:code>
			<xsl:variable name="matches" as="xs:boolean" select="(xs:dateTime($retrieved_start) &lt; xs:dateTime($provided_end)) and (xs:dateTime($retrieved_end) &gt; xs:dateTime($provided_start))"/>
			<xsl:value-of select="$matches"/>
		</ctl:code>
	</ctl:function>
</ctl:package>

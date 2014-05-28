<?xml version="1.0" encoding="UTF-8"?>
<ctl:package xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ctl="http://www.occamlab.com/ctl" xmlns:ctlp="http://www.occamlab.com/te/parsers" xmlns:os="http://a9.com/-/spec/opensearch/1.1/" xmlns:eo="http://a9.com/-/opensearch/extensions/eo/1.0/" xmlns:eosp="http://a9.com/-/opensearch/extensions/eosp/1.0/" xmlns:param="http://a9.com/-/spec/opensearch/extensions/parameters/1.0/">
	<!-- ************************************************************************************************* -->
	<!-- The following function returns the name assigned by the server to a parameter.-->
	<ctl:function name="eosp:getSearchParameterName">
		<ctl:param name="osdd">OpenSearch Description Document of the Service</ctl:param>
		<ctl:param name="param">searched parameter</ctl:param>
		<ctl:description>Retrieve info about a specific search parameter</ctl:description>
		<ctl:code>
			<xsl:variable name="template_atom" select="$osdd//os:OpenSearchDescription/os:Url[@type = 'application/atom+xml']/@template"/>
			<xsl:variable name="param_name">
				<xsl:for-each select="tokenize($template_atom, '&amp;')">
					<xsl:variable name="param_type" select="substring-after(.,'=')"/>
					<xsl:if test="contains($param_type, $param)">
						<xsl:value-of select="substring-before(., '=')"/>
					</xsl:if>
				</xsl:for-each>
			</xsl:variable>
			<xsl:value-of select="$param_name"/>
		</ctl:code>
	</ctl:function>
	<!-- The following function finds the prefix associated to the "http://a9.com/-/opensearch/extensions/eo/1.0/" or "http://a9.com/-/opensearch/extensions/eosp/1.0/" namespaces-->
	<ctl:function name="eosp:getEoPrefix">
		<ctl:param name="osdd">OpenSearch Description Document of the Service</ctl:param>
		<ctl:param name="ns">A parameter indicating if the searched parameter is for the eo or eosp namespace; possibile values: 'eo, 'eosp'</ctl:param>
		<ctl:description>Retrieve info about a specific search parameter</ctl:description>
		<ctl:code>
			<xsl:choose>
				<xsl:when test="compare($ns, 'eosp')=0">
					<xsl:for-each select="$osdd//os:OpenSearchDescription/namespace::*">
						<xsl:if test="compare(., 'http://a9.com/-/opensearch/extensions/eosp/1.0/')=0">
							<xsl:value-of select="name()"/>
						</xsl:if>
					</xsl:for-each>
				</xsl:when>
				<xsl:when test="compare($ns, 'eo')=0">
					<xsl:for-each select="$osdd//os:OpenSearchDescription/namespace::*">
						<xsl:if test="compare(., 'http://a9.com/-/opensearch/extensions/eo/1.0/')=0">
							<xsl:value-of select="name()"/>
						</xsl:if>
					</xsl:for-each>
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select=""/>
				</xsl:otherwise>
			</xsl:choose>
		</ctl:code>
	</ctl:function>
	<!-- The following function returns the fixed query part of a search request (without parameters) -->
	<ctl:function name="eosp:getQueryFixedPart">
		<ctl:param name="osdd">OpenSearch Description Document of the Service</ctl:param>
		<ctl:code>
			<xsl:variable name="template_atom" select="$osdd//os:OpenSearchDescription/os:Url[@type = 'application/atom+xml']/@template"/>
			<xsl:variable name="search_parameters" select="tokenize($template_atom, '&amp;')"/>
			<xsl:value-of select="$search_parameters[1]"/>
		</ctl:code>
	</ctl:function>
	<!-- The following function returns a polygon in WKT -->
	<ctl:function name="eosp:getDefaultPolygon">
		<ctl:code>
			<!-- The POLYGON shall be provided with coordinates clockwise; the following are the coordinates of a (minimal) polygon including Tuscany-->
			<xsl:variable name="values_encoded" select="encode-for-uri('4.70 42.21,4.70 44.28,12.22 44.28,12.22 42.21,4.70 42.21')"/>
			<xsl:value-of select="concat( 'POLYGON((',  $values_encoded,  '))' )"/>
		</ctl:code>
	</ctl:function>
	<!-- The following function returns a polygon in WKT not encoded for URI-->
	<ctl:function name="eosp:getDefaultPolygonNotEncoded">
		<ctl:code>
			<!-- The POLYGON shall be provided with coordinates clockwise; the following are the coordinates of a (minimal) polygon including Tuscany-->
			<xsl:variable name="values" select=" '4.70 42.21,4.70 44.28,12.22 44.28,12.22 42.21,4.70 42.21' "/>
			<xsl:value-of select="concat( 'POLYGON((',  $values,  '))' )"/>
		</ctl:code>
	</ctl:function>
	<!-- The following function returns the minimum value (when applicable) for a given parameter -->
	<ctl:function name="eosp:getParamMinValue">
		<ctl:param name="osdd">OpenSearch Description Document of the Service</ctl:param>
		<ctl:param name="param">searched parameter</ctl:param>
		<ctl:code>
			<xsl:variable name="param_name" select="eosp:getSearchParameterName($osdd, $param)"/>
			<xsl:variable name="param_value" select="$osdd//os:Url[@type = 'application/atom+xml']/param:Parameter[@name = $param_name ]/@minInclusive"/>
			<xsl:value-of select="encode-for-uri($param_value)"/>
		</ctl:code>
	</ctl:function>
	<!-- The following function returns the first value in an enumeration (when applicable) for a given parameter -->
	<ctl:function name="eosp:getParamFirstValue">
		<ctl:param name="osdd">OpenSearch Description Document of the Service</ctl:param>
		<ctl:param name="param">searched parameter</ctl:param>
		<ctl:code>
			<xsl:variable name="param_name" select="eosp:getSearchParameterName($osdd, $param)"/>
			<xsl:variable name="param_value_temp1" select="$osdd//os:Url[@type = 'application/atom+xml']/param:Parameter[@name = $param_name ]/param:option[1]/@value"/>
			<xsl:variable name="param_value_temp2" select="replace($param_value_temp1, '\{', '')"/>
			<xsl:variable name="param_value" select="replace($param_value_temp2, '\}', '')"/>
			<xsl:value-of select="encode-for-uri($param_value)"/>
		</ctl:code>
	</ctl:function>
	<!-- The following function returns the fixed query part of a request + default geometry (see the function getDefaultPolygon) + default start time (current time) + default end time (current time + a week) -->
	<ctl:function name="eosp:getBaseQuery">
		<ctl:param name="osdd">OpenSearch Description Document of the Service</ctl:param>
		<ctl:code>
			<xsl:variable name="query_fixed_part" select="eosp:getQueryFixedPart($osdd)"/>
			<xsl:variable name="query_default_polygon" select="eosp:getDefaultPolygon()"/>
			<!-- <xsl:variable name="query_default_time_start" select="current-dateTime()"/> -->
			<xsl:variable name="query_default_time_start" as="xs:dateTime" select="adjust-dateTime-to-timezone(current-dateTime(),xs:dayTimeDuration('PT0H'))"/>
			<xsl:variable name="query_default_time_end" as="xs:dateTime" select="xs:dateTime($query_default_time_start) + xs:dayTimeDuration('P7D') "/>
			<xsl:variable name="geometry_name" select="eosp:getSearchParameterName($osdd, 'geometry')"/>
			<xsl:variable name="time_start_name" select="eosp:getSearchParameterName($osdd, 'start')"/>
			<xsl:variable name="time_end_name" select="eosp:getSearchParameterName($osdd, 'end')"/>
			<xsl:variable name="base_query" select="concat($query_fixed_part, '&amp;', $geometry_name, '=', $query_default_polygon, '&amp;', $time_start_name, '=', $query_default_time_start, '&amp;', $time_end_name, '=', $query_default_time_end)"/>
			<xsl:value-of select="$base_query"/>
		</ctl:code>
	</ctl:function>
	<!-- The following function returns the base query of a request, as returend by the getBaseQuery() function, + sensorType=OPT-->
	<ctl:function name="eosp:getBaseQueryOPT">
		<ctl:param name="osdd">OpenSearch Description Document of the Service</ctl:param>
		<ctl:code>
			<xsl:variable name="base_query" select="eosp:getBaseQuary($osdd)"/>
			<xsl:variable name="base_query_opt" select="concat($base_query, '&amp;', 'sensorType=SAR')"/>
			<xsl:value-of select="$base_query_opt"/>
		</ctl:code>
	</ctl:function>
	<ctl:function name="eosp:getBaseQuerySAR">
		<ctl:param name="osdd">OpenSearch Description Document of the Service</ctl:param>
		<ctl:code>
			<xsl:variable name="base_query" select="eosp:getBaseQuary($osdd)"/>
			<xsl:variable name="base_query_sar" select="concat($base_query, '&amp;', 'sensorType=SAR')"/>
			<xsl:value-of select="$base_query_sar"/>
		</ctl:code>
	</ctl:function>
</ctl:package>

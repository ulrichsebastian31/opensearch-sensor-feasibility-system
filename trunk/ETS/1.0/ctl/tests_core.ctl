<?xml version="1.0" encoding="UTF-8"?>
<ctl:package xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ctl="http://www.occamlab.com/ctl" xmlns:ctlp="http://www.occamlab.com/te/parsers" xmlns:os="http://a9.com/-/spec/opensearch/1.1/" xmlns:eo="http://a9.com/-/opensearch/extensions/eo/1.0/" xmlns:eosp="http://a9.com/-/opensearch/extensions/eosp/1.0/" xmlns:param="http://a9.com/-/spec/opensearch/extensions/parameters/1.0/" xmlns:eosps20="http://www.opengis.net/eosps/2.0" xmlns:eop="http://earth.esa.int/eop" xmlns:swe="http://www.opengis.net/swe/2.0" xmlns:gml32="http://www.opengis.net/gml/3.2">
	<!-- ************************************************************************************************* -->
	<ctl:test name="eosp:tests_core">
		<ctl:param name="endpoint.url.osdd.gen">URL of the generic OpenSearchDescriptionDocument</ctl:param>
		<ctl:assertion>Tests of basic capabilities</ctl:assertion>
		<ctl:code>
			<xsl:variable name="osdd_gen">
				<ctl:request>
					<ctl:url>
						<xsl:value-of select="$endpoint.url.osdd.gen"/>
					</ctl:url>
					<ctl:method>GET</ctl:method>
					<ctlp:HTTPParser return="content"/>
				</ctl:request>
			</xsl:variable>
			<xsl:variable name="template_atom" select="$osdd_gen//os:OpenSearchDescription/os:Url[@type = 'application/atom+xml']/@template"/>
			<xsl:variable name="search_parameters" select="tokenize($template_atom, '&amp;')"/>
			<xsl:variable name="eosp_prefix">
				<xsl:for-each select="$osdd_gen//os:OpenSearchDescription/namespace::*">
					<xsl:if test="compare(., 'http://a9.com/-/opensearch/extensions/eosp/1.0/')=0">
						<xsl:value-of select="name()"/>
					</xsl:if>
				</xsl:for-each>
			</xsl:variable>
			<xsl:variable name="eo_prefix">
				<xsl:for-each select="$osdd_gen//os:OpenSearchDescription/namespace::*">
					<xsl:if test="compare(., 'http://a9.com/-/opensearch/extensions/eo/1.0/')=0">
						<xsl:value-of select="name()"/>
					</xsl:if>
				</xsl:for-each>
			</xsl:variable>
			<!-- <ctl:message>Template atom:</ctl:message>
			<ctl:message select="$template_atom"/>
			<ctl:message>Search parameters:</ctl:message>
			<xsl:for-each select="tokenize($template_atom, '&amp;')">
				<ctl:message select="."/>
			</xsl:for-each>
			<ctl:message>EO namespace prefix:</ctl:message>
			<ctl:message select="$eo_prefix"/>
			<ctl:message>EO SP namespace prefix:</ctl:message>
			<ctl:message select="$eosp_prefix"/> -->
			<!-- ***************Call the tests of this block ***********************-->
			<ctl:call-test name="eosp:valid_osdd_gen">
				<ctl:with-param name="endpoint.url.osdd.gen" select="$endpoint.url.osdd.gen"/>
			</ctl:call-test>
			<ctl:call-test name="eosp:atom_support_gen">
				<ctl:with-param name="endpoint.url.osdd.gen" select="$endpoint.url.osdd.gen"/>
			</ctl:call-test>
			<!-- ************************************************************************************************* -->
			<!-- The tests on Geographic and Temporal Extension -->
			<ctl:call-test name="eosp:geo_extension">
				<ctl:with-param name="endpoint.url.osdd.gen" select="$endpoint.url.osdd.gen"/>
				<ctl:with-param name="osdd" select="$osdd_gen"/>
			</ctl:call-test>
			<ctl:call-test name="eosp:temporal_extension">
				<ctl:with-param name="endpoint.url.osdd.gen" select="$endpoint.url.osdd.gen"/>
				<ctl:with-param name="osdd" select="$osdd_gen"/>
			</ctl:call-test>
			<!-- ************************************************************************************************* -->
			<!-- The Sensor type belong to the logical group "Equipment"-->
			<ctl:call-test name="eosp:sensorType">
				<ctl:with-param name="endpoint.url.osdd.gen" select="$endpoint.url.osdd.gen"/>
				<ctl:with-param name="osdd" select="$osdd_gen"/>
			</ctl:call-test>
			<!-- ************************************************************************************************* -->
			<!-- The tests on Azimuth angle, Elevation angle, Along track angle, Across track angle belongs to a same logical group "Acquisition Angle"-->
			<ctl:call-test name="eosp:azimuth">
				<ctl:with-param name="endpoint.url.osdd.gen" select="$endpoint.url.osdd.gen"/>
				<ctl:with-param name="osdd" select="$osdd_gen"/>
			</ctl:call-test>
			<ctl:call-test name="eosp:elevation">
				<ctl:with-param name="endpoint.url.osdd.gen" select="$endpoint.url.osdd.gen"/>
				<ctl:with-param name="osdd" select="$osdd_gen"/>
			</ctl:call-test>
			<ctl:call-test name="eosp:acrossTrack">
				<ctl:with-param name="endpoint.url.osdd.gen" select="$endpoint.url.osdd.gen"/>
				<ctl:with-param name="osdd" select="$osdd_gen"/>
			</ctl:call-test>
			<ctl:call-test name="eosp:alongTrack">
				<ctl:with-param name="endpoint.url.osdd.gen" select="$endpoint.url.osdd.gen"/>
				<ctl:with-param name="osdd" select="$osdd_gen"/>
			</ctl:call-test>
			<!-- ************************************************************************************************* -->
			<ctl:call-test name="eosp:coverageType">
				<ctl:with-param name="endpoint.url.osdd.gen" select="$endpoint.url.osdd.gen"/>
				<ctl:with-param name="osdd" select="$osdd_gen"/>
			</ctl:call-test>
		</ctl:code>
	</ctl:test>
	<!-- ************************************************************************************************* -->
	<ctl:test name="eosp:valid_osdd_gen">
		<ctl:param name="endpoint.url.osdd.gen">URL of the generic OpenSearchDescriptionDocument</ctl:param>
		<ctl:assertion>Verifies that the server generates a valid OpenSearch description document.</ctl:assertion>
		<ctl:link>A.1 OpenSearch description document</ctl:link>
		<ctl:code>
			<xsl:variable name="Server_Response">
				<ctl:request>
					<ctl:url>
						<xsl:value-of select="$endpoint.url.osdd.gen"/>
					</ctl:url>
					<ctl:method>GET</ctl:method>
					<ctlp:HTTPParser return="content"/>
				</ctl:request>
			</xsl:variable>
			<xsl:choose>
				<xsl:when test="not($Server_Response/*)">
					<ctl:message>[FAILURE] No response from the Server</ctl:message>
					<ctl:fail/>
				</xsl:when>
				<xsl:when test="not($Server_Response//os:OpenSearchDescription)">
					<ctl:message>[FAILURE] Incorrect response from the Server</ctl:message>
					<ctl:fail/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:variable name="eosp_namespace">
						<xsl:for-each select="$Server_Response//os:OpenSearchDescription/namespace::*">
							<xsl:if test="compare(., 'http://a9.com/-/opensearch/extensions/eosp/1.0/')=0">
								<xsl:value-of select="."/>
							</xsl:if>
						</xsl:for-each>
					</xsl:variable>
					<xsl:choose>
						<xsl:when test="string-length(string($eosp_namespace)) = 0">
							<ctl:message>[FAILURE] The response does not contain the OGC 13-039 namespace</ctl:message>
							<ctl:fail/>
						</xsl:when>
						<xsl:otherwise>
							<ctl:message>[SUCCESS] The Server returned an OpenSearch Document</ctl:message>
							<!-- <ctl:message>
								<xsl:value-of select="$eosp_namespace"/>
							</ctl:message> -->
						</xsl:otherwise>
					</xsl:choose>
				</xsl:otherwise>
			</xsl:choose>
		</ctl:code>
	</ctl:test>
	<!-- ************************************************************************************************* -->
	<ctl:test name="eosp:atom_support_gen">
		<ctl:param name="endpoint.url.osdd.gen">URL of the generic OpenSearchDescriptionDocument</ctl:param>
		<ctl:assertion>Verifies that the server supports the ATOM format</ctl:assertion>
		<ctl:link>A.2 ATOM Response Type</ctl:link>
		<ctl:code>
			<xsl:variable name="Server_Response">
				<ctl:request>
					<ctl:url>
						<xsl:value-of select="$endpoint.url.osdd.gen"/>
					</ctl:url>
					<ctl:method>GET</ctl:method>
					<ctlp:HTTPParser return="content"/>
				</ctl:request>
			</xsl:variable>
			<xsl:choose>
				<xsl:when test="not($Server_Response/*)">
					<ctl:message>[FAILURE] No response from the Server</ctl:message>
					<ctl:fail/>
				</xsl:when>
				<xsl:when test="not($Server_Response//os:OpenSearchDescription/os:Url/@type = 'application/atom+xml')">
					<ctl:message>[FAILURE] The Server does not support the ATOM format</ctl:message>
					<ctl:fail/>
				</xsl:when>
				<xsl:otherwise>
					<ctl:message>[SUCCESS] The Server supports the ATOM format</ctl:message>
				</xsl:otherwise>
			</xsl:choose>
		</ctl:code>
	</ctl:test>
	<!-- ************************************************************************************************* -->
	<ctl:test name="eosp:geo_extension">
		<ctl:param name="endpoint.url.osdd.gen">URL of the generic OpenSearchDescriptionDocument</ctl:param>
		<ctl:param name="osdd">OpenSearch Description Document of the Service</ctl:param>
		<ctl:assertion>Verifies that a search request with geographic extension is correctly executed</ctl:assertion>
		<ctl:link>External - Check geographical support</ctl:link>
		<ctl:code>
			<xsl:variable name="param_name" select="eosp:getSearchParameterName($osdd, 'start')"/>
			<ctl:message>Param name:</ctl:message>
			<ctl:message select="$param_name"/>
			<xsl:variable name="param_value" select="eosp:getDefaultPolygonNotEncoded()"/>
			<ctl:message>Param value:</ctl:message>
			<ctl:message select="string($param_value)"/>
			<xsl:variable name="base_search_request" select="eosp:getBaseQuery($osdd)"/>
			<xsl:variable name="search_request" select="$base_search_request"/>
			<xsl:variable name="search_request_ext" select="concat($search_request, '&amp;', 'sensorType=sar')"/>
			<ctl:message>Final request:</ctl:message>
			<ctl:message select="$search_request_ext"/>
			<xsl:variable name="Server_Response">
				<ctl:request>
					<ctl:url>
						<xsl:value-of select="$search_request_ext"/>
					</ctl:url>
					<ctl:method>GET</ctl:method>
					<ctl:header name="Accept">application/atom+xml</ctl:header>
					<ctlp:HTTPParser return="content"/>
				</ctl:request>
			</xsl:variable>
			<xsl:choose>
				<xsl:when test="not($Server_Response//eosps20:GetFeasibilityResponse)">
					<ctl:message>[FAILURE] No response from the Server</ctl:message>
					<ctl:fail/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:variable name="geo_match">
						<xsl:for-each select="$Server_Response//eosps20:GetFeasibilityResponse//eosps20:FeasibilityStudy//gml32:coordinates">
							<xsl:if test="not(eosp:geographic_match(., $param_value) = 'true')">
								<xsl:text>false</xsl:text>
							</xsl:if>
						</xsl:for-each>
					</xsl:variable>
					<xsl:choose>
						<xsl:when test="not(normalize-space(string($geo_match)) = '') ">
							<ctl:message>[FAILURE] At least a polygon does not match the provided region of interest</ctl:message>
							<ctl:fail/>
						</xsl:when>
						<xsl:otherwise>
							<ctl:message>[SUCCESS] The Server returned a valid response</ctl:message>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:otherwise>
			</xsl:choose>
		</ctl:code>
	</ctl:test>
	<!-- ************************************************************************************************* -->
	<ctl:test name="eosp:temporal_extension">
		<ctl:param name="endpoint.url.osdd.gen">URL of the generic OpenSearchDescriptionDocument</ctl:param>
		<ctl:param name="osdd">OpenSearch Description Document of the Service</ctl:param>
		<ctl:assertion>Verifies that a search request with temporal extension is correctly executed</ctl:assertion>
		<ctl:link>External - Check temporal support</ctl:link>
		<ctl:code>
			<xsl:variable name="query_fixed_part" select="eosp:getQueryFixedPart($osdd)"/>
			<xsl:variable name="query_default_polygon" select="eosp:getDefaultPolygon()"/>
			<xsl:variable name="query_default_time_start" as="xs:dateTime" select="adjust-dateTime-to-timezone(current-dateTime(),xs:dayTimeDuration('PT0H'))"/>
			<xsl:variable name="query_default_time_end" as="xs:dateTime" select="xs:dateTime($query_default_time_start) + xs:dayTimeDuration('P7D') "/>
			<xsl:variable name="geometry_name" select="eosp:getSearchParameterName($osdd, 'geometry')"/>
			<xsl:variable name="time_start_name" select="eosp:getSearchParameterName($osdd, 'start')"/>
			<xsl:variable name="time_end_name" select="eosp:getSearchParameterName($osdd, 'end')"/>
			<xsl:variable name="base_query" select="concat($query_fixed_part, '&amp;', $geometry_name, '=', $query_default_polygon, '&amp;', $time_start_name, '=', $query_default_time_start,'&amp;', $time_end_name, '=', $query_default_time_end)"/>
			<xsl:variable name="search_request" select="$base_query"/>
			<xsl:variable name="search_request_ext" select="concat($search_request, '&amp;', 'sensorType=sar')"/>
			<ctl:message>Final request:</ctl:message>
			<ctl:message select="$search_request_ext"/>
			<xsl:variable name="Server_Response">
				<ctl:request>
					<ctl:url>
						<xsl:value-of select="$search_request_ext"/>
					</ctl:url>
					<ctl:method>GET</ctl:method>
					<ctl:header name="Accept">application/atom+xml</ctl:header>
					<ctlp:HTTPParser return="content"/>
				</ctl:request>
			</xsl:variable>
			<xsl:choose>
				<xsl:when test="not($Server_Response//eosps20:GetFeasibilityResponse)">
					<ctl:message>[FAILURE] No response from the Server</ctl:message>
					<ctl:fail/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:variable name="temporal_match">
						<xsl:for-each select="$Server_Response//eosps20:GetFeasibilityResponse//eosps20:FeasibilityStudy//eosps20:Segment">
							<xsl:if test="not(eosp:temporal_match(eosps20:acquisitionStartTime, eosps20:acquisitionStopTime, $query_default_time_start, $query_default_time_end) = 'true')">
								<xsl:text>false</xsl:text>
								<ctl:message>Acquisition start and stop:</ctl:message>
								<ctl:message select="xs:dateTime(eosps20:acquisitionStartTime)"/>
								<ctl:message select="xs:dateTime(eosps20:acquisitionStopTime)"/>
								<ctl:message>Provided start and stop:</ctl:message>
								<ctl:message select="xs:dateTime($query_default_time_start)"/>
								<ctl:message select="xs:dateTime($query_default_time_end)"/>
							</xsl:if>
						</xsl:for-each>
					</xsl:variable>
					<xsl:choose>
						<xsl:when test="not(normalize-space(string($temporal_match)) = '') ">
							<ctl:message>[FAILURE] At least an acquisition time does not match the provided time interval</ctl:message>
							<ctl:fail/>
						</xsl:when>
						<xsl:otherwise>
							<ctl:message>[SUCCESS] The Server returned a valid response</ctl:message>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:otherwise>
			</xsl:choose>
		</ctl:code>
	</ctl:test>
	<!-- ************************************************************************************************* -->
	<ctl:test name="eosp:sensorType">
		<ctl:param name="endpoint.url.osdd.gen">URL of the generic OpenSearchDescriptionDocument</ctl:param>
		<ctl:param name="osdd">OpenSearch Description Document of the Service</ctl:param>
		<ctl:assertion>If the sensorType parameter is supported, verifies a search request with this parameter</ctl:assertion>
		<ctl:link>A.3 Parameter options suggestion</ctl:link>
		<ctl:code>
			<xsl:variable name="param_name" select="eosp:getSearchParameterName($osdd, 'sensorType')"/>
			<ctl:message>Param name:</ctl:message>
			<ctl:message select="$param_name"/>
			<xsl:variable name="param_value" select="eosp:getParamFirstValue($osdd, 'sensorType') "/>
			<ctl:message>Param value:</ctl:message>
			<ctl:message select="string($param_value)"/>
			<xsl:choose>
				<xsl:when test="string-length($param_value) = 0">
					<ctl:message>Cannot retrieve a valid value for the sensorType search parameter</ctl:message>
					<ctl:fail/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:variable name="base_search_request" select="eosp:getBaseQuery($osdd)"/>
					<xsl:variable name="search_request" select="concat($base_search_request, '&amp;', $param_name, '=', $param_value)"/>
					<ctl:message>Final request:</ctl:message>
					<ctl:message select="$search_request"/>
					<xsl:variable name="Server_Response">
						<ctl:request>
							<ctl:url>
								<xsl:value-of select="$search_request"/>
							</ctl:url>
							<ctl:method>GET</ctl:method>
							<ctl:header name="Accept">application/atom+xml</ctl:header>
							<ctlp:HTTPParser return="content"/>
						</ctl:request>
					</xsl:variable>
					<xsl:choose>
						<xsl:when test="not($Server_Response//eosps20:GetFeasibilityResponse)">
							<ctl:message>[FAILURE] No response from the Server</ctl:message>
							<ctl:fail/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:variable name="sensor_match">
								<xsl:for-each select="$Server_Response//eosps20:GetFeasibilityResponse//eop:EarthObservationEquipment//eop:sensorType">
									<xsl:if test="compare(lower-case($param_value), .) != 0 and compare(upper-case($param_value), .) != 0">
										<xsl:text>false</xsl:text>
									</xsl:if>
								</xsl:for-each>
							</xsl:variable>
							<xsl:choose>
								<xsl:when test="not(normalize-space(string($sensor_match)) = '') ">
									<ctl:message>[FAILURE] At least a sensorType does not match the provided search parameter value</ctl:message>
									<ctl:fail/>
								</xsl:when>
								<xsl:otherwise>
									<ctl:message>[SUCCESS] The Server returned a valid response</ctl:message>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:otherwise>
			</xsl:choose>
		</ctl:code>
	</ctl:test>
	<!-- ************************************************************************************************* -->
	<ctl:test name="eosp:azimuth">
		<ctl:param name="endpoint.url.osdd.gen">URL of the generic OpenSearchDescriptionDocument</ctl:param>
		<ctl:param name="osdd">OpenSearch Description Document of the Service</ctl:param>
		<ctl:assertion>If the azimuth parameter is supported, verifies a search request with this parameter</ctl:assertion>
		<ctl:link>A.5 Earth Observation range suggestion for numerical type parameter</ctl:link>
		<ctl:code>
			<xsl:variable name="param_name" select="eosp:getSearchParameterName($osdd, 'incidenceAzimuthAngle')"/>
			<ctl:message>Param name:</ctl:message>
			<ctl:message select="$param_name"/>
			<xsl:variable name="param_value" select="eosp:getParamMinValue($osdd, 'incidenceAzimuthAngle') "/>
			<ctl:message>Param value:</ctl:message>
			<ctl:message select="$param_value"/>
			<xsl:choose>
				<xsl:when test="string-length($param_value) = 0">
					<ctl:message>Cannot retrieve a valid value for the azimuth search parameter</ctl:message>
					<ctl:fail/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:variable name="base_search_request" select="eosp:getBaseQuery($osdd)"/>
					<xsl:variable name="search_request" select="concat($base_search_request, '&amp;', $param_name, '=', $param_value)"/>
					<xsl:variable name="search_request_ext" select="concat($search_request, '&amp;', 'sensorType=sar')"/>
					<ctl:message>Final request:</ctl:message>
					<ctl:message select="$search_request_ext"/>
					<xsl:variable name="Server_Response">
						<ctl:request>
							<ctl:url>
								<xsl:value-of select="$search_request_ext"/>
							</ctl:url>
							<ctl:method>GET</ctl:method>
							<ctl:header name="Accept">application/atom+xml</ctl:header>
							<ctlp:HTTPParser return="content"/>
						</ctl:request>
					</xsl:variable>
					<xsl:choose>
						<xsl:when test="not($Server_Response//eosps20:GetFeasibilityResponse)">
							<ctl:message>[FAILURE] No response from the Server</ctl:message>
							<ctl:fail/>
						</xsl:when>
						<xsl:otherwise>
							<ctl:message>[SUCCESS] The Server returned a valid response</ctl:message>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:otherwise>
			</xsl:choose>
		</ctl:code>
	</ctl:test>
	<!-- ************************************************************************************************* -->
	<ctl:test name="eosp:elevation">
		<ctl:param name="endpoint.url.osdd.gen">URL of the generic OpenSearchDescriptionDocument</ctl:param>
		<ctl:param name="osdd">OpenSearch Description Document of the Service</ctl:param>
		<ctl:assertion>If the elevation parameter is supported, verifies a search request with this parameter</ctl:assertion>
		<ctl:link>A.5 Earth Observation range suggestion for numerical type parameter</ctl:link>
		<ctl:code>
			<xsl:variable name="param_name" select="eosp:getSearchParameterName($osdd, 'incidenceElevationAngle')"/>
			<ctl:message>Param name:</ctl:message>
			<ctl:message select="$param_name"/>
			<xsl:variable name="param_value" select="eosp:getParamMinValue($osdd, 'incidenceElevationAngle') "/>
			<ctl:message>Param value:</ctl:message>
			<ctl:message select="$param_value"/>
			<xsl:choose>
				<xsl:when test="string-length($param_value) = 0">
					<ctl:message>Cannot retrieve a valid value for the elevation search parameter</ctl:message>
					<ctl:fail/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:variable name="base_search_request" select="eosp:getBaseQuery($osdd)"/>
					<xsl:variable name="search_request" select="concat($base_search_request, '&amp;', $param_name, '=', $param_value)"/>
					<xsl:variable name="search_request_ext" select="concat($search_request, '&amp;', 'sensorType=sar')"/>
					<ctl:message>Final request:</ctl:message>
					<ctl:message select="$search_request_ext"/>
					<xsl:variable name="Server_Response">
						<ctl:request>
							<ctl:url>
								<xsl:value-of select="$search_request_ext"/>
							</ctl:url>
							<ctl:method>GET</ctl:method>
							<ctl:header name="Accept">application/atom+xml</ctl:header>
							<ctlp:HTTPParser return="content"/>
						</ctl:request>
					</xsl:variable>
					<xsl:choose>
						<xsl:when test="not($Server_Response//eosps20:GetFeasibilityResponse)">
							<ctl:message>[FAILURE] No response from the Server</ctl:message>
							<ctl:fail/>
						</xsl:when>
						<xsl:otherwise>
							<ctl:message>[SUCCESS] The Server returned a valid response</ctl:message>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:otherwise>
			</xsl:choose>
		</ctl:code>
	</ctl:test>
	<!-- ************************************************************************************************* -->
	<ctl:test name="eosp:acrossTrack">
		<ctl:param name="endpoint.url.osdd.gen">URL of the generic OpenSearchDescriptionDocument</ctl:param>
		<ctl:param name="osdd">OpenSearch Description Document of the Service</ctl:param>
		<ctl:assertion>If the across track parameter is supported, verifies a search request with this parameter</ctl:assertion>
		<ctl:link>A.5 Earth Observation range suggestion for numerical type parameter</ctl:link>
		<ctl:code>
			<xsl:variable name="param_name" select="eosp:getSearchParameterName($osdd, 'pointingAcrossTrackAngle')"/>
			<ctl:message>Param name:</ctl:message>
			<ctl:message select="$param_name"/>
			<xsl:variable name="param_value" select="eosp:getParamMinValue($osdd, 'pointingAcrossTrackAngle') "/>
			<ctl:message>Param value:</ctl:message>
			<ctl:message select="$param_value"/>
			<xsl:choose>
				<xsl:when test="string-length($param_value) = 0">
					<ctl:message>Cannot retrieve a valid value for the across track search parameter</ctl:message>
					<ctl:fail/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:variable name="base_search_request" select="eosp:getBaseQuery($osdd)"/>
					<xsl:variable name="search_request" select="concat($base_search_request, '&amp;', $param_name, '=', $param_value)"/>
					<xsl:variable name="search_request_ext" select="concat($search_request, '&amp;', 'sensorType=sar')"/>
					<ctl:message>Final request:</ctl:message>
					<ctl:message select="$search_request_ext"/>
					<xsl:variable name="Server_Response">
						<ctl:request>
							<ctl:url>
								<xsl:value-of select="$search_request_ext"/>
							</ctl:url>
							<ctl:method>GET</ctl:method>
							<ctl:header name="Accept">application/atom+xml</ctl:header>
							<ctlp:HTTPParser return="content"/>
						</ctl:request>
					</xsl:variable>
					<xsl:choose>
						<xsl:when test="not($Server_Response//eosps20:GetFeasibilityResponse)">
							<ctl:message>[FAILURE] No response from the Server</ctl:message>
							<ctl:fail/>
						</xsl:when>
						<xsl:otherwise>
							<ctl:message>[SUCCESS] The Server returned a valid response</ctl:message>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:otherwise>
			</xsl:choose>
		</ctl:code>
	</ctl:test>
	<!-- ************************************************************************************************* -->
	<ctl:test name="eosp:alongTrack">
		<ctl:param name="endpoint.url.osdd.gen">URL of the generic OpenSearchDescriptionDocument</ctl:param>
		<ctl:param name="osdd">OpenSearch Description Document of the Service</ctl:param>
		<ctl:assertion>If the along track parameter is supported, verifies a search request with this parameter</ctl:assertion>
		<ctl:link>A.5 Earth Observation range suggestion for numerical type parameter</ctl:link>
		<ctl:code>
			<xsl:variable name="param_name" select="eosp:getSearchParameterName($osdd, 'pointingAlongTrackAngle')"/>
			<ctl:message>Param name:</ctl:message>
			<ctl:message select="$param_name"/>
			<xsl:variable name="param_value" select="eosp:getParamMinValue($osdd, 'pointingAlongTrackAngle') "/>
			<ctl:message>Param value:</ctl:message>
			<ctl:message select="$param_value"/>
			<xsl:choose>
				<xsl:when test="string-length($param_value) = 0">
					<ctl:message>Cannot retrieve a valid value for the along track search parameter</ctl:message>
					<ctl:fail/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:variable name="base_search_request" select="eosp:getBaseQuery($osdd)"/>
					<xsl:variable name="search_request" select="concat($base_search_request, '&amp;', $param_name, '=', $param_value)"/>
					<xsl:variable name="search_request_ext" select="concat($search_request, '&amp;', 'sensorType=sar')"/>
					<ctl:message>Final request:</ctl:message>
					<ctl:message select="$search_request_ext"/>
					<xsl:variable name="Server_Response">
						<ctl:request>
							<ctl:url>
								<xsl:value-of select="$search_request_ext"/>
							</ctl:url>
							<ctl:method>GET</ctl:method>
							<ctl:header name="Accept">application/atom+xml</ctl:header>
							<ctlp:HTTPParser return="content"/>
						</ctl:request>
					</xsl:variable>
					<xsl:choose>
						<xsl:when test="not($Server_Response//eosps20:GetFeasibilityResponse)">
							<ctl:message>[FAILURE] No response from the Server</ctl:message>
							<ctl:fail/>
						</xsl:when>
						<xsl:otherwise>
							<ctl:message>[SUCCESS] The Server returned a valid response</ctl:message>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:otherwise>
			</xsl:choose>
		</ctl:code>
	</ctl:test>
	<!-- ************************************************************************************************* -->
	<ctl:test name="eosp:coverageType">
		<ctl:param name="endpoint.url.osdd.gen">URL of the generic OpenSearchDescriptionDocument</ctl:param>
		<ctl:param name="osdd">OpenSearch Description Document of the Service</ctl:param>
		<ctl:assertion>If the coverageType parameter is supported, verifies a search request with this parameter</ctl:assertion>
		<ctl:link>A.4 Parameter list suggestion for character string type parameter</ctl:link>
		<ctl:code>
			<xsl:variable name="param_name" select="eosp:getSearchParameterName($osdd, 'coverageType')"/>
			<ctl:message>Param name:</ctl:message>
			<ctl:message select="$param_name"/>
			<xsl:variable name="param_value" select="eosp:getParamFirstValue($osdd, 'coverageType') "/>
			<ctl:message>Param value:</ctl:message>
			<ctl:message select="string($param_value)"/>
			<xsl:choose>
				<xsl:when test="string-length($param_value) = 0">
					<ctl:message>Cannot retrieve a valid value for the coverageType search parameter</ctl:message>
					<ctl:fail/>
				</xsl:when>
				<xsl:otherwise>
					<xsl:variable name="base_search_request" select="eosp:getBaseQuery($osdd)"/>
					<xsl:variable name="search_request" select="concat($base_search_request, '&amp;', $param_name, '=', $param_value)"/>
					<xsl:variable name="search_request_ext" select="concat($search_request, '&amp;', 'sensorType=sar')"/>
					<ctl:message>Final request:</ctl:message>
					<ctl:message select="$search_request_ext"/>
					<xsl:variable name="Server_Response">
						<ctl:request>
							<ctl:url>
								<xsl:value-of select="$search_request_ext"/>
							</ctl:url>
							<ctl:method>GET</ctl:method>
							<ctl:header name="Accept">application/atom+xml</ctl:header>
							<ctlp:HTTPParser return="content"/>
						</ctl:request>
					</xsl:variable>
					<xsl:choose>
						<xsl:when test="not($Server_Response//eosps20:GetFeasibilityResponse)">
							<ctl:message>[FAILURE] No response from the Server</ctl:message>
							<ctl:fail/>
						</xsl:when>
						<xsl:otherwise>
							<ctl:message>[SUCCESS] The Server returned a valid response</ctl:message>
						</xsl:otherwise>
					</xsl:choose>
				</xsl:otherwise>
			</xsl:choose>
		</ctl:code>
	</ctl:test>
</ctl:package>

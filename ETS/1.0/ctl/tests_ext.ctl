<?xml version="1.0" encoding="UTF-8"?>
<ctl:package xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ctl="http://www.occamlab.com/ctl" xmlns:ctlp="http://www.occamlab.com/te/parsers" xmlns:os="http://a9.com/-/spec/opensearch/1.1/" xmlns:eo="http://a9.com/-/opensearch/extensions/eo/1.0/" xmlns:eosp="http://a9.com/-/opensearch/extensions/eosp/1.0/" xmlns:param="http://a9.com/-/spec/opensearch/extensions/parameters/1.0/" xmlns:eosps20="http://www.opengis.net/eosps/2.0" xmlns:eop="http://earth.esa.int/eop" xmlns:swe="http://www.opengis.net/swe/2.0">
	<!-- ************************************************************************************************* -->
	<ctl:test name="eosp:tests_ext">
		<ctl:param name="endpoint.url.osdd.opt">URL of the OpenSearchDescriptionDocument for one or more OPT collections</ctl:param>
		<ctl:param name="endpoint.url.osdd.sar">URL of the OpenSearchDescriptionDocument for one or more SAR collections</ctl:param>
		<ctl:assertion>Tests for optical/radar capabilities</ctl:assertion>
		<ctl:code>
			<!-- ***************Call the tests of this block ***********************-->
			<ctl:call-test name="eosp:valid_osdd_opt">
				<ctl:with-param name="endpoint.url.osdd.opt" select="$endpoint.url.osdd.opt"/>
			</ctl:call-test>
			<ctl:call-test name="eosp:atom_support_opt">
				<ctl:with-param name="endpoint.url.osdd.opt" select="$endpoint.url.osdd.opt"/>
			</ctl:call-test>
			<ctl:call-test name="eosp:valid_osdd_sar">
				<ctl:with-param name="endpoint.url.osdd.sar" select="$endpoint.url.osdd.sar"/>
			</ctl:call-test>
			<ctl:call-test name="eosp:atom_support_sar">
				<ctl:with-param name="endpoint.url.osdd.sar" select="$endpoint.url.osdd.sar"/>
			</ctl:call-test>
			<!-- The Platform and instrument belong to the logical group "Equipment"-->
			<xsl:variable name="osdd_opt">
				<ctl:request>
					<ctl:url>
						<xsl:value-of select="$endpoint.url.osdd.opt"/>
					</ctl:url>
					<ctl:method>GET</ctl:method>
					<ctlp:HTTPParser return="content"/>
				</ctl:request>
			</xsl:variable>
			<ctl:call-test name="eosp:platform">
				<ctl:with-param name="endpoint.url.osdd.opt" select="$endpoint.url.osdd.opt"/>
				<ctl:with-param name="osdd" select="$osdd_opt"/>
			</ctl:call-test>
			<ctl:call-test name="eosp:instrument">
				<ctl:with-param name="endpoint.url.osdd.opt" select="$endpoint.url.osdd.opt"/>
				<ctl:with-param name="osdd" select="$osdd_opt"/>
			</ctl:call-test>
		</ctl:code>
	</ctl:test>
	<!-- *************************************************************************-->
	<ctl:test name="eosp:valid_osdd_opt">
		<ctl:param name="endpoint.url.osdd.opt">URL of the OpenSearchDescriptionDocument for one or more OPT collections</ctl:param>
		<ctl:assertion>Verifies that the server generates a valid OpenSearch description document for OPT collections</ctl:assertion>
		<ctl:link>A.1 OpenSearch description document</ctl:link>
		<ctl:code>
			<ctl:call-test name="eosp:valid_osdd">
				<ctl:with-param name="endpoint.url.osdd" select="$endpoint.url.osdd.opt"/>
			</ctl:call-test>
		</ctl:code>
	</ctl:test>
	<ctl:test name="eosp:atom_support_opt">
		<ctl:param name="endpoint.url.osdd.opt">URL of the OpenSearchDescriptionDocument for one or more OPT collections</ctl:param>
		<ctl:assertion>Verifies that the server supports the ATOM format for OPT collections</ctl:assertion>
		<ctl:link>A.2 ATOM Response Type</ctl:link>
		<ctl:code>
			<ctl:call-test name="eosp:atom_support">
				<ctl:with-param name="endpoint.url.osdd" select="$endpoint.url.osdd.opt"/>
			</ctl:call-test>
		</ctl:code>
	</ctl:test>
	<ctl:test name="eosp:valid_osdd_sar">
		<ctl:param name="endpoint.url.osdd.sar">URL of the OpenSearchDescriptionDocument for one or more SAR collections</ctl:param>
		<ctl:assertion>Verifies that the server generates a valid OpenSearch description document for SAR collections</ctl:assertion>
		<ctl:link>A.1 OpenSearch description document</ctl:link>
		<ctl:code>
			<ctl:call-test name="eosp:valid_osdd">
				<ctl:with-param name="endpoint.url.osdd" select="$endpoint.url.osdd.sar"/>
			</ctl:call-test>
		</ctl:code>
	</ctl:test>
	<ctl:test name="eosp:atom_support_sar">
		<ctl:param name="endpoint.url.osdd.sar">URL of the OpenSearchDescriptionDocument for one or more SAR collections</ctl:param>
		<ctl:assertion>Verifies that the server supports the ATOM format for SAR collections</ctl:assertion>
		<ctl:link>A.2 ATOM Response Type</ctl:link>
		<ctl:code>
			<ctl:call-test name="eosp:atom_support">
				<ctl:with-param name="endpoint.url.osdd" select="$endpoint.url.osdd.sar"/>
			</ctl:call-test>
		</ctl:code>
	</ctl:test>
	<!-- ************************************************************************************************* -->
	<ctl:test name="eosp:valid_osdd">
		<ctl:param name="endpoint.url.osdd">URL of the OpenSearchDescriptionDocument</ctl:param>
		<ctl:assertion>Verifies that the server generates a valid OpenSearch description document.</ctl:assertion>
		<ctl:link>A.1 OpenSearch description document</ctl:link>
		<ctl:code>
			<xsl:variable name="Server_Response">
				<ctl:request>
					<ctl:url>
						<xsl:value-of select="$endpoint.url.osdd"/>
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
	<ctl:test name="eosp:atom_support">
		<ctl:param name="endpoint.url.osdd">URL of the OpenSearchDescriptionDocument</ctl:param>
		<ctl:assertion>Verifies that the server supports the ATOM format</ctl:assertion>
		<ctl:link>A.2 ATOM Response Type</ctl:link>
		<ctl:code>
			<xsl:variable name="Server_Response">
				<ctl:request>
					<ctl:url>
						<xsl:value-of select="$endpoint.url.osdd"/>
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
	<ctl:test name="eosp:platform">
		<ctl:param name="endpoint.url.osdd.opt">URL of the generic OpenSearchDescriptionDocument</ctl:param>
		<ctl:param name="osdd">OpenSearch Description Document of the Service</ctl:param>
		<ctl:assertion>If the platform parameter is supported, verifies a search request with this parameter</ctl:assertion>
		<ctl:link>A.4 Parameter list suggestion for character string type parameter</ctl:link>
		<ctl:code>
			<xsl:variable name="param_name" select="eosp:getSearchParameterName($osdd, 'platform')"/>
			<ctl:message>Param name:</ctl:message>
			<ctl:message select="$param_name"/>
			<xsl:variable name="param_value" select="eosp:getParamFirstValue($osdd, 'platform') "/>
			<ctl:message>Param value:</ctl:message>
			<ctl:message select="string($param_value)"/>
			<xsl:choose>
				<xsl:when test="string-length($param_value) = 0">
					<ctl:message>Cannot retrieve a valid value for the platform search parameter</ctl:message>
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
							<xsl:variable name="platform_match">
								<xsl:for-each select="$Server_Response//eosps20:GetFeasibilityResponse//eop:EarthObservationEquipment//eop:Platform">
									<xsl:if test="compare($param_value, eop:shortName) != 0 and compare($param_value, eop:serialIdentifier) != 0">
										<xsl:text>false</xsl:text>
									</xsl:if>
								</xsl:for-each>
							</xsl:variable>
							<xsl:choose>
								<xsl:when test="not(normalize-space(string($platform_match)) = '') ">
									<ctl:message>[FAILURE] At least a platform does not match the provided search parameter value</ctl:message>
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
	<ctl:test name="eosp:instrument">
		<ctl:param name="endpoint.url.osdd.gen">URL of the generic OpenSearchDescriptionDocument</ctl:param>
		<ctl:param name="osdd">OpenSearch Description Document of the Service</ctl:param>
		<ctl:assertion>If the instrument parameter is supported, verifies a search request with this parameter</ctl:assertion>
		<ctl:link>A.4 Parameter list suggestion for character string type parameter</ctl:link>
		<ctl:code>
			<xsl:variable name="param_name" select="eosp:getSearchParameterName($osdd, 'instrument')"/>
			<ctl:message>Param name:</ctl:message>
			<ctl:message select="$param_name"/>
			<xsl:variable name="param_value" select="eosp:getParamFirstValue($osdd, 'instrument') "/>
			<ctl:message>Param value:</ctl:message>
			<ctl:message select="string($param_value)"/>
			<xsl:choose>
				<xsl:when test="string-length($param_value) = 0">
					<ctl:message>Cannot retrieve a valid value for the instrument search parameter</ctl:message>
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
							<xsl:variable name="instrument_match">
								<xsl:for-each select="$Server_Response//eosps20:GetFeasibilityResponse//eop:EarthObservationEquipment//eop:Instrument/eop:shortName">
									<xsl:if test="compare($param_value, .)  != 0">
										<xsl:text>false</xsl:text>
									</xsl:if>
								</xsl:for-each>
							</xsl:variable>
							<xsl:choose>
								<xsl:when test="not(normalize-space(string($instrument_match)) = '') ">
									<ctl:message>[FAILURE] At least an instrument does not match the provided search parameter value</ctl:message>
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
</ctl:package>

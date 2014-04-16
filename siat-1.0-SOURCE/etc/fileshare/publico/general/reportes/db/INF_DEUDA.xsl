<?xml version = '1.0' encoding = 'ISO-8859-1'?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:fo="http://www.w3.org/1999/XSL/Format" version="1.0">
	
	<xsl:variable name="Auto" select="//data/InformeDeudaAdapter/EsAutoliquidable"/>
	<xsl:variable name="PoseeComercio" select="//data/InformeDeudaAdapter/PoseeComercio"/>
	<!-- Template principal -->
	<xsl:template match="/siat">
		<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">

			<fo:layout-master-set>
				<fo:simple-page-master margin-right="2.5cm"
						margin-left="1cm" margin-bottom="1.5cm" margin-top="1cm"
						page-width="21cm" page-height="29.7cm" master-name="left">
					<fo:region-before region-name="region-header" extent="1cm" />
					<fo:region-body region-name="region-body" margin-top="1cm" />
					<fo:region-after region-name="region-footer" extent="1.5cm" />
				</fo:simple-page-master>
			</fo:layout-master-set>
			
			
			<fo:page-sequence master-reference="left">
				<fo:flow flow-name="region-body">
						<!-- Si corresponde se imprime la caratula -->						
						<xsl:if test="./data/InformeDeudaAdapter/PoseeCaratula='true'">
							<xsl:call-template name="Caratula">								
								 <xsl:with-param name="adapter"  select="./data/InformeDeudaAdapter"/>
							</xsl:call-template>
							<fo:block break-after="page"/>
						</xsl:if>
						
						<!-- aplica el template para dibujar el LiqDeudaAdapter de la cuenta Principal -->
						<xsl:apply-templates select="./data/InformeDeudaAdapter/LiqDeudaAdapterCuentaPpal"/>

																	
						<!-- se dibujan las cuentas relacionadas por Objmp -->
						<xsl:for-each select="./data/InformeDeudaAdapter/ListCuentaRelObjImp/LiqDeudaAdapter">
							<fo:block break-before="page"/>
							<xsl:apply-templates select="."/>
						</xsl:for-each>

						<!-- se dibujan las cuentas relacionadas por Uni/Des -->
						<xsl:for-each select="./data/InformeDeudaAdapter/ListCuentaRelDesUni/LiqDeudaAdapter">
							<fo:block break-before="page"/>
							<xsl:apply-templates select="."/>
						</xsl:for-each>
						<fo:block id="last-page"/>
				</fo:flow>
			</fo:page-sequence>
			
		</fo:root>	
	</xsl:template>
	<!-- FIN Template principal -->
	
	<!-- Template de caratula -->
	<xsl:template name="Caratula">
		<xsl:param name="adapter"/>
		
		<fo:block-container position="absolute" top="0mm" left="0mm" height="250mm" width="100%"
						border-color="black" border-style="solid" border-width="0.75pt">
		
		<fo:block space-before="10mm" margin-left="8mm" margin-right="8mm">
			<fo:table>
					<fo:table-column column-width="proportional-column-width(50)" />
					<fo:table-column column-width="proportional-column-width(50)"/>
					<fo:table-header>
						<fo:table-row>
							<fo:table-cell number-columns-spanned="2">
								<fo:external-graphic
									height="19mm" width="13mm"
									src="{concat(//FileSharePath, '/publico/general/reportes/images/escudo_muni.jpg')}" />
							</fo:table-cell>
						</fo:table-row>
						<fo:table-row>
							<fo:table-cell text-align="center"  number-columns-spanned="2">
								<fo:block font-weight="bold" font-size="12pt">Municipalidad de [LOCALIDAD] </fo:block>
								<fo:block font-weight="bold" font-size="12pt">DIRECCION GENERAL GESTION DE RECURSOS</fo:block>
								<fo:block font-weight="bold" font-size="12pt">Direcci&#243;n de Atenci&#243;n al Contribuyente y Mantenimiento de Padr&#243;n</fo:block>
							</fo:table-cell>
						</fo:table-row>
						<fo:table-row>	
							<fo:table-cell number-columns-spanned="2" text-align="center">
								<fo:block space-before="3mm" font-size="15pt">INFORME DE DEUDA</fo:block>
							</fo:table-cell>
						</fo:table-row>
					</fo:table-header>

					<fo:table-body>
						<fo:table-row>
							<fo:table-cell number-columns-spanned="2" text-align="left">
								<fo:block space-before="5mm" font-size="10pt">
									Identificaci&#243;n del Sellado: <xsl:value-of select="$adapter/InfomeDeudaCaratula/DesTipoTramite"/>
								</fo:block>
								<fo:block space-before="5mm" font-size="10pt">
									Nro. Liquidaci&#243;n: <xsl:value-of select="$adapter/InfomeDeudaCaratula/NroLiquidacion"/>
								</fo:block>
								<fo:block space-before="5mm" font-size="10pt">
									<xsl:if test="$adapter/InfomeDeudaCaratula/IsNroRecibo='true'">
										Recibo: <xsl:value-of select="$adapter/InfomeDeudaCaratula/NroRecibo"/>
									</xsl:if>
									<xsl:if test="$adapter/InfomeDeudaCaratula/IsNroRecibo!='true'">
										C&#243;digo Referencia Pago: <xsl:value-of select="$adapter/InfomeDeudaCaratula/CodRefPag"/>
									</xsl:if> 
								</fo:block>
								<fo:block space-before="5mm" font-size="10pt">
									La Direcci&#243;n General de Gesti&#243;n de Recursos CERTIFICA: que el inmueble empadronado en la
									Secc. 
<xsl:value-of select="substring($adapter/InfomeDeudaCaratula/Cuenta/ListAtributoObjImp/LiqAtrValorVO[Key='Catastral']/Value, 1, 2)"/>
									Manz.									
<xsl:value-of select="substring($adapter/InfomeDeudaCaratula/Cuenta/ListAtributoObjImp/LiqAtrValorVO[Key='Catastral']/Value, 4, 3)"/>
									Graf. 
<xsl:value-of select="substring($adapter/InfomeDeudaCaratula/Cuenta/ListAtributoObjImp/LiqAtrValorVO[Key='Catastral']/Value, 8, 3)"/>
									S/D.
<xsl:value-of select="substring($adapter/InfomeDeudaCaratula/Cuenta/ListAtributoObjImp/LiqAtrValorVO[Key='Catastral']/Value, 12, 3)"/>
									S/P. 
<xsl:value-of select="substring($adapter/InfomeDeudaCaratula/Cuenta/ListAtributoObjImp/LiqAtrValorVO[Key='Catastral']/Value, 16, 3)"/>
									Cuenta: 
									<xsl:value-of select="$adapter/InfomeDeudaCaratula/Cuenta/NroCuenta"/>, 
									cuya ubicaci&#243;n y dato dominial se suministra por el solicitante, 
									presenta el dia <xsl:value-of select="substring(cabecera/FechaActual,1,2)"/>
									de <xsl:value-of select="cabecera/NombreMes"/>
									de <xsl:value-of select="substring(cabecera/FechaActual,7,4)"/>
									la siguiente situaci&#243;n fiscal seg&#250;n se detalla:
								</fo:block>
								
								<fo:block space-before="5mm" font-size="10pt">
									
									<!-- cuenta principal -->
									<xsl:call-template name="escribirCuentaCaratula">
											<xsl:with-param name="cadenaAnterior">1)</xsl:with-param>
											<xsl:with-param name="Cuenta" select="$adapter/LiqDeudaAdapterCuentaPpal/Cuenta"/>
									</xsl:call-template>
														
									<!-- cuentas relacionadas por objImp -->									
									<xsl:for-each select="$adapter/ListCuentaRelObjImp/LiqDeudaAdapter">
										<xsl:call-template name="escribirCuentaCaratula">
											<xsl:with-param name="Cuenta" select="Cuenta"/>
										</xsl:call-template>
									</xsl:for-each>
															
									<!-- cuentas relacionadas por desgloses y unificaciones -->						
									<xsl:for-each select="$adapter/ListCuentaRelDesUni/LiqDeudaAdapter">
										<xsl:call-template name="escribirCuentaCaratula">
											<xsl:with-param name="Cuenta" select="Cuenta"/>
										</xsl:call-template>
									</xsl:for-each>
								
								</fo:block>
								
								<fo:block space-before="5mm" font-size="10pt">
									Adjuntando comprobante de pago de la deuda indicada, surte efectos de Libre Deuda 								
								</fo:block>
							</fo:table-cell>
						</fo:table-row>
					</fo:table-body>
			</fo:table>
		</fo:block>	
		
		</fo:block-container>
	</xsl:template>
	<!-- Template de caratula -->	
	
	<!-- Template para una fila de LiqDeudaVO -->
	<xsl:template match="LiqDeudaVO" mode="filas">
			<fo:table-row>
				<fo:table-cell padding="1pt">														
					<fo:block ><xsl:value-of select="PeriodoDeuda"/></fo:block>
				</fo:table-cell>
				<fo:table-cell>
					<fo:block><xsl:value-of select="FechaVto"/></fo:block>
				</fo:table-cell>
				<xsl:choose>
   					<xsl:when test="normalize-space(PoseeObservacion)='true'">
   						<xsl:variable name="SpanCols">
							<xsl:choose>
								<xsl:when test="$Auto='true'">
									<xsl:value-of select="5"/>
								</xsl:when>
								<xsl:otherwise>
									<xsl:value-of select="3"/>
								</xsl:otherwise>
							</xsl:choose>
						</xsl:variable>
							<fo:table-cell number-columns-spanned="{($SpanCols)}"><fo:block><xsl:value-of select="Observacion"/></fo:block></fo:table-cell>
					</xsl:when>
					<xsl:otherwise>
						<xsl:if test="$Auto = 'true'">
								<fo:table-cell><fo:block><xsl:value-of select="ImporteView"/></fo:block></fo:table-cell>
						</xsl:if>																			
						<fo:table-cell><fo:block><xsl:value-of select="SaldoView"/></fo:block></fo:table-cell>
						<xsl:if test="$Auto = 'true'">
								<fo:table-cell><fo:block><xsl:value-of select="FechaPago"/></fo:block></fo:table-cell>
						</xsl:if>																			
						<fo:table-cell><fo:block><xsl:value-of select="ActualizacionView"/></fo:block></fo:table-cell>
						<fo:table-cell><fo:block><xsl:value-of select="TotalView"/></fo:block></fo:table-cell>
					</xsl:otherwise>
				</xsl:choose>
			</fo:table-row>
	</xsl:template>
	
	
	
	<!-- FIN Template para una fila de LiqDeudaVO -->
	
	<!-- Template para escribir un LiqDeudaAdapter -->
	<xsl:template match="*">
					<!-- Cabecera -->
	                  <fo:block-container position="absolute" top="100mm" left="40mm" height="55mm" width="100%" >
							<fo:block font-size="30pt">SIN VALOR LEGAL</fo:block>
					  </fo:block-container>
							<fo:block>
								<fo:table>
									<fo:table-column column-width="proportional-column-width(8)" />
									<fo:table-column column-width="proportional-column-width(21)"/>
									<fo:table-column column-width="proportional-column-width(57)"/>
									<fo:table-column column-width="proportional-column-width(14)"/>
									<fo:table-header>
										<fo:table-row>
											<fo:table-cell>
												<fo:external-graphic
													height="19mm" width="13mm"
													src="{concat(//FileSharePath, '/publico/general/reportes/images/escudo_muni.jpg')}" />
											</fo:table-cell>
											<fo:table-cell text-align="center">
												<fo:block font-weight="bold" font-size="12pt">Municipalidad de [LOCALIDAD]</fo:block>
												<fo:block font-weight="bold" font-size="12pt">DE</fo:block>
												<fo:block font-weight="bold" font-size="12pt">[LOCALIDAD]</fo:block>
											</fo:table-cell>
											<fo:table-cell text-align="center">
												<fo:block font-weight="bold" font-size="13pt">DIRECCION GENERAL GESTION DE</fo:block>
												<fo:block font-weight="bold" font-size="13pt">RECURSOS</fo:block>
												<fo:block space-before="3mm" font-size="10pt" font-weight="bold">
													<xsl:value-of select="//Categoria"/>
												</fo:block>
											</fo:table-cell>
											<fo:table-cell text-align="left" font-size="8pt">
												<fo:block space-before="4mm" >
													<fo:inline font-weight="bold">Hoja:&#160;</fo:inline>
													<fo:inline><fo:page-number/> </fo:inline>
													<fo:inline> / <fo:page-number-citation ref-id="last-page"/></fo:inline>
												</fo:block>
												<fo:block space-before="1mm">
													<fo:inline font-weight="bold">Fecha:&#160;</fo:inline>
													<fo:inline><xsl:call-template name="FechaConfeccion"/></fo:inline></fo:block>
												<fo:block space-before="1mm">
													<fo:inline font-weight="bold">Usuario:&#160;</fo:inline>
													<fo:inline><xsl:value-of select="/siat/cabecera/Usuario"/></fo:inline></fo:block>
											</fo:table-cell>
										</fo:table-row>
						<!-- Datos de la cuenta -->	
										<fo:table-row font-size="10pt">
											<fo:table-cell number-columns-spanned="4">												
												<fo:block border-color="black" border-style="solid" border-width="1.5pt" space-after="2mm" space-before="2mm">													
													<xsl:call-template name="datoCuenta">
														<xsl:with-param name="label">Nro. Cuenta</xsl:with-param>
														 <xsl:with-param name="valor"><xsl:value-of select="Cuenta/NroCuentaView" /></xsl:with-param>
													</xsl:call-template>
													<xsl:call-template name="datoCuenta">
														<xsl:with-param name="label">Recurso</xsl:with-param>
														 <xsl:with-param name="valor"><xsl:value-of select="Cuenta/DesRecurso" /></xsl:with-param>
													</xsl:call-template>
													<xsl:if test="$PoseeComercio='true'">
														<xsl:call-template name="datoCuenta">
															<xsl:with-param name="label">Nro. Comercio</xsl:with-param>
															<xsl:with-param name="valor">
															 	<xsl:value-of select="Cuenta/ListAtributoObjImp/LiqAtrValorVO[Key='NroComercio']/Value" /></xsl:with-param>
														</xsl:call-template>
													</xsl:if>
													<xsl:if test="Cuenta/ListAtributoContr/LiqAtrValorVO[Key='Regimen']/Value!=null">
														<xsl:if test="$PoseeComercio='true'">
															<xsl:call-template name="datoCuenta">
																<xsl:with-param name="label">R&#233;gimen Actual</xsl:with-param>
																<xsl:with-param name="valor">
																 	<xsl:value-of select="Cuenta/ListAtributoCuenta/LiqAtrValorVO[Key='Regimen']/Value" /></xsl:with-param>
															</xsl:call-template>
														</xsl:if>
													</xsl:if>
													<xsl:if test="Cuenta/ListAtributoContr/LiqAtrValorVO[Key='CER']/Value!=null">
														<xsl:call-template name="datoCuenta">
															<xsl:with-param name="label">Contribuyente CER</xsl:with-param>
															<xsl:with-param name="valor">
															 	<xsl:value-of select="Cuenta/ListAtributoContr/LiqAtrValorVO[Key='CER']/Value" /></xsl:with-param>
														</xsl:call-template>
													</xsl:if>
													<xsl:if test="//DatosCdMVO/NumeroObra !=''">
														<xsl:call-template name="datoCuenta">
															<xsl:with-param name="label">Nro de Obra</xsl:with-param>
															 <xsl:with-param name="valor"><xsl:value-of select="//DatosCdMVO/NumeroObra" /></xsl:with-param>
														</xsl:call-template>
													</xsl:if>
														
													<xsl:call-template name="datoCuenta">
														<xsl:with-param name="label">Cod. Gesti&#243;n Personal</xsl:with-param>
														 <xsl:with-param name="valor"><xsl:value-of select="Cuenta/CodGestionPersonalView"/></xsl:with-param>
													</xsl:call-template>
													<xsl:call-template name="datoCuenta">
														<xsl:with-param name="label">Domicilio de Envio</xsl:with-param>
														 <xsl:with-param name="valor"><xsl:value-of select="Cuenta/DesDomEnv"/></xsl:with-param>
													</xsl:call-template>
													<xsl:call-template name="datoCuenta">
														<xsl:with-param name="label">Catastral</xsl:with-param>
														 <xsl:with-param name="valor">
														 	<xsl:value-of select="Cuenta/ListAtributoObjImp/LiqAtrValorVO[Key='Catastral']/Value" /></xsl:with-param>
													</xsl:call-template>
													<xsl:if test="$Auto != 'true'">
														<xsl:call-template name="datoCuenta">
															<xsl:with-param name="label">Tipo de Parcela</xsl:with-param>
															 <xsl:with-param name="valor"><xsl:value-of select="Cuenta/ListAtributoObjImp/LiqAtrValorVO[Key='TipoParcela']/Value" /></xsl:with-param>
														</xsl:call-template>
														<xsl:call-template name="datoCuenta">
															<xsl:with-param name="label">Ubicaci&#243;n</xsl:with-param>
															 <xsl:with-param name="valor"><xsl:value-of select="Cuenta/ListAtributoObjImp/LiqAtrValorVO[Key='Ubicacion']/Value" /></xsl:with-param>
														</xsl:call-template>
													</xsl:if>
													<xsl:if test="$Auto='true'">
														<xsl:call-template name="datoCuenta">
															<xsl:with-param name="label">Filtros de Visualizaci&#243;n Aplicados</xsl:with-param>
															 <xsl:with-param name="valor"><xsl:value-of select="Cuenta/DetalleFiltros" /></xsl:with-param>
														</xsl:call-template>
													</xsl:if>
													
													<xsl:if test="Cuenta/Observacion!=''">
														<xsl:call-template name="datoCuenta">
															<xsl:with-param name="label">Observaci&#243;n</xsl:with-param>
															<xsl:with-param name="valor">
															 	<xsl:value-of select="Cuenta/Observacion" /></xsl:with-param>
														</xsl:call-template>
													</xsl:if>
													
													<xsl:if test="Cuenta/DesEstado!=''">
														<xsl:call-template name="datoCuenta">
															<xsl:with-param name="label">Observaci&#243;n</xsl:with-param>
															<xsl:with-param name="valor">
																<xsl:value-of select="Cuenta/DesEstado"/>&#160;-&#160;<xsl:value-of select="Cuenta/ExpedienteCierre"/>
															 </xsl:with-param>
														</xsl:call-template>
													</xsl:if>
													
												</fo:block>																						
											</fo:table-cell>
										</fo:table-row>
							<!-- FIN Datos de la cuenta -->

							
									</fo:table-header>
				<!-- FIN Cabecera -->
									<fo:table-body>

					<!-- Titulares - no llama al template - Se saco del header porque si son muchos titulares no entra mas que la cabecera en la hoja y pincha-->
										<xsl:call-template name="tituloListDeuda">
											<xsl:with-param name="titulo">Titulares</xsl:with-param>
										</xsl:call-template>
						
										<fo:table-row>
											<fo:table-cell number-columns-spanned="4" font-size="10pt">
													<fo:block space-before="1mm">
														<fo:inline color="black" text-align="left">															
															<xsl:if test="count(Cuenta/ListTitular/LiqTitularVO)>0">
													    		<xsl:for-each select="Cuenta/ListTitular/LiqTitularVO">													    		
													    			<xsl:if test="position()>1">,&#160;&#160;&#160;</xsl:if>
													    			<xsl:value-of select="DesTitularContr"/>										
													    		</xsl:for-each>
													    	</xsl:if>
													    </fo:inline>
													</fo:block>																																						
											</fo:table-cell>
										</fo:table-row>
										
										<fo:table-row>
				
				<!-- Celda contenedora de la lista de deudas -->									
											<fo:table-cell number-columns-spanned="4" font-size="10pt">
												<!-- Tabla de lista de deudas -->
												<fo:table padding="2pt" height="200mm">
													<fo:table-column />
													<fo:table-column />
													<xsl:choose>
														<xsl:when test="$Auto ='true'">
															<fo:table-column />
														</xsl:when>
													</xsl:choose>
													<fo:table-column />
													<fo:table-column />
													<fo:table-column />
													<fo:table-body>

				<!--  Deudas procedimientoCYQ -->		
														<xsl:choose>						
															<xsl:when test="count(ListProcedimientoCyQ/LiqDeudaCyQVO)>0">

																<xsl:call-template name="tituloListDeuda">
																	<xsl:with-param name="titulo">Cobro en Concursos y Quiebras</xsl:with-param>
																</xsl:call-template>										
																														
																<!-- Recorre cada procedimiento -->											
																<xsl:for-each select="ListProcedimientoCyQ/LiqDeudaCyQVO">
																	<fo:table-row>
																		<fo:table-cell number-columns-spanned="2">
																			<fo:block space-before="2mm" space-after="2mm" text-align="left" wrap-option="no-wrap">
																				Nro. Procedimiento: <xsl:value-of select="NroProcedimiento"/>
																			</fo:block>
																		</fo:table-cell>
																		<fo:table-cell number-columns-spanned="3">
																			<fo:block wrap-option="no-wrap" space-before="2mm" space-after="2mm">
																				Fecha Actualizacion: <xsl:value-of select="FechaActualizacion"/>
																			</fo:block>
																		</fo:table-cell>
																														
																	</fo:table-row>
														    		<xsl:apply-templates select="ListDeuda">										        		
																		<xsl:with-param name="tituloTotal">TOTAL:</xsl:with-param>
																		<xsl:with-param name="valorTotal"><xsl:value-of select="SubTotalView"/></xsl:with-param>
																		<xsl:with-param name="leyendaResultVacio">&#160;</xsl:with-param><!-- No se encontraron Deudas en Concursos y Quiebras -->
																	</xsl:apply-templates>																
																</xsl:for-each>
																<xsl:call-template name="totalListaDeudas">
																	<xsl:with-param name="leyenda">Concursos y Quiebras</xsl:with-param>
																	<xsl:with-param name="valor"><xsl:value-of select="TotalDeudaCyQView"/></xsl:with-param>
																</xsl:call-template>
															</xsl:when>
															<xsl:otherwise>
																<fo:table-row>
																	<fo:table-cell number-columns-spanned="5">
																		<fo:block text-align="left" wrap-option="no-wrap">&#160;</fo:block><!-- No Se encontraron deudas en Concursos y Quiebras -->
																	</fo:table-cell>													
																</fo:table-row>
															</xsl:otherwise>	
														</xsl:choose>
	
					<!--  Deudas en Gestion Judicial -->	
														
														<xsl:choose>						
															<xsl:when test="count(ListProcurador/LiqDeudaProcuradorVO)>0">

																<xsl:call-template name="tituloListDeuda">
																	<xsl:with-param name="titulo">Cobro en Gesti&#243;n Judicial</xsl:with-param>
																</xsl:call-template>										

																<!-- Recorre cada procurador -->											
																<xsl:for-each select="ListProcurador/LiqDeudaProcuradorVO">
																	<fo:table-row>
																		<fo:table-cell number-columns-spanned="4">
																			<fo:block space-before="2mm" space-after="2mm" text-align="left">
																				Procurador:<xsl:value-of select="DesProcurador"/>
																			</fo:block>
																		</fo:table-cell>													
																	</fo:table-row>
														    		<xsl:apply-templates select="ListDeuda">										        		
																		<xsl:with-param name="tituloTotal">TOTAL</xsl:with-param>
																		<xsl:with-param name="valorTotal"><xsl:value-of select="SubTotalView"/></xsl:with-param>
																		<xsl:with-param name="leyendaResultVacio">No se encontraron Deudas en Gesti&#243;n Judicial</xsl:with-param>
																	</xsl:apply-templates>																
																</xsl:for-each>
																<xsl:call-template name="totalListaDeudas">
																	<xsl:with-param name="leyenda">Gesti&#243;n Judicial</xsl:with-param>
																	<xsl:with-param name="valor"><xsl:value-of select="TotalDeudaProcuradorView"/></xsl:with-param>
																</xsl:call-template>															
															</xsl:when>
															<xsl:otherwise>
																<fo:table-row>
																	<fo:table-cell number-columns-spanned="5">
																		<fo:block text-align="left" wrap-option="no-wrap">&#160;</fo:block><!-- No Se encontraron deudas en Gesti&#243;n Judicial -->
																	</fo:table-cell>													
																</fo:table-row>
															</xsl:otherwise>	
														</xsl:choose>
																															
					<!-- Deudas en Gestion Administrativa -->
														<xsl:if test="count(ListGestionDeudaAdmin/LiqDeudaAdminVO/ListDeuda)>0">		
															<xsl:call-template name="tituloListDeuda">
																<xsl:with-param name="titulo">Cobro en Gesti&#243;n Administrativa</xsl:with-param>
															</xsl:call-template>										
																						
												    		<xsl:apply-templates select="ListGestionDeudaAdmin/LiqDeudaAdminVO/ListDeuda">										        		
																<xsl:with-param name="leyendaResultVacio">No se encontraron Deudas en Gesti&#243;n Administrativa</xsl:with-param>
															</xsl:apply-templates>
															<xsl:call-template name="totalListaDeudas">
																<xsl:with-param name="leyenda">Gesti&#243;n Administrativa</xsl:with-param>
																<xsl:with-param name="valor"><xsl:value-of select="ListGestionDeudaAdmin/LiqDeudaAdminVO/SubTotalView"/></xsl:with-param>
															</xsl:call-template>															
														</xsl:if>	
																										        		
					<!-- Total General -->
														<fo:table-row>
															<fo:table-cell number-columns-spanned="5">
																<fo:block space-before="5mm" space-after="5mm">Comprende pagos asentados al:&#160;<xsl:value-of select="FechaAcentamiento"/></fo:block>	
															</fo:table-cell>
														</fo:table-row>

														<xsl:if test="$Auto='true'">
															<fo:table-row>
																<fo:table-cell number-columns-spanned="5">
																	<fo:block space-before="5mm" space-after="5mm" font-weight="bold">(*) No incluye posible deuda por per&#237;odos no declarados</fo:block>	
																</fo:table-cell>
															</fo:table-row>
														</xsl:if>	

														<fo:table-row>
															<fo:table-cell number-columns-spanned="5" >
																<fo:block>
																	<fo:leader leader-length="6in" leader-pattern="dots" alignment-baseline="middle"
															             rule-thickness="1.5pt" color="black"/>
																</fo:block>
															</fo:table-cell>
														</fo:table-row>
														<fo:table-row>
															<fo:table-cell number-columns-spanned="4" >
																<fo:block>TOTAL GENERAL AL DIA:<xsl:call-template name="FechaConfeccion"/></fo:block>																
															</fo:table-cell>
															<fo:table-cell>
																<fo:block><xsl:value-of select="TotalView"/></fo:block>
															</fo:table-cell>
														</fo:table-row>								
														<fo:table-row>
															<fo:table-cell number-columns-spanned="5" >
																<fo:block>
																	<fo:leader leader-length="6in" leader-pattern="dots" alignment-baseline="middle"
															             rule-thickness="1.5pt" color="black"/>
																</fo:block>
															</fo:table-cell>
														</fo:table-row>
				<!-- FIN Total General -->														
													</fo:table-body>
												</fo:table>
				<!-- FIN Tabla de lista de deudas -->
											</fo:table-cell>
				<!-- FIN Celda contenedora de la lista de deudas -->
										</fo:table-row>
									</fo:table-body>
								</fo:table>
							</fo:block>
							
				<!-- Convenios -->
				<xsl:if test="count(ListConvenioCuentaAdapter/LiqConvenioCuentaAdapter)>0">
							<xsl:variable name="cantConvenios" select="count(ListConvenioCuentaAdapter/LiqConvenioCuentaAdapter)"/>							
					<!-- Cabecera Convenios -->
								<fo:table>
									<fo:table-column column-width="proportional-column-width(8)" />
									<fo:table-column column-width="proportional-column-width(21)"/>
									<fo:table-column column-width="proportional-column-width(57)"/>
									<fo:table-column column-width="proportional-column-width(14)"/>
									<fo:table-header>
										<fo:table-row>
											<fo:table-cell>
												<fo:external-graphic
													height="17mm" width="13mm"
													src="{concat(//FileSharePath, '/publico/general/reportes/images/escudo_muni.jpg')}" />
											</fo:table-cell>
											<fo:table-cell text-align="center">
												<fo:block font-weight="bold" font-size="12pt">Municipalidad de [LOCALIDAD]</fo:block>
												<fo:block font-weight="bold" font-size="12pt">DE</fo:block>
												<fo:block font-weight="bold" font-size="12pt">[LOCALIDAD]</fo:block>
											</fo:table-cell>
											<fo:table-cell text-align="center">
												<fo:block font-weight="bold" font-size="13pt">DIRECCION GENERAL GESTION DE</fo:block>
												<fo:block font-weight="bold" font-size="13pt">RECURSOS</fo:block>
												<fo:block space-before="3mm" font-size="10pt" font-weight="bold">DETALLES DE CONVENIOS</fo:block>
											</fo:table-cell>
											<fo:table-cell text-align="left" font-size="8pt">
												<fo:block space-before="4mm">
													<fo:inline font-weight="bold">Hoja:&#160;</fo:inline>
													<fo:inline><fo:page-number/></fo:inline>
												</fo:block>
												<fo:block space-before="1mm">
													<fo:inline font-weight="bold">Fecha:&#160;</fo:inline>													
													<fo:inline><xsl:call-template name="FechaConfeccion"/></fo:inline>
												</fo:block>
												<fo:block space-before="1mm">
													<fo:inline font-weight="bold">Usuario:&#160;</fo:inline>
													<fo:inline><xsl:value-of select="/siat/cabecera/Usuario"/></fo:inline>
												</fo:block>
											</fo:table-cell>
										</fo:table-row>

									</fo:table-header>
									<fo:table-body>
					<!-- FIN Cabecera Convenios -->
										<fo:table-row>
											<fo:table-cell font-size="10pt" number-columns-spanned="4">
												<fo:block>
													<fo:table padding="2pt" height="220mm">
														<fo:table-column column-width="proportional-column-width(1)"/>
														<fo:table-column column-width="proportional-column-width(99)"/>
														<fo:table-body>
															<fo:table-row height="200mm">
																<fo:table-cell>&#160;</fo:table-cell>
																<fo:table-cell>
																	<xsl:for-each select ="ListConvenioCuentaAdapter/LiqConvenioCuentaAdapter">
										<!-- Datos del convenio -->
																		<fo:block space-before="3mm" border-color="black" border-style="solid" border-width="1.5pt">
																			<fo:table>
																				<fo:table-column/>
																				<fo:table-header>																				
																					<fo:table-row font-weight="bold">
																						<fo:table-cell border-bottom-color="black" border-bottom-style="solid" border-bottom-width="0.5pt">
																							<fo:block text-align="center">Datos del Convenio</fo:block>
																						</fo:table-cell>																						
																					</fo:table-row>
																				</fo:table-header>
																				<fo:table-body>
																					<fo:table-row >
																						<fo:table-cell>
																							<fo:block>
																								<fo:inline font-weight="bold">Nro. convenio:&#160;</fo:inline>
																								<fo:inline><xsl:value-of select="Convenio/NroConvenio"/></fo:inline>
																							</fo:block>																							
																							<fo:block>
																								<fo:inline font-weight="bold">Plan de Pago:&#160;</fo:inline>
																								<fo:inline><xsl:value-of select="Convenio/DesPlan"/></fo:inline>
																							</fo:block>
																							<fo:block>
																								<fo:inline font-weight="bold">V&#237;a Deuda:&#160;</fo:inline>
																								<fo:inline><xsl:value-of select="Convenio/DesViaDeuda"/></fo:inline>
																							</fo:block>
																							<fo:block>
																								<fo:inline font-weight="bold">Estado Convenio:&#160;</fo:inline>
																								<fo:inline><xsl:value-of select="Convenio/DesEstadoConvenio"/></fo:inline>
																							</fo:block>
																							<fo:block>
																								<fo:inline font-weight="bold">Cant. Cuotas:&#160;</fo:inline>
																								<fo:inline><xsl:value-of select="Convenio/CanCuotasPlan"/></fo:inline>
																							</fo:block>
																							<fo:block>
																								<fo:inline font-weight="bold">Importe Total:&#160;</fo:inline>
																								<fo:inline><xsl:value-of select="Convenio/TotImporteConvenioView"/></fo:inline>
																							</fo:block>
																						</fo:table-cell>
																					</fo:table-row>
																				</fo:table-body>
																			</fo:table>
																		</fo:block>
										<!-- Datos de la formalizacion -->	
																		<fo:block space-before="5mm" border-color="black" border-style="solid" border-width="1.5pt">
																			<fo:table>
																				<fo:table-column/>
																				<fo:table-header>
																					<fo:table-row font-weight="bold">
																						<fo:table-cell border-bottom-color="black" border-bottom-style="solid" border-bottom-width="0.5pt">
																							<fo:block text-align="center">Datos de la Formalizaci&#243;n</fo:block>
																						</fo:table-cell>																						
																					</fo:table-row>																			
																				</fo:table-header>
																				<fo:table-body>
																					<fo:table-row>
																						<fo:table-cell>
																							<fo:block>
																								<fo:inline font-weight="bold">Fecha de Formalizaci&#243;n:&#160;</fo:inline>
																								<fo:inline><xsl:value-of select="Convenio/FechaFor"/></fo:inline>
																							</fo:block>
																							<fo:block>
																								<fo:inline font-weight="bold">Apellido:&#160;</fo:inline>
																								<fo:inline><xsl:value-of select="Convenio/Persona/Apellido"/></fo:inline>
																							</fo:block>
																							<fo:block>
																								<fo:inline font-weight="bold">Nombres:&#160;</fo:inline>
																								<fo:inline><xsl:value-of select="Convenio/Persona/Nombres"/></fo:inline>
																							</fo:block>
																							<fo:block>
																								<fo:inline font-weight="bold">Apellido Materno:&#160;</fo:inline>
																								<fo:inline><xsl:value-of select="Convenio/Persona/ApellidoMaterno"/></fo:inline>
																							</fo:block>
																							<fo:block>
																								<fo:inline font-weight="bold">Tipo y Nro. Doc.:&#160;</fo:inline>
																								<fo:inline><xsl:value-of select="Convenio/Persona/Documento/TipoyNumeroView"/></fo:inline>
																							</fo:block>
																							<fo:block>
																								<fo:inline font-weight="bold">En Carater de:&#160;</fo:inline>
																								<fo:inline><xsl:value-of select="Convenio/TipoPerFor/DesTipoPerFor"/></fo:inline>
																							</fo:block>
																							<fo:block>
																								<fo:inline font-weight="bold">Domiclio Particular:&#160;</fo:inline>
																								<fo:inline><xsl:value-of select="Convenio/Persona/Domicilio/View"/></fo:inline>
																							</fo:block>
																							<fo:block>
																								<fo:inline font-weight="bold">Telefono:&#160;</fo:inline>
																								<fo:inline><xsl:value-of select="Convenio/Persona/Telefono"/></fo:inline>
																							</fo:block>
																							<fo:block>
																								<fo:inline font-weight="bold">Documentaci&#243;n Aportada:&#160;</fo:inline>
																								<fo:inline><xsl:value-of select="Convenio/TipoDocApo/DesTipoDocApo"/></fo:inline>
																							</fo:block>
																							<fo:block>
																								<fo:inline font-weight="bold">Observaci&#243;n:&#160;</fo:inline>
																								<fo:inline><xsl:value-of select="Convenio/ObservacionFor"/></fo:inline>
																							</fo:block>
																							<fo:block>
																								<fo:inline font-weight="bold">Agente Interviniente:&#160;</fo:inline>
																								<fo:inline><xsl:value-of select="Convenio/UsusarioFor"/></fo:inline>
																							</fo:block>
																						</fo:table-cell>
																					</fo:table-row>
																				</fo:table-body>
																			</fo:table>
																		</fo:block>				
										<!-- Periodos incluidos -->																		
																		<fo:block space-before="5mm" border-color="black" border-style="solid" border-width="1.5pt">
																			<fo:table text-align="center">
																				<fo:table-column/>
																				<fo:table-column/>
																				<fo:table-column/>
																				<fo:table-column/>
																				<fo:table-column/>
																				<fo:table-header>
																				
																					<fo:table-row font-weight="bold" >
																						<fo:table-cell number-columns-spanned="5" border-bottom-color="black" border-bottom-style="solid" border-bottom-width="0.5pt">
																							<fo:block>Per&#237;odos Incluidos</fo:block>
																						</fo:table-cell>
																					</fo:table-row>
																					<fo:table-row>
																						<fo:table-cell font-weight="bold" border-right-color="black" border-right-style="solid" border-right-width="0.5pt">
																							<fo:block>Deuda</fo:block>
																						</fo:table-cell>
																						<fo:table-cell font-weight="bold" border-right-color="black" border-right-style="solid" border-right-width="0.5pt">
																							<fo:block>Fecha Vto.</fo:block>
																						</fo:table-cell>
																						<fo:table-cell font-weight="bold" border-right-color="black" border-right-style="solid" border-right-width="0.5pt">
																							<fo:block>Importe</fo:block>
																						</fo:table-cell>
																						<fo:table-cell font-weight="bold" border-right-color="black" border-right-style="solid" border-right-width="0.5pt">
																							<fo:block>Actualizaci&#243;n</fo:block>
																						</fo:table-cell>
																						<fo:table-cell font-weight="bold" border-right-color="black" border-right-style="solid" border-right-width="0.5pt">
																							<fo:block>Total</fo:block>
																						</fo:table-cell>
																					</fo:table-row>
																				</fo:table-header>																				
																				<fo:table-body>
																					<xsl:for-each select="Convenio/ListPeriodoIncluido/LiqDeudaVO">
																						<fo:table-row>
																							<fo:table-cell border-top-color="black" border-top-style="solid" border-top-width="0.5pt" border-right-color="black" border-right-style="solid" border-right-width="0.5pt">
																								<fo:block><xsl:value-of select="PeriodoDeuda"/></fo:block>
																							</fo:table-cell>
																							<fo:table-cell border-top-color="black" border-top-style="solid" border-top-width="0.5pt" border-right-color="black" border-right-style="solid" border-right-width="0.5pt">
																								<fo:block><xsl:value-of select="FechaVto"/></fo:block>
																							</fo:table-cell>
																							<fo:table-cell border-top-color="black" border-top-style="solid" border-top-width="0.5pt" border-right-color="black" border-right-style="solid" border-right-width="0.5pt">
																								<fo:block><xsl:value-of select="SaldoView"/></fo:block>
																							</fo:table-cell>
																							<fo:table-cell border-top-color="black" border-top-style="solid" border-top-width="0.5pt" border-right-color="black" border-right-style="solid" border-right-width="0.5pt">
																								<fo:block><xsl:value-of select="ActualizacionView"/></fo:block>
																							</fo:table-cell>
																							<fo:table-cell border-top-color="black" border-top-style="solid" border-top-width="0.5pt" border-right-color="black" border-right-style="solid" border-right-width="0.5pt">
																								<fo:block><xsl:value-of select="TotalView"/></fo:block>
																							</fo:table-cell>
																						</fo:table-row>																																										
																					</xsl:for-each>																				
																				</fo:table-body>
																			</fo:table>
																		</fo:block>
																		
										<!-- Cuotas pagas -->
																		<fo:block space-before="5mm" border-color="black" border-style="solid" border-width="1.5pt">
																			<fo:table text-align="center">
																				<fo:table-column column-width="proportional-column-width(12)"/>
																				<fo:table-column column-width="proportional-column-width(12)"/>
																				<fo:table-column column-width="proportional-column-width(12)"/>
																				<fo:table-column column-width="proportional-column-width(12)"/>
																				<fo:table-column column-width="proportional-column-width(16)"/>
																				<fo:table-column column-width="proportional-column-width(12)"/>
																				<fo:table-column column-width="proportional-column-width(12)"/>
																				<fo:table-column column-width="proportional-column-width(12)"/>
																				<fo:table-header>																				
																					<fo:table-row font-weight="bold" >
																						<fo:table-cell number-columns-spanned="8"><fo:block>Cuotas Pagas</fo:block></fo:table-cell>
																					</fo:table-row>	
																					<fo:table-row font-weight="bold" >
																						<fo:table-cell border-top-color="black" border-top-style="solid" border-top-width="0.5pt" border-right-color="black" border-right-style="solid" border-right-width="0.5pt">
																							<fo:block>Cuota</fo:block></fo:table-cell>
																						<fo:table-cell border-top-color="black" border-top-style="solid" border-top-width="0.5pt" border-right-color="black" border-right-style="solid" border-right-width="0.5pt">
																							<fo:block>Fec. Vto.</fo:block></fo:table-cell>
																						<fo:table-cell border-top-color="black" border-top-style="solid" border-top-width="0.5pt" border-right-color="black" border-right-style="solid" border-right-width="0.5pt">
																							<fo:block>Importe</fo:block></fo:table-cell>
																						<fo:table-cell border-top-color="black" border-top-style="solid" border-top-width="0.5pt" border-right-color="black" border-right-style="solid" border-right-width="0.5pt">
																							<fo:block>Inter&#233;s</fo:block></fo:table-cell>
																						<fo:table-cell border-top-color="black" border-top-style="solid" border-top-width="0.5pt" border-right-color="black" border-right-style="solid" border-right-width="0.5pt">
																							<fo:block>Actualizaci&#243;n</fo:block></fo:table-cell>
																						<fo:table-cell border-top-color="black" border-top-style="solid" border-top-width="0.5pt" border-right-color="black" border-right-style="solid" border-right-width="0.5pt">
																							<fo:block>Total</fo:block></fo:table-cell>
																						<fo:table-cell border-top-color="black" border-top-style="solid" border-top-width="0.5pt" border-right-color="black" border-right-style="solid" border-right-width="0.5pt">
																							<fo:block>Fec. Pago</fo:block></fo:table-cell>																						
																						<fo:table-cell border-top-color="black" border-top-style="solid" border-top-width="0.5pt" border-right-color="black" border-right-style="solid" border-right-width="0.5pt">
																							<fo:block>Estado</fo:block></fo:table-cell>																						
																					</fo:table-row>
																				</fo:table-header>																				
																				<fo:table-body>																				
																					<xsl:choose>
																						<xsl:when test="count(Convenio/ListCuotaPaga/LiqCuotaVO)>0">
																							<xsl:for-each select="Convenio/ListCuotaPaga/LiqCuotaVO">
																								<fo:table-row>
																									<fo:table-cell border-top-color="black" border-top-style="solid" border-top-width="0.5pt" border-right-color="black" border-right-style="solid" border-right-width="0.5pt">
																										<fo:block><xsl:value-of select="NroCuota"/></fo:block></fo:table-cell>
																									<fo:table-cell border-top-color="black" border-top-style="solid" border-top-width="0.5pt" border-right-color="black" border-right-style="solid" border-right-width="0.5pt">
																										<fo:block><xsl:value-of select="FechaVto"/></fo:block></fo:table-cell>
																									<fo:table-cell border-top-color="black" border-top-style="solid" border-top-width="0.5pt" border-right-color="black" border-right-style="solid" border-right-width="0.5pt">
																										<fo:block><xsl:value-of select="CapitalView"/></fo:block></fo:table-cell>
																									<fo:table-cell border-top-color="black" border-top-style="solid" border-top-width="0.5pt" border-right-color="black" border-right-style="solid" border-right-width="0.5pt">
																										<fo:block><xsl:value-of select="InteresView"/></fo:block></fo:table-cell>
																									<fo:table-cell border-top-color="black" border-top-style="solid" border-top-width="0.5pt" border-right-color="black" border-right-style="solid" border-right-width="0.5pt">
																										<fo:block><xsl:value-of select="ActualizacionView"/></fo:block></fo:table-cell>
																									<fo:table-cell border-top-color="black" border-top-style="solid" border-top-width="0.5pt" border-right-color="black" border-right-style="solid" border-right-width="0.5pt">
																										<fo:block><xsl:value-of select="TotalView"/></fo:block></fo:table-cell>																																																																																				
																									<fo:table-cell border-top-color="black" border-top-style="solid" border-top-width="0.5pt" border-right-color="black" border-right-style="solid" border-right-width="0.5pt">
																										<fo:block><xsl:value-of select="FechaPago"/></fo:block></fo:table-cell>
																									<fo:table-cell border-top-color="black" border-top-style="solid" border-top-width="0.5pt" border-right-color="black" border-right-style="solid" border-right-width="0.5pt">
																										<fo:block><xsl:value-of select="DesEstado"/></fo:block></fo:table-cell>
																								</fo:table-row>																																									
																							</xsl:for-each>
																						</xsl:when>
																						<xsl:otherwise>
																							<fo:table-row>
																								<fo:table-cell  number-columns-spanned="8" border-top-color="black" border-top-style="solid" border-top-width="0.5pt">
																									<fo:block text-align="center" wrap-option="no-wrap">No se registran cuotas pagas</fo:block>
																								</fo:table-cell>
																							</fo:table-row>
																						</xsl:otherwise>	
																					</xsl:choose>																				
																				</fo:table-body>
																			</fo:table>																		
																		</fo:block>
																		
										<!-- Cuotas impagas -->
																		<fo:block space-before="5mm" border-color="black" border-style="solid" border-width="1.5pt">																		
																			<fo:table text-align="center">
																				<fo:table-column/>
																				<fo:table-column/>
																				<fo:table-column/>
																				<fo:table-column/>
																				<fo:table-column/>
																				<fo:table-column/>
																				<fo:table-column/>																																							
																				<fo:table-header>
																					<fo:table-row>
																						<fo:table-cell number-columns-spanned="8" font-weight="bold"><fo:block>Cuotas Impagas</fo:block></fo:table-cell>
																					</fo:table-row>
																					<fo:table-row font-weight="bold">
																						<fo:table-cell border-top-color="black" border-top-style="solid" border-top-width="0.5pt" border-right-color="black" border-right-style="solid" border-right-width="0.5pt">
																							<fo:block>Cuota</fo:block></fo:table-cell>
																						<fo:table-cell border-top-color="black" border-top-style="solid" border-top-width="0.5pt" border-right-color="black" border-right-style="solid" border-right-width="0.5pt">
																							<fo:block>Fec. Vto.</fo:block></fo:table-cell>
																						<fo:table-cell border-top-color="black" border-top-style="solid" border-top-width="0.5pt" border-right-color="black" border-right-style="solid" border-right-width="0.5pt">
																							<fo:block>Capital</fo:block></fo:table-cell>
																						<fo:table-cell border-top-color="black" border-top-style="solid" border-top-width="0.5pt" border-right-color="black" border-right-style="solid" border-right-width="0.5pt">
																							<fo:block>Inter&#233;s</fo:block></fo:table-cell>
																						<fo:table-cell border-top-color="black" border-top-style="solid" border-top-width="0.5pt" border-right-color="black" border-right-style="solid" border-right-width="0.5pt">
																							<fo:block>Actualizaci&#243;n</fo:block></fo:table-cell>
																						<fo:table-cell border-top-color="black" border-top-style="solid" border-top-width="0.5pt" border-right-color="black" border-right-style="solid" border-right-width="0.5pt">
																							<fo:block>Total</fo:block></fo:table-cell>
																						<fo:table-cell border-top-color="black" border-top-style="solid" border-top-width="0.5pt" border-right-color="black" border-right-style="solid" border-right-width="0.5pt">
																							<fo:block>Observaci&#243;n</fo:block></fo:table-cell>																						
																					</fo:table-row>
																				</fo:table-header>	
																				<fo:table-body>
																					<xsl:choose>
																						<xsl:when test="count(Convenio/ListCuotaInpaga/LiqCuotaVO)>0">
																							<xsl:for-each select="Convenio/ListCuotaInpaga/LiqCuotaVO">
																								<fo:table-row>
																									<fo:table-cell border-top-color="black" border-top-style="solid" border-top-width="0.5pt" border-right-color="black" border-right-style="solid" border-right-width="0.5pt">
																										<fo:block><xsl:value-of select="NroCuota"/></fo:block></fo:table-cell>
																									<fo:table-cell border-top-color="black" border-top-style="solid" border-top-width="0.5pt" border-right-color="black" border-right-style="solid" border-right-width="0.5pt">
																										<fo:block><xsl:value-of select="FechaVto"/></fo:block></fo:table-cell>
																									<fo:table-cell border-top-color="black" border-top-style="solid" border-top-width="0.5pt" border-right-color="black" border-right-style="solid" border-right-width="0.5pt">
																										<fo:block><xsl:value-of select="CapitalView"/></fo:block></fo:table-cell>
																									<fo:table-cell border-top-color="black" border-top-style="solid" border-top-width="0.5pt" border-right-color="black" border-right-style="solid" border-right-width="0.5pt">
																										<fo:block><xsl:value-of select="InteresView"/></fo:block></fo:table-cell>
																									<fo:table-cell border-top-color="black" border-top-style="solid" border-top-width="0.5pt" border-right-color="black" border-right-style="solid" border-right-width="0.5pt">
																										<fo:block><xsl:value-of select="ActualizacionView"/></fo:block></fo:table-cell>
																									<fo:table-cell border-top-color="black" border-top-style="solid" border-top-width="0.5pt" border-right-color="black" border-right-style="solid" border-right-width="0.5pt">
																										<fo:block><xsl:value-of select="TotalView"/></fo:block></fo:table-cell>																																																																																				
																									<fo:table-cell border-top-color="black" border-top-style="solid" border-top-width="0.5pt" border-right-color="black" border-right-style="solid" border-right-width="0.5pt">
																										<fo:block><xsl:value-of select="Observacion"/></fo:block></fo:table-cell>																	
																								</fo:table-row>																																									
																							</xsl:for-each>
																						</xsl:when>
																						<xsl:otherwise>
																							<fo:table-row>
																								<fo:table-cell number-columns-spanned="7">
																									<fo:block wrap-option="no-wrap" text-align="center">No se registran cuotas Impagas</fo:block>
																								</fo:table-cell>
																							</fo:table-row>
																						</xsl:otherwise>	
																					</xsl:choose>																																								
																				</fo:table-body>
																			</fo:table>																		
																		</fo:block>
										<!-- Fin cuotas impagas -->
																	   <xsl:if test="position()&lt;$cantConvenios">
																			<fo:block break-after="page"/>
																		</xsl:if>
																	</xsl:for-each>	
																</fo:table-cell>
															</fo:table-row>
														</fo:table-body>
													</fo:table>
												</fo:block>	
											</fo:table-cell>											
										</fo:table-row>
									</fo:table-body>
								</fo:table>																									 			 		
				</xsl:if>			
				<!-- FIN Convenios -->
											
	</xsl:template>
	<!-- FIN Template para escribir un LiqDeudaAdapter -->
	
	<!-- Template para escribir un titulo de Lista de deudas -->
	<xsl:template name="tituloListDeuda">
		<xsl:param name="titulo"/>
		<fo:table-row font-size="11pt" font-weight="bold" >
			<fo:table-cell number-columns-spanned="4">
				<fo:block text-decoration="underline" space-before="2mm" space-after="2mm" text-align="center" wrap-option="no-wrap">
					<xsl:value-of select="$titulo"/>
				</fo:block>
			</fo:table-cell>													
		</fo:table-row>

	</xsl:template>
	<!-- FIN Template para escribir un titulo de Lista de deudas -->
	
	<!-- Template para escribir una lista de deudas -->
	<xsl:template match="ListDeuda">
		<xsl:param name="tituloTotal"/>
		<xsl:param name="valorTotal"/>
		<xsl:param name="leyendaResultVacio"/>
		<xsl:choose>
			<xsl:when test="count(./LiqDeudaVO)>0">
				<fo:table-row>
				<xsl:variable name="Col">
					<xsl:choose>
						<xsl:when test="$Auto='true'">
							<xsl:value-of select="7"/>
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="5"/>
						</xsl:otherwise>
					</xsl:choose>
					
					
				</xsl:variable>
					<fo:table-cell number-columns-spanned="{($Col)}">
						<fo:block border-color="black" border-style="solid" border-width="1.5pt" space-after="2mm" space-before="2mm">
							<fo:table>
								<fo:table-column/><fo:table-column/><fo:table-column/><fo:table-column/><fo:table-column/>
								<xsl:if test="$Auto = 'true'">
									<fo:table-column/><fo:table-column/>
								</xsl:if>
								<fo:table-header>								
									<fo:table-row>
										<fo:table-cell font-weight="bold"><fo:block space-after="2mm"><fo:inline>Per&#237;odo</fo:inline></fo:block></fo:table-cell>
										<fo:table-cell font-weight="bold"><fo:block><fo:inline>Fecha Vto.</fo:inline></fo:block></fo:table-cell>
										<xsl:choose>
											<xsl:when test="$Auto ='true'">
												<fo:table-cell font-weight="bold"><fo:block><fo:inline>Importe</fo:inline></fo:block></fo:table-cell>
												<fo:table-cell font-weight="bold"><fo:block><fo:inline>Saldo</fo:inline></fo:block></fo:table-cell>
												<fo:table-cell font-weight="bold"><fo:block><fo:inline>Fecha Pago</fo:inline></fo:block></fo:table-cell>
											</xsl:when>
											<xsl:otherwise>
												<fo:table-cell font-weight="bold"><fo:block><fo:inline>Importe</fo:inline></fo:block></fo:table-cell>
											</xsl:otherwise>
										</xsl:choose>		
										<fo:table-cell font-weight="bold"><fo:block><fo:inline>Actualizaci&#243;n</fo:inline></fo:block></fo:table-cell>
										<fo:table-cell font-weight="bold"><fo:block><fo:inline>Total</fo:inline></fo:block></fo:table-cell>
									</fo:table-row>
								</fo:table-header>
								<fo:table-body>							
									<xsl:for-each select="LiqDeudaVO">
										<xsl:apply-templates select="." mode="filas"/>
									</xsl:for-each>
									<xsl:if test="$valorTotal>0">		
										<fo:table-row>
											<fo:table-cell number-columns-spanned="4" padding="1pt">
												<fo:block space-before="4mm"><xsl:value-of select="$tituloTotal"/></fo:block>
											</fo:table-cell>
											<fo:table-cell>
												<fo:block space-before="4mm"><xsl:value-of select="$valorTotal"/></fo:block>
											</fo:table-cell>													
										</fo:table-row>
									</xsl:if>								
								</fo:table-body>
							</fo:table>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
	
			</xsl:when>	
			<xsl:otherwise>
				<fo:table-row>
					<fo:table-cell number-columns-spanned="4">
						<fo:block text-align="left" wrap-option="no-wrap">
							&#160; <!-- xsl:value-of select="$leyendaResultVacio"/ -->	
						</fo:block>
					</fo:table-cell>													
				</fo:table-row>																																
			</xsl:otherwise>
		</xsl:choose>	
	</xsl:template>
	<!-- FIN Template para escribir una lista de deudas -->
	
	<!--  Template para el total de una lista de deudas  -->
	<xsl:template name="totalListaDeudas">
		<xsl:param name="leyenda"/>
		<xsl:param name="valor"/>
		<fo:table-row font-size="11pt" font-weight="bold">
			<fo:table-cell number-columns-spanned="4">
				<fo:block space-before="5mm">TOTAL cobro en <xsl:value-of select="$leyenda"/></fo:block>
			</fo:table-cell>
			<fo:table-cell>
			<xsl:if test="$valor = '0,00' and $Auto = 'true'">
				<fo:block space-before="5mm">-</fo:block>
			</xsl:if>
			<xsl:if test="$valor != '0,00'">
				<fo:block space-before="5mm"><xsl:value-of select="$valor"/></fo:block>
			</xsl:if>
			</fo:table-cell>
		</fo:table-row>
		<fo:table-row font-size="11pt" font-weight="bold">
			<fo:table-cell number-columns-spanned="4"><fo:block>Al D&#237;a:&#160;<xsl:call-template name="FechaConfeccion"/></fo:block></fo:table-cell>
		</fo:table-row>

	</xsl:template>	
	<!--  FIN Template para el total de una lista de deudas  -->

	
	<!-- Template para la fecha -->
	<xsl:template name="FechaConfeccion">
		<fo:inline><xsl:value-of select="//FechaConfeccionView" /></fo:inline>
	</xsl:template>
	<!-- FIN Template para la fecha -->


	<!-- Template para un dato de la cuenta (se usa en la cabecera) -->
	<xsl:template name="datoCuenta">
		<xsl:param name="label"/>
		<xsl:param name="valor"/>
			<fo:block space-before="1mm" wrap-option="no-wrap">
				<fo:inline color="black" text-align="left" font-weight="bold" ><xsl:value-of select="$label"/>:&#160;</fo:inline>
				<fo:inline color="black" text-align="left" wrap-option="wrap">
			    	<xsl:value-of select="$valor"/>	
			    </fo:inline>
			</fo:block>	
	</xsl:template>
	<!-- FIN Template para un datos de la cuenta (se usa en la cabecera) -->
	
	
	<xsl:template name="escribirCuentaCaratula">
		<xsl:param name="cadenaAnterior"/>
		<xsl:param name="Cuenta"/>
		<fo:block>
			<xsl:value-of select="$cadenaAnterior"/> 
			<xsl:value-of select="$Cuenta/DesRecurso"/> - 
			<xsl:value-of select="$Cuenta/NroCuentaView"/>
			
			<xsl:if test="$Cuenta/EsLitoralGas=1">
				- Es cuenta Gas
			</xsl:if>
			
			<xsl:if test="$Cuenta/EsObraPeatonal=1">
				- La cuenta esta afectada a la remodelaci&#243;n peatonal 
			</xsl:if>
			
		</fo:block>
	</xsl:template>
</xsl:stylesheet>

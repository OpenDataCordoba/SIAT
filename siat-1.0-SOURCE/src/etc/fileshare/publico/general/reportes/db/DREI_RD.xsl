<?xml version = '1.0' encoding = 'ISO-8859-1'?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:fo="http://www.w3.org/1999/XSL/Format" version="1.0">
	<xsl:import href="@@FileSharePath/publico/general/reportes/2of5i.xsl"/>
	<xsl:output method="xml" indent="yes"/>
	<xsl:template match="/">
      <fo:root font-family="Times" font-size="10pt">
         <fo:layout-master-set>
	   <fo:simple-page-master master-name="barcodes-page">
	     <fo:region-body margin="0.5in" border="0.25pt solid silver" padding="1pt"/>
	   </fo:simple-page-master>
	  </fo:layout-master-set>
		
  	  <xsl:for-each select="./siat/data/LiqRecibos/ListLiqReciboVO/LiqReciboVO">
		
		  	<fo:page-sequence master-reference="barcodes-page">
		    	<fo:flow flow-name="xsl-region-body" font-family="Helvetica">
		          <xsl:apply-templates/>
		<!-- Cabecera -->
	                  <fo:block-container position="absolute" top="100mm" left="40mm" height="55mm" width="100%" >
							<fo:block font-size="30pt">SIN VALOR LEGAL</fo:block>
					  </fo:block-container>
                  <fo:block-container position="absolute" top="0mm" left="0mm" height="55mm" width="100%" >
							<fo:block>
									<fo:table>
										<fo:table-column column-width="proportional-column-width(10)" />
										<fo:table-column column-width="proportional-column-width(40)" />
										<fo:table-column column-width="proportional-column-width(50)" />										
										<fo:table-body>
											<fo:table-row>
												<fo:table-cell>
													<fo:block>
														<fo:external-graphic
															height="20mm" width="15mm"
															src="{concat(//FileSharePath, '/publico/general/reportes/images/escudo_muni.jpg')}" />
													</fo:block>
												</fo:table-cell>
												<fo:table-cell>
													<fo:block font-size="16pt" text-align="center">Municipalidad de [LOCALIDAD] </fo:block>
													<fo:block font-size="14" margin-left="4mm" text-align="left">
														<xsl:value-of select="LeyendaSubtitulo"/>													
													</fo:block>
												</fo:table-cell>
												<fo:table-cell>&#160;
													<fo:block text-align="right" font-size="13pt">
														<fo:inline>
															<xsl:value-of select="Recurso/DesRecurso"/>															
														</fo:inline>
														<fo:inline font-weight="bold" font-size="16pt">
															&#160;&#160;&#160;&#160;<xsl:value-of select="Recurso/CodRecurso"/>
														</fo:inline>
													</fo:block>													
													<xsl:if test="/siat/cabecera/usuario!=''">
															<fo:block space-before="5pt" font-size="8pt" text-align="right">Usuario:&#160;<xsl:value-of select="/siat/cabecera/usuario"/></fo:block>
													</xsl:if>	
												</fo:table-cell>			
											</fo:table-row>
										</fo:table-body>
									</fo:table>
									<fo:block>&#160;</fo:block>	
								</fo:block>								
		<!-- Datos de la cuenta -->								
								<fo:block border-color="black" border-style="solid" border-width="1.5pt" font-size="10pt">
									<xsl:call-template name="datosCuenta">
										<xsl:with-param name="LiqReciboVO" ><xsl:value-of select="../."/></xsl:with-param>											
									</xsl:call-template>																	
								</fo:block>
						</fo:block-container>	
	
		<!-- Lista de Deudas -->
					<xsl:if test="EsCuotaSaldo='false'"> 
						<fo:block-container position="absolute" top="52mm" left="0pt" height="130mm" width="100%" >
						
							<fo:block>
								<!-- tabla que contiene a las 2 -->
								<fo:table font-size="10pt" height="147mm"
									border-color="black" border-style="solid" border-width="1.5pt">
									<fo:table-column />
									<fo:table-column />
									<fo:table-body>
										<fo:table-row>
											<fo:table-cell>															
												<!-- tabla de la izq -->							
												<fo:table font-size="10pt" height="145mm">
													<fo:table-column />
													<fo:table-body>
														<fo:table-row>
															<fo:table-cell padding="4pt">
																<fo:table font-size="8pt"  text-align="center">
																	<fo:table-column column-width="10%" />
																	<fo:table-column column-width="7%" />
																	<fo:table-column column-width="10%" />
																	<fo:table-column column-width="40%" />
																	<fo:table-column column-width="13%"/>
																	<fo:table-column column-width="20%"/>
																	<fo:table-header>
													 					<fo:table-row>
													 						 <fo:table-cell border-color="black" border-style="solid" border-width="1pt" text-align="center">
																				<fo:block>
																					PERIODO
																				</fo:block>
																			</fo:table-cell>
																			<fo:table-cell border-color="black" border-style="solid" border-width="1pt">
																				<fo:block>PERS</fo:block>
																			</fo:table-cell>
																			<fo:table-cell border-color="black" border-style="solid" border-width="1pt">
																				<fo:block>RUBROS</fo:block>
																			</fo:table-cell>
																			<fo:table-cell border-color="black" border-style="solid" border-width="1pt">
																				<fo:block>BASE IMPONIBLE O CANTIDAD</fo:block>
																			</fo:table-cell>
																			<fo:table-cell border-color="black" border-style="solid" border-width="1pt">
																				<fo:block>ALICUOTA</fo:block>
																			</fo:table-cell>
																			<fo:table-cell border-color="black" border-style="solid" border-width="1pt">
																				<fo:block>IMPORTE</fo:block>
																			</fo:table-cell>
																		</fo:table-row>
																	</fo:table-header>
																	<fo:table-body>
																		<xsl:for-each
																			select="ListReciboDeuda/LiqReciboDeudaVO">
																			<xsl:if test="position()&lt;24 or position()=24">
																				<fo:table-row font-family="Courier">															
																					<fo:table-cell padding="2pt">
																						<fo:block><xsl:value-of	select="PeriodoDeuda" /></fo:block>
																					</fo:table-cell>
																					<fo:table-cell padding="2pt">
																						<fo:block><xsl:value-of select="CapitalOriginalView" /></fo:block>
																					</fo:table-cell>
																					<fo:table-cell padding="2pt">
																						<fo:block><xsl:value-of	select="TotActualizacionView" /></fo:block>
																					</fo:table-cell>
																					<fo:table-cell padding="2pt">
																						<fo:block><xsl:value-of	select="TotReciboDeudaView"/></fo:block>
																					</fo:table-cell>
																				</fo:table-row>
																			</xsl:if>														
																			
																		</xsl:for-each>
																	</fo:table-body>
																</fo:table>
															</fo:table-cell>
														</fo:table-row>
														
	<!-- Leyenda Descuento -->
														<fo:table-row>
															<fo:table-cell padding="1pt" number-columns-spanned="2">
																<fo:block font-size="8pt" wrap-option="no-wrap">													
																	<xsl:value-of select="DesDescuento" />										
																</fo:block>															
															</fo:table-cell>
														</fo:table-row>													
	<!-- Sellado -->
														<fo:table-row>
															<fo:table-cell padding="1pt">
																<xsl:if test="SelladoView!='0,00'">
																	<fo:block font-size="8pt">
																		<fo:inline color="black">Importe Sellado:&#160;</fo:inline>
																		<fo:inline font-family="Courier">
																			$&#160;<xsl:value-of select="SelladoView" />
																		</fo:inline>										
																	</fo:block>
																</xsl:if>															
															</fo:table-cell>
														</fo:table-row>
													</fo:table-body>
												</fo:table>
												<!-- FIN tabla de la izq -->											
											</fo:table-cell>
											
											<fo:table-cell>
												<!-- tabla de la derecha -->
												<xsl:if test="count(ListReciboDeuda/LiqReciboDeudaVO)>24">							
													<fo:table font-size="10pt" height="145mm">
														<fo:table-column />
														<fo:table-body>
															<fo:table-row>
																<fo:table-cell padding="4pt">
																	<fo:table font-size="8pt"  text-align="center">
																		<fo:table-column column-width="45px" />
																		<fo:table-column column-width="65px" />
																		<fo:table-column column-width="65px" />
																		<fo:table-column column-width="70px" />
																					
																		<fo:table-header>
														 					<fo:table-row>
														 						 <fo:table-cell border-color="black" border-style="solid" border-width="1pt" text-align="center">
																					<fo:block>
																						<xsl:value-of select="TipoDeudaStr"/>
																					</fo:block>
																				</fo:table-cell>
																				<fo:table-cell border-color="black" border-style="solid" border-width="1pt">
																					<fo:block>IMPORTE</fo:block>
																				</fo:table-cell>
																				<fo:table-cell border-color="black" border-style="solid" border-width="1pt">
																					<fo:block>INTERES</fo:block>
																				</fo:table-cell>
																				<fo:table-cell border-color="black" border-style="solid" border-width="1pt">
																					<fo:block>TOTAL</fo:block>
																				</fo:table-cell>
					
																			</fo:table-row>
																		</fo:table-header>
																		<fo:table-body>
																			<xsl:for-each
																				select="ListReciboDeuda/LiqReciboDeudaVO">
																				<xsl:if test="position()&gt;24">
																					<fo:table-row font-family="Courier">															
																						<fo:table-cell padding="2pt">
																							<fo:block><xsl:value-of	select="PeriodoDeuda" /></fo:block>
																						</fo:table-cell>
																						<fo:table-cell padding="2pt">
																							<fo:block><xsl:value-of select="CapitalOriginalView" /></fo:block>
																						</fo:table-cell>
																						<fo:table-cell padding="2pt">
																							<fo:block><xsl:value-of	select="TotActualizacionView" /></fo:block>
																						</fo:table-cell>
																						<fo:table-cell padding="2pt">
																							<fo:block><xsl:value-of	select="TotReciboDeudaView"/></fo:block>
																						</fo:table-cell>
																					</fo:table-row>
																				</xsl:if>														
																				
																			</xsl:for-each>
																		</fo:table-body>
																	</fo:table>
																</fo:table-cell>
															</fo:table-row>
														</fo:table-body>
													</fo:table>
												</xsl:if>
												<!-- FIN tabla de la derecha -->											
											</fo:table-cell>	
										</fo:table-row>								
									</fo:table-body>
								</fo:table>	
							</fo:block>
							</fo:block-container>
						
					</xsl:if>
						
				<!-- Cuota saldo cdm -->						
					<xsl:if test="EsCuotaSaldo='true'"> 
						<fo:block-container position="absolute" top="52mm" left="0pt" height="130mm" width="100%" >
							<fo:block>	
								<fo:table font-size="10pt" height="147mm"
									border-color="black" border-style="solid" border-width="1.5pt">
									<fo:table-column />
									<fo:table-body>
										<fo:table-row>
											<fo:table-cell>	
												<fo:block margin-top="5mm" margin-left="5mm">
													<xsl:if test="count(ListReciboDeuda/LiqReciboDeudaVO)=1">
														Cuota <xsl:value-of select="ListReciboDeuda/LiqReciboDeudaVO/PeriodoDeuda"/>
													</xsl:if>
													
													<xsl:if test="count(ListReciboDeuda/LiqReciboDeudaVO)>1">
														<xsl:variable name="last" select="count(ListReciboDeuda/LiqReciboDeudaVO)"/>
														Desde cuota <xsl:value-of select="ListReciboDeuda/LiqReciboDeudaVO[1]/PeriodoDeuda"/> 
														hasta cuota <xsl:value-of select="ListReciboDeuda/LiqReciboDeudaVO[$last]/PeriodoDeuda"/>
													</xsl:if>
												</fo:block>											
											</fo:table-cell>
										</fo:table-row>
									</fo:table-body>
								</fo:table>			
							</fo:block>
						</fo:block-container>
					</xsl:if>		
															
							<fo:block-container position="absolute" top="165mm" left="80mm" height="19mm" width="86mm" >

	<!-- Leyenda Reconfeccion especial -->
								<xsl:if test="EsReconfeccionEspecial='true'">
									<fo:block font-size="8pt" wrap-option="no-wrap" text-align="center" font-weight="bold">
									Importes Actualizados al <xsl:value-of select="FechaActualizacionEspView"/>
									<xsl:if test="CasoView!=''">- Expediente: <xsl:value-of select="CasoView"/></xsl:if>
									</fo:block>
								</xsl:if>

	<!-- mensaje valido hasta su fecha Vto. -->	
								<xsl:if test="PagaVencido='false'">
									<fo:block font-size="8pt" wrap-option="no-wrap" space-after="2mm" text-align="center">
										<fo:inline>Recibo v&#225;lido &#250;nicamente hasta su fecha de vencimiento</fo:inline>
									</fo:block>
								</xsl:if>
							
	<!-- Vto e Importe Final -->	
								<fo:block font-size="10pt" background-color="#D9D9D9" padding="6pt" border-color="black" border-width="0.5px" border-style="solid">													
										<fo:inline color="black" font-weight="bold">&#160;Vencimiento:&#160;</fo:inline>
										<fo:inline color="black" font-family="Courier">
												<xsl:value-of select="FechaVtoView" />
										</fo:inline>
										<fo:inline color="black" font-weight="bold">&#160;&#160;Total:&#160;</fo:inline>
										<fo:inline color="black" font-family="Courier">
												$&#160;<xsl:value-of select="TotalPagarView" />
										</fo:inline>
								</fo:block>
							</fo:block-container>
	<!--  Talonarios -->			
			
							<fo:block-container position="absolute" top="188mm" left="0pt" height="75mm" width="100%" >					
								<fo:block>
									<fo:table  width="100%" font-size="10pt" text-align="center">
										<fo:table-column column-width="proportional-column-width(32)"/>
										<fo:table-column column-width="proportional-column-width(68)"/>
										<fo:table-body>
											<fo:table-row height="75mm">
												<fo:table-cell border-top-color="black" border-top-style="dashed" border-top-width="0.5px" padding="6pt" border-right-color="black" border-right-style="dashed" border-right-width="0.5px">
													<fo:block font-size="14pt" font-weight="bold">Municipalidad de [LOCALIDAD] DE</fo:block>
													<fo:block font-size="14pt" font-weight="bold">[LOCALIDAD]</fo:block>									
													<fo:block>&#160;</fo:block>
													<fo:block font-size="11pt">
														<xsl:value-of select="Recurso/DesRecurso"/>													
													</fo:block>
													<fo:block>&#160;</fo:block>
													<fo:block font-size="6pt">(Tal&#243;n&#160;para&#160;el&#160;Banco)</fo:block>
													<fo:block>&#160;</fo:block>
													<fo:block>
								<!-- Talonario Banco (Izquierda) -->
														<fo:table>
															<fo:table-column/>
															<fo:table-column/>
															<fo:table-body>
																<fo:table-row>
																	<xsl:if test="NumeroReciboView!=000000000">
																		<fo:table-cell>
																			<fo:block text-align="left">Nro. Recibo:</fo:block>
																		</fo:table-cell>
																		<fo:table-cell font-family="Courier">
																			<fo:block text-align="left"><xsl:value-of select="NumeroReciboView" /></fo:block>
																		</fo:table-cell>
																	</xsl:if>	
																</fo:table-row>
																<fo:table-row>
																	<xsl:choose>
																		<xsl:when test="CodRefPagView!=00000000000">
																			<fo:table-cell>
																				<fo:block text-align="left">Cod. Ref. Pago:</fo:block>
																			</fo:table-cell>
																			<fo:table-cell font-family="Courier">
																				<fo:block text-align="left"><xsl:value-of select="CodRefPagView" /></fo:block>
																			</fo:table-cell>
																		</xsl:when>
																		<xsl:otherwise>
																			<fo:table-cell>
																				<fo:block text-align="left"><xsl:value-of select="TipoDeudaStrLowCase"/>:</fo:block>
																			</fo:table-cell>
																			<fo:table-cell font-family="Courier">
																				<fo:block text-align="left"><xsl:value-of select="ListReciboDeuda/LiqReciboDeudaVO[1]/PeriodoDeuda"/></fo:block>
																			</fo:table-cell>																		
																		</xsl:otherwise>
																	</xsl:choose>		
																</fo:table-row>																
																<fo:table-row>
																	<fo:table-cell>
																		<fo:block text-align="left">Cuenta:</fo:block>
																	</fo:table-cell>
																	<fo:table-cell>
																		<fo:block font-family="Courier" text-align="left">
																			<xsl:value-of select="Cuenta/NroCuentaView" />
																		</fo:block>
																	</fo:table-cell>
																</fo:table-row>												
															</fo:table-body>
														</fo:table>
														<fo:block font-size="15pt">&#160;</fo:block>	
												<!-- Vencimiento y Total Izq. -->
														<fo:table font-size="10pt" width="50mm" background-color="#D9D9D9" padding="6pt" border-color="black" border-width="0.5px" border-style="solid">
															<fo:table-column/>
															<fo:table-body>
																<fo:table-row>
																	<fo:table-cell>
																		<fo:block text-align="left">
																			<fo:inline color="black" font-weight="bold">Vencimiento:&#160;</fo:inline>
																			<fo:inline color="black" font-family="Courier">
																					<xsl:value-of select="FechaVtoView" />
																			</fo:inline>
																		</fo:block>																	
																	</fo:table-cell>
																</fo:table-row>
																<fo:table-row height="5px">
																	<fo:table-cell>
																	</fo:table-cell>
																</fo:table-row>	
																<fo:table-row>
																	<fo:table-cell>
																		<fo:block text-align="left">
																			<fo:inline color="black" font-weight="bold">Total:&#160;</fo:inline>
																			<fo:inline color="black" font-family="Courier">
																					$&#160;<xsl:value-of select="TotalPagarView" />
																			</fo:inline>
																		</fo:block>																	
																	</fo:table-cell>
																</fo:table-row>
															</fo:table-body>
														</fo:table>
													</fo:block>										
												</fo:table-cell>
												<fo:table-cell>
								<!-- Talonario Muni (Derecha) -->								
													<fo:block border-top-color="black" border-top-style="dashed" border-top-width="0.5px" padding="2pt">
														<fo:table>
															<fo:table-column column-width="proportional-column-width(64)"/>
															<fo:table-column column-width="proportional-column-width(28)"/>
															<fo:table-column column-width="proportional-column-width(8)"/>
															<fo:table-body>
																<fo:table-row>
																	<fo:table-cell text-align="left" padding="2pt">
																		<fo:block font-weight="bold" font-size="14pt">Municipalidad de [LOCALIDAD] </fo:block>
																		<fo:block>
																			<xsl:value-of select="LeyendaSubtitulo"/>
																		</fo:block>
																	</fo:table-cell>
																	<fo:table-cell text-align="left" font-size="11pt">
																		<fo:block>
																			<xsl:value-of select="Recurso/DesRecurso"/>																		
																		</fo:block>											
																	</fo:table-cell>
																	<fo:table-cell>
																		<fo:block font-weight="bold" font-size="14pt">
																			<xsl:value-of select="Recurso/CodRecurso"/>
																		</fo:block>
																	</fo:table-cell>
																</fo:table-row>
															</fo:table-body>
														</fo:table>
														<fo:block font-size="6pt">(Tal&#243;n para la Municipalidad de [LOCALIDAD])</fo:block>
														<fo:block>&#160;</fo:block>
														<fo:table>
															<fo:table-column column-width="proportional-column-width(60)"/>
															<fo:table-column column-width="proportional-column-width(40)"/>
															<fo:table-body>
																<fo:table-row>
																	<fo:table-cell padding="1pt">
																		<fo:block text-align="left" margin-left="2pt">
																			<fo:inline color="black" text-align="left">Cuenta:&#160;</fo:inline>
																		    <fo:inline color="black" text-align="left" font-family="Courier">
																		    	<xsl:value-of select="Cuenta/NroCuentaView"/>
																		    </fo:inline>
																		</fo:block>  
																	</fo:table-cell>
																	<fo:table-cell>
																			<xsl:if test="NumeroReciboView!=000000000">
																				<fo:block>
																				    <fo:inline color="black" text-align="left">Nro. Recibo:&#160;</fo:inline>
																				    <fo:inline color="black" text-align="left" font-family="Courier">
																				    	<xsl:value-of select="NumeroReciboView" />
																				    </fo:inline>												
																			    </fo:block>
																			 </xsl:if> 
																		<xsl:choose>
																			 <xsl:when test="CodRefPagView!=00000000000">																			
																				<fo:block wrap-option="no-wrap">
																					<fo:inline text-align="left">Cod. Ref. Pago:&#160;</fo:inline>
																					<fo:inline text-align="left" font-family="Courier"><xsl:value-of select="CodRefPagView" /></fo:inline>
																				</fo:block>																				
																			</xsl:when>
																			<xsl:otherwise>
																				<fo:block wrap-option="no-wrap">
																					<fo:inline color="black" text-align="left"><xsl:value-of select="TipoDeudaStrLowCase"/>:&#160;</fo:inline>
																					<fo:inline color="black" text-align="left" font-family="Courier"><xsl:value-of select="ListReciboDeuda/LiqReciboDeudaVO[1]/PeriodoDeuda"/></fo:inline>
																				</fo:block>																																																											
																			</xsl:otherwise>
																		</xsl:choose>		   
																	</fo:table-cell>
																</fo:table-row>
															</fo:table-body>
														</fo:table>
														<fo:block margin-left="2pt" padding="1pt" text-align="left">
														    <fo:inline color="black">Catastral:&#160;</fo:inline>
														    <fo:inline color="black" font-family="Courier">
														    	<xsl:value-of select="Cuenta/ListAtributoObjImp/LiqAtrValorVO[Key='Catastral']/Value" />
														    </fo:inline>
												        </fo:block>
														<fo:block margin-left="2pt" padding="1pt" text-align="left">
														    <fo:inline color="black">
														    	<xsl:if test="EsComercio='false'">
														    		Propietario:&#160;
														    	</xsl:if>
														    	<xsl:if test="EsComercio='true'">
														    		Contribuyente:&#160;
														    	</xsl:if>
														    </fo:inline>
														    <fo:inline color="black" font-family="Courier">
														    	<xsl:value-of select="Propietario"/>
														    </fo:inline>
												        </fo:block>				
														<fo:block margin-left="2pt" padding="1pt" text-align="left">
														    <fo:inline color="black">Ubicaci&#243;n:&#160;</fo:inline>
														    <fo:inline color="black" font-family="Courier">
														    	<xsl:if test="EsComercio='false'">
														    		<xsl:value-of select="Cuenta/ListAtributoObjImp/LiqAtrValorVO[Key='Ubicacion']/Value" />
														    	</xsl:if>
														    	<xsl:if test="EsComercio='true'">
														    		<xsl:value-of select="Cuenta/DesDomEnv" />
														    	</xsl:if>
														    </fo:inline>
												        </fo:block>				
														<fo:block margin-left="2pt" padding="1pt" text-align="left">
														    <fo:inline color="black">Cod. Gesti&#243;n Personal:&#160;</fo:inline>
														    <fo:inline color="black" font-family="Courier">
														    	<xsl:value-of select="Cuenta/CodGestionPersonalView" />
														    </fo:inline>
												        </fo:block>
											        </fo:block>
												</fo:table-cell>
											</fo:table-row>
										</fo:table-body>						
									</fo:table>
								</fo:block>
							</fo:block-container>
				
				<!-- Vto e Importe Final Talonario Muni-->
				
							<fo:block-container position="absolute" top="234mm" left="83mm" height="20mm" width="86mm">
								<fo:block font-size="10pt" background-color="#D9D9D9" padding="6pt" border-color="black" border-width="0.5px" border-style="solid">													
										<fo:inline color="black" font-weight="bold">Vencimiento:&#160;</fo:inline>
										<fo:inline color="black" font-family="Courier">
												<xsl:value-of select="FechaVtoView" />
										</fo:inline>
										<fo:inline color="black" font-weight="bold">&#160;&#160;Total:</fo:inline>
										<fo:inline color="black" font-family="Courier">
												$&#160;<xsl:value-of select="TotalPagarView" />
										</fo:inline>
								</fo:block>
								<fo:block>&#160;</fo:block>
							</fo:block-container>			
							
				<!--  Codigo de Barra -->
				
							<fo:block-container position="absolute" top="245mm" left="115mm" height="20mm" padding="0pt">
								<fo:block  padding="0pt" text-align="center">
									<fo:inline font-family="Courier" font-size="10pt"><xsl:value-of select="NroCodigoBarraDelim"/></fo:inline>
								</fo:block>			
							</fo:block-container>				
							<fo:block-container position="absolute" top="247mm" left="120mm" height="20mm" padding="0pt">
								<fo:block text-align="center" padding="0pt" font-size="12pt">
									    <fo:instream-foreign-object content-width="100%" content-height="100%">				
									      <xsl:call-template name="barcode-2of5i">
										      <xsl:with-param name="print-text" select="false"/>
									          <xsl:with-param name="value" select="NroCodigoBarra"/>
									          <xsl:with-param name="addchecksum" select="false"/>
									          <xsl:with-param name="height" select="15"/>
									          <xsl:with-param name="module" select="0.266"/>
									      </xsl:call-template>
									    </fo:instream-foreign-object>
								</fo:block>
							</fo:block-container>
		 		</fo:flow>
	  		</fo:page-sequence>
		  </xsl:for-each>
	  </fo:root>
    </xsl:template>
    
    <!-- Template para datos de la cuenta -->
    <xsl:template name="datosCuenta">
    	<xsl:param name="LiqReciboVO"/>
    	

	<fo:table>
			<fo:table-column column-width="proportional-column-width(33)"/>
			<fo:table-column column-width="proportional-column-width(33)"/>
			<fo:table-column column-width="proportional-column-width(33)"/>
			<fo:table-body>
				<fo:table-row >
					<fo:table-cell padding="1pt">
						<fo:block text-align="left" margin-left="2pt">
							<fo:inline color="black" text-align="left">Cuenta:&#160;</fo:inline>
						    <fo:inline color="black" text-align="left" font-family="Courier">
						    	<xsl:value-of select="Cuenta/NroCuentaView" />
						    </fo:inline>
						</fo:block>																										
					</fo:table-cell>
					
					<fo:table-cell padding="1pt">
						<fo:block text-align="left" margin-left="2pt">
							<fo:inline color="black" text-align="left">CUIT:&#160;</fo:inline>
						    <fo:inline color="black" text-align="left" font-family="Courier">
						    	<xsl:value-of select="Cuenta/CuitTitularPrincipalContr" />
						    </fo:inline>
						</fo:block>																										
					</fo:table-cell>
				
					<fo:table-cell padding="1pt">
						<fo:block text-align="left" margin-right="2pt">
							<xsl:if test="NumeroReciboView!=000000000">
							    <fo:inline color="black">Nro. Recibo:&#160;</fo:inline>
							    <fo:inline color="black" text-align="left" font-family="Courier" >
							    	<xsl:value-of select="NumeroReciboView" />
							    </fo:inline>
							</xsl:if>    												
					    </fo:block>
					 </fo:table-cell>
				</fo:table-row>
				<fo:table-row>
					<fo:table-cell padding="1pt">
						<fo:block text-align="left" margin-left="2pt">
							<fo:inline color="black">Nro Ingresos Brutos:&#160;</fo:inline>
							<fo:inline color="black" text-align="left" font-family="Courier">
								<xsl:value-of select="Cuenta/NroIsib"/>
							</fo:inline>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="1pt">
						<fo:block text-align="left" margin-left="2pt">
							<fo:inline color="black">Convenio Multilateral:&#160;</fo:inline>
							<fo:inline color="black" text-align="left" font-family="Courier">
								<xsl:value-of select="Cuenta/ConvMultilateral"/>
							</fo:inline>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="1pt">
						<fo:block text-align="left" margin-right="2pt">
							<fo:inline color="black">Cod. Ref. Pago:&#160;</fo:inline>
							<fo:inline color="black" text-align="left" font-familiy="Courier">
								<xsl:value-of select="CodRefPagView"/>
							</fo:inline>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				
				
				<fo:table-row>
					<fo:table-cell padding="1pt">
						<fo:block text-align="left" margin-left="2pt">
							<fo:inline color="black">Catastral:&#160;</fo:inline>
							<fo:inline color="black" text-align="left" font-family="Courier">
								<xsl:value-of select="Cuenta/ListAtributoObjImp/LiqAtrValorVO[Key='Catastral']/Value"/>
							</fo:inline>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="1pt">
						<fo:block text-align="left" margin-left="2pt">
							<fo:inline color="black">Per&#237;odo:&#160;</fo:inline>
							<fo:inline color="black" text-align="left" font-family="Courier">
								<xsl:value-of select="ListReciboDeuda/LiqReciboDeudaVO[1]/PeriodoDeuda"/>
							</fo:inline>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="1pt">
						<fo:block text-align="left" margin-right="2pt">
							<fo:inline color="black">Cod. Gesti&#243;n Personal:&#160;</fo:inline>
							<fo:inline color="black" text-align="left" font-familiy="Courier">
								<xsl:value-of select="Cuenta/CodGestionPersonalView"/>
							</fo:inline>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				
				<fo:table-row>
					<fo:table-cell padding="1pt">
						<fo:block text-align="left" margin-left="2pt">
							<fo:inline color="black">Contribuyente:&#160;</fo:inline>
							<fo:inline color="black" text-align="left" font-familiy="Courier">
								<xsl:value-of select="Propietario"/>
							</fo:inline>
						</fo:block>
					</fo:table-cell>
					<fo:table-cell padding="1pt">
						<fo:block text-align="left" margin-left="2pt">
							<fo:inline color="black">Domicilio:&#160;</fo:inline>
							<fo:inline color="black" text-align="left" font-familiy="Courier">
								<xsl:value-of select="Cuenta/DesDomEnv"/>
							</fo:inline>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
				
				<fo:table-row>
					<fo:table-cell padding="1pt">
						<fo:block text-align="left" margin-left="2pt">
							<fo:inline color="black">1&#176; Rubro:&#160;</fo:inline>
							<fo:inline color="black" text-align="left" font-familiy="Courier">
								<xsl:value-of select="Cuenta/ListAtributoObjImp/LiqAtrValorVO[Key='Rubro']/MultiValue[1]"/>
							</fo:inline>
						</fo:block>
					</fo:table-cell>
				</fo:table-row>
			</fo:table-body>
		</fo:table>	   	
        
    </xsl:template>
    <!-- FIN Template para datos de la cuenta -->
    
 
</xsl:stylesheet>

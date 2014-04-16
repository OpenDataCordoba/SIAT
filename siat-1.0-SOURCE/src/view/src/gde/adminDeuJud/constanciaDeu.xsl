	<?xml version = '1.0' encoding = 'UTF-8'?>
	<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
		xmlns:fo="http://www.w3.org/1999/XSL/Format" version="1.0">
	
		<!-- Template principal -->
		<xsl:template match="/siat">
			<fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
	
				<fo:layout-master-set>
					<fo:simple-page-master margin-right="2.5cm"
							margin-left="1cm" margin-bottom="3.5cm" margin-top="1cm"
							page-width="21cm" page-height="29.7cm" master-name="left">
						<fo:region-before region-name="region-header" extent="1cm" />
						<fo:region-body region-name="region-body" margin-top="1cm" />
						<fo:region-after region-name="region-footer" extent="1.5cm" />
					</fo:simple-page-master>
				</fo:layout-master-set>
				
				<fo:page-sequence master-reference="left">
					<fo:flow flow-name="region-body">
						<fo:block text-align="center">Anexo VI</fo:block>
																	
						<fo:table  border-color="black" width="190mm" border-style="solid" border-width="1.5pt"  height="200mm">
							<fo:table-column/>						
							<fo:table-body>
								<fo:table-row>
									<fo:table-cell font-family="Times" font-size="10pt">
		<!-- titulo constancia -->
										<fo:block>
											<fo:table >
												<fo:table-column column-width="proportional-column-width(45)" />
												<fo:table-column column-width="proportional-column-width(55)"/>
												<fo:table-body>
													<fo:table-row>
														<fo:table-cell text-align="center">
															<fo:block space-before="20mm" wrap-option="no-wrap">Dirección General Legal Tributaria</fo:block>
															<fo:block wrap-option="no-wrap">Subsecretaría de Economía</fo:block>
															<fo:block wrap-option="no-wrap" space-after="5mm">Municipalidad de Rosario</fo:block>
														</fo:table-cell>
														<fo:table-cell text-align="left">
															<fo:block  space-before="10mm" font-size="14pt" font-weight="bold" wrap-option="no-wrap">Constancia de Deuda</fo:block>
														</fo:table-cell>
													</fo:table-row>
												</fo:table-body>
											</fo:table>	
										</fo:block>
		<!-- FIN titulo constancia -->									
									</fo:table-cell>
								</fo:table-row>
								<fo:table-row>
									<fo:table-cell border-top-style="solid" border-width="1.5pt" border-color="black">
		<!-- Datos de la cuenta -->							
										<xsl:call-template name="datoCuenta">
											<xsl:with-param name="label">Tributo</xsl:with-param>
											 <xsl:with-param name="valor"><xsl:value-of select="./data/ConstanciaDeuVO/Cuenta/Recurso/DesRecurso"/></xsl:with-param>
										</xsl:call-template>
										<fo:block font-family="Times" font-size="10pt">
											<fo:inline font-weight="bold">Titulares: </fo:inline>
											<fo:inline><xsl:value-of select="./data/ConstanciaDeuVO/DesTitulares"/></fo:inline>												
										</fo:block>																
										<xsl:call-template name="datoCuenta">
											<xsl:with-param name="label">Ubicación del Inmueble</xsl:with-param>
											 <xsl:with-param name="valor"><xsl:value-of select="./data/ConstanciaDeuVO/Domicilio/View"/></xsl:with-param>
										</xsl:call-template>
										<xsl:call-template name="datoCuenta">
											<xsl:with-param name="label">Cuenta</xsl:with-param>
											 <xsl:with-param name="valor"><xsl:value-of select="./data/ConstanciaDeuVO/Cuenta/NumeroCuenta"/></xsl:with-param>
										</xsl:call-template>																			
										<fo:block-container position="relative" top="5mm" left="100mm" height="25mm" width="100%">
											<xsl:call-template name="datoCuenta">
												<xsl:with-param name="label">Catastral</xsl:with-param>
												 <xsl:with-param name="valor"><xsl:value-of select="./data/ConstanciaDeuVO/Cuenta/ObjImp/ClaveFuncional"/></xsl:with-param>
											</xsl:call-template>									
										</fo:block-container>																																												
										
		<!-- FIN Datos de la cuenta -->									
									</fo:table-cell>
								</fo:table-row>
																
								<fo:table-row>
									<fo:table-cell font-family="Times" font-size="10pt"  border-top-style="solid" border-width="1.5pt" border-color="black">
										<fo:block space-before="3mm">
											<fo:inline font-weight="bold">Procurador:</fo:inline>
											<fo:inline><xsl:value-of select="./data/ConstanciaDeuVO/Procurador/Descripcion"/></fo:inline>
										</fo:block>
										<fo:block space-before="5mm">Saldo por Caducidad de los períodos de los años 2003 que surgen de un Convenio Administrativo 14521-06 formalizado el 15/3/2004, Ordenanza 6580/98 (ojo puede ser 7902/05 o 7926/05 si es judicial), sist. 58 ( o 74, o 98 etc), Plan 36 cuotas, pagas 5 cuotas produciéndose la caducidad en el mes de noviembre de 2004 por la acumulación de 3 cuotas impagas consecutivas.</fo:block>																	
		<!-- Lista de deudas -->																
										<fo:block>
											<fo:table padding="3pt" text-align="center">
												<fo:table-column column-width="3mm"/>
												<fo:table-column column-width="49mm"/>
												<fo:table-column column-width="49mm"/>
												<fo:table-column column-width="49mm"/>
												<fo:table-column column-width="15mm"/>
												<fo:table-column column-width="25mm"/>
												<fo:table-header>													
													<fo:table-row>
														<fo:table-cell number-columns-spanned="5">
															<fo:block font-weight="bold" space-before="3mm" text-align="center">Detalle de la deuda</fo:block>
														</fo:table-cell>
													</fo:table-row>
													<fo:table-row font-weight="bold">
														<fo:table-cell>&#160;</fo:table-cell>
														<fo:table-cell><fo:block>Planilla</fo:block></fo:table-cell>
														<fo:table-cell><fo:block>Períodos</fo:block></fo:table-cell>
														<fo:table-cell><fo:block>Años</fo:block></fo:table-cell>													
														<fo:table-cell><fo:block>Total</fo:block></fo:table-cell>
														<fo:table-cell>&#160;</fo:table-cell>
													</fo:table-row>												
												</fo:table-header>
												<fo:table-body>		
													<xsl:for-each select="./data/ConstanciaDeuVO/ListConDeuDet/ConDeuDetVO">
														<fo:table-row font-size="10pt">
																<fo:table-cell>&#160;</fo:table-cell>
																<fo:table-cell><fo:block><xsl:value-of select="../../PlaEnvDeuPro/NroBarraAnioPlanillaView" /></fo:block></fo:table-cell>
																<fo:table-cell><fo:block><xsl:value-of select="Deuda/PeriodoView" /></fo:block></fo:table-cell>
																<fo:table-cell><fo:block><xsl:value-of select="Deuda/AnioView" /></fo:block></fo:table-cell>
																<fo:table-cell><fo:block><xsl:value-of select="Deuda/ImporteView" /></fo:block></fo:table-cell>
																<fo:table-cell>&#160;</fo:table-cell>															
														</fo:table-row>	
													</xsl:for-each>
													<fo:table-row font-size="8pt">
														<fo:table-cell>&#160;</fo:table-cell>
														<fo:table-cell>&#160;</fo:table-cell>
														<fo:table-cell number-columns-spanned="2" text-align="left" number-rows-spanned="2">
															<fo:block>
																TOTAL DE DEUDA POR LOS AÑOS DETALLADOS EN GESTIÓN JUDICIAL, AL MES DE 
																<xsl:value-of select="./data/ConstanciaDeuVO/NombreMes"/> DE 
																<xsl:value-of select="./data/ConstanciaDeuVO/Anio"/> 
															</fo:block>
														</fo:table-cell>
														<fo:table-cell border-top-style="solid" border-bottom-style="solid"><fo:block>$ <xsl:value-of select="./data/ConstanciaDeuVO/TotalView" /></fo:block></fo:table-cell>
														<fo:table-cell>&#160;</fo:table-cell>
													</fo:table-row>
													<fo:table-row font-size="8pt">
														<fo:table-cell>&#160;</fo:table-cell>
														<fo:table-cell number-columns-spanned="4" text-align="right">
															<fo:block space-after="3mm" space-before="6mm">
																En Letras: <xsl:value-of select="./data/ConstanciaDeuVO/TotalEnPalabrasView"/> 
															</fo:block>
														</fo:table-cell>
													</fo:table-row>																										
												</fo:table-body>	
											</fo:table>
										</fo:block>
		<!-- FIN Lista de deudas -->																
									</fo:table-cell>
								</fo:table-row>
								<fo:table-row>
									<fo:table-cell>
										<fo:block space-before="7mm" font-size="8pt">Se extiende la presente liquidación de Deuda conforme con los registros de cuentas respectivos, siendo TÍTULO EJECUTIVO HÁBIL a los efectos de la  demanda de Apremio por el cobro del monto expresado, de acuerdo al Código Civil (art. 979 incs. 2 y 5), Ley 5066 (art. 6 inc. a) y Código Tributario Municipal '(arts. 1, 3, 8, 9, 10, 11, 12, 13, 25, 62, 63, 68, 70 y 71). A ESTA DEUDA, DEBERÁ ADICIONARSE AL MOMENTO DE SU PAGO LOS ACCESORIOS 'CORRESPONDIENTES, conforme Ordenanza Nº 9476/78, y sus modificatorias, Ordenanza Nº 5270/91, Código Tributario Municipal (arts. 34, 35 y 41) y Ordenanza General Impositiva (art. 112 modificado por Ordenanza Nº 5271/91). La presente LIQUIDACIÓN no implica inexistencia de deuda por 'Tasa General de Inmuebles referida a otros períodos no detallados en la misma.  </fo:block>									
									</fo:table-cell>
								</fo:table-row>
								<fo:table-row>
									<fo:table-cell>	
										<fo:block>
											<fo:table padding="3pt">
												<fo:table-column column-width="proportional-column-width(60)"/>
												<fo:table-column column-width="proportional-column-width(40)"/>
												<fo:table-body>
													<fo:table-row>
														<fo:table-cell text-align="left">
															<fo:block font-size="10pt" space-before="5mm">Rosario, <xsl:value-of select="./data/ConstanciaDeuVO/FechaEnLetras"/></fo:block>
														</fo:table-cell>
														<fo:table-cell text-align="center">
															<fo:block text-align="center" font-size="9pt" space-before="25mm">FIRMA Y SELLO DEL FUNCIONARIO</fo:block>
															<fo:block text-align="center" font-size="9pt">AUTORIZADO</fo:block>													
														</fo:table-cell>
													</fo:table-row>
												</fo:table-body>
											</fo:table>
										</fo:block>																							
									</fo:table-cell>
								</fo:table-row>							
							</fo:table-body>
						</fo:table>
												
					</fo:flow>
				</fo:page-sequence>
				
			</fo:root>	
		</xsl:template>
		<!-- FIN Template principal -->
		
		
		<!-- Template para un dato de la cuenta -->
		<xsl:template name="datoCuenta">
			<xsl:param name="label"/>
			<xsl:param name="valor"/>
				<fo:block space-before="1mm" wrap-option="no-wrap" font-family="Times" font-size="10pt">
					<fo:inline color="black" text-align="left" font-weight="bold" ><xsl:value-of select="$label"/>:&#160;</fo:inline>
					<fo:inline color="black" text-align="left"><xsl:value-of select="$valor"/></fo:inline>
				</fo:block>	
		</xsl:template>
		<!-- FIN Template para un datos de la cuenta (se usa en la cabecera) -->
		
	</xsl:stylesheet>
		
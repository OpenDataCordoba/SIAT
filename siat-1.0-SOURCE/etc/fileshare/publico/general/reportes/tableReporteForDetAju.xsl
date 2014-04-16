<?xml version="1.0" encoding="iso-8859-1"?>
<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:fo="http://www.w3.org/1999/XSL/Format"
    version="1.0">

  <xsl:output method="xml" version="1.0" indent="yes"/>

<!-- Template para el encabezado -->
	<xsl:template name="tableReporte">
		<xsl:param name="tableVO"/>

		<!-- tabla que contiene la fila titulo  -->
		<xsl:if test="$tableVO/FilaCabecera/ListCelda/CeldaVO[1]/Valor != 'OBSERVACIONES'">
			<fo:block space-before="0.2cm" font-size="10pt"  border-color="black" border-style="solid" border-width="0.6pt" width="100%">
				<fo:block color="black" text-align="center" font-weight="bold" font-style="italic">
					<xsl:value-of select="$tableVO/Titulo" />
				</fo:block>		
			
				<!-- Fila cabecera de cada tabla  -->
				<fo:table font-size="10pt"  border-color="black" border-style="solid" border-width="0.5pt" table-layout="fixed" 
			                inline-progression-dimension="14cm" inline-progression-dimension.maximum="100%">
					<xsl:for-each select="$tableVO/FilaCabecera/ListCelda/CeldaVO">
						<xsl:variable name="w" select="Width" />
						<xsl:if test="$w=0">
							<fo:table-column border-style="solid" border-width="0.1pt" column-width="proportional-column-width(1)"/> <!-- auto en la nueva version de fop -->	
						</xsl:if>
						<xsl:if test="$w!=0">
							<fo:table-column border-style="solid" border-width="0.1pt" column-width="{$w}mm"/>
						</xsl:if>
					</xsl:for-each>
					<fo:table-header>
						<fo:table-row>
							<xsl:for-each select="$tableVO/FilaCabecera/ListCelda/CeldaVO">
								<fo:table-cell padding="1pt" border-style="solid">
										<fo:block color="black" text-align="left" font-weight="bold" font-size="7pt">
											<xsl:value-of select="Valor" />
										</fo:block>  
								</fo:table-cell>	
							</xsl:for-each>
						</fo:table-row>	
					</fo:table-header>
					<fo:table-body>
						<xsl:for-each select="$tableVO/ListFila/FilaVO">
							<fo:table-row >
							    <xsl:attribute name="background-color">
									 	<xsl:value-of select="Color"/>
	  						    </xsl:attribute>
							
								<xsl:for-each select="ListCelda/CeldaVO">
									<fo:table-cell padding="1pt" border-style="solid" border-width="0.1pt">
										<fo:block text-align="center">
											<xsl:value-of select="Valor" />
										</fo:block>  
										<xsl:for-each select="ListCelda/CeldaVO">
											<fo:block text-align="left">
												<xsl:value-of select="Valor" />
											</fo:block>
										</xsl:for-each>
									</fo:table-cell>	
								</xsl:for-each>
							</fo:table-row>							
						</xsl:for-each>
						<xsl:for-each select="$tableVO/ListFilaPie/FilaVO">
							<fo:table-row >
								<xsl:for-each select="ListCelda/CeldaVO">
									<xsl:if test="position()=1">
									<fo:table-cell padding="1pt" border-style="solid" border-width="0.5pt" number-columns-spanned="7">
										<fo:block text-align="right" font-weight="bold">
											<xsl:value-of select="Valor" />
										</fo:block>
									</fo:table-cell>
									
									</xsl:if>
									<xsl:if test="position()!=1">
									<fo:table-cell padding="1pt" border-style="solid" border-width="0.5pt">
										<fo:block text-align="center" font-weight="bold">
											<xsl:value-of select="Valor" />
										</fo:block>
									</fo:table-cell>
									</xsl:if>
								</xsl:for-each>
							</fo:table-row>							
						</xsl:for-each>
					</fo:table-body>
				</fo:table>			
			</fo:block>	
		</xsl:if>
		
		<xsl:if test="$tableVO/FilaCabecera/ListCelda/CeldaVO[1]/Valor = 'OBSERVACIONES'">
			<fo:block space-before="1cm" font-size="10pt"  border-color="black" border-style="none" border-width="0.6pt" width="100%">
				<fo:block color="black" text-align="center" font-weight="bold" font-style="italic">
					<xsl:value-of select="$tableVO/Titulo" />
				</fo:block>		
			
				<!-- Fila cabecera de cada tabla  -->
				<fo:table font-size="10pt"  border-color="black" border-style="none" border-width="0.5pt" table-layout="fixed" 
			                inline-progression-dimension="14cm" inline-progression-dimension.maximum="100%">
					<xsl:for-each select="$tableVO/FilaCabecera/ListCelda/CeldaVO">
						<xsl:variable name="w" select="Width" />
							<fo:table-column border-style="none" border-width="0.1pt" column-width="proportional-column-width(1)"/> <!-- auto en la nueva version de fop -->	
					</xsl:for-each>
					<fo:table-header>
						<fo:table-row>
							<xsl:for-each select="$tableVO/FilaCabecera/ListCelda/CeldaVO">
								<fo:table-cell padding="1pt" border-style="none">
										<fo:block color="black" text-align="left" font-weight="bold" font-size="10pt">
											<xsl:value-of select="Valor" />
										</fo:block>  
								</fo:table-cell>	
							</xsl:for-each>
						</fo:table-row>	
					</fo:table-header>
					<fo:table-body>
						<xsl:for-each select="$tableVO/ListFila/FilaVO">
							<fo:table-row >
							    <xsl:attribute name="background-color">
									 	<xsl:value-of select="Color"/>
	  						    </xsl:attribute>
							
								<xsl:for-each select="ListCelda/CeldaVO">
									<fo:table-cell padding="1pt" border-style="none" border-width="0.1pt">
										<fo:block text-align="left">
											<xsl:value-of select="Valor" />
										</fo:block>  
										<xsl:for-each select="ListCelda/CeldaVO">
											<fo:block text-align="left">
												<xsl:value-of select="Valor" />
											</fo:block>
										</xsl:for-each>
									</fo:table-cell>	
								</xsl:for-each>
							</fo:table-row>							
						</xsl:for-each>
					</fo:table-body>
				</fo:table>			
			</fo:block>	
		</xsl:if>
		
		
	</xsl:template>
</xsl:stylesheet>

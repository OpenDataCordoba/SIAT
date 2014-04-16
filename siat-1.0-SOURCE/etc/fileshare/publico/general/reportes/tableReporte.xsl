<?xml version="1.0" encoding="iso-8859-1"?>
<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:fo="http://www.w3.org/1999/XSL/Format"
    version="1.0">

  <xsl:output method="xml" version="1.0" indent="yes"/>

<!-- Template para el encabezado -->
	<xsl:template name="tableReporte">
		<xsl:param name="tableVO"/>

		<!-- tabla que contiene la fila titulo -->
		<fo:block space-before="1cm" font-size="10pt"  border-color="black" border-style="solid" border-width="0.6pt" width="100%">
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
								<fo:block color="black" text-align="center" font-weight="bold">
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
									<fo:block>
										<xsl:if test="WhiteSpaceCollapse = 'true'"> 
											<xsl:attribute name="text-align">
												 <xsl:value-of select="TextAlign"/>
		  									</xsl:attribute>
	  									</xsl:if>
	  									<xsl:if test="WhiteSpaceCollapse != 'true'"> 
	  									    <xsl:attribute name="white-space-collapse">
											 	<xsl:value-of select="WhiteSpaceCollapse"/>
	  									    </xsl:attribute>
	  									</xsl:if>
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
								<fo:table-cell padding="1pt" border-style="solid" border-width="0.5pt">
									<fo:block text-align="center" font-weight="bold">
										<xsl:value-of select="Valor" />
									</fo:block>
								</fo:table-cell>
							</xsl:for-each>
						</fo:table-row>							
					</xsl:for-each>
				</fo:table-body>
			</fo:table>			
		</fo:block>	

	</xsl:template>
</xsl:stylesheet>

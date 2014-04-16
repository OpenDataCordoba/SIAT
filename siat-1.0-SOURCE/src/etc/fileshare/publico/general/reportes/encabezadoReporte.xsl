<?xml version="1.0" encoding="iso-8859-1"?>
<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:fo="http://www.w3.org/1999/XSL/Format"
    version="1.0">

  <!-- Datos Sistema y Filtro -->	 
  <xsl:attribute-set name="inline_title"> 
	<xsl:attribute name="font-size">8pt</xsl:attribute>  
    <xsl:attribute name="font-weight">bold</xsl:attribute>
    <xsl:attribute name="text-align">left</xsl:attribute>
  </xsl:attribute-set>
  
  <xsl:attribute-set name="inline_title_r">
  	<xsl:attribute name="font-size">8pt</xsl:attribute> 
    <xsl:attribute name="font-weight">bold</xsl:attribute>
    <xsl:attribute name="text-align">right</xsl:attribute>
  </xsl:attribute-set>
  
  <xsl:attribute-set name="inline_normal">
  	<xsl:attribute name="font-size">8pt</xsl:attribute> 
    <xsl:attribute name="text-align">left</xsl:attribute>
  </xsl:attribute-set>

  <xsl:output method="xml" version="1.0" indent="yes"/>


<!-- Template para el encabezado -->
	<xsl:template name="encabezadoReporte">
		<xsl:param name="tituloReporte"/>
		<xsl:param name="fecha"   />
		<xsl:param name="hora"    />
		<xsl:param name="usuario" />

          <fo:block>
            <fo:table table-layout="fixed" width="100%">	
              <fo:table-column column-width="5cm"/>
              <fo:table-column column-width="proportional-column-width(1)"/>
              <fo:table-column column-width="5cm"/>
              <fo:table-body start-indent="0.0pt">	        
                <fo:table-row>
                  <!-- Logo -->
                  <fo:table-cell>
                      <fo:block>
	   			  			<fo:external-graphic height="19mm" width="13mm"
									src="{concat(//FileSharePath, '/publico/general/reportes/images/escudo_muni_color.jpg')}" />
	   				</fo:block>
                  </fo:table-cell>
                  <fo:table-cell>
					<!-- Titulo -->
					<fo:block text-align="center" font-weight="bold">
						<fo:inline font-size="14pt" color="black" text-align="center">
							<xsl:value-of select="$tituloReporte" />
						</fo:inline>					
					</fo:block>
                  </fo:table-cell>
										
                <!-- fecha hora y usuario -->
                <!-- Usuario y fecha -->
                   <fo:table-cell>
	               <fo:block text-align="right">
                           <fo:inline xsl:use-attribute-sets="inline_title">Fecha: </fo:inline>
                           <fo:inline xsl:use-attribute-sets="inline_normal">
		                       <xsl:value-of select="$fecha"/>
                           </fo:inline>
                       </fo:block>
	               <fo:block text-align="right">
                           <fo:inline xsl:use-attribute-sets="inline_title">Hora: </fo:inline>
                           <fo:inline xsl:use-attribute-sets="inline_normal">
		                       <xsl:value-of select="$hora"/>
                           </fo:inline>
                       </fo:block>
                       <fo:block text-align="right">
		                   <fo:inline xsl:use-attribute-sets="inline_title">Usuario: </fo:inline>
		                   <fo:inline xsl:use-attribute-sets="inline_normal">
		                       <xsl:value-of select="$usuario"/>
	        	        </fo:inline>
                       </fo:block>
                   </fo:table-cell>
                </fo:table-row>
              </fo:table-body>
            </fo:table>
          </fo:block>		
	</xsl:template>
	
	<xsl:template name="encabezadoReporteFiscalizacion">
		<xsl:param name="tituloReporte"/>
		<xsl:param name="fecha"   />
		<xsl:param name="hora"    />
		<xsl:param name="usuario" />

          <fo:block>
            <fo:table table-layout="fixed" width="100%">	
              <fo:table-column column-width="5cm"/>
              <fo:table-column column-width="proportional-column-width(1)"/>
              <fo:table-column column-width="5cm"/>
              <fo:table-body start-indent="0.0pt">	        
                <fo:table-row>
                  <!-- Logo -->
                 <fo:table-cell font-family="Times" font-size="10pt">
								<fo:block >
									<fo:external-graphic height="20mm" width="15mm"
									 src="{concat(//FileSharePath, '/publico/general/reportes/images/escudo_muni.jpg')}" />
								</fo:block>												
								<fo:block font-size="14pt" wrap-option="no-wrap" font-weight="bold" space-before="4mm" space-after="4mm">MUNICIPALIDAD DE [LOCALIDAD]</fo:block>
								<fo:block font-size="13pt" wrap-option="no-wrap" space-after="2mm">SUBDIRECCION GENERAL</fo:block>
								<fo:block font-size="13pt" wrap-option="no-wrap" space-after="2mm">DIRECCION TRIBUTARIA</fo:block>
							</fo:table-cell>
                  <fo:table-cell>
					<!-- Titulo -->
					<fo:block text-align="center" font-weight="bold">
						<fo:inline font-size="14pt" color="black" text-align="center">
							<xsl:value-of select="$tituloReporte" />
						</fo:inline>					
					</fo:block>
                  </fo:table-cell>
										
                <!-- fecha hora y usuario -->
                <!-- Usuario y fecha -->
                   <fo:table-cell>
	               <fo:block text-align="right">
                           <fo:inline xsl:use-attribute-sets="inline_title">Fecha: </fo:inline>
                           <fo:inline xsl:use-attribute-sets="inline_normal">
		                       <xsl:value-of select="$fecha"/>
                           </fo:inline>
                       </fo:block>
	               <fo:block text-align="right">
                           <fo:inline xsl:use-attribute-sets="inline_title">Hora: </fo:inline>
                           <fo:inline xsl:use-attribute-sets="inline_normal">
		                       <xsl:value-of select="$hora"/>
                           </fo:inline>
                       </fo:block>
                       <fo:block text-align="right">
		                   <fo:inline xsl:use-attribute-sets="inline_title">Usuario: </fo:inline>
		                   <fo:inline xsl:use-attribute-sets="inline_normal">
		                       <xsl:value-of select="$usuario"/>
	        	        </fo:inline>
                       </fo:block>
                   </fo:table-cell>
                </fo:table-row>
              </fo:table-body>
            </fo:table>
          </fo:block>		
	</xsl:template>
	
	
</xsl:stylesheet>

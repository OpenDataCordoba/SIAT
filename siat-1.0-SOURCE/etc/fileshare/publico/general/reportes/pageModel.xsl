<?xml version = '1.0' encoding = 'ISO-8859-1'?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:fo="http://www.w3.org/1999/XSL/Format" version="1.0">

<!-- importamos xsl de otro archivo -->	 	
<xsl:import href="@@FileSharePath/publico/general/reportes/encabezadoReporte.xsl"/>
<!-- importamos xsl de otro archivo -->	 	
<xsl:import href="@@FileSharePath/publico/general/reportes/tableReporte.xsl"/>

<xsl:output method="xml" indent="yes"/>
	
<xsl:template match="/siat">
<fo:root font-family="Times" font-size="10pt">
    <fo:layout-master-set>
	<xsl:variable name="pageWidth" select="data/ContenedorVO/PageWidth" />
	<xsl:variable name="pageHeight" select="data/ContenedorVO/PageHeight" />

         <fo:simple-page-master margin-right="1.5cm" 
				margin-left="1.5cm" 
				margin-bottom="1cm" 
				margin-top="1cm" 
				page-width="{$pageWidth}cm" 
				page-height="{$pageHeight}cm" 
				master-name="left">
	   <fo:region-body   region-name="region-body"   margin-top="1cm" margin-bottom="1cm"/>
	   <fo:region-before region-name="region-header" extent="0.5cm"/>
	   <fo:region-after  region-name="region-footer" extent="0.5cm"/>
	   <!-- fo:region-after region-name="region-footer" extent="1.5cm"/ -->
	 </fo:simple-page-master>
    </fo:layout-master-set>

    <fo:page-sequence master-reference="left">
	<!-- Numero de Pagina-->                
	<fo:static-content flow-name="region-footer">
	    <fo:block>
	        Página <fo:page-number/>					
	     </fo:block>
	</fo:static-content>

 	<fo:flow flow-name="region-body" font-family="Times">
		<xsl:apply-templates/>
	
<xsl:call-template name="encabezadoReporte">
<!-- xsl:with-param name="logo" select="'@@FileSharePath/publico/general/reportes/logo_siat.jpg'"/ -->
<xsl:with-param name="tituloReporte" select="cabecera/TituloReporte"/>
<xsl:with-param name="fecha"   select="cabecera/Fecha"/>
<xsl:with-param name="hora"    select="cabecera/Hora"/>
<xsl:with-param name="usuario" select="cabecera/Usuario"/>
</xsl:call-template>


	<!-- Cabecera -->
	<fo:block space-before="0.1cm" text-align="left" font-size="10pt">
		<fo:block color="black" text-align="left" font-weight="bold" font-size="11pt" text-decoration="underline">
			<xsl:value-of select="data/ContenedorVO/TablaFiltros/Titulo" />
		</fo:block>
		<xsl:for-each select="data/ContenedorVO/TablaFiltros">
			<xsl:for-each select="ListFila/FilaVO">
				<xsl:for-each select="ListCelda/CeldaVO">
					<fo:block space-before="0.1cm">
						<fo:inline color="black" text-align="left" font-weight="bold">
							<xsl:value-of select="Etiqueta" />:&#160;
						</fo:inline>
						<fo:inline color="black" text-align="left">
							<xsl:value-of select="Valor" />
					        </fo:inline>
					</fo:block>
				</xsl:for-each>
			</xsl:for-each>	
		</xsl:for-each >
	</fo:block>  

<!-- lista de bloques -->
<xsl:for-each select="data/ContenedorVO/ListBloque/ContenedorVO">
	<fo:block border-style="solid" space-before="1cm" padding-top="1pt" padding-left="1pt">
	<fo:block border-style="solid" text-align="center" font-weight="bold" font-style="italic" border-width="0.6pt">
		<xsl:value-of select="TablaCabecera/Titulo" />
	</fo:block>
	<xsl:for-each select="TablaCabecera/FilaCabecera/ListCelda/CeldaVO">
		<fo:block>
			<fo:inline color="black" text-align="left" font-weight="bold">
				<xsl:value-of select="Etiqueta" />:&#160;
			</fo:inline>
			<fo:inline color="black" text-align="left">
				<xsl:value-of select="Valor" />
			</fo:inline>
		</fo:block>
	</xsl:for-each>
	
	<!-- lista de bloques de cada contenedor -->	
	<xsl:for-each select="ListBloque/ContenedorVO">
		<fo:block border-style="solid" space-before="1cm" padding-top="1pt" padding-left="1pt">	
		<xsl:for-each select="TablaCabecera/FilaCabecera/ListCelda/CeldaVO">
			<fo:block>
				<fo:inline color="black" text-align="left" font-weight="bold">
					<xsl:value-of select="Etiqueta" />:&#160;
				</fo:inline>
				<fo:inline color="black" text-align="left">
					<xsl:value-of select="Valor" />
				</fo:inline>
			</fo:block>
		</xsl:for-each>
		<xsl:for-each select="ListTabla/TablaVO">
			<xsl:call-template name="tableReporte">
			<xsl:with-param name="tableVO" select="."/>
			</xsl:call-template>
		</xsl:for-each>
		</fo:block>
	</xsl:for-each>
	<!-- listado de tablas de cada contenedor -->
	<xsl:for-each select="ListTabla/TablaVO">
		<xsl:call-template name="tableReporte">
		<xsl:with-param name="tableVO" select="."/>
		</xsl:call-template>
	</xsl:for-each>

	</fo:block>
</xsl:for-each>

<!-- lista de Tablas de Contenidos -->
<xsl:for-each select="data/ContenedorVO/ListTabla/TablaVO">
		<xsl:call-template name="tableReporte">
		<xsl:with-param name="tableVO" select="."/>
		</xsl:call-template>
</xsl:for-each>

 <!-- fo:block id="last-page"/ -->
</fo:flow>
</fo:page-sequence>
</fo:root>
</xsl:template>	
</xsl:stylesheet>

<?xml version = '1.0' encoding = 'ISO-8859-1'?>
<xsl:stylesheet 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:fo="http://www.w3.org/1999/XSL/Format" version="1.0">
   
   <!-- importamos algunas funciones y cosas utiles -->
   <!--xsl:import href="@@FileSharePath/publico/general/reportes/statements.xsl"></xsl:import!-->
   
   <xsl:template match="/siat">
     <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">

       <fo:layout-master-set>
         <fo:simple-page-master margin-right="1.5cm" 
				margin-left="1.5cm" 
				margin-bottom="2cm" 
				margin-top="1cm" 
				page-width="21cm" 
				page-height="29.7cm" 
				master-name="left">

	   <fo:region-before region-name="region-header" extent="1cm"/>
	   <fo:region-body region-name="region-body" margin-top="1cm"/>
	   <fo:region-after region-name="region-footer" extent="1.5cm"/>
	 </fo:simple-page-master>
       </fo:layout-master-set>
       
       <fo:page-sequence master-reference="left">
         <fo:flow flow-name="region-body">
	   
	   <fo:block>
	     <fo:external-graphic height="10mm" width="10mm" src="'@@FileSharePath/publico/general/reportes/images/logoTecso.jpg'"/>
	   </fo:block>


	   <!-- Cabecera del Atributo -->
	   <fo:table width="100%">
	     <fo:table-column column-width="proportional-column-width(50)"/>
   	     <fo:table-column column-width="proportional-column-width(50)"/>
	     <fo:table-body>
               <fo:table-row>
		 <fo:table-cell>
		   <fo:block>
		     Código del Atributo: <xsl:value-of select="./data/AtributoVO/CodAtributo"/>
		   </fo:block>
		 </fo:table-cell>
	       </fo:table-row>
	       
               <fo:table-row>
		 <fo:table-cell>
		   <fo:block>
		     Descripción: <xsl:value-of select="./data/AtributoVO/DesAtributo"/>
		   </fo:block>
		 </fo:table-cell>
	       </fo:table-row>

               <fo:table-row>
		 <fo:table-cell>
		   <fo:block>
		     Tipo: <xsl:value-of select="./data/AtributoVO/TipoAtributo/CodTipoAtributo"/> - <xsl:value-of select="./data/AtributoVO/TipoAtributo/DesTipoAtributo"/> 
		   </fo:block>
		 </fo:table-cell>
	       </fo:table-row>
	     </fo:table-body>
	   </fo:table>

	   <!-- Lista de valores de dominio -->
	   <fo:table width="100%" space-before="20px" border="solid black 2px">
	     <fo:table-column column-width="proportional-column-width(50)"/>
   	     <fo:table-column column-width="proportional-column-width(50)"/>

	     <fo:table-header>
	       <fo:table-row text-align="center" >
		 <fo:table-cell border="solid black 1px">
		   <fo:block>
		     Descripcion
		   </fo:block>
		 </fo:table-cell>
		 <fo:table-cell border="solid black 1px">
		   <fo:block>
		     Valor
		   </fo:block>
		 </fo:table-cell>
	       </fo:table-row>
	     </fo:table-header>

	     <fo:table-body>

	       <xsl:for-each select="./data/AtributoVO/DomAtr/ListDomAtrVal/DomAtrValVO">
		 <fo:table-row>
		   <fo:table-cell border="solid black 1px">
		     <fo:block>
		       <xsl:value-of select="./DesValor"/>
		     </fo:block>
		   </fo:table-cell>
		   <fo:table-cell border="solid black 1px">
		     <fo:block>
		       <xsl:value-of select="./Valor"/>
		     </fo:block>
		   </fo:table-cell>
		 </fo:table-row>
	       </xsl:for-each>

	     </fo:table-body>
	   </fo:table>
         </fo:flow>         
       </fo:page-sequence>
     </fo:root>
   </xsl:template>
</xsl:stylesheet>
































<!--
						<fo:table-cell>
							<fo:block text-align="right" display-align="center">
								<xsl:apply-templates select=".//Logo"/>	
							</fo:block>
									
							<xsl:value-of select=".//HcfActiva/NroHcf"/>
							<fo:block>
								APELLIDO: <xsl:value-of select=".//Apellido"/>
							</fo:block>																			
						</fo:table-cell>

																<fo:block>
																	EQUIPO DE REF.:  <xsl:value-of select="concat(.//AdscripcionActiva/EquipoPersonal/PersonalEfector/Personal/Nombres,' ' , .//AdscripcionActiva/EquipoPersonal/PersonalEfector/Personal/Apellido)"/> 
																</fo:block>
								<fo:table-body>
									<xsl:for-each select=".//ar.gov.rosario.aps.per.iface.model.PersonaVO/Paciente/ListHistoricoAdscripciones">
										<fo:table-row>
											<fo:table-cell padding-start="1mm">
												<fo:block 
													padding-before="1mm" padding-after="1mm"
													font-size="6.5pt" font-weight="bold">
													<xsl:value-of select=".//ar.gov.rosario.aps.hc.iface.model.AdscripcionVO/EquipoReferencia/Efector/Nombre"/>
												</fo:block>
											</fo:table-cell>
									</xsl:for-each>
									
					</fo:table>
            </fo:flow>         
         </fo:page-sequence>
      </fo:root>
   </xsl:template>
</xsl:stylesheet>

-->

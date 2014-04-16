<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	version="1.0" 
	xmlns:fo="http://www.w3.org/1999/XSL/Format" 
	xmlns:fox="http://xml.apache.org/fop/extensions">
  
  <xsl:attribute-set name="table_grid_head">
    <xsl:attribute name="border-right-style">solid</xsl:attribute>
    <xsl:attribute name="border-left-style">solid</xsl:attribute>
    <!-- xsl:attribute name="border-bottom-style">solid</xsl:attribute-->
    <xsl:attribute name="border-top-style">solid</xsl:attribute>
  </xsl:attribute-set>
  
  <xsl:attribute-set name="table_grid_body">
    <xsl:attribute name="border-right-style">solid</xsl:attribute>
    <xsl:attribute name="border-left-style">solid</xsl:attribute>
    <xsl:attribute name="border-bottom-style">solid</xsl:attribute>
    <xsl:attribute name="border-top-style">solid</xsl:attribute>	
  </xsl:attribute-set>
  
  <xsl:attribute-set name="cell_head">
    <xsl:attribute name="border-right-style">solid</xsl:attribute>
    <xsl:attribute name="border-left-style">solid</xsl:attribute>
    <xsl:attribute name="border-bottom-style">solid</xsl:attribute>
    <xsl:attribute name="border-top-style">solid</xsl:attribute>
  </xsl:attribute-set>
  
  <xsl:attribute-set name="cell_body">
    <xsl:attribute name="border-right-style">solid</xsl:attribute>
    <xsl:attribute name="border-left-style">solid</xsl:attribute>
    <xsl:attribute name="border-bottom-style">solid</xsl:attribute>
    <xsl:attribute name="border-top-style">solid</xsl:attribute>
  </xsl:attribute-set>
  
  <xsl:attribute-set name="block_title_gral">
    <xsl:attribute name="font-size">10pt</xsl:attribute>
    <xsl:attribute name="font-weight">bold</xsl:attribute>
    <xsl:attribute name="text-align">center</xsl:attribute>
  </xsl:attribute-set>
  
  <xsl:attribute-set name="block_subtitle">
  	<xsl:attribute name="font-size">8pt</xsl:attribute>
    <xsl:attribute name="font-weight">bold</xsl:attribute>
    <xsl:attribute name="text-align">left</xsl:attribute>
  </xsl:attribute-set>
 
  <!-- Datos Sistema y Filtro-->	 
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
  
  <xsl:attribute-set name="inline_normal_r">
	<xsl:attribute name="font-size">8pt</xsl:attribute> 
    <xsl:attribute name="text-align">right</xsl:attribute>
  </xsl:attribute-set>
  
  <!-- Encabezado de las grillas -->
  <xsl:attribute-set name="block_title">
    <xsl:attribute name="font-size">8pt</xsl:attribute> 
    <xsl:attribute name="font-weight">bold</xsl:attribute>
    <xsl:attribute name="text-align">center</xsl:attribute>
  </xsl:attribute-set>
  
  <xsl:attribute-set name="block_title_l">
    <xsl:attribute name="font-size">8pt</xsl:attribute> 
    <xsl:attribute name="font-weight">bold</xsl:attribute>
    <xsl:attribute name="text-align">left</xsl:attribute>
  </xsl:attribute-set>
  
  <xsl:attribute-set name="block_title_r">
    <xsl:attribute name="font-size">8pt</xsl:attribute> 
    <xsl:attribute name="font-weight">bold</xsl:attribute>
    <xsl:attribute name="text-align">right</xsl:attribute>
  </xsl:attribute-set>
  <!-- Texto de las grillas excepto monto -->	
  <xsl:attribute-set name="block_normal">
  	<xsl:attribute name="font-size">8pt</xsl:attribute>
    <xsl:attribute name="text-align">center</xsl:attribute>
  </xsl:attribute-set>
  
  <xsl:attribute-set name="block_normal_l">
  	<xsl:attribute name="font-size">8pt</xsl:attribute>
    <xsl:attribute name="text-align">left</xsl:attribute>
  </xsl:attribute-set>
  
  <xsl:attribute-set name="block_normal_r">
  	<xsl:attribute name="font-size">8pt</xsl:attribute>
    <xsl:attribute name="text-align">right</xsl:attribute>
  </xsl:attribute-set>
  
  <xsl:attribute-set name="block_monto">
    <xsl:attribute name="font-size">8pt</xsl:attribute>
    <xsl:attribute name="text-align">right</xsl:attribute>
  </xsl:attribute-set>
	
  <!-- Bloque general encierra el detalle de un informe -->	
  <xsl:attribute-set name="block_body">
    <xsl:attribute name="border-right-style">solid</xsl:attribute>
    <xsl:attribute name="border-left-style">solid</xsl:attribute>
    <xsl:attribute name="border-bottom-style">solid</xsl:attribute>
    <xsl:attribute name="border-top-style">solid</xsl:attribute>
    <xsl:attribute name="space-before">2mm</xsl:attribute>
    <xsl:attribute name="space-after">2mm</xsl:attribute>
    <xsl:attribute name="padding-before">2mm</xsl:attribute>
  </xsl:attribute-set>
 
  <!-- Bloques con lineas superior e inferior para los titulos -->
  <xsl:attribute-set name="block_title_bsi">
    <xsl:attribute name="border-bottom-style">solid</xsl:attribute>
    <xsl:attribute name="border-top-style">solid</xsl:attribute>
    <xsl:attribute name="space-after">2mm</xsl:attribute>
    <xsl:attribute name="margin-top">2mm</xsl:attribute>
    <xsl:attribute name="margin-bottom">2mm</xsl:attribute>
    <xsl:attribute name="margin-left">2mm</xsl:attribute>
    <xsl:attribute name="margin-right">2mm</xsl:attribute>    
  </xsl:attribute-set>
 
  <!-- Linea de separacion--> 
  <xsl:attribute-set name="block_seraprador">
    <xsl:attribute name="border-top-style">solid</xsl:attribute>
    <xsl:attribute name="space-after">2mm</xsl:attribute>
    <xsl:attribute name="space-before">2mm</xsl:attribute>
    <xsl:attribute name="margin-top">1mm</xsl:attribute>
    <xsl:attribute name="margin-bottom">1mm</xsl:attribute>
    <xsl:attribute name="margin-left">1mm</xsl:attribute>
    <xsl:attribute name="margin-right">1mm</xsl:attribute>    
  </xsl:attribute-set>
  

  <!-- Fechas, Ruts y Montos-->	
  <xsl:attribute-set name="columm-w-corto">
    <xsl:attribute name="column-width">15mm</xsl:attribute>
  </xsl:attribute-set>
  
  <!-- Campos como numero serie Cheque, etc. -->
  <xsl:attribute-set name="columm-w-medio">
    <xsl:attribute name="column-width">25mm</xsl:attribute>
  </xsl:attribute-set>
  
  <!-- En caso que "auto" no corresponda -->
  <xsl:attribute-set name="columm-w-largo">
    <xsl:attribute name="column-width">60mm</xsl:attribute>
  </xsl:attribute-set>
  

  <!-- Bordes para impresion de Cheques, para ver la lineas de referencia mientras se configuran las posiciones de los atributos
  	   Comentar para produccion -->	
  <xsl:attribute-set name="bordes_cheque">
    <!-- xsl:attribute name="border-right-style">solid</xsl:attribute>
    <xsl:attribute name="border-left-style">solid</xsl:attribute>
    <xsl:attribute name="border-bottom-style">solid</xsl:attribute>
    <xsl:attribute name="border-top-style">solid</xsl:attribute-->
  </xsl:attribute-set>
 
  <!-- Inserta la imagen del Logo del INP -->	
  <xsl:template match="Logo">
		<fo:block>
			<fo:external-graphic height="10mm" width="10mm" src="{.}"/>
		</fo:block>
   </xsl:template>
  
  <!--Fecha formateada dd/mm/aaaa -->
  <xsl:template name="format-date">
    <xsl:param name="date"/>
    <xsl:param name="format" select="0"/>
	<xsl:if test="$date!= ''">
	    <xsl:variable name="day" select="substring($date,9,2)"/>
	    <xsl:variable name="month" select="substring($date,6,2)"/>
	    <xsl:variable name="year" select="substring($date,1,4)"/>
	    <xsl:value-of select="concat($day, '/', $month, '/', $year)"/>  
    </xsl:if>
  </xsl:template>
  
  <!--Hora de una Fecha formateada hh:mm -->
  <xsl:template name="format-time">
    <xsl:param name="date"/>
    <xsl:param name="format" select="0"/>
	<xsl:if test="$date!= ''">
	    <xsl:variable name="hour" select="substring($date,12,5)"/>
	    
	    <xsl:value-of select="$hour"/>  
    </xsl:if>
  </xsl:template>
  
  <!--Fecha formateada dd/mm/aaaa hh:mm:ss -->
  <xsl:template name="format-date-time">
    <xsl:param name="date"/>
    <xsl:param name="format" select="0"/>
	<xsl:if test="$date!= ''">
	    <xsl:variable name="day" select="substring($date,9,2)"/>
	    <xsl:variable name="month" select="substring($date,6,2)"/>
	    <xsl:variable name="year" select="substring($date,1,4)"/>
	    
	    <xsl:variable name="hour" select="substring($date,12,8)"/>
	    
	    <xsl:value-of select="concat($day, '/', $month, '/', $year, ' ', $hour)"/>  
    </xsl:if>
  </xsl:template>
  
  <!--Fecha formateada dd de "mes en letras" de aaaa -->
  <xsl:template name="format-date-letras">
    <xsl:param name="date"/>
    <xsl:param name="format" select="0"/>
	<xsl:if test="$date!= ''">
	    <xsl:variable name="day" select="substring($date,9,2)"/>
	    <xsl:variable name="month" select="substring($date,6,2)"/>
	    <xsl:variable name="year" select="substring($date,1,4)"/>
	    
	    <xsl:choose>
          <xsl:when test="1=$month">
				<xsl:value-of select="concat($day, ' de enero de ', $year)"/>  
          </xsl:when>
          
          <xsl:when test="2=$month">
				<xsl:value-of select="concat($day, ' de febrero de ', $year)"/>  
          </xsl:when>

          <xsl:when test="3=$month">
				<xsl:value-of select="concat($day, ' de marzo de ', $year)"/>  
          </xsl:when>

          <xsl:when test="4=$month">
				<xsl:value-of select="concat($day, ' de abril de ', $year)"/>  
          </xsl:when>

          <xsl:when test="5=$month">
				<xsl:value-of select="concat($day, ' de mayo de ', $year)"/>  
          </xsl:when>

          <xsl:when test="6=$month">
				<xsl:value-of select="concat($day, ' de junio de ', $year)"/>  
          </xsl:when>

          <xsl:when test="7=$month">
				<xsl:value-of select="concat($day, ' de julio de ', $year)"/>  
          </xsl:when>

          <xsl:when test="8=$month">
				<xsl:value-of select="concat($day, ' de agosto de ', $year)"/>  
          </xsl:when>

          <xsl:when test="9=$month">
				<xsl:value-of select="concat($day, ' de septiembre de ', $year)"/>  
          </xsl:when>

          <xsl:when test="10=$month">
				<xsl:value-of select="concat($day, ' de octubre de ', $year)"/>  
          </xsl:when>

          <xsl:when test="11=$month">
				<xsl:value-of select="concat($day, ' de noviembre de ', $year)"/>  
          </xsl:when>
          
          <xsl:when test="12=$month">
				<xsl:value-of select="concat($day, ' de diciembre de ', $year)"/>
          </xsl:when>
        </xsl:choose>
    </xsl:if>
  </xsl:template>
  
  <!-- Numero de Dia de una Fecha --> 
  <xsl:template name="day-date">
    <xsl:param name="date"/>
    <xsl:param name="format" select="0"/>
	<xsl:if test="$date!= ''">
	    <xsl:variable name="day" select="substring($date,9,2)"/>
	    <xsl:value-of select="$day"/>
    </xsl:if>
  </xsl:template>

  <!-- Año de una Fecha --> 
  <xsl:template name="year-date">
    <xsl:param name="date"/>
    <xsl:param name="format" select="0"/>
	<xsl:if test="$date!= ''">
	    <xsl:variable name="year" select="substring($date,1,4)"/>
	    <xsl:value-of select="$year"/>
    </xsl:if>
  </xsl:template>

  <!-- Nombre del mes de una Fecha --> 	
  <xsl:template name="month-date-letras">
    <xsl:param name="date"/>
    <xsl:param name="format" select="0"/>
	<xsl:if test="$date!= ''">
	    <xsl:variable name="month" select="substring($date,6,2)"/>
	    
	    <xsl:choose>
          <xsl:when test="1=$month">
				<xsl:value-of select="'enero'"/>  
          </xsl:when>
          
          <xsl:when test="2=$month">
				<xsl:value-of select="'febrero'"/>  
          </xsl:when>

          <xsl:when test="3=$month">
				<xsl:value-of select="'marzo'"/>  
          </xsl:when>

          <xsl:when test="4=$month">
				<xsl:value-of select="'abril'"/>  
          </xsl:when>

          <xsl:when test="5=$month">
				<xsl:value-of select="'mayo'"/>  
          </xsl:when>

          <xsl:when test="6=$month">
				<xsl:value-of select="'junio'"/>  
          </xsl:when>

          <xsl:when test="7=$month">
				<xsl:value-of select="'julio'"/>  
          </xsl:when>

          <xsl:when test="8=$month">
				<xsl:value-of select="'agosto'"/>  
          </xsl:when>

          <xsl:when test="9=$month">
				<xsl:value-of select="'septiembre'"/>  
          </xsl:when>

          <xsl:when test="10=$month">
				<xsl:value-of select="'octubre'"/>  
          </xsl:when>

          <xsl:when test="11=$month">
				<xsl:value-of select="'noviembre'"/>  
          </xsl:when>
          
          <xsl:when test="12=$month">
				<xsl:value-of select="'diciembre'"/>
          </xsl:when>
        </xsl:choose>
    </xsl:if>
  </xsl:template>

</xsl:stylesheet>

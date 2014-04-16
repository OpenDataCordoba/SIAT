<?xml version="1.0" encoding="iso-8859-1"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:my="utils">

  <xsl:output method="xml" version="1.0" indent="yes"/>

  <!-- Template para escribir una cadena de espacios en blanco  -->
  <xsl:template name="filler">
    <xsl:param name="largo"/>
    <xsl:value-of select="substring('&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;', 1, $largo)"/>
  </xsl:template>

  <!-- Template para completar una cadena con espacios en blanco a la derecha -->
  <xsl:template name="fillWithBlanksRight">
    <xsl:param name="valor"/>
    <xsl:param name="largo"/>

    <!-- Es el largo de la cadena $valor -->
    <xsl:variable name="largoCadenaValor" select="string-length($valor)"/>
    <xsl:choose>		
      <!-- Si el largo de $valor es menor a $largo lo completa con espacios -->
      <xsl:when test="$largoCadenaValor &lt; $largo"> 
        <xsl:variable name="espaciosCadena" select="substring('&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;', 1, $largo - $largoCadenaValor)"/>
        <xsl:value-of select="concat($valor, $espaciosCadena)"/>
      </xsl:when>
      <!-- Si el largo de $valor es mayor o igual a $largo, lo escribe hasta $largo caracteres -->
      <xsl:otherwise>
        <xsl:value-of select="substring($valor, 1, $largo)"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <!-- Template para completar una cadena con espacios en blanco a la izquierda -->
  <xsl:template name="fillWithBlanksLeft">
    <xsl:param name="valor"/>
    <xsl:param name="largo"/>

    <!-- Es el largo de la cadena $valor -->
    <xsl:variable name="largoCadenaValor" select="string-length($valor)"/>
    <xsl:choose>		
      <!-- Si el largo de $valor es menor a $largo lo completa con espacios -->
      <xsl:when test="$largoCadenaValor &lt; $largo"> 
        <xsl:variable name="espaciosCadena" select="substring('&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;&#32;', 1, $largo - $largoCadenaValor)"/>
        <xsl:value-of select="concat($espaciosCadena, $valor)"/>
      </xsl:when>
      <!-- Si el largo de $valor es mayor o igual a $largo, lo escribe hasta $largo caracteres -->
      <xsl:otherwise>
        <xsl:value-of select="substring($valor, 1, $largo)"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <!-- Template para mostrar un mes en letras -->
  <xsl:template name="mesEnLetras">
    <xsl:param name="mes"/>
	<xsl:choose>		
      <xsl:when test="$mes =  1" >Enero</xsl:when>
      <xsl:when test="$mes =  2" >Febrero</xsl:when>
      <xsl:when test="$mes =  3" >Marzo</xsl:when>
      <xsl:when test="$mes =  4" >Abril</xsl:when>
      <xsl:when test="$mes =  5" >Mayo</xsl:when>
      <xsl:when test="$mes =  6" >Junio</xsl:when>
      <xsl:when test="$mes =  7" >Julio</xsl:when>
	  <xsl:when test="$mes =  8" >Agosto</xsl:when>
	  <xsl:when test="$mes =  9" >Septiembre</xsl:when>
	  <xsl:when test="$mes =  10">Octubre</xsl:when>
	  <xsl:when test="$mes =  11">Noviembre</xsl:when>
	  <xsl:when test="$mes =  12">Diciembre</xsl:when>
    </xsl:choose>
  </xsl:template>

  <xsl:template name="toUpperCase">
	<xsl:param name="input"/>
	<xsl:value-of select="translate($input,'aábcdeéfghiíjklmnñoópqrstuúüvwxyz','AÁBCDEÉFGHIÍJKLMNÑOÓPQRSTUÚÜVWXYZ')" />
  </xsl:template>

</xsl:stylesheet>
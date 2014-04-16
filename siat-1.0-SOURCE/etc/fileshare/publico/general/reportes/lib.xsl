<?xml version="1.0" encoding="iso-8859-1"?>
<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:fo="http://www.w3.org/1999/XSL/Format"
    version="1.0">

  <xsl:output method="xml" version="1.0" indent="yes"/>

<!-- En este archivo se colocan las "funciones" o templates para reutilizar en otros xsl -->

<!--  templata para pasar un texto a MAYUSCULAS -->
<xsl:template name="toUppercase">
   	<xsl:param name="cadena" select="''" />
   	<xsl:value-of select="translate($cadena,'aábcdeéfghiíjklmnñoópqrstuúüvwxyz','AÁBCDEÉFGHIÍJKLMNÑOÓPQRSTUÚÜVWXYZ')" />
</xsl:template>

</xsl:stylesheet>

<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<table class="tablamenu">
	<tr>
   		<th>SIAT &gt; Mensaje: Error durante la impresión</th>
	</tr>
</table>
<!-- Mensajes y/o Advertencias -->
<%@ include file="/base/warning.jsp" %>
<!-- Errors  -->
<html:errors bundle="base"/>
	
<table width="100%" border="0">
	<tr>
		<td align="right">
		  	<html:button property="btnCerrar"  styleClass="boton" onclick="history.back();">
				<bean:message bundle="base" key="abm.button.volver"/>
			</html:button>
		</td>
	</tr>
</table>

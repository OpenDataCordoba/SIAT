<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

	<input type="button" class="boton" onclick="submitForm('enviarArchivos', 
		'<bean:write name="pasoCorridaVO" property="id" bundle="base" formatKey="general.format.id"/>');"
		value="<bean:message bundle="pro" key="pro.abm.button.enviarArchivos"/>"
	/>

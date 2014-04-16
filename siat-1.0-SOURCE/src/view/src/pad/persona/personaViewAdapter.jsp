<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/pad/AdministrarPersona.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="pad" key="pad.personaAdapter.title"/></h1>
		
	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
			
		<!-- Persona -->
		<fieldset>
			<logic:equal name="personaAdapterVO" property="persona.esPersonaFisica" value="true">
				<legend><bean:message bundle="pad" key="pad.persona.title"/></legend>
			</logic:equal>
			<logic:notEqual name="personaAdapterVO" property="persona.esPersonaFisica" value="true">
				<legend><bean:message bundle="pad" key="pad.persona.juridica.title"/></legend>
			</logic:notEqual>
		
		
			
			<!-- Inclusion de los datos de la persona -->
			<bean:define id="personaVO" name="personaAdapterVO" property="persona"/>
			<%@ include file="/pad/persona/includePersona.jsp" %>
		</fieldset>
		<!-- Fin Persona -->

		<table class="tablabotones">
			<tr>				
				<td align="left">
	    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
	   	
		<input type="hidden" name="method" value=""/>
<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
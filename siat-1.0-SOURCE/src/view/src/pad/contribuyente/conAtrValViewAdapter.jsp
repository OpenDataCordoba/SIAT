<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/pad/AdministrarConAtrVal.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				

	<h1><bean:message bundle="pad" key="pad.contribuyenteAdapter.title"/></h1>	

	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>

	<!-- Contribuyente -->
    <fieldset>
    	<legend><bean:message bundle="pad" key="pad.contribuyente.title"/></legend>
        <table class="tabladatos">
			<bean:define id="personaVO" name="conAtrValAdapterVO" property="contribuyente.persona"/>
			<%@ include file="/pad/persona/includePersonaReducida.jsp"%>
		</table>
	</fieldset>
	<fieldset>
		<legend><bean:message bundle="pad" key="pad.conAtrVal.title"/></legend>
		<table class="tabladatos">
			
			<bean:define id="AtrVal" name="conAtrValAdapterVO" property="conAtrDefinition"/>

			<%@ include file="/def/atrDefinition4ViewWithVig.jsp"%>

		</table>
	</fieldset>
	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
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

	<!-- Inclusion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->

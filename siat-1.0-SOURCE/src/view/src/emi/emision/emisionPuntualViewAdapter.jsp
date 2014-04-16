<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/emi/AdministrarEmisionPuntual.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="emi" key="emi.emisionPuntualViewAdapter.title"/></h1>	

	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
	   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
	
	<fieldset>
		<legend><bean:message bundle="emi" key="emi.emision.title"/></legend>
		
		<table class="tabladatos">
			<tr>
				<!-- Recurso -->
				<td><label><bean:message bundle="emi" key="emi.emision.recurso.label"/>: </label></td>
				<td class="normal"><bean:write name="emisionPuntualAdapterVO" property="emision.recurso.desRecurso"/></td>
			</tr>

			<tr>
				<!-- Cuenta -->
				<td><label><bean:message bundle="emi" key="emi.emision.cuenta.label"/>: </label></td>
				<td class="normal"><bean:write name="emisionPuntualAdapterVO" property="emision.cuenta.numeroCuenta"/></td>
			</tr>

			<tr>
				<!-- Fecha Emision -->
				<td><label><bean:message bundle="emi" key="emi.emision.fechaEmision.label"/>: </label></td>
				<td class="normal"><bean:write name="emisionPuntualAdapterVO" property="emision.fechaEmisionView"/></td>
			</tr>

			<tr>
				<!-- Anio -->
				<td><label><bean:message bundle="emi" key="emi.emision.anio.label"/>: </label></td>
				<td class="normal"><bean:write name="emisionPuntualAdapterVO" property="emision.anioView"/></td>
			</tr>

			<tr>
				<!-- Periodo Desde -->
				<td><label><bean:message bundle="emi" key="emi.emision.periodoDesde.label"/>: </label></td>
				<td class="normal"><bean:write name="emisionPuntualAdapterVO" property="emision.periodoDesdeView"/></td>

				<!-- Periodo Hasta -->
				<td><label><bean:message bundle="emi" key="emi.emision.periodoHasta.label"/>: </label></td>
				<td class="normal"><bean:write name="emisionPuntualAdapterVO" property="emision.periodoHastaView"/></td>
			</tr>
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
<!-- emisionPuntualViewAdapter.jsp -->
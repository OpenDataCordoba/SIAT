<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- formulario unico -->
<html:form styleId="filter" action="/gde/AdministrarCobranza.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
 	<h1><bean:message bundle="gde" key="gde.cobranzaAsignarAdapter.title"/></h1>
	<p align="right">
		<button type="button" name="btn2" class="boton" onclick="submitForm('volver','');"><bean:message bundle="base" key="abm.button.volver"/></button>
	</p>
	 

	<!-- Cobranza -->
		<fieldset>
			<legend><bean:message bundle="gde" key="gde.cobranzaAdapter.legend"/></legend>
			<table class="tabladatos">
				<tr>
					<td><label><bean:message bundle="pad" key="pad.contribuyente.label"/>: </label></td>
					<td class="normal"><bean:write name="cobranzaAdapterVO" property="cobranza.contribuyente.persona.view"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="gde" key="gde.cobranzaAdapter.ordenControl.label"/>: </label></td>
					<td class="normal"><bean:write name="cobranzaAdapterVO" property="cobranza.ordenControl.ordenControlyTipoView"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="gde" key="gde.cobranza.fechaInicio.label"/>: </label></td>
					<td class="normal"><bean:write name="cobranzaAdapterVO" property="cobranza.fechaInicioView"/></td>
					<td><label><bean:message bundle="gde" key="gde.cobranza.fechaFin.label"/>: </label></td>
					<td class="normal"><bean:write name="cobranzaAdapterVO" property="cobranza.fechaFinView"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="gde" key="gde.cobranza.estadoCobranza.label"/>: </label></td>
					<td class="normal"><bean:write name="cobranzaAdapterVO" property="cobranza.estadoCobranza.desEstado"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="gde" key="gde.cobranza.importeACobrar.label"/></label></td>
					<td class="normal"><bean:write name="cobranzaAdapterVO" property="cobranza.importeACobrarView"/></td>
					<td><label><bean:message bundle="gde" key="gde.cobranza.impPagGes.label"/>: </label></td>
					<td class="normal"><bean:write name="cobranzaAdapterVO" property="cobranza.impPagGesView"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="gde" key="gde.cobranza.proFecCon.label"/>: </label></td>
					<td class="normal"><bean:write name="cobranzaAdapterVO" property="cobranza.proFecConView"/></td>
				</tr>
			</table>
		</fieldset>
		<fieldset>
			<legend><bean:message bundle="gde" key="gde.cobranzaAsignarAdapter.asignacion.label"/></legend>
				<table class="tabladatos">
			    	<tr>
				    	<td><label><bean:message bundle="gde" key="gde.cobranaAsignarAdapter.asignarA.label"/>: </label></td>
				    	<td class="normal">
				    		<html:select name="cobranzaAdapterVO" property="cobranza.perCob.id">
				    			<html:optionsCollection name="cobranzaAdapterVO" property="listPerCob" label="nombreApellido" value="id"/>
				    		</html:select>
				    	</td>
				    </tr>           
				</table>
		</fieldset>
		<p align="right">
			<html:button property="btnAsignar" styleClass="boton" onclick="submitForm('asignarGesCob','');">
				<bean:message bundle="gde" key="gde.cobranzaAsignaAdapter.asignar.button"/>
			</html:button>
		</p>
		
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
		
	<!-- Inclusion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>

</html:form>
<!-- Fin formulario -->
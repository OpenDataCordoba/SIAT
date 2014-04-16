<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/gde/AdministrarSalPorCadMasivo.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="gde" key="gde.salPorCadMasivoAdminAdapter.title"/></h1>	
	<table class="tablabotones" width="100%">
		<tr>
			<td align="right"><html:button property="btnVolver"
				styleClass="boton" onclick="submitForm('volver', '');">
				<bean:message bundle="base" key="abm.button.volver" />
			</html:button></td>
		</tr>
	</table>
	<!-- Saldo Por Caducidad -->
	<fieldset>
		<legend><bean:message bundle="gde" key="gde.salPorCadMasivoAdminAdapter.saldo.legend"/></legend>
		
			<table class="tabladatos">
				<!-- combo Recurso -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.salPorCad.fechaSalCad"/></td>
					<td class="normal"><bean:write name="salPorCadMasivoAdministrarAdapterVO" property="saldoPorCaducidad.fechaSaldoView"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
					<td class="normal" colspan="3">
						<bean:write name="salPorCadMasivoAdministrarAdapterVO" property="saldoPorCaducidad.recurso.desRecurso"/>
					</td>
				</tr>
				<!-- combo Plan -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.plan.label"/>: </label></td>				
					<td class="normal" colspan="3">
						<bean:write name="salPorCadMasivoAdministrarAdapterVO" property="saldoPorCaducidad.plan.desPlan"/>
					</td>
				</tr>
				<!-- fechaDesde y fechaHasta -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.salPorCadMasivoAdapter.fechaForDesde"/>: </label></td>
					<td class="normal">
						<bean:write name="salPorCadMasivoAdministrarAdapterVO" property="saldoPorCaducidad.fechaFormDesdeSaldoView"/>
					</td>
					<td><label><bean:message bundle="gde" key="gde.salPorCadMasivoAdapter.fechaForHasta"/>: </label></td>
					<td class="normal">
						<bean:write name="salPorCadMasivoAdministrarAdapterVO" property="saldoPorCaducidad.fechaFormHastaSaldoView"/>
					</td>
				</tr>
				<!-- Cuota Superior A -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.salPorCadMasivoAdapter.montoSuperior"/></td>
					<td class="normal"><bean:write name="salPorCadMasivoAdministrarAdapterVO" property="saldoPorCaducidad.cuotaSuperiorAView"/></td>
				</tr>
				<!-- Caso -->
				<tr>
					<td><label><bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
					<td colspan="3">
						<bean:define id="IncludedVO" name="salPorCadMasivoAdministrarAdapterVO" property="saldoPorCaducidad"/>
						<%@ include file="/cas/caso/includeCasoView.jsp" %>				
					</td>
				</tr>
				<!-- Observacion -->				
				<tr>
					<td><label><bean:message bundle="gde" key="gde.salPorCadMasivoAdapter.observacion"/>: </label></td>
					<td class="normal" colspan="3">	
						<bean:write name="salPorCadMasivoAdministrarAdapterVO" property="saldoPorCaducidad.observacion"/>
					</td>
				</tr>
			</table>
	</fieldset>	
	<fieldset>
		<legend><bean:message bundle="gde" key="gde.salPorCadMasivoAdminAdapter.legend"/></legend>
		<table>
			<tr>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td><label><bean:message bundle="gde" key="gde.salPorCadMasivoAdminAdapter.estado.label"/></label>
				<td class="normal"><bean:write name="salPorCadMasivoAdministrarAdapterVO" property="saldoPorCaducidad.corrida.estadoCorrida.desEstadoCorrida"/></td>
			</tr>
			<tr><td>&nbsp;</td></tr>
			<tr>
				<td>&nbsp;</td>
			</tr>
		</table>
		<p align="right"><html:button property="btnActivar" styleClass="boton" onclick="submitForm('activar','');">
						<bean:message bundle="base" key="abm.button.alta"/>
					</html:button>
		</p>
		</table>
	</fieldset>
	<!-- Rescate -->
	
			<p align="left">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</p>
	
	<input type="hidden" name="method" value=""/>
	<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
	<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="isSubmittedForm" value="true"/>

	<!-- Inclucion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>
	
</html:form>
<!-- Fin Tabla que contiene todos los formularios -->
<%@ taglib uri="/tags/struts-bean" prefix="bean"%>
<%@ taglib uri="/tags/struts-html" prefix="html"%>
<%@ taglib uri="/tags/struts-logic" prefix="logic"%>

<!-- formulario unico -->
<html:form styleId="filter" action="/gde/AdministrarDesgloseAjuste.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp"%>
	<!-- Errors  -->
	<html:errors bundle="base" />

	<!-- Para la web lo muestro, para usuario Interno NO -->
	<logic:equal name="userSession" property="isAnonimoView" value="1">
		<%@ include file="/gde/gdeuda/includeDivButtons.jsp"%>
	</logic:equal>


	<h1><bean:message bundle="gde"
		key="gde.desgloseAjusteAdapter.title" /></h1>

	<table class="tablabotones" width="100%">
		<tr>
			<td align="left">
			<p>Permite realizar un desglose de un Ajuste Fiscal en un
			registro correspondiente a Capital (Importe Histórico), el cual no se
			actualiza y otro registro de deuda donde se capitalizan los intereses
			devengados a una fecha límite, pudiéndo a partir de esta fecha
			devengar nuevos intereses.</p>
			</td>
			<td align="right">
			<button type="button" name="btnVolver" class="boton"
				onclick="submitForm('volverACuenta', '<bean:write name="desgloseAjusteAdapterVO" property="cuenta.idCuenta" bundle="base" formatKey="general.format.id"/>');">
			<bean:message bundle="base" key="abm.button.volver" /></button>
			</td>
		</tr>
	</table>
	<!-- LiqCuenta -->
	<bean:define id="DeudaAdapterVO" name="desgloseAjusteAdapterVO" />
	<%@ include file="/gde/gdeuda/includeLiqCuenta.jsp"%>
	<!-- LiqCuenta -->

	<!-- listDeuda -->
	<logic:notEmpty name="desgloseAjusteAdapterVO" property="listDeuda">
		<div class="horizscroll">
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1"
			width="100%">
			<caption><bean:message bundle="gde"
				key="gde.liqFormConvenioAdapter.listaDeudas.title" /></caption>
			<tbody>
				<tr>
					<th align="left"><bean:message bundle="gde"
						key="gde.desgloseAjusteAdapter.liqDeuda.periodoDeuda" /></th>
					<th align="left"><bean:message bundle="gde"
						key="gde.desgloseAjusteAdapter.liqDeuda.importe" /></th>
					<th align="left"><bean:message bundle="gde"
						key="gde.desgloseAjusteAdapter.liqDeuda.fechaVencimiento" /></th>
					<th align="left"><bean:message bundle="gde"
						key="gde.desgloseAjusteAdapter.liqDeuda.saldo" /></th>
					<th align="left"><bean:message bundle="gde"
						key="gde.desgloseAjusteAdapter.liqDeuda.actualizacion" /></th>
				</tr>

				<!-- Item LiqDeudaVO -->
				<logic:iterate id="LiqDeudaVO" name="desgloseAjusteAdapterVO"
					property="listDeuda">
					<tr>
						<td><bean:write name="LiqDeudaVO" property="periodoDeuda" />&nbsp;</td>
						<td><bean:write name="LiqDeudaVO" property="importe"
							bundle="base" formatKey="general.format.currency" />&nbsp;</td>
						<td><bean:write name="LiqDeudaVO" property="fechaVto" />&nbsp;</td>
						<td><bean:write name="LiqDeudaVO" property="saldo"
							bundle="base" formatKey="general.format.currency" />&nbsp;</td>
						<td><bean:write name="LiqDeudaVO" property="actualizacion"
							bundle="base" formatKey="general.format.currency" />&nbsp;</td>
					</tr>
				</logic:iterate>

			</tbody>
		</table>
		</div>
	</logic:notEmpty>
	<!-- Fin listDeuda -->

	<!-- Fecha Limite -->
	<fieldset><legend><bean:message bundle="gde"
		key="gde.desgloseAjusteAdapter.datosDesglose.title" /></legend>
	<table class="tabladatos">
		<tr>
			<td><label><bean:message bundle="gde" key="gde.desgloseAjusteAdapter.fechaLimite.label" />:</label></td> 
			<td class="normal">
				<html:text name="desgloseAjusteAdapterVO" property="fechaLimiteView" styleId="fechaLimiteView" styleClass="datos" size="8" /> 
				<a class="link_siat" onclick="return show_calendar('fechaLimiteView');"id="a_fechaLimiteView"> <img border="0" src="<%= request.getContextPath()%>/images/calendario.gif" /></a>
			</td>
		</tr>
			<!-- Fin Fecha Limite -->

			<!-- Observacion -->
		<tr>
			<td><label><bean:message bundle="gde" key="gde.desgloseAjusteAdapter.observacion.label" />:</label></td>
			<td class="normal"><html:textarea
             	name="desgloseAjusteAdapterVO" property="observacion" cols="80"
				rows="15" />
			</td>
		</tr>
		<!-- Observacion -->
     
		<!-- Inclucion de Caso -->
		<tr>
			<td><label><bean:message bundle="cas" key="cas.caso.label" />: </label></td>
			<td class="normal">
			<bean:define id="IncludedVO" name="desgloseAjusteAdapterVO" property="desglose" /> 
			<bean:define id="voName" value="desglose" /> 
			<%@ include file="/cas/caso/includeCaso.jsp"%>
			<td>
		</tr>
	</table>
    
	</fieldset>


	<!--  boton Desglosar Ajuste -->
	<p align="right">
	<button type="button" name="btnDesgloseAjuste" class="boton"
		onclick="submitForm('desglosar', '');"><bean:message
		bundle="gde" key="gde.desgloseAjusteAdapter.button.desglosar" /></button>
	</p>

	<!--  FIN boton Desglosar Ajuste  -->

	<!-- Volver -->
	<button type="button" name="btnVolver" class="boton"
		onclick="submitForm('volverACuenta', '<bean:write name="desgloseAjusteAdapterVO" property="cuenta.idCuenta" bundle="base" formatKey="general.format.id"/>');">
	<bean:message bundle="base" key="abm.button.volver" /></button>

	<input type="hidden" name="method" value="" />
	<input type="hidden" name="anonimo"
		value="<bean:write name="userSession" property="isAnonimoView"/>" />
	<input type="hidden" name="urlReComenzar"
		value="<bean:write name="userSession" property="urlReComenzar"/>" />
	<input type="hidden" name="selectedId" value="" />
	<input type="hidden" name="isSubmittedForm" value="true" />

	<!-- Inclusion del Codigo Javascript del Calendar-->
	<div id="calendarDiv"
		style="position: absolute; visibility: hidden; background-	color: white; layer-background-color: white;"></div>
</html:form>
<!-- Fin formulario -->
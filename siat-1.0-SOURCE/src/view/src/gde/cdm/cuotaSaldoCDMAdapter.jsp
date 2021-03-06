<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- formulario unico -->
<html:form styleId="filter" action="/gde/AdministrarCambioPlanCDM.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				

	<h1><bean:message bundle="rec" key="rec.cuotaSaldoCDMAdapter.title"/></h1>
	
	<!-- LiqCuenta -->
		<bean:define id="DeudaAdapterVO" name="cambioPlanCDMAdapterVO"/>
		<%@ include file="/gde/gdeuda/includeLiqCuenta.jsp" %>
	<!-- LiqCuenta -->
	
	<fieldset>
		<legend><bean:message bundle="rec" key="rec.cambioPlanCDMAdapter.obra.label"/></legend>
		
		<table class="tabladatos">
			<tr>
				<td><label><bean:message bundle="rec" key="rec.cambioPlanCDMAdapter.obra.label"/>: </label></td>
				<td class="normal" colspan="3"><bean:write name="cambioPlanCDMAdapterVO" property="plaCuaDet.planillaCuadra.obra.desObra"/></td>
			</tr>
			<tr>	
				<td><label><bean:message bundle="rec" key="rec.cambioPlanCDMAdapter.plan.label"/>: </label></td>
				<td class="normal" colspan="3"><bean:write name="cambioPlanCDMAdapterVO" property="plaCuaDet.obrForPag.desFormaPago"/></td>
			</tr>
			
			<tr>
				<td><label><bean:message bundle="rec" key="rec.cambioPlanCDMAdapter.totalObra.label"/>: </label></td>
				<td class="normal" colspan="3"><bean:write name="cambioPlanCDMAdapterVO" property="plaCuaDet.importeTotal" bundle="base" formatKey="general.format.currency"/></td>				
			</tr>

			<tr>
				<td><label><bean:message bundle="rec" key="rec.cambioPlanCDMAdapter.capitalCancelado.label"/>: </label></td>
				<td class="normal"><bean:write name="cambioPlanCDMAdapterVO" property="capitalCancelado" bundle="base" formatKey="general.format.currency"/></td>
				
				<td><label><bean:message bundle="rec" key="rec.cambioPlanCDMAdapter.interesCancelado.label"/>: </label></td>
				<td class="normal"><bean:write name="cambioPlanCDMAdapterVO" property="interesCancelado" bundle="base" formatKey="general.format.currency"/></td>
			</tr>
		
			<tr>
				<td><label><bean:message bundle="rec" key="rec.cambioPlanCDMAdapter.totalPendiente.label"/>: </label></td>
				<td class="normal" colspan="3"><bean:write name="cambioPlanCDMAdapterVO" property="totalPendiente" bundle="base" formatKey="general.format.currency"/></td>
			</tr>
		</table>
	</fieldset>
	
	<!-- List Planes -->
	<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
		<caption><bean:message bundle="rec" key="rec.cuotaSaldoCDMAdapter.planContado.label"/></caption>
    	<tbody>
			<logic:notEmpty  name="cambioPlanCDMAdapterVO" property="listPlanes">
		    	<tr>
					<th align="left"><bean:message bundle="rec" key="rec.cambioPlanCDMAdapter.planes.descripcion.label"/></th>
					<th align="left"><bean:message bundle="rec" key="rec.cambioPlanCDMAdapter.planes.montoCuota.label"/></th>							
					<th align="left"><bean:message bundle="rec" key="rec.cambioPlanCDMAdapter.planes.montoTotal.label"/></th>
				</tr>
				<logic:iterate id="ObraFormaPagoVO" name="cambioPlanCDMAdapterVO" property="listPlanes">
					<tr>
						<td><bean:write name="ObraFormaPagoVO" property="desFormaPago" />&nbsp;</td>
						<td><bean:write name="ObraFormaPagoVO" property="montoMinimoCuota"  bundle="base" formatKey="general.format.currency"/>&nbsp;</td>								
						<td><bean:write name="ObraFormaPagoVO" property="montoTotal"  bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
					</tr>
				</logic:iterate>
			</logic:notEmpty>
			<logic:empty  name="cambioPlanCDMAdapterVO" property="listPlanes">
				<tr><td align="center">
					<bean:message bundle="rec" key="rec.cambioPlanCDMAdapter.noPlanesDisponibles"/>
				</td></tr>
			</logic:empty>					
		</tbody>
	</table>
	<!-- List Planes -->

	<!-- Volver -->
	<p align="center">
		<button type="button" name="btnAceptar" class="boton" onclick="submitForm('generarCuotaSaldoCDM', '<bean:write name="cambioPlanCDMAdapterVO" property="cuenta.idCuenta" bundle="base" formatKey="general.format.id"/>');">
  	    	<bean:message bundle="base" key="abm.button.aceptar"/>
		</button>				
	</p>
	
	<button type="button" name="btnVolver" class="boton" onclick="submitForm('volverACuenta', '<bean:write name="cambioPlanCDMAdapterVO" property="cuenta.idCuenta" bundle="base" formatKey="general.format.id"/>');">
  	    <bean:message bundle="base" key="abm.button.volver"/>
	</button>

	<input type="hidden" name="method" value=""/>
	<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
	<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>
	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="cuentaId" value="<bean:write name="cambioPlanCDMAdapterVO" property="cuenta.idCuenta" bundle="base" formatKey="general.format.id"/>"/>
	<input type="hidden" name="isSubmittedForm" value="true"/>

</html:form>
<!-- Fin formulario -->

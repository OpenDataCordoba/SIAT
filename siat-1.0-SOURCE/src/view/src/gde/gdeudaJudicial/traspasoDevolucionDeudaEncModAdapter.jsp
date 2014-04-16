<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<logic:equal name="encTraspasoDevolucionDeudaAdapterVO" property="accionTraspasoDevolucion.esTraspaso" value="true">
	<h1><bean:message bundle="gde" key="gde.traspasoDevolucionDeudaAdapter.traspaso.title"/></h1>	

	<!-- TraspasoDeuda -->
	<fieldset>
		<legend><bean:message bundle="gde" key="gde.traspasoDeuda.title"/></legend>
		<table class="tabladatos">
				<!-- Recurso y fecha -->
				<tr>
					<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
					<td class="normal"><bean:write name="encTraspasoDevolucionDeudaAdapterVO" property="traspasoDeuda.recurso.desRecurso"/></td>
					<td><label><bean:message bundle="gde" key="gde.traspasoDeuda.fecha" />: </label></td>
					<td class="normal"><bean:write name="encTraspasoDevolucionDeudaAdapterVO" property="traspasoDeuda.fechaView"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="gde" key="gde.traspasoDevolucionDeudaAdapter.procuradorOrigen.label"/>: </label></td>
					<td class="normal"><bean:write name="encTraspasoDevolucionDeudaAdapterVO" property="traspasoDeuda.proOri.descripcion"/></td>
					<td><label><bean:message bundle="gde" key="gde.traspasoDevolucionDeudaAdapter.procuradorDestino.label"/>: </label></td>
					<td class="normal"><bean:write name="encTraspasoDevolucionDeudaAdapterVO" property="traspasoDeuda.proDes.descripcion"/></td>
				</tr>
				<!-- Cuenta y UsuarioAlta -->
				<tr>
					<td><label><bean:message bundle="pad" key="pad.cuenta.label" />: </label></td>
					<td class="normal"><bean:write name="encTraspasoDevolucionDeudaAdapterVO" property="traspasoDeuda.cuenta.numeroCuenta"/></td>
					<td><label><bean:message bundle="gde" key="gde.traspasoDeuda.usuarioAlta.label" />: </label></td>
					<td class="normal"><bean:write name="encTraspasoDevolucionDeudaAdapterVO" property="traspasoDeuda.usuarioAlta"/></td>					
				</tr>
				<!-- PlaEnvDeuProDest -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.traspasoDeuda.plaEnvDeuProDest.label"/>: </label></td>
					<td class="normal" colspan="3">	<bean:write name="encTraspasoDevolucionDeudaAdapterVO" property="traspasoDeuda.plaEnvDeuProDest.nroBarraAnioPlanillaView"/></td>
				</tr>
				
				<!-- Observacion -->
				<tr>
					<td><label><bean:message bundle="gde" key="gde.traspasoDevolucionDeudaAdapter.observacion"/>: </label></td>
					<td class="normal" colspan="3">	
						<html:textarea name="encTraspasoDevolucionDeudaAdapterVO" property="traspasoDeuda.observacion" cols="80" rows="15"/>
					</td>
				</tr>
			</table>
		</fieldset>
</logic:equal>
	
<logic:equal name="encTraspasoDevolucionDeudaAdapterVO" property="accionTraspasoDevolucion.esDevolucion" value="true">
	<h1><bean:message bundle="gde" key="gde.traspasoDevolucionDeudaAdapter.devolucion.title"/></h1>	

	<!-- DevolucionDeuda -->
	<fieldset>
		<legend><bean:message bundle="gde" key="gde.devolucionDeuda.title"/></legend>
		<table class="tabladatos">
			<!-- Recurso y Fecha -->
			<tr>
				<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
				<td class="normal" ><bean:write name="encTraspasoDevolucionDeudaAdapterVO" property="devolucionDeuda.recurso.desRecurso"/></td>
				<td><label><bean:message bundle="gde" key="gde.devolucionDeuda.fecha" />: </label></td>
				<td class="normal"><bean:write name="encTraspasoDevolucionDeudaAdapterVO" property="devolucionDeuda.fechaView"/></td>
			</tr>
			<!-- Procurador -->
			<tr>
				<td><label><bean:message bundle="gde" key="gde.devolucionDeuda.procurador"/>: </label></td>
				<td class="normal" ><bean:write name="encTraspasoDevolucionDeudaAdapterVO" property="devolucionDeuda.procurador.descripcion"/></td>
			</tr>
			<!-- Cuenta y UsuarioAlta -->
			<tr>
				<td><label><bean:message bundle="pad" key="pad.cuenta.label" />: </label></td>
				<td class="normal"><bean:write name="encTraspasoDevolucionDeudaAdapterVO" property="devolucionDeuda.cuenta.numeroCuenta"/></td>
				<td><label><bean:message bundle="gde" key="gde.traspasoDeuda.usuarioAlta.label" />: </label></td>
				<td class="normal"><bean:write name="encTraspasoDevolucionDeudaAdapterVO" property="devolucionDeuda.usuarioAlta"/></td>					
			</tr>
			<!-- Observacion -->
			<tr>
				<td><label><bean:message bundle="gde" key="gde.traspasoDevolucionDeudaAdapter.observacion"/>: </label></td>
				<td class="normal" colspan="3">	
					<html:textarea name="encTraspasoDevolucionDeudaAdapterVO" property="devolucionDeuda.observacion" cols="80" rows="15"/>
				</td>
			</tr>
		</table>
	</fieldset>
</logic:equal>
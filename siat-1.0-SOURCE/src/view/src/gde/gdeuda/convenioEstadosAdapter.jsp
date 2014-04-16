<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- formulario unico -->
<html:form styleId="filter" action="/gde/AdministrarLiqConvenioCuenta.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				

	<!-- Para la web lo muestro, para usuario Interno NO -->
	<logic:equal name="userSession" property="isAnonimoView" value="1">
		<%@ include file="/gde/gdeuda/includeDivButtons.jsp" %>
	</logic:equal>	
	
	<h1><bean:message bundle="gde" key="gde.convenioEstadosAdapter.title"/></h1>	

<table class="tablabotones" width="100%">
		<tr>
			<td align="left">
				<p>En esta pantalla se muestran los distintos cambios de estados del convenio </p>
			</td>				
			<td align="right">
	 			<button type="button" name="btnVolver" class="boton" onclick="submitForm('verConvenio', '<bean:write name="convenioEstadosAdapterVO" property="convenio.idConvenio" bundle="base" formatKey="general.format.id"/>');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</button>
			</td>
		</tr>
	</table>
	
	
	<!-- LiqConvenio -->
	<fieldset>
		<legend><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenio.title"/> </legend>
			
			<!-- Nro Convenio -->
			<p>
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenio.nroConvenio.label"/>:</label>
	      		<bean:write name="convenioEstadosAdapterVO" property="convenio.nroConvenio"/>
			</p>
			
			<!-- Plan de Pago -->
			<p>
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenio.planPago.label"/>:</label>
	      		<bean:write name="convenioEstadosAdapterVO" property="convenio.desPlan"/>
	      	</p>
	      	<logic:equal name="convenioEstadosAdapterVO" property="convenio.poseeCaso" value="true" >
	      		<p>	
					<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenio.caso.label"/>:</label>
					<bean:write name="convenioEstadosAdapterVO" property="convenio.caso.sistemaOrigen.desSistemaOrigen"/>
					&nbsp;							
					<bean:write name="convenioEstadosAdapterVO" property="convenio.caso.numero"/>	
				</p>
	      	</logic:equal>
			<!-- Via Deuda -->
			<p>
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenio.viaDeuda.label"/>:</label>
	      		<bean:write name="convenioEstadosAdapterVO" property="convenio.desViaDeuda"/>
			</p>
			<logic:equal name="convenioEstadosAdapterVO" property="convenio.poseeProcurador" value="true" >
				<p>	
					<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenio.procurador.label"/>:</label>
		      		<bean:write name="convenioEstadosAdapterVO" property="convenio.procurador.idView"/> -
		      		<bean:write name="convenioEstadosAdapterVO" property="convenio.procurador.descripcion"/>
				</p>
	      	</logic:equal>
			
			<p>
			<!-- Estado Convenio -->
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenio.estadoConvenio.label"/>:</label>
	      		<bean:write name="convenioEstadosAdapterVO" property="convenio.desEstadoConvenio"/>
			</p>
			<!-- Cantidad Cuotas -->
			<p>
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenio.cantCuotas.label"/>:</label>
	      		<bean:write name="convenioEstadosAdapterVO" property="convenio.canCuotasPlan"/>
			</p>
			<!-- Total Conveniado -->
			<p>
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenio.totalImporteConvenido.label"/>:</label>
	      		<bean:write name="convenioEstadosAdapterVO" property="convenio.totImporteConvenio" bundle="base" formatKey="general.format.currency"/>
			</p>
				
	</fieldset>
	<!-- LiqConvenio -->	
	
	<!-- Datos de Formalizacion -->
	<fieldset>
		<legend><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenioFor.title"/> </legend>
			<!-- Fecha Formalizacion -->
			<p>
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenioFor.fechaFor.label"/>:</label>
	      		<bean:write name="convenioEstadosAdapterVO" property="convenio.fechaFor"/>
			</p>
			
			<logic:equal name="convenioEstadosAdapterVO" property="convenio.poseeDatosPersona" value="true" >
				<!-- Apellido -->
				<p>
		      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenioFor.apellido.label"/>:</label>
		      		<bean:write name="convenioEstadosAdapterVO" property="convenio.persona.apellido"/>
				<!-- Nombre -->
		      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenioFor.nombre.label"/>:</label>
		      		<bean:write name="convenioEstadosAdapterVO" property="convenio.persona.nombres"/>
				</p>
				<!-- Apellido Materno -->
				<p>
		      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenioFor.apellidoMaterno.label"/>:</label>
		      		<bean:write name="convenioEstadosAdapterVO" property="convenio.persona.apellidoMaterno"/>
				<!-- Tipo y Nro Doc -->
		      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenioFor.tipoNroDoc.label"/>:</label>
		      		<bean:write name="convenioEstadosAdapterVO" property="convenio.persona.documento.tipoyNumeroView"/>
				</p>
			</logic:equal>
			
			<logic:notEqual name="convenioEstadosAdapterVO" property="convenio.poseeDatosPersona" value="true" >
				<p>
					<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenioFor.sinDatosPerFor"/></label>
				</p>				
			</logic:notEqual>
			
			<!-- Tipo Per For -->
			<p>
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenioFor.desTipoPerFor.label"/>:</label>
	      		<bean:write name="convenioEstadosAdapterVO" property="convenio.tipoPerFor.desTipoPerFor"/>
			<!-- Domicilio -->
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenioFor.desDomicilioParticular.label"/>:</label>
	      		<bean:write name="convenioEstadosAdapterVO" property="convenio.persona.domicilio.view"/>
			</p>
			
			<!-- Telefono -->
			<p>
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenioFor.telefono.label"/>:</label>
	      		<bean:write name="convenioEstadosAdapterVO" property="convenio.persona.telefono"/>
			<!-- Tipo Doc Aportada -->
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenioFor.desTipoDocApo.label"/>:</label>
	      		<bean:write name="convenioEstadosAdapterVO" property="convenio.tipoDocApo.desTipoDocApo"/>
			</p>
			
			<!-- Obs -->
			<p>
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenioFor.observacionFor.label"/>:</label>
	      		<bean:write name="convenioEstadosAdapterVO" property="convenio.observacionFor"/>
			</p>
			<p>
			<!-- Agente Interviniente -->
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenioFor.ususarioFor.label"/>:</label>
	      		<bean:write name="convenioEstadosAdapterVO" property="convenio.ususarioFor"/>
			</p>

	</fieldset>
	<!-- Datos de Formalizacion -->
	
	<fieldset>
		<legend><bean:message bundle="gde" key="gde.convenioEstadosAdapter.title"/></legend>
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
			<tr>
				<th><bean:message bundle="gde" key="gde.convenioEstadosAdapter.fechaCambio.label"/></th>
				<th><bean:message bundle="gde" key="gde.convenioEstadosAdapter.desEstado.label"/></th>
				<th><bean:message bundle="gde" key="gde.convenioEstadosAdapter.observacion.label"/></th>
				<th><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenio.caso.label"/></th>
				<th><bean:message bundle="gde" key="gde.convenioEstadosAdapter.usuario.label"/></th>
			</tr>
			<logic:iterate name="convenioEstadosAdapterVO" property="listConEstCon" id="conEstCon">
				<tr>
					<td><bean:write name="conEstCon" property="fechaConEstConView"/>&nbsp;</td>
					<td><bean:write name="conEstCon" property="desEstado"/>&nbsp;</td>
					<td><bean:write name="conEstCon" property="observacion"/>&nbsp;</td>
					<td>
						<bean:write name="conEstCon" property="caso.sistemaOrigen.desSistemaOrigen"/> &nbsp; 
						<bean:write name="conEstCon" property="caso.numero"/>
					</td>
					<td><bean:write name="conEstCon" property="usuario"/>&nbsp;</td>
				</tr>
			</logic:iterate>
		</table>
				
	</fieldset>

	
	<button type="button" name="btnVolver" class="boton" onclick="submitForm('verConvenio', '<bean:write name="convenioEstadosAdapterVO" property="convenio.idConvenio" bundle="base" formatKey="general.format.id"/>');">
			<bean:message bundle="base" key="abm.button.volver"/>
	</button>
	
	<input type="hidden" name="method" value=""/>
	<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
	<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>
	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="isSubmittedForm" value="true"/>

</html:form>
<!-- Fin formulario -->
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- formulario unico -->
<html:form styleId="filter" action="/gde/AdministrarLiqConvenioCuenta.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				

	<h1><bean:message bundle="gde" key="gde.liqConvenioPagoCuenta.title"/></h1>	
	
	
	<table class="tablabotones" width="100%">
		<tr>
			<td align="left">
				<p>En esta pantalla se muestra el detalle de un convenio de Pago para la Aplicación de Pagos a Cuenta. </p>
			</td>				
			<td align="right">
	 			<button type="button" name="btn2" class="boton" onclick="submitForm('verConvenio','<bean:write name="liqConvenioPagoCuentaAdapterVO" property="convenio.idConvenio" bundle="base" formatKey="general.format.id"/>');">Volver</button>
			</td>
		</tr>
	</table>
	<!-- LiqCuenta -->
		<bean:define id="DeudaAdapterVO" name="liqConvenioPagoCuentaAdapterVO"/>
		<%@ include file="/gde/gdeuda/includeLiqCuenta.jsp" %>
	<!-- LiqCuenta -->
	
	<!-- LiqConvenio -->
	<fieldset>
		<legend><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenio.title"/> </legend>
			
			<!-- Nro Convenio -->
			<p>
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenio.nroConvenio.label"/>:</label>
	      		<bean:write name="liqConvenioPagoCuentaAdapterVO" property="convenio.nroConvenio"/>
			</p>
			
			<!-- Plan de Pago -->
			<p>
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenio.planPago.label"/>:</label>
	      		<bean:write name="liqConvenioPagoCuentaAdapterVO" property="convenio.desPlan"/>
			</p>
			<!-- Via Deuda -->
			<p>
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenio.viaDeuda.label"/>:</label>
	      		<bean:write name="liqConvenioPagoCuentaAdapterVO" property="convenio.desViaDeuda"/>
			
			<!-- Estado Convenio -->
			
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenio.estadoConvenio.label"/>:</label>
	      		<bean:write name="liqConvenioPagoCuentaAdapterVO" property="convenio.desEstadoConvenio"/>
			</p>
			<!-- Cantidad Cuotas -->
			<p>
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenio.cantCuotas.label"/>:</label>
	      		<bean:write name="liqConvenioPagoCuentaAdapterVO" property="convenio.canCuotasPlan"/>
			</p>
			<!-- Total Conveniado -->
			<p>
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenio.totalImporteConvenido.label"/>:</label>
	      		<bean:write name="liqConvenioPagoCuentaAdapterVO" property="convenio.totImporteConvenio" bundle="base" formatKey="general.format.currency"/>
			</p>
				
	</fieldset>
	<!-- LiqConvenio -->	
	
	<!-- Datos de Formalizacion -->
	<fieldset>
		<legend><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenioFor.title"/> </legend>
			<!-- Fecha Formalizacion -->
			<p>
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenioFor.fechaFor.label"/>:</label>
	      		<bean:write name="liqConvenioPagoCuentaAdapterVO" property="convenio.fechaFor"/>
			</p>
			<!-- Apellido -->
			<p>
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenioFor.apellido.label"/>:</label>
	      		<bean:write name="liqConvenioPagoCuentaAdapterVO" property="convenio.persona.apellido"/>
			<!-- Nombre -->
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenioFor.nombre.label"/>:</label>
	      		<bean:write name="liqConvenioPagoCuentaAdapterVO" property="convenio.persona.nombres"/>
			</p>
			<!-- Apellido Materno -->
			<p>
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenioFor.apellidoMaterno.label"/>:</label>
	      		<bean:write name="liqConvenioPagoCuentaAdapterVO" property="convenio.persona.apellidoMaterno"/>
			<!-- Tipo y Nro Doc -->
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenioFor.tipoNroDoc.label"/>:</label>
	      		<bean:write name="liqConvenioPagoCuentaAdapterVO" property="convenio.persona.documento.tipoyNumeroView"/>
			</p>
			<!-- Tipo Per For -->
			<p>
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenioFor.desTipoPerFor.label"/>:</label>
	      		<bean:write name="liqConvenioPagoCuentaAdapterVO" property="convenio.tipoPerFor.desTipoPerFor"/>
			<!-- Domicilio -->
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenioFor.desDomicilioParticular.label"/>:</label>
	      		<bean:write name="liqConvenioPagoCuentaAdapterVO" property="convenio.persona.domicilio.view"/>
			</p>
			
			<!-- Telefono -->
			<p>
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenioFor.telefono.label"/>:</label>
	      		<bean:write name="liqConvenioPagoCuentaAdapterVO" property="convenio.persona.telefono"/>
			<!-- Tipo Doc Aportada -->
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenioFor.desTipoDocApo.label"/>:</label>
	      		<bean:write name="liqConvenioPagoCuentaAdapterVO" property="convenio.tipoDocApo.desTipoDocApo"/>
			</p>
			
			<!-- Obs -->
			<p>
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenioFor.observacionFor.label"/>:</label>
	      		<bean:write name="liqConvenioPagoCuentaAdapterVO" property="convenio.observacionFor"/>
			</p>
			<p>
			<!-- Agente Interviniente -->
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenioFor.ususarioFor.label"/>:</label>
	      		<bean:write name="liqConvenioPagoCuentaAdapterVO" property="convenio.ususarioFor"/>
			</p>

	</fieldset>
	<!-- Datos de Formalizacion -->
	
		<!-- Pagos a Cuenta-->	
	<div class="horizscroll">
	    <table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
	    	<caption><bean:message bundle="gde" key="gde.liqConvenioPagoCuenta.listaPagosCuenta.title"/></caption>
	      	<tbody>
	      	<!-- Item LiqCuotaVO -->
		       	<tr>
					<th align="left"><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.liqCuota.nroCuota.label"/></th>
					<th align="left"><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.liqCuota.fechaVto.label"/></th>
					<th align="left"><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.liqCuota.capital.label"/></th>
					<th align="left"><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.liqCuota.interes.label"/></th>
					<th align="left"><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.liqCuota.actualizacion.label"/></th>
					<th align="left"><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.liqCuota.total.label"/></th>
					<th align="left"><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.liqCuota.fechaPago.label"/></th>
					<th align="left"><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.liqCuota.desEstado.label"/></th>
				</tr>
				<!-- Item LiqCuotaVO -->
				<logic:iterate id="PagosACuenta" name="liqConvenioPagoCuentaAdapterVO" property="listCuotas">
				  	<tr>
				  		<td><bean:write name="PagosACuenta" property="nroCuota"/>&nbsp;</td>
					    <td><bean:write name="PagosACuenta" property="fechaVto"/>&nbsp;</td>
				  		<td><bean:write name="PagosACuenta" property="capital" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
					    <td><bean:write name="PagosACuenta" property="interes" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
					    <td><bean:write name="PagosACuenta" property="actualizacion"  bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
					    <td><bean:write name="PagosACuenta" property="total"  bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
					    <td><bean:write name="PagosACuenta" property="fechaPago"/>&nbsp;</td>
					    <td><bean:write name="PagosACuenta" property="desEstado"/>&nbsp;</td>
				    </tr>
				</logic:iterate>
			</tbody>
		</table>
	</div>	
	<!-- Fin Pagos a Cuenta -->
	
	<!-- Datos de la aplicacion de Pagos a Cuenta -->
		<fieldset>
			<legend><bean:message bundle="gde" key="gde.liqConvenioPagoCuenta.title"/></legend>
				<p> Al ejecutar la Aplicación de Pagos a Cuenta se reasentarán estos Pagos ordenados por Fecha, controlando la caducidad del convenio para cada uno, obteniendo
					como resultado Pagos Buenos o Pagos a Cuenta según corresponda.
				</p>
		</fieldset>
		<p align="right">
		<button type="button" name="btn1" class="boton" onclick="submitForm('aplicarPagoACuenta','<bean:write name="liqConvenioPagoCuentaAdapterVO" property="convenio.idConvenio" bundle="base" formatKey="general.format.id"/>');">Aplicar Pagos a Cuenta</button>
		</p>
			<p align="left">
				<button type="button" name="btn2" class="boton" onclick="submitForm('verConvenio','<bean:write name="liqConvenioPagoCuentaAdapterVO" property="convenio.idConvenio" bundle="base" formatKey="general.format.id"/>');">Volver</button>
			</p>

		<input type="hidden" name="method" value=""/>
        <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
        <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
</html:form>
<!-- Fin formulario -->
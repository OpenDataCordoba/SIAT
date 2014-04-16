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
	
	<h1><bean:message bundle="gde" key="gde.verPagoConvenio.title"/></h1>	
	
	<table class="tablabotones" width="100%">
		<tr>
			<td align="left">
				<p>En esta pantalla se muestra de detalle de las imputaciones generadas por el pago de cada Cuota sobre 
					la Deuda Original.</p>
			</td>				
			<td align="right">
	 			<button type="button" name="btnVolver" class="boton" onclick="submitForm('verConvenio', '<bean:write name="verPagosConvenioAdapterVO" property="convenio.idConvenio" bundle="base" formatKey="general.format.id"/>');">
					Volver
				</button>
			</td>
		</tr>
	</table>
	
	<!-- LiqCuenta -->
		<bean:define id="DeudaAdapterVO" name="verPagosConvenioAdapterVO"/>
		<%@ include file="/gde/gdeuda/includeLiqCuenta.jsp" %>
	<!-- LiqCuenta -->

	
	<!-- LiqConvenio -->
	<fieldset>
		<legend><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenio.title"/> </legend>
			
			<!-- Nro Convenio -->
			<p>
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenio.nroConvenio.label"/>:</label>
	      		<bean:write name="verPagosConvenioAdapterVO" property="convenio.nroConvenio"/>
			</p>
			
			<!-- Plan de Pago -->
			<p>
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenio.planPago.label"/>:</label>
	      		<bean:write name="verPagosConvenioAdapterVO" property="convenio.desPlan"/>
	      	</p>
	      	<logic:equal name="verPagosConvenioAdapterVO" property="convenio.poseeCaso" value="true" >
	      		<p>	
					<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenio.caso.label"/>:</label>
					<bean:write name="verPagosConvenioAdapterVO" property="convenio.caso.sistemaOrigen.desSistemaOrigen"/>
					&nbsp;							
					<bean:write name="verPagosConvenioAdapterVO" property="convenio.caso.numero"/>	
				</p>
	      	</logic:equal>
			<!-- Via Deuda -->
			<p>
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenio.viaDeuda.label"/>:</label>
	      		<bean:write name="verPagosConvenioAdapterVO" property="convenio.desViaDeuda"/>
			</p>
			<logic:equal name="verPagosConvenioAdapterVO" property="convenio.poseeProcurador" value="true" >
				<p>	
					<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenio.procurador.label"/>:</label>
		      		<bean:write name="verPagosConvenioAdapterVO" property="convenio.procurador.idView"/> -
		      		<bean:write name="verPagosConvenioAdapterVO" property="convenio.procurador.descripcion"/>
				</p>
	      	</logic:equal>
			
			<p>
			<!-- Estado Convenio -->
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenio.estadoConvenio.label"/>:</label>
	      		<bean:write name="verPagosConvenioAdapterVO" property="convenio.desEstadoConvenio"/>
			</p>
			<!-- Cantidad Cuotas -->
			<p>
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenio.cantCuotas.label"/>:</label>
	      		<bean:write name="verPagosConvenioAdapterVO" property="convenio.canCuotasPlan"/>
			</p>
			<!-- Total Conveniado -->
			<p>
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenio.totalImporteConvenido.label"/>:</label>
	      		<bean:write name="verPagosConvenioAdapterVO" property="convenio.totImporteConvenio" bundle="base" formatKey="general.format.currency"/>
			</p>
				
	</fieldset>
	<!-- LiqConvenio -->	
	
	<!-- Datos de Formalizacion -->
	<fieldset>
		<legend><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenioFor.title"/> </legend>
			<!-- Fecha Formalizacion -->
			<p>
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenioFor.fechaFor.label"/>:</label>
	      		<bean:write name="verPagosConvenioAdapterVO" property="convenio.fechaFor"/>
			</p>
			
			<logic:equal name="verPagosConvenioAdapterVO" property="convenio.poseeDatosPersona" value="true" >
				<!-- Apellido -->
				<p>
		      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenioFor.apellido.label"/>:</label>
		      		<bean:write name="verPagosConvenioAdapterVO" property="convenio.persona.apellido"/>
				<!-- Nombre -->
		      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenioFor.nombre.label"/>:</label>
		      		<bean:write name="verPagosConvenioAdapterVO" property="convenio.persona.nombres"/>
				</p>
				<!-- Apellido Materno -->
				<p>
		      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenioFor.apellidoMaterno.label"/>:</label>
		      		<bean:write name="verPagosConvenioAdapterVO" property="convenio.persona.apellidoMaterno"/>
				<!-- Tipo y Nro Doc -->
		      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenioFor.tipoNroDoc.label"/>:</label>
		      		<bean:write name="verPagosConvenioAdapterVO" property="convenio.persona.documento.tipoyNumeroView"/>
				</p>
			</logic:equal>
			
			<logic:notEqual name="verPagosConvenioAdapterVO" property="convenio.poseeDatosPersona" value="true" >
				<p>
					<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenioFor.sinDatosPerFor"/></label>
				</p>				
			</logic:notEqual>
			
			<!-- Tipo Per For -->
			<p>
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenioFor.desTipoPerFor.label"/>:</label>
	      		<bean:write name="verPagosConvenioAdapterVO" property="convenio.tipoPerFor.desTipoPerFor"/>
			<!-- Domicilio -->
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenioFor.desDomicilioParticular.label"/>:</label>
	      		<bean:write name="verPagosConvenioAdapterVO" property="convenio.persona.domicilio.view"/>
			</p>
			
			<!-- Telefono -->
			<p>
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenioFor.telefono.label"/>:</label>
	      		<bean:write name="verPagosConvenioAdapterVO" property="convenio.persona.telefono"/>
			<!-- Tipo Doc Aportada -->
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenioFor.desTipoDocApo.label"/>:</label>
	      		<bean:write name="verPagosConvenioAdapterVO" property="convenio.tipoDocApo.desTipoDocApo"/>
			</p>
			
			<!-- Obs -->
			<p>
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenioFor.observacionFor.label"/>:</label>
	      		<bean:write name="verPagosConvenioAdapterVO" property="convenio.observacionFor"/>
			</p>
			<p>
			<!-- Agente Interviniente -->
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenioFor.ususarioFor.label"/>:</label>
	      		<bean:write name="verPagosConvenioAdapterVO" property="convenio.ususarioFor"/>
			</p>

	</fieldset>
	<!-- Datos de Formalizacion -->
	
	<!-- Periodos Incluidos -->	
	<div class="horizscroll">
	    <table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
	    	<caption><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.periodosIncluidos.title"/></caption>
	      	<tbody>
	      	<!-- Item LiqDeudaVO -->
		       	<tr>
					<th align="left"><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.liqDeuda.periodoDeuda.label"/></th>
					<th align="left"><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.liqDeuda.fechaVto.label"/></th>
					<th align="left"><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.liqDeuda.saldo.label"/></th>
					<th align="left"><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.liqDeuda.actualizacion.label"/></th>
					<th align="left"><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.liqDeuda.total.label"/></th>
				</tr>
				<!-- Item LiqDeudaVO -->
				<logic:iterate id="LiqPeriodo" name="verPagosConvenioAdapterVO" property="convenio.listPeriodoIncluido">
			  		<tr>	
			  			<td><bean:write name="LiqPeriodo" property="periodoDeuda"/>&nbsp;</td>
				        <td><bean:write name="LiqPeriodo" property="fechaVto"/>&nbsp;</td>
				        <td><bean:write name="LiqPeriodo" property="saldo" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
				        <td><bean:write name="LiqPeriodo" property="actualizacion"  bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
				        <td><bean:write name="LiqPeriodo" property="total"  bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
			       	</tr>
				</logic:iterate>
				<tr>
					<td colspan="5" class="celdatotales" align="right">
						<bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.liqDeuda.periodos.Total"/>:<bean:write name="verPagosConvenioAdapterVO" property="convenio.totalPeriodos" bundle="base" formatKey="general.format.currency"/>
					</td>
				</tr>	
	      	</tbody>
		</table>
	</div>	
	<!-- Fin Periodos Incluidos -->	
		<!-- Cuotas Pagas-->	
	<div class="horizscroll">
	    <table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
	    	<caption><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.cuotasPagas.title"/></caption>
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
				<!-- Item LiqDeudaVO -->
				<logic:notEmpty name="verPagosConvenioAdapterVO" property="convenio.listCuotaPaga">
					<logic:iterate id="LiqCuotaPaga" name="verPagosConvenioAdapterVO" property="convenio.listCuotaPaga">
				  		<tr>
				  			<td><bean:write name="LiqCuotaPaga" property="nroCuota"/>&nbsp;</td>
					        <td><bean:write name="LiqCuotaPaga" property="fechaVto"/>&nbsp;</td>
				  			<td><bean:write name="LiqCuotaPaga" property="capital" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
					        <td><bean:write name="LiqCuotaPaga" property="interes" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
					        <td><bean:write name="LiqCuotaPaga" property="actualizacion"  bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
					        <td><bean:write name="LiqCuotaPaga" property="total"  bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
					        <td><bean:write name="LiqCuotaPaga" property="fechaPago"/>&nbsp;</td>
					        <td><bean:write name="LiqCuotaPaga" property="desEstado"/>&nbsp;</td>
				       	</tr>
					</logic:iterate>
					<tr>
						<td colspan="7" class="celdatotales" align="right">
							<bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.liqDeuda.periodos.Total"/>:
							<bean:write name="verPagosConvenioAdapterVO" property="convenio.totalCuotasPagas" bundle="base" formatKey="general.format.currency"/>
						</td>
						<td colspan="2">&nbsp;</td>
					</tr>						
				</logic:notEmpty>
				<logic:empty name="verPagosConvenioAdapterVO" property="convenio.listCuotaPaga">
					<tr>
						<td colspan="9"><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.cuotasPagas.noSeRegistranCuotasPagas"/></td>
					</tr>
				</logic:empty>
	      	</tbody>
		</table>
	</div>	
	<!-- Fin Cuotas Pagas -->
	<!-- Imputaciones de cuotas -->	
	<div class="horizscroll">
	    <table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
	    	<caption><bean:message bundle="gde" key="gde.verPagosConvenio.detalle.title"/></caption>
	      	<tbody>
	      	<!-- Item LiqCuotaVO -->
		       	<tr>
					<th align="left" width="10%"><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.liqCuota.nroCuota.label"/></th>
					<th align="left" width="10%"><bean:message bundle="gde" key="gde.verPagosConvenio.nroCuotaImputada.label"/></th>
					<th align="left" width="10%"><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.liqCuota.fechaPago.label"/></th>
					<th align="left" width="20%"><bean:message bundle="gde" key="gde.verPagosConvenio.capitalCuota.label"/></th>
					<th align="left" width="10%"><bean:message bundle="gde" key="gde.verPagosConvenio.saldoEnPlanCub.label"/></th>
					<th align="left" width="15%"><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.liqDeuda.periodoDeuda.label"/></th>
					<th align="left" width="10%"><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.liqDeuda.total.label"/></th>
					<th align="left" width="15%"><bean:message bundle="gde" key="gde.verPagosConvenio.saldo.label"/></th>
				</tr>
				
				<!-- Item LiqDeudaVO -->
				<logic:notEmpty name="verPagosConvenioAdapterVO" property="listCuotaDeuda">
					<logic:iterate id="LiqCuotaPaga" name="verPagosConvenioAdapterVO" property="listCuotaDeuda">
				  		<tr>
				  			<td><bean:write name="LiqCuotaPaga" property="nroCuota"/>&nbsp;</td>
				  			<td><bean:write name="LiqCuotaPaga" property="nroCuotaImputada"/>&nbsp;</td>
				  			<td><bean:write name="LiqCuotaPaga" property="fechaPago"/>&nbsp;</td>
				  			<td><bean:write name="LiqCuotaPaga" property="capitalView"/>&nbsp;</td>
				  			<td><bean:write name="LiqCuotaPaga" property="saldoEnPlanCubView"/>&nbsp;</td>
				  			<td><bean:write name="LiqCuotaPaga" property="periodoDeudaView"/>&nbsp;</td>
				  			<td><bean:write name="LiqCuotaPaga" property="totalView"/>&nbsp;</td>
				  			<td><bean:write name="LiqCuotaPaga" property="saldoView"/>&nbsp;</td>
				       	</tr>
					</logic:iterate>
				</logic:notEmpty>
	      	</tbody>
		</table>
	</div>	
	<p>La columna "Capital Cuota / Importe" muestra el capital de la cuota para los Pagos Buenos y el Importe para los Pagos a Cuenta</p>
	<p>La Columna "Saldo / Imputacion" muestra el saldo + actualizacion de la deuda en plan para Pagos Buenos 
	y la imputación del importe sobre el Capital de deuda para Pagos a Cuenta </p> 
	<p>OBSERVACIÓN: Los Pagos a Cuenta solo generan relación con la deuda al realizar un Saldo por Caducidad</p>
	<!-- Fin Imputaciones de cuotas -->

	
	<button type="button" name="btnVolver" class="boton" onclick="submitForm('verConvenio', '<bean:write name="verPagosConvenioAdapterVO" property="convenio.idConvenio" bundle="base" formatKey="general.format.id"/>');">
			Volver
	</button>
	
	<input type="hidden" name="method" value=""/>
	<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
	<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>
	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="isSubmittedForm" value="true"/>

</html:form>
<!-- Fin formulario -->
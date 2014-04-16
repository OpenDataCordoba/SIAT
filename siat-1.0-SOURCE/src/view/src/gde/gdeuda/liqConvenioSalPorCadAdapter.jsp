<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- formulario unico -->
<html:form styleId="filter" action="/gde/AdministrarLiqConvenioCuenta.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	<logic:equal name="liqConvenioSalPorCadAdapterVO" property="esVueltaAtras" value="false">
		<logic:equal name="liqConvenioSalPorCadAdapterVO" property="esAnulacion" value="false">
			<h1><bean:message bundle="gde" key="gde.liqConvenioSalPorCad.title"/></h1>	
			<table class="tablabotones" width="100%">
				<tr>
					<td align="left">
						<p>En esta pantalla se muestra el detalle del convenio de Pagos de su cuenta. </p>
						<p>IMPORTANTE: al procesar el Saldo Por Caducidad se aplicar&aacute;n los pagos Registrados en el 
					   		Convenio a la Deuda Original, se cambiar&aacute; el estado del Convenio a "Recompuesto" no permitiendo realizar ninguna acci&oacute;n 
					   		posterior sobre el mismo.</p>
					</td>				
					<td align="right">
			 			<button type="button" name="btn2" class="boton" onclick="submitForm('verConvenio','<bean:write name="liqConvenioSalPorCadAdapterVO" property="convenio.idConvenio" bundle="base" formatKey="general.format.id"/>');">Volver</button>
					</td>
				</tr>
			</table>	
			
		 </logic:equal>
		 <logic:equal name="liqConvenioSalPorCadAdapterVO" property="esAnulacion" value="true">
		 	<h1><bean:message bundle="gde" key="gde.liqConvenioSalPorCad.anulacion.title"/></h1>
		 	
		 		
		 		<table class="tablabotones" width="100%">
					<tr>
						<td align="left">
							<p>IMPORTANTE: al procesar la anulación el estado del convenio quedar&aacute; "Anulado" sin verse afectada la prescripci&oacute;n de deuda
		 						Utilice esta opci&oacute;n cuando se trate de un error al tomar los datos en la formalizaci&oacute;n,
		 					</p>
						</td>				
						<td align="right">
				 			<button type="button" name="btn2" class="boton" onclick="submitForm('verConvenio','<bean:write name="liqConvenioSalPorCadAdapterVO" property="convenio.idConvenio" bundle="base" formatKey="general.format.id"/>');">Volver</button>
						</td>
					</tr>
				</table>
		 </logic:equal> 
	</logic:equal>
	
	<logic:notEqual name="liqConvenioSalPorCadAdapterVO" property="esVueltaAtras" value="false">
		<h1><bean:message bundle="gde" key="gde.liqConvenioSalPorCadVuelta.title"/></h1>	
	
		<table class="tablabotones" width="100%">
		<tr>
			<td align="left">
				<p>En esta pantalla se muestra el detalle de un convenio de Pago de su cuenta en estado Recompuesto. </p>
			</td>				
			<td align="right">
	 			<button type="button" name="btn2" class="boton" onclick="submitForm('verConvenio','<bean:write name="liqConvenioSalPorCadAdapterVO" property="convenio.idConvenio" bundle="base" formatKey="general.format.id"/>');">Volver</button>
			</td>
		</tr>
	</table>
	</logic:notEqual>

	<!-- LiqCuenta -->
		<bean:define id="DeudaAdapterVO" name="liqConvenioSalPorCadAdapterVO"/>
		<%@ include file="/gde/gdeuda/includeLiqCuenta.jsp" %>
	<!-- LiqCuenta -->
	
	<!-- LiqConvenio -->
	<fieldset>
		<legend><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenio.title"/> </legend>
			
			<!-- Nro Convenio -->
			<p>
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenio.nroConvenio.label"/>:</label>
	      		<bean:write name="liqConvenioSalPorCadAdapterVO" property="convenio.nroConvenio"/>
			</p>
			
			<!-- Plan de Pago -->
			<p>
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenio.planPago.label"/>:</label>
	      		<bean:write name="liqConvenioSalPorCadAdapterVO" property="convenio.desPlan"/>
			</p>
			<!-- Via Deuda -->
			<p>
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenio.viaDeuda.label"/>:</label>
	      		<bean:write name="liqConvenioSalPorCadAdapterVO" property="convenio.desViaDeuda"/>
			
			<!-- Estado Convenio -->
			
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenio.estadoConvenio.label"/>:</label>
	      		<bean:write name="liqConvenioSalPorCadAdapterVO" property="convenio.desEstadoConvenio"/>
			</p>
			<!-- Cantidad Cuotas -->
			<p>
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenio.cantCuotas.label"/>:</label>
	      		<bean:write name="liqConvenioSalPorCadAdapterVO" property="convenio.canCuotasPlan"/>
			</p>
			<!-- Total Conveniado -->
			<p>
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenio.totalImporteConvenido.label"/>:</label>
	      		<bean:write name="liqConvenioSalPorCadAdapterVO" property="convenio.totImporteConvenio" bundle="base" formatKey="general.format.currency"/>
			</p>
				
	</fieldset>
	<!-- LiqConvenio -->	
	
	<!-- Datos de Formalizacion -->
	<fieldset>
		<legend><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenioFor.title"/> </legend>
			<!-- Fecha Formalizacion -->
			<p>
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenioFor.fechaFor.label"/>:</label>
	      		<bean:write name="liqConvenioSalPorCadAdapterVO" property="convenio.fechaFor"/>
			</p>
			<!-- Apellido -->
			<p>
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenioFor.apellido.label"/>:</label>
	      		<bean:write name="liqConvenioSalPorCadAdapterVO" property="convenio.persona.apellido"/>
			<!-- Nombre -->
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenioFor.nombre.label"/>:</label>
	      		<bean:write name="liqConvenioSalPorCadAdapterVO" property="convenio.persona.nombres"/>
			</p>
			<!-- Apellido Materno -->
			<p>
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenioFor.apellidoMaterno.label"/>:</label>
	      		<bean:write name="liqConvenioSalPorCadAdapterVO" property="convenio.persona.apellidoMaterno"/>
			<!-- Tipo y Nro Doc -->
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenioFor.tipoNroDoc.label"/>:</label>
	      		<bean:write name="liqConvenioSalPorCadAdapterVO" property="convenio.persona.documento.tipoyNumeroView"/>
			</p>
			<!-- Tipo Per For -->
			<p>
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenioFor.desTipoPerFor.label"/>:</label>
	      		<bean:write name="liqConvenioSalPorCadAdapterVO" property="convenio.tipoPerFor.desTipoPerFor"/>
			<!-- Domicilio -->
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenioFor.desDomicilioParticular.label"/>:</label>
	      		<bean:write name="liqConvenioSalPorCadAdapterVO" property="convenio.persona.domicilio.view"/>
			</p>
			
			<!-- Telefono -->
			<p>
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenioFor.telefono.label"/>:</label>
	      		<bean:write name="liqConvenioSalPorCadAdapterVO" property="convenio.persona.telefono"/>
			<!-- Tipo Doc Aportada -->
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenioFor.desTipoDocApo.label"/>:</label>
	      		<bean:write name="liqConvenioSalPorCadAdapterVO" property="convenio.tipoDocApo.desTipoDocApo"/>
			</p>
			
			<!-- Obs -->
			<p>
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenioFor.observacionFor.label"/>:</label>
	      		<bean:write name="liqConvenioSalPorCadAdapterVO" property="convenio.observacionFor"/>
			</p>
			<p>
			<!-- Agente Interviniente -->
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenioFor.ususarioFor.label"/>:</label>
	      		<bean:write name="liqConvenioSalPorCadAdapterVO" property="convenio.ususarioFor"/>
			</p>

	</fieldset>
	<!-- Datos de Formalizacion -->
	
	<!-- Datos del Saldo por Caducidad -->
		<fieldset>
		<logic:equal name="liqConvenioSalPorCadAdapterVO" property="esVueltaAtras" value="false">
			<logic:equal name="liqConvenioSalPorCadAdapterVO" property="esAnulacion" value="true">
				<legend><bean:message bundle="gde" key="gde.liqConvenioSalPorCad.anulacion.title"/></legend>
				<table>
					<tr>
						<td colspan="2">Usted va a relizar una Anulaci&oacute;n de un convenio de pagos.</td>
					</tr>
					<tr><td>&nbsp;</td></tr>
					<tr>
						<td><label><bean:message bundle="gde" key="gde.liqConvenioSalPorCad.datos.fecha"/>:</label></td>
						<td class="normal"><bean:write name="liqConvenioSalPorCadAdapterVO" property="fechaSaldoView"/></td>	
					<tr>
						<td><label><bean:message bundle="gde" key="gde.liqConvenioSalPorCad.datos.obs"/>:</label></td>
						<td class="normal"><html:textarea name="liqConvenioSalPorCadAdapterVO" property="observacion" cols="80" rows="15"> 
						</html:textarea></td>
					</tr>
				</table>
			</logic:equal>
			<logic:notEqual name="liqConvenioSalPorCadAdapterVO" property="esAnulacion" value="true">	
				<legend><bean:message bundle="gde" key="gde.liqConvenioSalPorCad.datos.title"/></legend>
				<table>
					<tr>
						<td colspan="2">Usted va a relizar un Saldo por Caducidad.</td>
					</tr>
					<tr><td>&nbsp;</td></tr>
					<tr>
						<td><label><bean:message bundle="gde" key="gde.liqConvenioSalPorCad.datos.fecha"/>:</label></td>
						<td class="normal"><bean:write name="liqConvenioSalPorCadAdapterVO" property="fechaSaldoView"/></td>
					</tr>
					
					<!-- Inclucion de Caso -->
					<tr>
						<td><label><bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
						<td colspan="3">
							<bean:define id="IncludedVO" name="liqConvenioSalPorCadAdapterVO" property="casoContainer"/>
							<bean:define id="voName" value="casoContainer" />
							<%@ include file="/cas/caso/includeCaso.jsp" %>
						</td>
					</tr>
					<!-- Fin Inclucion de Caso -->	
					
					<tr>
						<td><label><bean:message bundle="gde" key="gde.liqConvenioSalPorCad.datos.obs"/>:</label></td>
						<td class="normal"><html:textarea name="liqConvenioSalPorCadAdapterVO" property="observacion" cols="80" rows="15"> 
						</html:textarea></td>
					</tr>
				</table>
			</logic:notEqual>
		</logic:equal>
		<logic:notEqual name="liqConvenioSalPorCadAdapterVO" property="esVueltaAtras" value="false">
			<legend><bean:message bundle="gde" key="gde.liqConvenioSalPorCadVuelta.datos.title"/></legend>
			<table>
				<tr>
					<td colspan="2">Usted va a realizar una Vuelta Atrás de un Saldo por Caducidad.</td>
				</tr>
				<tr><td>&nbsp;</td></tr>
				<tr>
					<td><label><bean:message bundle="gde" key="gde.liqConvenioSalPorCad.datos.fecha"/>:</label></td>
					<td class="normal"><bean:write name="liqConvenioSalPorCadAdapterVO" property="fechaSaldoView"/></td>
				</tr>
				
				<!-- Inclucion de Caso -->
				<tr>
					<td><label><bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
					<td colspan="3">
						<bean:define id="IncludedVO" name="liqConvenioSalPorCadAdapterVO" property="casoContainer"/>
						<bean:define id="voName" value="casoContainer" />
						<%@ include file="/cas/caso/includeCaso.jsp" %>
					</td>
				</tr>
				<!-- Fin Inclucion de Caso -->	
				
				<tr>
					<td><label><bean:message bundle="gde" key="gde.liqConvenioSalPorCad.datos.obs"/>:</label></td>
					<td class="normal"><html:textarea name="liqConvenioSalPorCadAdapterVO" property="observacion" cols="80" rows="15"> 
					</html:textarea></td>
				</tr>
			</table>
		</logic:notEqual>
		</fieldset>
		<logic:equal name="liqConvenioSalPorCadAdapterVO" property="esVueltaAtras" value="false">
			<p align="right">
				<button type="button" name="btn1" class="boton" onclick="submitForm('ejecutarSalPorCad','<bean:write name="liqConvenioSalPorCadAdapterVO" property="convenio.idConvenio" bundle="base" formatKey="general.format.id"/>');">
					<logic:equal name="liqConvenioSalPorCadAdapterVO" property="esAnulacion" value="false">
					  Generar Saldo por Caducidad
					</logic:equal>
					<logic:notEqual name="liqConvenioSalPorCadAdapterVO" property="esAnulacion" value="false">
						Anular Convenio
					</logic:notEqual>
				</button>
			</p>
		</logic:equal>
		<logic:notEqual name="liqConvenioSalPorCadAdapterVO" property="esVueltaAtras" value="false">
			<p align="right">
				<button type="button" name="btn1" class="boton" onclick="submitForm('vueltaAtrasSalPorCad','<bean:write name="liqConvenioSalPorCadAdapterVO" property="convenio.idConvenio" bundle="base" formatKey="general.format.id"/>');">Vuelta Atrás Saldo Caducidad</button>
			</p>
		</logic:notEqual>
			<p align="left">
				<button type="button" name="btn2" class="boton" onclick="submitForm('verConvenio','<bean:write name="liqConvenioSalPorCadAdapterVO" property="convenio.idConvenio" bundle="base" formatKey="general.format.id"/>');">Volver</button>
			</p>

		<input type="hidden" name="method" value=""/>
        <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
        <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
</html:form>
<!-- Fin formulario -->
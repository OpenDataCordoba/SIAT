<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- formulario unico -->
<html:form styleId="filter" action="/gde/AdministrarLiqConvenioCuenta.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				

	<h1><bean:message bundle="gde" key="gde.liqConvenioCuotaSaldo.title"/></h1>	
	
	<table class="tablabotones" width="100%">
		<tr>
			<td align="left">
				<p>En esta pantalla se muestra el detalle de un convenio de Pago para la confección de una Cuota Saldo. </p>
			</td>				
			<td align="right">
	 			<button type="button" name="btn2" class="boton" onclick="submitForm('verConvenio','<bean:write name="liqConvenioCuotaSaldoAdapterVO" property="convenio.idConvenio" bundle="base" formatKey="general.format.id"/>');">Volver</button>
			</td>
		</tr>
	</table>
	
	<!-- LiqCuenta -->
		<bean:define id="DeudaAdapterVO" name="liqConvenioCuotaSaldoAdapterVO"/>
		<%@ include file="/gde/gdeuda/includeLiqCuenta.jsp" %>
	<!-- LiqCuenta -->

	
	<!-- LiqConvenio -->
	<fieldset>
		<legend><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenio.title"/> </legend>
			
			<!-- Nro Convenio -->
			<p>
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenio.nroConvenio.label"/>:</label>
	      		<bean:write name="liqConvenioCuotaSaldoAdapterVO" property="convenio.nroConvenio"/>
			</p>
			
			<!-- Plan de Pago -->
			<p>
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenio.planPago.label"/>:</label>
	      		<bean:write name="liqConvenioCuotaSaldoAdapterVO" property="convenio.desPlan"/>
			</p>
			<!-- Via Deuda -->
			<p>
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenio.viaDeuda.label"/>:</label>
	      		<bean:write name="liqConvenioCuotaSaldoAdapterVO" property="convenio.desViaDeuda"/>
			
			<!-- Estado Convenio -->
			
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenio.estadoConvenio.label"/>:</label>
	      		<bean:write name="liqConvenioCuotaSaldoAdapterVO" property="convenio.desEstadoConvenio"/>
			</p>
			<!-- Cantidad Cuotas -->
			<p>
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenio.cantCuotas.label"/>:</label>
	      		<bean:write name="liqConvenioCuotaSaldoAdapterVO" property="convenio.canCuotasPlan"/>
			</p>
			<!-- Total Conveniado -->
			<p>
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenio.totalImporteConvenido.label"/>:</label>
	      		<bean:write name="liqConvenioCuotaSaldoAdapterVO" property="convenio.totImporteConvenio" bundle="base" formatKey="general.format.currency"/>
			</p>
				
	</fieldset>
	<!-- LiqConvenio -->	
	
	<!-- Datos de Formalizacion -->
	<fieldset>
		<legend><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenioFor.title"/> </legend>
			<!-- Fecha Formalizacion -->
			<p>
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenioFor.fechaFor.label"/>:</label>
	      		<bean:write name="liqConvenioCuotaSaldoAdapterVO" property="convenio.fechaFor"/>
			</p>
			<!-- Apellido -->
			<p>
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenioFor.apellido.label"/>:</label>
	      		<bean:write name="liqConvenioCuotaSaldoAdapterVO" property="convenio.persona.apellido"/>
			<!-- Nombre -->
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenioFor.nombre.label"/>:</label>
	      		<bean:write name="liqConvenioCuotaSaldoAdapterVO" property="convenio.persona.nombres"/>
			</p>
			<!-- Apellido Materno -->
			<p>
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenioFor.apellidoMaterno.label"/>:</label>
	      		<bean:write name="liqConvenioCuotaSaldoAdapterVO" property="convenio.persona.apellidoMaterno"/>
			<!-- Tipo y Nro Doc -->
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenioFor.tipoNroDoc.label"/>:</label>
	      		<bean:write name="liqConvenioCuotaSaldoAdapterVO" property="convenio.persona.documento.tipoyNumeroView"/>
			</p>
			<!-- Tipo Per For -->
			<p>
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenioFor.desTipoPerFor.label"/>:</label>
	      		<bean:write name="liqConvenioCuotaSaldoAdapterVO" property="convenio.tipoPerFor.desTipoPerFor"/>
			<!-- Domicilio -->
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenioFor.desDomicilioParticular.label"/>:</label>
	      		<bean:write name="liqConvenioCuotaSaldoAdapterVO" property="convenio.persona.domicilio.view"/>
			</p>
			
			<!-- Telefono -->
			<p>
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenioFor.telefono.label"/>:</label>
	      		<bean:write name="liqConvenioCuotaSaldoAdapterVO" property="convenio.persona.telefono"/>
			<!-- Tipo Doc Aportada -->
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenioFor.desTipoDocApo.label"/>:</label>
	      		<bean:write name="liqConvenioCuotaSaldoAdapterVO" property="convenio.tipoDocApo.desTipoDocApo"/>
			</p>
			
			<!-- Obs -->
			<p>
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenioFor.observacionFor.label"/>:</label>
	      		<bean:write name="liqConvenioCuotaSaldoAdapterVO" property="convenio.observacionFor"/>
			</p>
			<p>
			<!-- Agente Interviniente -->
	      		<label><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.convenioFor.ususarioFor.label"/>:</label>
	      		<bean:write name="liqConvenioCuotaSaldoAdapterVO" property="convenio.ususarioFor"/>
			</p>

	</fieldset>
	<!-- Datos de Formalizacion -->
	
		<!-- Cuotas Pagas-->	
	<div class="horizscroll">
	    <table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
	    	<caption><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.cuotasPagas.title"/></caption>
	      	<tbody>
	      	<!-- Item LiqCuotaVO -->
		       	<tr>
					<th align="left">&nbsp;</th>
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
				<logic:notEmpty name="liqConvenioCuotaSaldoAdapterVO" property="convenio.listCuotaPaga">
					<logic:iterate id="LiqCuotaPaga" name="liqConvenioCuotaSaldoAdapterVO" property="convenio.listCuotaPaga">
				  		<tr>
				  			<td>
				  				<a style="cursor: pointer;" 
				  					href="/siat/gde/AdministrarLiqDeuda.do?method=verDetalleCuota&selectedId=<bean:write name="LiqCuotaPaga" property="idCuota" bundle="base" formatKey="general.format.id"/>">
						        	<img title="Ver" src="<%=request.getContextPath()%>/images/iconos/selec0.gif" border="0"/>
						        </a>
				  			</td>	
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
				</logic:notEmpty>
				<logic:empty name="liqConvenioCuotaSaldoAdapterVO" property="convenio.listCuotaPaga">
					<tr>
						<td colspan="9"><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.cuotasPagas.noSeRegistranCuotasPagas"/></td>
					</tr>
				</logic:empty>
	      	</tbody>
		</table>
	</div>	
	<!-- Fin Cuotas Pagas -->
	
	<!-- Cuotas Impagas-->	
	<div class="horizscroll">
	    <table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
	    	<caption><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.cuotasImpagas.title"/></caption>
	      	<tbody>
	      	<!-- Item LiqCuotaVO -->
		       	<tr>
					<th align="left"><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.liqCuota.nroCuota.label"/></th>
					<th align="left"><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.liqCuota.fechaVto.label"/></th>
					<th align="left"><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.liqCuota.capital.label"/></th>
					<th align="left"><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.liqCuota.interes.label"/></th>
					<th align="left"><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.liqCuota.actualizacion.label"/></th>
					<th align="left"><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.liqCuota.total.label"/></th>
				</tr>
				<!-- Item LiqDeudaVO -->
				<logic:notEmpty name="liqConvenioCuotaSaldoAdapterVO" property="convenio.listCuotaInpaga">
					<logic:iterate id="LiqCuotaImpaga" name="liqConvenioCuotaSaldoAdapterVO" property="convenio.listCuotaInpaga">
				  		<tr>
				  			<td><bean:write name="LiqCuotaImpaga" property="nroCuota"/>&nbsp;</td>
					        <td><bean:write name="LiqCuotaImpaga" property="fechaVto"/>&nbsp;</td>
					        <logic:notEqual name="LiqCuotaImpaga" property="poseeObservacion" value="true">
					  			<td><bean:write name="LiqCuotaImpaga" property="capital" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
						        <td><bean:write name="LiqCuotaImpaga" property="interes" bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
						        <td><bean:write name="LiqCuotaImpaga" property="actualizacion"  bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
						        <td><bean:write name="LiqCuotaImpaga" property="total"  bundle="base" formatKey="general.format.currency"/>&nbsp;</td>
						    </logic:notEqual>
						    
						    <!-- Es Indeterminada -->
					        <logic:equal name="LiqCuotaImpaga" property="esIndeterminada" value="true">
			       				<td colspan="5">
			       					<b><bean:write name="LiqCuotaImpaga" property="observacion"/>&nbsp;</b>
								</td>
			       			</logic:equal>
			       			
					        <!-- Es Reclamada -->
					        <logic:equal name="LiqCuotaImpaga" property="esReclamada" value="true">
			       				<td colspan="5">
			       					<b><bean:write name="LiqCuotaImpaga" property="observacion"/>&nbsp;</b>
								</td>
			       			</logic:equal>
			       			
				       	</tr>
					</logic:iterate>
				</logic:notEmpty>
				<logic:empty name="liqConvenioCuotaSaldoAdapterVO" property="convenio.listCuotaInpaga">
					<tr>						
						<td colspan="8"><bean:message bundle="gde" key="gde.liqConvenioCuentaAdapter.cuotasImpagas.noSeRegistranCuotasImpagas"/></td>
					</tr>
				</logic:empty>
	      	</tbody>
		</table>
	</div>	
	<!-- Fin Cuotas Impagas -->
	
	<logic:notEqual name="userSession" property="esAnonimo" value="true">
		
		<!-- Datos de la Cuota Saldo -->
		<fieldset>
			<legend><bean:message bundle="gde" key="gde.liqConvenioCuotaSaldo.datos.title"/></legend>
				<table>
					<tr><td>&nbsp;</td></tr>
					<logic:equal name="liqConvenioCuotaSaldoAdapterVO" property="tieneCuotasVencidas" value="true">
						<tr>
							<td colspan="5">ATENCIÓN: El convenio registra cuotas impagas vencidas a la fecha, solicite los comprobantes necesarios para continuar con la confección de la Cuota Saldo</td>
						</tr>
					</logic:equal>
					<tr><td>&nbsp;</td></tr>
					<tr>
						<td>Ingrese la Cuota desde para la Cuota Saldo:</td>
						<td class="normal"><html:text name="liqConvenioCuotaSaldoAdapterVO" property="cuotaDesdeView"/></td>
					</tr>
					<tr><td>&nbsp;</td></tr>
			</table>
		</fieldset>
	</logic:notEqual>
		<p align="right">
		<button type="button" name="btn1" class="boton" onclick="submitForm('generarCuotaSaldo','<bean:write name="liqConvenioCuotaSaldoAdapterVO" property="convenio.idConvenio" bundle="base" formatKey="general.format.id"/>');">Confeccionar Cuota Saldo</button>
		</p>
			<p align="left">
				<button type="button" name="btn2" class="boton" onclick="submitForm('verConvenio','<bean:write name="liqConvenioCuotaSaldoAdapterVO" property="convenio.idConvenio" bundle="base" formatKey="general.format.id"/>');">Volver</button>
			</p>

		<input type="hidden" name="method" value=""/>
        <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
        <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
</html:form>
<!-- Fin formulario -->
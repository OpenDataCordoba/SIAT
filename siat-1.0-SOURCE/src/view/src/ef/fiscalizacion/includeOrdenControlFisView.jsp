		<!-- OrdenControlFis -->
		<fieldset>
			<legend><bean:message bundle="ef" key="ef.ordenControl.title"/></legend>
			<table class="tabladatos">
			
				<!-- nroOrden -->
				<tr>
					<td><label><bean:message bundle="ef" key="ef.ordenControl.nroOrden.label"/>: </label></td>
					<td class="normal"><bean:write name="ordenControlFis" property="numeroOrden" bundle="base" formatKey="general.format.id"/></td>				
				
				<!-- anioOrden -->
					<td><label><bean:message bundle="ef" key="ef.ordenControl.anioOrden.label"/>: </label></td>
					<td class="normal"><bean:write name="ordenControlFis" property="anioOrden" bundle="base" formatKey="general.format.id"/></td>				
				</tr>
				
				<tr>
				<!-- Inclusion de Caso -->
					<td><label><bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
					<td>
						<bean:define id="IncludedVO" name="ordenControlFis"/>
						<bean:define id="voName" value="ordenControlFis" />
						<%@ include file="/cas/caso/includeCasoView.jsp" %>
					</td>
				<!-- Fin Inclusion de Caso -->	
				
				<!-- origenOrden -->
					<td><label><bean:message bundle="ef" key="ef.origen.label"/>: </label></td>
					<td class="normal"><bean:write name="ordenControlFis" property="origenOrden.desOrigen"/></td>
				</tr>
				
				<tr>
				<!-- Inspector -->
					<td><label><bean:message bundle="ef" key="ef.inspector.label"/>: </label></td>
					<td class="normal"><bean:write name="ordenControlFis" property="inspector.desInspector"/></td>
	
				<!-- Supervisor -->
					<td><label><bean:message bundle="ef" key="ef.supervisor.label"/>: </label></td>
					<td class="normal"><bean:write name="ordenControlFis" property="supervisor.desSupervisor"/></td>
				</tr>
				
				<!-- tipoOrden -->
				<tr>
					<td><label><bean:message bundle="ef" key="ef.tipoOrden.label"/>: </label></td>
					<td class="normal" colspan="3"><bean:write name="ordenControlFis" property="tipoOrden.desTipoOrden"/></td>
				</tr>
				
				<!-- Estado -->
				<tr>
					<td><label><bean:message bundle="ef" key="ef.estadoOrden.label"/>: </label></td>
					<td class="normal" colspan="3"><b><bean:write name="ordenControlFis" property="estadoOrden.desEstadoOrden"/></b></td>
				</tr>
							
				<tr>
				<!-- fechaEmision -->
					<td><label><bean:message bundle="ef" key="ef.ordenControl.fechaEmision.label"/>: </label></td>
					<td class="normal"><bean:write name="ordenControlFis" property="fechaEmisionView"/></td>
				<!-- fechaCierre -->
					<td><label><bean:message bundle="ef" key="ef.ordenControl.fechaCierre.label"/>: </label></td>
					<td class="normal"><bean:write name="ordenControlFis" property="fechaCierreView"/></td>				
				</tr>
				
				<!-- observacion -->
				<tr>
					<td><label><bean:message bundle="ef" key="ef.ordenControl.observacion.label"/>: </label></td>
					<td class="normal" colspan="3"><bean:write name="ordenControlFis" property="observacion"/></td>				
				</tr>		
				
				
						
			</table>
		</fieldset>	
		<!-- Procedimiento -->
				
					<logic:notEmpty name="ordenControlFis" property="procedimiento.id">
						<bean:define id="procedimientoVO" name="ordenControlFis" property="procedimiento"/>
						<%@ include file="/cyq/concursoyQuiebra/includeEncProcedimiento.jsp"%>
					<!-- Procedimiento -->
					</logic:notEmpty>
		<!-- FIN Procedimiento -->

		<!-- datos del contribuyente-->
		<fieldset>
			<legend><bean:message bundle="pad" key="pad.contribuyente.title"/></legend>
			<table class="tabladatos">
			
				<tr>
					<td><label><bean:message bundle="ef" key="ef.ordenControlFisViewAdapter.nombreContr"/>: </label></td>
					<td class="normal" colspan="3">
						<bean:write name="ordenControlFis" property="contribuyente.persona.represent"/>
					</td>
				</tr>
				<tr>
					<td><label><bean:message bundle="pad" key="pad.persona.domicilio.label"/>: </label></td>
					<td class="normal" colspan="3"><bean:write name="ordenControlFis" property="contribuyente.persona.domicilio.view"/></td>
				</tr>
				<tr>
					<td><label><bean:message bundle="pad" key="pad.persona.cuit.label"/>: </td>
					<td class="normal"><bean:write name="ordenControlFis" property="contribuyente.persona.cuit"/></td>
				
					<td><label><bean:message bundle="ef" key="ef.opeInvConAdapter.nroIsib.label"/>: </td>
					<td class="normal"><bean:write name="ordenControlFis" property="contribuyente.nroIsib"/></td>
				</tr>
			
			</table>
		</fieldset>			
		<!-- FIN datos del contribuyente-->
		
		<a name="cuentas">&nbsp;</a>
		<!-- lista de cuentas -->
		<logic:notEmpty name="ordenControlFis" property="listOrdConCue">
			<fieldset>
				<legend><bean:message bundle="ef" key="ef.ordenControl.listOrdConCue.title"/></legend>
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="ef" key="ef.ordenControlFisViewAdapter.listOrdConCue.title"/></caption>
					<tbody>
						<logic:equal name="ordenControlFis" property="opciones4CuentasBussEnabled" value="true">
							<th width="1">&nbsp;</th> <!-- liqDeuda -->
							<th width="1">&nbsp;</th> <!-- Estado cuenta -->
						</logic:equal>	
						<th><bean:message bundle="pad" key="pad.cuenta.label"/></th>
						<th><bean:message bundle="pad" key="pad.cuenta.recurso.label"/></th>
						<th><bean:message bundle="pad" key="pad.cuenta.fechaAlta.label"/></th>
						<th><bean:message bundle="ef" key="ef.ordConCue.fiscalizar.label"/></th>
						<logic:iterate id="OrdConCueVO" name="ordenControlFis" property="listOrdConCue">
							<tr>
								<logic:equal name="ordenControlFis" property="opciones4CuentasBussEnabled" value="true">
									<!-- Liquidacion deuda -->
									<td>
										<logic:equal name="OrdConCueVO" property="liquidacionDeudaBussEnabled" value="true">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('liquidacionDeuda', '<bean:write name="OrdConCueVO" property="cuenta.id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="gde" key="gde.button.liquidacionDeuda"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/liquidacionDeuda0.gif"/>
												</a>
										</logic:equal>
										<logic:notEqual name="OrdConCueVO" property="liquidacionDeudaBussEnabled" value="true">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/liquidacionDeuda1.gif"/>
										</logic:notEqual>
									</td>
									
									<!-- Estado cuenta -->								
									<td>
										<logic:equal name="OrdConCueVO" property="estadoCuentaBussEnabled" value="true">
												<a style="cursor: pointer; cursor: hand;" onclick="submitForm('estadoCuenta', '<bean:write name="OrdConCueVO" property="cuenta.id" bundle="base" formatKey="general.format.id"/>');">
													<img title="<bean:message bundle="gde" key="gde.button.estadoCuenta"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/estadoCuenta0.gif"/>
												</a>										
										</logic:equal>
										<logic:notEqual name="OrdConCueVO" property="estadoCuentaBussEnabled" value="true">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/estadoCuenta1.gif"/>
										</logic:notEqual>
									</td>						
								</logic:equal>
								
								<td><bean:write name="OrdConCueVO" property="cuenta.numeroCuenta"/></td>
								<td><bean:write name="OrdConCueVO" property="cuenta.recurso.desRecurso"/></td>
								<td><bean:write name="OrdConCueVO" property="cuenta.fechaAltaView"/></td>
								<td><bean:write name="OrdConCueVO" property="fiscalizarView"/></td>
							</tr>	
						</logic:iterate>
					</tbody>	
				</table>
			</fieldset>				
		</logic:notEmpty>
		<!-- fin lista de cuentas -->
		
		<a name="historico">&nbsp;</a>
		<!-- historico de estados -->
		<logic:notEmpty name="ordenControlFis" property="listHisEstOrdCon">
			<fieldset>
				<legend><bean:message bundle="ef" key="ef.ordenControl.listHistEstOrdCon.title"/></legend>
				<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
					<caption><bean:message bundle="ef" key="ef.ordenControlFisViewAdapter.listHistEstOrdCon.title"/></caption>
					<tbody>
				
						<th><bean:message bundle="ef" key="ef.hisEstOrdCon.fecha.label"/></th>
						<th><bean:message bundle="ef" key="ef.hisEstOrdCon.estado.label"/></th>
						<th><bean:message bundle="ef" key="ef.hisEstOrdCon.observacion.label"/></th>
					
						<logic:iterate id="HisEstOrdCon" name="ordenControlFis" property="listHisEstOrdCon">
							<tr>											
								<td><bean:write name="HisEstOrdCon" property="fechaView"/></td>
								<td><bean:write name="HisEstOrdCon" property="estadoOrden.desEstadoOrden"/></td>
								<td><bean:write name="HisEstOrdCon" property="observacion"/></td>
							</tr>	
						</logic:iterate>
					</tbody>	
				</table>
			</fieldset>				
		</logic:notEmpty>
		<!-- FIN historico de estados -->
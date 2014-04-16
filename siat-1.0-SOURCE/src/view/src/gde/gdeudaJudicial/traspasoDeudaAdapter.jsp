<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

	<h1><bean:message bundle="gde" key="gde.traspasoDevolucionDeudaAdapter.traspaso.title"/></h1>	
	
	<!-- TraspasoDeuda -->
	<fieldset>
		<legend><bean:message bundle="gde" key="gde.traspasoDeuda.title"/></legend>
		
		<table class="tabladatos">
			<!-- Recurso y Fecha -->
			<tr>
				<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
				<td class="normal"><bean:write name="traspasoDevolucionDeudaAdapterVO" property="traspasoDeuda.recurso.desRecurso"/></td>
				<td><label><bean:message bundle="gde" key="gde.traspasoDeuda.fecha" />: </label></td>
				<td class="normal"><bean:write name="traspasoDevolucionDeudaAdapterVO" property="traspasoDeuda.fechaView"/></td>
			</tr>
			<!-- Procurador Origen y Procurador Destino -->				
			<tr>
				<td><label><bean:message bundle="gde" key="gde.traspasoDevolucionDeudaAdapter.procuradorOrigen.label"/>: </label></td>
				<td class="normal"><bean:write name="traspasoDevolucionDeudaAdapterVO" property="traspasoDeuda.proOri.descripcion"/></td>
				<td><label><bean:message bundle="gde" key="gde.traspasoDevolucionDeudaAdapter.procuradorDestino.label"/>: </label></td>
				<td class="normal"><bean:write name="traspasoDevolucionDeudaAdapterVO" property="traspasoDeuda.proDes.descripcion"/></td>
			</tr>
			<!-- Cuenta y UsuarioAlta -->
			<tr>
				<td><label><bean:message bundle="pad" key="pad.cuenta.label" />: </label></td>
				<td class="normal"><bean:write name="traspasoDevolucionDeudaAdapterVO" property="traspasoDeuda.cuenta.numeroCuenta"/></td>
				<td><label><bean:message bundle="gde" key="gde.traspasoDeuda.usuarioAlta.label" />: </label></td>
				<td class="normal"><bean:write name="traspasoDevolucionDeudaAdapterVO" property="traspasoDeuda.usuarioAlta"/></td>					
			</tr>
			<!-- PlaEnvDeuProDest -->
			<tr>
				<td><label><bean:message bundle="gde" key="gde.traspasoDeuda.plaEnvDeuProDest.label"/>: </label></td>
				<td class="normal" colspan="3">	<bean:write name="traspasoDevolucionDeudaAdapterVO" property="traspasoDeuda.plaEnvDeuProDest.nroBarraAnioPlanillaView"/></td>
			</tr>
			<!-- Observacion -->
			<tr>
				<td><label><bean:message bundle="gde" key="gde.traspasoDevolucionDeudaAdapter.observacion"/>: </label></td>
				<td class="normal" colspan="3">	<bean:write name="traspasoDevolucionDeudaAdapterVO" property="traspasoDeuda.observacion"/></td>
			</tr>
		</table>
	</fieldset>	
	<!-- TraspasoDeuda -->
	
	<logic:notEqual name="traspasoDevolucionDeudaAdapterVO" property="act" value="eliminar">
		<!-- TraDeuDet -->	
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">            
			<caption><bean:message bundle="gde" key="gde.traspasoDeuda.listTraDeuDet.label"/></caption>
		    	<tbody>
					<logic:notEmpty  name="traspasoDevolucionDeudaAdapterVO" property="traspasoDeuda.listTraDeuDet">
				    	<tr>
				    		<logic:equal name="traspasoDevolucionDeudaAdapterVO" property="act" value="agregarTraDevDeuDet">
				    		<th width="1">&nbsp;</th> <!-- check -->
				    		</logic:equal>
							<th width="1">&nbsp;</th> <!-- Ver -->
							<th width="1">&nbsp;</th> <!-- Ver Constancia -->							
							<th align="left"><bean:message bundle="gde" key="gde.deuda.label"/></th>
							<th align="left"><bean:message bundle="gde" key="gde.deuda.importe.label"/></th>						
							<th align="left"><bean:message bundle="gde" key="gde.deuda.saldo.label"/></th>
							<th align="left"><bean:message bundle="gde" key="gde.deuda.fechaVencimiento.label"/></th>
							<th align="left"><bean:message bundle="gde" key="gde.traDeuDet.constanciaDeuOri"/></th>
						</tr>
						<logic:iterate id="TraDeuDetVO" name="traspasoDevolucionDeudaAdapterVO" property="traspasoDeuda.listTraDeuDet">
							<tr>
								<!--  check de agregacion -->
								<logic:equal name="traspasoDevolucionDeudaAdapterVO" property="act" value="agregarTraDevDeuDet">
									<!--  check de seleccion para agregar -->
									<td>
										<logic:equal name="traspasoDevolucionDeudaAdapterVO" property="agregarTraDevDeuDetEnabled" value="enabled">
											<logic:equal name="TraDeuDetVO" property="agregarBussEnabled" value="true">
												<html:multibox name="traspasoDevolucionDeudaAdapterVO" property="idsTraDevDeuDetsSelected">
													<bean:write name="TraDeuDetVO" property="deuda.idView"/>-<bean:write name="TraDeuDetVO" property="constanciaDeuOri.idView"/>
												</html:multibox>
											</logic:equal>
											<logic:notEqual name="TraDeuDetVO" property="agregarBussEnabled" value="true">
												<input type="checkbox" disabled="disabled"/>
											</logic:notEqual>
										</logic:equal>
										<logic:notEqual name="traspasoDevolucionDeudaAdapterVO" property="agregarTraDevDeuDetEnabled" value="enabled">
											<input type="checkbox" disabled="disabled"/>
										</logic:notEqual>
									</td>
								</logic:equal>	
								
								<!-- Ver -->
								<td>
									<logic:equal name="traspasoDevolucionDeudaAdapterVO" property="verTraDevDeuDetEnabled" value="enabled">
										<logic:equal name="TraDeuDetVO" property="verEnabled" value="enabled">
								  			<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verDetalleDeuda', '<bean:write name="TraDeuDetVO" property="idForDetalleDeudaView" />');">
									        	<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
									        </a>
										</logic:equal>
										<logic:notEqual name="TraDeuDetVO" property="verEnabled" value="enabled">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
										</logic:notEqual>
									</logic:equal>
									<logic:notEqual name="traspasoDevolucionDeudaAdapterVO" property="verTraDevDeuDetEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/selec1.gif"/>
									</logic:notEqual>
								</td>
								<!-- Ver Constancia Deuda -->
								<td>
									<logic:equal name="traspasoDevolucionDeudaAdapterVO" property="verTraDevDeuDetEnabled" value="enabled">
										<logic:equal name="TraDeuDetVO" property="verConstanciaBussEnabled" value="true">
								  			<a style="cursor: pointer; cursor: hand;" onclick="submitForm('verConstancia', '<bean:write name="TraDeuDetVO" property="conDeuDet.id" bundle="base" formatKey="general.format.id"/>');">
									        	<img title="<bean:message bundle="gde" key="traspasoDevolucionDeudaAdapter.button.verConstancia"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/constancia0.gif"/>
									        </a>
										</logic:equal>
										<logic:notEqual name="TraDeuDetVO" property="verConstanciaBussEnabled" value="true">
											<img border="0" src="<%=request.getContextPath()%>/images/iconos/constancia1.gif"/>
										</logic:notEqual>
									</logic:equal>
									<logic:notEqual name="traspasoDevolucionDeudaAdapterVO" property="verTraDevDeuDetEnabled" value="enabled">
										<img border="0" src="<%=request.getContextPath()%>/images/iconos/constancia1.gif"/>
									</logic:notEqual>
								</td>
								
								<td><bean:write name="TraDeuDetVO" property="deuda.anioPeriodoView"/>&nbsp;</td>
								<td><bean:write name="TraDeuDetVO" property="deuda.importe" bundle="base" formatKey="general.format.currency" />&nbsp;</td>
								<td><bean:write name="TraDeuDetVO" property="deuda.saldo"   bundle="base" formatKey="general.format.currency" />&nbsp;</td>
								
								<td><bean:write name="TraDeuDetVO" property="deuda.fechaVencimientoView"/>&nbsp;</td>
								<td><bean:write name="TraDeuDetVO" property="constanciaDeuOriView" />&nbsp;</td>
							</tr>
						</logic:iterate>
					</logic:notEmpty>
					<logic:empty  name="traspasoDevolucionDeudaAdapterVO" property="traspasoDeuda.listTraDeuDet">
						<tr><td align="center">
						<bean:message bundle="base" key="base.noExistenRegitros"/>
						</td></tr>
					</logic:empty>
				</tbody>
			</table>
			<!-- TraDeuDet -->
	</logic:notEqual>

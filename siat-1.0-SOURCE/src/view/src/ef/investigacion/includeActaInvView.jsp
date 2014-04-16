			<fieldset>
				<legend><bean:message bundle="ef" key="ef.actaInvAdapter.datosActaInv.label"/></legend>
				<table class="tabladatos">
					<!-- Numero -->
					<tr>
						<td><label><bean:message bundle="ef" key="ef.actaInv.numeroActa.label"/>: </label></td>
						<td class="normal" colspan="3">
							<bean:write name="actaInv" property="numeroActa" bundle="base" formatKey="general.format.id"/>
						</td>
					</tr>
	
					<!-- Anio -->
					<tr>
						<td><label><bean:message bundle="ef" key="ef.actaInv.anioActa.label"/>: </label></td>
						<td class="normal" colspan="3">
							<bean:write name="actaInv" property="anioActa" bundle="base" formatKey="general.format.id"/>
						</td>
					</tr>
					
					<!-- Fecha Inicio -->
					<tr>
						<td><label><bean:message bundle="ef" key="ef.actaInv.fechaInicio.label"/>: </label></td>
						<td class="normal" colspan="3">
							<bean:write name="actaInv" property="fechaInicioView"/>
						</td>
					</tr>	
	
					<!-- Fecha Fin -->
					<tr>
						<td><label><bean:message bundle="ef" key="ef.actaInv.fechaFin.label"/>: </label></td>
						<td class="normal" colspan="3">
							<bean:write name="actaInv" property="fechaFinView"/>
						</td>
					</tr>	
	
					<!-- Estado Acta -->
					<tr>
						<td><label><bean:message bundle="ef" key="ef.actaInv.estadoActa.label"/>: </label></td>
						<td class="normal" colspan="3">
							<b><bean:write name="actaInv" property="estadoActa.desEstadoActa"/></b>
						</td>
					</tr>	
								
					<!-- Investigador -->
					<tr>
						<td><label><bean:message bundle="ef" key="ef.investigador.label"/>: </label></td>
						<td class="normal" colspan="3">
							<bean:write name="actaInv" property="investigador.desInvestigador"/>
						</td>
					</tr>	
							
					<!-- Obs -->
					<tr>
						<td><label><bean:message bundle="ef" key="ef.actaInv.observacion.label"/>: </label></td>
						<td class="normal" colspan="3">
							<bean:write name="actaInv" property="observacion"/>
						</td>
					</tr>
					
					<!-- otros datos -->
					<tr>
						<td><label><bean:message bundle="ef" key="ef.actaInv.otrosDatos.label"/>: </label></td>
						<td class="normal" colspan="3">
							<bean:write name="actaInv" property="otrosDatos"/>
						</td>
					</tr>												
				</table>
			</fieldset>	
			<!-- datos del objImp solo lectura -->
			<fieldset>
				<legend><bean:message bundle="ef" key="ef.actaInvAdapter.datosObjImp.label"/></legend>						
				<table class="tabladatos">
					<!-- nroFicha -->
					<tr>
						<td><label><bean:message bundle="ef" key="ef.actaInv.nroFicha.label"/>: </label></td>
						<td class="normal" colspan="3">
							<bean:write name="actaInv" property="nroFicha" bundle="base" formatKey="general.format.id"/>
						</td>
					</tr>
					
					<!-- Fecha Inicio actividad -->
					<tr>
						<td><label><bean:message bundle="ef" key="ef.actaInv.fecIniAct.label"/>: </label></td>
						<td class="normal" colspan="3">
							<bean:write name="actaInv" property="fecIniActView"/>
						</td>
					</tr>	
					
					<!-- PerEnRelDep -->
					<tr>
						<td><label><bean:message bundle="ef" key="ef.actaInv.perEnRelDep.label"/>: </label></td>
						<td class="normal" colspan="3">
							<bean:write name="actaInv" property="perEnRelDep" bundle="base" formatKey="general.format.id"/>
						</td>
					</tr>
					
					<!-- ActDes -->
					<tr>
						<td><label><bean:message bundle="ef" key="ef.actaInv.actDes.label"/>: </label></td>
						<td class="normal" colspan="3">
							<bean:write name="actaInv" property="actDes"/>
						</td>
					</tr>
					
					<!-- LocRos -->
					<tr>
						<td><label><bean:message bundle="ef" key="ef.actaInv.locRos.label"/>: </label></td>
						<td class="normal" colspan="3">
							<bean:write name="actaInv" property="locRosario" bundle="base" formatKey="general.format.id"/>
						</td>
					</tr>
					
					<!-- LocOtrProv -->
					<tr>
						<td><label><bean:message bundle="ef" key="ef.actaInv.locOtrPro.label"/>: </label></td>
						<td class="normal" colspan="3">
							<bean:write name="actaInv" property="locOtrPro" bundle="base" formatKey="general.format.id"/>
						</td>
					</tr>					

					<!-- pubRod -->
					<tr>
						<td><label><bean:message bundle="ef" key="ef.actaInv.pubRod.label"/>: </label></td>
						<td class="normal" colspan="3">
							<bean:write name="actaInv" property="pubRod" bundle="base" formatKey="general.format.id"/>
						</td>
					</tr>					

					<!-- locfueRosEnSfe -->
					<tr>
						<td><label><bean:message bundle="ef" key="ef.actaInv.locFueRosEnSFe.label"/>: </label></td>
						<td class="normal" colspan="3">
							<bean:write name="actaInv" property="locFueRosEnSFe" bundle="base" formatKey="general.format.id"/>
						</td>
					</tr>
					
					<!-- ubicacionLocales -->
					<tr>
						<td><label><bean:message bundle="ef" key="ef.actaInv.ubicacionLocales.label"/>: </label></td>
						<td class="normal" colspan="3">
							<bean:write name="actaInv" property="ubicacionLocales"/>
						</td>
					</tr>
					
					<!-- cartelesPubl -->
					<tr>
						<td><label><bean:message bundle="ef" key="ef.actaInv.cartelesPubl.label"/>: </label></td>
						<td class="normal" colspan="3">
							<bean:write name="actaInv" property="cartelesPubl" bundle="base" formatKey="general.format.id"/>
						</td>
					</tr>
					
					<!-- copiaContrato -->
					<tr>
						<td><label><bean:message bundle="ef" key="ef.actaInv.copiaContrato.label"/>: </label></td>
						<td class="normal" colspan="3">
							<bean:write name="actaInv" property="copiaContrato"/>
						</td>
					</tr>					
						
					<!-- terceros -->
					<tr>
						<td><label><bean:message bundle="ef" key="ef.actaInv.terceros.label"/>: </label></td>
						<td class="normal" colspan="3">
							<bean:write name="actaInv" property="terceros"/>
						</td>
					</tr>	

					<!-- ticFacNom -->
					<tr>
						<td><label><bean:message bundle="ef" key="ef.actaInv.ticFacNom.label"/>: </label></td>
						<td class="normal" colspan="3">
							<bean:write name="actaInv" property="ticFacNom"/>
						</td>
					</tr>	
															
				</table>
			</fieldset>					
	<!-- FIN Datos del acta -->	
<!-- pageName: includeActaInvView.jsp -->
<!-- 
	Inclucion de datos del Procedimiento
 -->

		<!-- Procedimiento -->
		<fieldset>
			<legend><bean:message bundle="cyq" key="cyq.procedimiento.title"/></legend>
			
			<table class="tabladatos">
				
				<tr>
					<!-- Numero / Año -->
					<td>
						<label>
							<bean:message bundle="cyq" key="cyq.procedimiento.numero.label"/>/ 
							<bean:message bundle="cyq" key="cyq.procedimiento.anio.label"/>: 
						</label>
					</td>
					<td class="normal" colspan="3">
						<bean:write name="procedimientoVO" property="numeroView"/>/
						<bean:write name="procedimientoVO" property="anioView"/>
					</td>
				</tr>
				
				<tr>
					<!-- fecha Alta -->
					<td><label><bean:message bundle="cyq" key="cyq.procedimiento.fechaAlta.label"/>: </label></td>
					<td class="normal"><bean:write name="procedimientoVO" property="fechaAltaView"/></td>
					<!-- fecha boletin -->
					<td><label><bean:message bundle="cyq" key="cyq.procedimiento.fechaBoletin.label"/>: </label></td>
					<td class="normal"><bean:write name="procedimientoVO" property="fechaBoletinView"/></td>
				</tr>
				
				<tr>
					<!-- auto-->
					<td><label><bean:message bundle="cyq" key="cyq.procedimiento.auto.label"/>: </label></td>
					<td class="normal"><bean:write name="procedimientoVO" property="auto"/></td>
					<!-- fechaAuto // es la "Fecha de Actualizacion de Deuda" mostrada en la liquidacion de la deuda.--> 
					<td><label><bean:message bundle="cyq" key="cyq.procedimiento.fechaAuto.label"/>: </label></td>
					<td class="normal"><bean:write name="procedimientoVO" property="fechaAutoView"/></td>
				</tr>
				
				<!-- Caratula -->
				<tr>
					<td><label><bean:message bundle="cyq" key="cyq.procedimiento.caratula.label"/>: </label></td>
					<td class="normal" colspan="3"><bean:write name="procedimientoVO" property="caratula"/></td>
				</tr>				

				<!-- TipoProceso -->
				<tr>
					<td><label><bean:message bundle="cyq" key="cyq.tipoProceso.label"/>: </label></td>
					<td class="normal" colspan="3"><bean:write name="procedimientoVO" property="tipoProceso.desTipoProceso"/></td>
				</tr>
				<tr>
					<!-- Juzgado-->
					<td><label><bean:message bundle="cyq" key="cyq.juzgado.label"/>: </label></td>
					<td class="normal"><bean:write name="procedimientoVO" property="juzgado.desJuzgado"/></td>
					<!-- Abogado-->				
					<td><label><bean:message bundle="cyq" key="cyq.abogado.label"/>: </label></td>
					<td class="normal"><bean:write name="procedimientoVO" property="abogado.descripcion"/></td>
				</tr>

				<!-- fechaVerOpo 	// Fecha de Verificacion -->
				<tr>
					<td><label><bean:message bundle="cyq" key="cyq.procedimiento.fechaVerOpo.label"/>: </label></td>
					<td class="normal" colspan="3"><bean:write name="procedimientoVO" property="fechaVerOpoView"/></td>
				</tr>

			   	<!-- Contribuyente -->
				<tr>
					<td colspan="4">
						<fieldset>
							<legend> 
								<a onclick="toggle(this, 'bloqueContribuyente')" style="cursor: pointer;"> (-) &nbsp; </a> 
								<bean:message bundle="pad" key="pad.contribuyente.label"/>
							</legend>

							<span id="bloqueContribuyente" style="display:block">
							
								<bean:define id="personaVO" name="procedimientoVO" property="contribuyente"/>
								
								<%@ include file="/pad/persona/includePersona.jsp"%>
							
							</span>
						</fieldset>
					</td>
				</tr>
				
				
				<!-- DesContribuyente -->
				<tr>
					<td><label><bean:message bundle="cyq" key="cyq.procedimiento.desContribuyente.label"/>: </label></td>
					<td class="normal"colspan="3"><bean:write name="procedimientoVO" property="desContribuyente"/></td>
				</tr>
				<!-- Domicilio -->
				<tr>
					<td><label><bean:message bundle="cyq" key="cyq.procedimiento.domicilio.label"/>: </label></td>
					<td class="normal" colspan="3"><bean:write name="procedimientoVO" property="domicilio"/></td>
				</tr>

				<tr>
					<!-- Nro expediente - numero del expediente del juzgado -->
					<td><label><bean:message bundle="cyq" key="cyq.procedimiento.numExp.label"/>: </label></td>
					<td class="normal"><bean:write name="procedimientoVO" property="numExpView" /></td>			
					<!-- Anio expediente - anio del expediente del juzgado -->
					<td><label><bean:message bundle="cyq" key="cyq.procedimiento.anioExp.label"/>: </label></td>
					<td class="normal"><bean:write name="procedimientoVO" property="anioExpView" /></td>			
				</tr>
		
				<!-- Caso - Expediente de la Municipalidad. -->
				<!-- Inclucion de CasoView -->
				<tr>
					<td><label><bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
					<td colspan="3">
						<bean:define id="IncludedVO" name="procedimientoVO"/>
						<%@ include file="/cas/caso/includeCasoView.jsp" %>				
					</td>
				</tr>
				<!-- Fin Inclucion de CasoView -->
	
				<!-- perOpoDeu // Sindico Designado  -->
				<tr>
					<td><label><bean:message bundle="cyq" key="cyq.procedimiento.perOpoDeu.label"/>: </label></td>
					<td class="normal" colspan="3"><bean:write name="procedimientoVO" property="perOpoDeu"/></td>
				</tr>	

				<tr>
					<!-- lugarOposicion // Domicilio Síndico -->
					<td><label><bean:message bundle="cyq" key="cyq.procedimiento.lugarOposicion.label"/>: </label></td>
					<td class="normal"><bean:write name="procedimientoVO" property="lugarOposicion"/></td>
					<!-- telefonoOposicion // Teléfono Sindico -->
					
					<td><label><bean:message bundle="cyq" key="cyq.procedimiento.telefonoOposicion.label"/>: </label></td>
					<td class="normal"><bean:write name="procedimientoVO" property="telefonoOposicion"/></td>
				</tr>
				
				<!-- observacion-->
				<tr>
					<td><label><bean:message bundle="cyq" key="cyq.procedimiento.observacion.label"/>: </label></td>
					<td class="normal" colspan="3"><bean:write name="procedimientoVO" property="observacion"/></td>
				</tr>
				
				<!-- EstadoProced -->
				<tr>
					<td><label><bean:message bundle="base" key="base.estado.label"/>: </label></td>
					<td class="normal" colspan="3"><bean:write name="procedimientoVO" property="estadoProced.desEstadoProced"/></td>
				</tr>
				
			</table>
		</fieldset>	
		<!-- Procedimiento -->

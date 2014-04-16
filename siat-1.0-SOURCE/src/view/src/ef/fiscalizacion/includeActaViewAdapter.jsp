	<!-- Acta -->
	<fieldset>
		<legend><bean:message bundle="ef" key="ef.acta.title"/></legend>
		
		<table class="tabladatos">
		
		<!-- nro Acta -->
		<tr>	
			<td><label><bean:message bundle="ef" key="ef.acta.nroActa.label"/>: </label></td>
			<td class="normal" colspan="3">
				<bean:write name="actaVO" property="numeroActaView"/>
			</td>					
		</tr>
				
		<!-- tipoActa -->
		<tr>	
			<td><label><bean:message bundle="ef" key="ef.tipoActa.label"/>: </label></td>
			<td class="normal" colspan="3">
				<bean:write name="actaVO" property="tipoActa.desTipoActa"/>
			</td>					
		</tr>
		
		<!-- fechaVisita -->
		<tr>
			<td><label><bean:message bundle="ef" key="ef.acta.fechaVisita.label"/>: </label></td>
			<td class="normal">
				<bean:write name="actaVO" property="fechaVisitaView"/>
			</td>

		<!-- HoraVisita -->
			<td><label><bean:message bundle="ef" key="ef.acta.horaVisita.label"/>: </label></td>
			<td class="normal">
				<bean:write name="actaVO" property="horaVisitaView" />
			</td>
		</tr>
		
		<!-- persona -->
		<tr>
			<td><label><bean:message bundle="pad" key="pad.persona.label"/>: </label></td>
			<td class="normal" colspan="3">
				<bean:write name="actaVO" property="persona.represent"/>				
			</td>
		</tr>
		
		<!-- en caracter -->
		<tr>
			<td><label><bean:message bundle="ef" key="ef.acta.enCaracter.label"/>: </label></td>
			<td class="normal" colspan="3"><bean:write name="actaVO" property="enCaracter"/></td>			
		</tr>
		
		<logic:notEqual name="actaVO" property="tipoActa.id" value="2">
			<!-- fechaPresentacion -->
			<tr>
				<td><label><bean:message bundle="ef" key="ef.acta.fechaPresentacion.label"/>: </label></td>
				<td class="normal">
					<bean:write name="actaVO" property="fechaPresentacionView"/>
				</td>
			
			<!-- horaPresentacion -->
				<td><label><bean:message bundle="ef" key="ef.acta.horaPresentacion.label"/>: </label></td>
				<td class="normal">
					<bean:write name="actaVO" property="horaPresentacionView"/>
				</td>
			</tr>
			
			<!-- luegarPresentacion -->
			<tr>
				<td><label><bean:message bundle="ef" key="ef.acta.lugarPresentacion.label"/>: </label></td>
				<td class="normal" colspan="3"><bean:write name="actaVO" property="lugarPresentacion"/></td>			
			</tr>
		</logic:notEqual>
		
		<logic:present name="modificarEncabezadoEnabled">
			<logic:notEqual name="modificarEncabezadoEnabled" value="null">
				<tr>
					<td colspan="4"> 					
						<input type="button" class="boton" <%=modificarEncabezadoEnabled%> onClick="submitForm('modificarEncabezado', 
							'<bean:write name="actaVO" property="id" bundle="base" formatKey="general.format.id"/>');" 
							value="<bean:message bundle="base" key="abm.button.modificar"/>"/>		
					</td>
				</tr>
			</logic:notEqual>	
		</logic:present>			
			<!-- <#Campos#> -->
		</table>
	</fieldset>
	<!-- Acta -->
		
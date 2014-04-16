	<fieldset>
		<legend><bean:message bundle="ef" key="ef.opeInv.title"/></legend>
		<table class="tabladatos">
			<tr>
				<td><label><bean:message bundle="ef" key="ef.planFiscal.label"/>: </label></td>
				<td class="normal"><bean:write name="opeInvVO" property="planFiscal.desPlanFiscal"/></td>
			</tr>
			<tr>
				<td><label><bean:message bundle="ef" key="ef.opeInv.desOpeInv.label"/>: </label></td>
				<td class="normal"><bean:write name="opeInvVO" property="desOpeInv"/></td>
			</tr>
			<tr>
				<td><label><bean:message bundle="ef" key="ef.opeInv.observacion.label"/>: </label></td>
				<td class="normal"><bean:write name="opeInvVO" property="observacion"/></td>				
			</tr>
			<tr>
				<td><label><bean:message bundle="ef" key="ef.opeInv.fechaInicio.label"/>: </label></td>
				<td class="normal"><bean:write name="opeInvVO" property="fechaInicioView"/></td>				
			</tr>
		</table>
	</fieldset>	
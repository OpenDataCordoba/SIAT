<!-- Emision CdM -->

	<!-- Obra -->
	<fieldset>
		<legend><bean:message bundle="rec" key="rec.obra.title"/></legend>
		<table class="tabladatos">
			<!-- obra -->
			<tr>
				<td><label><bean:message bundle="rec" key="rec.obra.label"/>: </label></td>
				<td class="normal"><bean:write name="emisionAdapterVO" property="emisionCdM.obra.desObra"/></td>
			</tr>
			<!-- Fecha Vto Primera cuota -->
			<tr>
				<td><label><bean:message bundle="emi" key="emi.emisionEditAdapter.fechaVencimiento.label"/>: </label></td>
				<td class="normal"><bean:write name="emisionAdapterVO" property="emisionCdM.fechaVencimientoView"/></td>
			</tr>
		</table>
	</fieldset>	
	<!-- Obra -->
		
<!-- Fin Emision CdM -->		

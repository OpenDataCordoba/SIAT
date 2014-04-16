<!-- Emision Corregida de CdM -->
	<fieldset>
		<legend><bean:message bundle="rec" key="rec.obra.title"/></legend>
		<table class="tabladatos">
			<tr>
				<!-- Obra -->
				<td><label><bean:message bundle="rec" key="rec.obra.label"/>: </label></td>
				<td class="normal"><bean:write name="emisionAdapterVO" property="emisionCorCdM.obra.desObra"/></td>
			</tr>

			<tr>	
				<!-- Emitir con valores actuales de Tipo de Obra -->
				<td><label><bean:message bundle="emi" key="emi.emisionCorCdMEditAdapter.valActTipObr.label"/>: </label></td>
				<td class="normal"><bean:write name="emisionAdapterVO" property="emisionCorCdM.valActTipObr.value"/></td>
			</tr>
		</table>
	</fieldset>	
<!-- Emision Corregida de CdM -->		

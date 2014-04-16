<!-- Parte del Edit Adapter de la Emision Corregida de CdM -->

	<fieldset>
		<legend><bean:message bundle="rec" key="rec.obra.title"/></legend>
		<table class="tabladatos">
			<tr>	
				<!-- Obra -->
				<td><label>(*)&nbsp;<bean:message bundle="rec" key="rec.obra.label"/>: </label></td>
				<td class="normal">
					<html:select name="emisionAdapterVO" property="emisionCorCdM.obra.id" styleClass="select">
						<html:optionsCollection name="emisionAdapterVO" property="emisionCorCdM.listObra" label="desObra" value="id" />
					</html:select>
				</td>					
			</tr>

			<tr>	
				<!-- Emitir con valores actuales de Tipo de Obra -->
				<td><label>(*)&nbsp;<bean:message bundle="emi" key="emi.emisionCorCdMEditAdapter.valActTipObr.label"/>: </label></td>
				<td class="normal">
					<html:select name="emisionAdapterVO" property="emisionCorCdM.valActTipObr.id" styleClass="select">
						<html:optionsCollection name="emisionAdapterVO" property="emisionCorCdM.listSiNo" label="value" value="id" />
					</html:select>
				</td>					
			</tr>

		</table>
	</fieldset>

<!-- Parte del Edit Adapter de Impresion CdM -->

<fieldset>
	<legend><bean:message bundle="emi" key="emi.impresionCdMEditAdapter.title"/></legend>
	<table class="tabladatos">
		<!-- Anio -->
		<tr>
			<td><label>(*)&nbsp;<bean:message bundle="emi" key="emi.impresionCdMEditAdapter.anio.label"/>: </label></td>
			<td class="normal"><html:text name="emisionAdapterVO" property="impresionCdM.anioView" size="4" maxlength="6"/></td>			
		</tr>

		<!-- Mes -->
		<tr>
			<td><label>(*)&nbsp;<bean:message bundle="emi" key="emi.impresionCdMEditAdapter.mes.label"/>: </label></td>
			<td class="normal"><html:text name="emisionAdapterVO" property="impresionCdM.mesView" size="4" maxlength="6"/></td>			
		</tr>

		<!-- Obras -->
		<tr>	
			<td><label>(*)&nbsp;<bean:message bundle="emi" key="emi.impresionCdMEditAdapter.obras.label"/>: </label></td>
			<td class="normal">
				<html:select name="emisionAdapterVO" property="impresionCdM.obra.id" styleClass="select">
					<html:optionsCollection name="emisionAdapterVO" property="impresionCdM.listObra" label="desObraConNumeroObra" value="id" />
				</html:select>
			</td>					
		</tr>

		<!-- Formato de salida -->
		<tr>	
			<td><label>(*)&nbsp;<bean:message bundle="emi" key="emi.impresionCdMEditAdapter.formatoSalida.label"/>: </label></td>
			<td class="normal">
				<html:select name="emisionAdapterVO" property="impresionCdM.formatoSalida.id" styleClass="select">
					<html:optionsCollection name="emisionAdapterVO" property="impresionCdM.listFormatoSalida" label="value" value="id" />
				</html:select>
			</td>					
		</tr>

		<!-- Impresion Total -->
		<tr>	
			<td><label><bean:message bundle="emi" key="emi.impresionCdMEditAdapter.imprimeTodo.label"/>: </label></td>
			<td class="normal"><html:radio name="emisionAdapterVO" property="impresionCdM.impresionTotal.id" value="1" /></td>
		</tr>

		<!-- Impresion Parcial -->
		<tr>
			<td><label><bean:message bundle="emi" key="emi.impresionCdMEditAdapter.imprimeParcial.label"/>: </label></td>
			<td class="normal"><html:radio name="emisionAdapterVO" property="impresionCdM.impresionTotal.id" value="0" /></td>
		</tr>

		<logic:equal name="emisionAdapterVO" property="act" value="modificar">
			<!-- Estado del Proceso -->
			<tr>
				<td><label><bean:message bundle="base" key="base.estado.label"/>: </label></td>
				<td class="normal"><bean:write name="emisionAdapterVO" 
					property="emision.corrida.estadoCorrida.desEstadoCorrida"/></td>
			</tr>
		</logic:equal>

	</table>
</fieldset>


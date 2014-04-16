<!-- Impresion CdM -->

<fieldset>
	<legend><bean:message bundle="emi" key="emi.impresionCdMEditAdapter.title"/></legend>
	<table class="tabladatos">
		<!-- Año -->
		<tr>
			<td><label><bean:message bundle="emi" key="emi.impresionCdMEditAdapter.anio.label"/>: </label></td>
			<td class="normal"><bean:write name="emisionAdapterVO" property="impresionCdM.anioView"/></td>
		</tr>
		<!-- Mes -->
		<tr>
			<td><label><bean:message bundle="emi" key="emi.impresionCdMEditAdapter.mes.label"/>: </label></td>
			<td class="normal"><bean:write name="emisionAdapterVO" property="impresionCdM.mesView"/></td>
		</tr>
		<!-- Obra -->
		<tr>
			<td><label><bean:message bundle="emi" key="emi.impresionCdMEditAdapter.obras.label"/>: </label></td>
			<td class="normal"><bean:write name="emisionAdapterVO" property="impresionCdM.obra.desObra"/></td>
		</tr>
		<!-- Formato de Salida -->
		<tr>
			<td><label><bean:message bundle="emi" key="emi.impresionCdMEditAdapter.formatoSalida.label"/>: </label></td>
			<td class="normal"><bean:write name="emisionAdapterVO" property="impresionCdM.formatoSalida.value"/></td>
		</tr>
		
		<!-- Modo de Impresion -->
		<tr>
			<logic:equal name="emisionAdapterVO" property="impresionCdM.impresionTotal.id" value="0">
				<td><label><bean:message bundle="emi" key="emi.impresionCdMEditAdapter.modo.label"/>: </label></td>
				<td class="normal"> <bean:message bundle="emi" key="emi.impresionCdMEditAdapter.imprimeParcial.label"/></label></td>
			</logic:equal>
				
			<logic:equal name="emisionAdapterVO" property="impresionCdM.impresionTotal.id" value="1">
				<td><label><bean:message bundle="emi" key="emi.impresionCdMEditAdapter.modo.label"/>: </label></td>
				<td class="normal"> <bean:message bundle="emi" key="emi.impresionCdMEditAdapter.imprimeTodo.label"/></label></td>
			</logic:equal>
		</tr>

		<!-- Estado del Proceso -->
		<tr>
			<td><label><bean:message bundle="base" key="base.estado.label"/>: </label></td>
			<td class="normal"><bean:write name="emisionAdapterVO" 
				property="emision.corrida.estadoCorrida.desEstadoCorrida"/></td>
		</tr>

	</table>
</fieldset>	

	

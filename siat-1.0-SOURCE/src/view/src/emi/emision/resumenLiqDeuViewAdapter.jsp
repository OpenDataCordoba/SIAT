<!-- Parte del Edit Adapter de Emision Resumen Liq Deuda -->

	<!-- Fecha Vencimiento -->
	<fieldset>
		<legend><bean:message bundle="emi" key="emi.procesoEmisionResumenLiqDeuAdapter.fecha.title"/></legend>
		<table class="tabladatos">
			<tr>	
				<td><label><bean:message bundle="emi" key="emi.emisionResumenLiqDeu.fechaVencimiento.label"/> </label></td>
				<td class="normal">
					<bean:write name="emisionAdapterVO" property="emisionResumenLiqDeu.fechaVencimientoView"/>
				</td>					
			</tr>
		</table>
	</fieldset>

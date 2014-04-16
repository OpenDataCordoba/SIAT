<!-- Parte del Edit Adapter de Emision Resumen Liq Deuda -->

	<!-- Fecha Vencimiento -->
	<fieldset>
		<legend><bean:message bundle="emi" key="emi.procesoEmisionResumenLiqDeuAdapter.fecha.title"/></legend>
		<table class="tabladatos">
			<tr>	
				<td><label>(*)&nbsp;<bean:message bundle="emi" key="emi.emisionResumenLiqDeu.fechaVencimiento.label"/> </label></td>
				<td class="normal">
					<html:text name="emisionAdapterVO" property="emisionResumenLiqDeu.fechaVencimientoView" styleId="fechaVencimientoView" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaVencimientoView');" id="a_fechaVencimientoView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>					
			</tr>
		</table>
	</fieldset>

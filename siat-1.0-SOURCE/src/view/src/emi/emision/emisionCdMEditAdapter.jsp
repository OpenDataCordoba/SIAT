<!-- Parte del Edit Adapter de Emision CdM -->

	<!-- Obra -->
	<fieldset>
		<legend><bean:message bundle="rec" key="rec.obra.title"/></legend>
		<table class="tabladatos">
			<!-- Obra -->
			<tr>	
				<td><label>(*)&nbsp;<bean:message bundle="rec" key="rec.obra.label"/>: </label></td>
				<td class="normal">
					<html:select name="emisionAdapterVO" property="emisionCdM.obra.id" styleClass="select">
						<html:optionsCollection name="emisionAdapterVO" property="emisionCdM.listObra" label="desObra" value="id" />
					</html:select>
				</td>					
			</tr>
			<!-- Fecha Vencimiento de la primera cuota -->
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="emi" key="emi.emisionEditAdapter.fechaVencimiento.label"/>: </label></td>
				<td class="normal">
					<html:text name="emisionAdapterVO" property="emisionCdM.fechaVencimientoView" styleId="fechaVencimientoView" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaVencimientoView');" id="a_fechaVencimientoView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>
			</tr>
		</table>
	</fieldset>

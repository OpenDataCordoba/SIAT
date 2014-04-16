<fieldset>
	<legend><bean:message bundle="ef" key="ef.ordConBasImp.label"/></legend>
	
	<table class="tabladatos">
		<tr>
			<td><label><bean:message bundle="ef" key="ef.fuenteInfo.label"/>: </label></td>
			<td class="normal"><bean:write name="OrdConBasImpVO" property="compFuente.plaFueDat.tituloView"/></td>
			
		</tr>
		<tr>
			<td><label><bean:message bundle="ef" key="ef.ordConBasImp.periodoDesde.label"/>: </label></td>
			<td class="normal"><bean:write name="OrdConBasImpVO" property="periodoAnioDesdeView"/></td>
			
		</tr>
		<tr>
			<td><label><bean:message bundle="ef" key="ef.ordConBasImp.periodoHasta.label"/>: </label></td>
			<td class="normal"><bean:write name="OrdConBasImpVO" property="periodoAnioHastaView"/></td>
			
		</tr>																
	</table>
</fieldset>	

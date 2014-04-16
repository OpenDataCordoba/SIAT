<!-- Emision Tgi -->
	<!-- Zona -->
	<fieldset> 
		<legend><bean:message bundle="def" key="def.zona.title"/></legend>
		<table class="tabladatos">
			<!-- Zona -->
			<tr>	
				<td><label><bean:message bundle="def" key="def.zona.descripcion.label"/>: </label></td>
				<td class="normal"><bean:write name="emisionAdapterVO" property="emisionTgi.zona.descripcion"/></td>				
			</tr>
		</table>
	</fieldset>
<!-- Fin Emision Tgi -->

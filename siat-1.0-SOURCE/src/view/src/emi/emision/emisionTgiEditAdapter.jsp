<!-- Parte del Edit Adapter de Emision Tgi -->

	<!-- Zona -->
	<fieldset>
		<legend><bean:message bundle="def" key="def.zona.title"/></legend>
		<table class="tabladatos">
			<!-- Zona -->
			<tr>	
				<td><label>(*)&nbsp;<bean:message bundle="def" key="def.zona.descripcion.label"/>: </label></td>
				<td class="normal">
					<html:select name="emisionAdapterVO" property="emisionTgi.zona.id" styleClass="select">
						<html:optionsCollection name="emisionAdapterVO" property="emisionTgi.genericAtrDefinition.atributo.domAtr.listDomAtrVal" label="desValor" value="valor" />
					</html:select>
				</td>					
			</tr>
		</table>
	</fieldset>

	<logic:equal name="solicitudSearchPageVO" property="mostrarFiltro" value="true">
		<fieldset>
		<legend><bean:message bundle="cas" key="cas.solicitudSearchPage.legendPendientes"/></legend>
			<table class="tabladatos">
				<!-- Tipo Solicitud -->
				<tr>
					<td><label><bean:message bundle="cas" key="cas.tipoSolicitud.label"/>: </label></td>
					<td class="normal" colspan="3">
						<html:select name="solicitudSearchPageVO" property="solicitud.tipoSolicitud.id" styleClass="select">
							<html:optionsCollection name="solicitudSearchPageVO" property="listTipoSolicitud" label="descripcion" value="id" />
						</html:select>
					</td>
				</tr>
	
				<!--  asunto -->
				<tr>
					<td><label><bean:message bundle="cas" key="cas.solicitud.asuntoSolicitud.label"/>: </label></td>
					<td class="normal" colspan="3">
						<html:text name="solicitudSearchPageVO" property="solicitud.asuntoSolicitud"/>					
					</td>
				</tr>
				
				<!-- cuenta -->
				<tr>
					<td><label><bean:message bundle="cas" key="cas.solicitud.cuenta.label"/>: </label></td>
					<td class="normal" colspan="3">
						<html:text name="solicitudSearchPageVO" property="solicitud.cuenta.numeroCuenta"/>
						<html:button property="btnBuscarCuenta"  styleClass="boton" onclick="submitForm('buscarCuenta', '');">
							<bean:message bundle="cas" key="cas.solicitudSearchPage.button.buscarCuenta"/>
						</html:button>					
					</td>
				</tr>
	
			</table>
			<p align="center">
				&nbsp;
			  	<html:button property="btnBuscar"  styleClass="boton" onclick="submitForm('buscar', '');">
					<bean:message bundle="cas" key="cas.solicitudSearchPage.button.filtrar"/>
				</html:button>
			</p>					
		</fieldset>	
	</logic:equal>
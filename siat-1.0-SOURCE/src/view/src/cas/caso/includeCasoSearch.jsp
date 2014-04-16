<%/*
 			************ Deprecado ***************
*/%>
	<table>
		<tr>
			<td class="normal">
				<!-- No Posee Caso -->
				<logic:equal name="IncludedVO" property="caso.esEditable" value="true">
					<select name="<bean:write name="voName"/>.caso.sistemaOrigen.id" styleClass="select">
						<logic:iterate id="SistemOrigenVO" name="IncludedVO" property="listSistemaOrigen">
							<option value='<bean:write name="SistemOrigenVO" property="id" bundle="base" formatKey="general.format.id"/>'
									<bean:define id="casoId" name="SistemOrigenVO" property="id"/>
									<% String idCaso = ""+casoId+"";%>
				
									<logic:equal name="IncludedVO" property="caso.sistemaOrigen.id" value="<%=idCaso%>">
										selected="selected"											
									</logic:equal>
							>
								<bean:write name="SistemOrigenVO" property="desSistemaOrigen"/>
							</option>
						</logic:iterate>
					</select>
					
					&nbsp;
				
					<input type="text" name="<bean:write name="voName"/>.caso.numero" 
						value='<bean:write name="IncludedVO" property="caso.numero"/>' size="10"/>
					
					&nbsp;
					
					<html:button property="btnValidarCaso"  styleClass="boton" 
						onclick="submitForm('validarCaso', '');">
						<bean:message bundle="cas" key="cas.button.validarCaso"/>							
					</html:button>
					
					<input type="hidden" name="<bean:write name="voName"/>.caso.accion" value="agregar"/>
				</logic:equal>
				
				<!-- Posee Caso -->
				<logic:equal name="IncludedVO" property="caso.esEditable" value="false">
					<select name="<bean:write name="voName"/>.caso.sistemaOrigen.id" styleClass="select" disabled="disabled">
						<logic:iterate id="SistemOrigenVO" name="IncludedVO" property="listSistemaOrigen">
							<option value='<bean:write name="SistemOrigenVO" property="id" bundle="base" formatKey="general.format.id"/>'
									<bean:define id="casoId" name="SistemOrigenVO" property="id"/>
									<% String idCaso = ""+casoId+"";%>
				
									<logic:equal name="IncludedVO" property="caso.sistemaOrigen.id" value="<%=idCaso%>">
										selected="selected"											
									</logic:equal>
							>
								<bean:write name="SistemOrigenVO" property="desSistemaOrigen"/>
							</option>
						</logic:iterate>
					</select>
					
					&nbsp;
					
					<input type="text" name="<bean:write name="voName"/>.caso.numero" 
						value='<bean:write name="IncludedVO" property="caso.numero"/>' size="10" disabled="disabled"/>
					
					&nbsp;
						
					<button type="button" name="btnQuitarCaso"  styleClass="boton" 
						onclick="clearCasoValues('<bean:write name="voName"/>');">
						<bean:message bundle="cas" key="cas.button.quitarCaso"/>
					</button>

					<input type="hidden" name="<bean:write name="voName"/>.caso.accion" value="eliminar"/>
				</logic:equal>
			</td>
		</tr>
	</table>
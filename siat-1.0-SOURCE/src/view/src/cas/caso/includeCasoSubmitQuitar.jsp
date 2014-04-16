<%/*
Muestra los datos de caso para Agregar o Quitar segun corresponda.
	Reciebe un "IncludedVO" que es un VO que poee un CasoVO.
	y un "voName" que es un String con el nombre del IncludeVO.
*/%>

<%@ page import="ar.gov.rosario.siat.cas.iface.model.SistemaOrigenVO, ar.gov.rosario.siat.cas.iface.model.CasoCache" %>

<table>
	<tr>
		<td class="normal">
			<!-- No Posee Caso -->
			<logic:equal name="IncludedVO" property="caso.esEditable" value="true">
				<select name="<bean:write name="voName"/>.caso.sistemaOrigen.id" styleClass="select">
					<%for(ar.gov.rosario.siat.cas.iface.model.SistemaOrigenVO sistemaOrigenVO:ar.gov.rosario.siat.cas.iface.model.CasoCache.getInstance().getListSistemaOrigen()){%>
						<option value='<%=sistemaOrigenVO.getId()%>'
							<% String idCaso = ""+ sistemaOrigenVO.getId() +"";%>
							<logic:equal name="IncludedVO" property="caso.sistemaOrigen.id" value="<%=idCaso%>">
									selected="selected"											
							</logic:equal>
						>
							<%=sistemaOrigenVO.getDesSistemaOrigen()%>
						</option>
					<%}%>
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
					<%for(ar.gov.rosario.siat.cas.iface.model.SistemaOrigenVO sistemaOrigenVO:ar.gov.rosario.siat.cas.iface.model.CasoCache.getInstance().getListSistemaOrigen()){%>
						<option value='<%=sistemaOrigenVO.getId()%>'
							<% String idCaso = ""+ sistemaOrigenVO.getId() +"";%>
							<logic:equal name="IncludedVO" property="caso.sistemaOrigen.id" value="<%=idCaso%>">
									selected="selected"											
							</logic:equal>
						>
							<%=sistemaOrigenVO.getDesSistemaOrigen()%>
						</option>
					<%}%>
				</select>
				
				&nbsp;
				
				<input type="text" name="<bean:write name="voName"/>.caso.numero" 
					value='<bean:write name="IncludedVO" property="caso.numero"/>' size="10" disabled="disabled"/>
				
				&nbsp;
					
				<button type="button" name="btnQuitarCaso"  styleClass="boton" 
					onclick="submitForm('quitarCaso','');">
					<bean:message bundle="cas" key="cas.button.quitarCaso"/>
				</button>

				<input type="hidden" name="<bean:write name="voName"/>.caso.accion" value=""/>
			</logic:equal>
		</td>
	</tr>
</table>
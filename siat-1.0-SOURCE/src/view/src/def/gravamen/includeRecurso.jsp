<%/*
Muestra combo de Recursos:
Parametros:
  includeRecursoList:        List<RecursoVO>  Lista con los recursos a mostrar
  includeIdRecursoSelected:  Id del recurso seleccionado.
*/%>

<% String idCategoriaAnt = "";%>
<logic:notEmpty name="includeRecursoList">
	<logic:iterate id="recurso" name="includeRecursoList">
		<logic:equal name="recurso" property="id" value="-1">
			<option value='<bean:write name="recurso" property="id" bundle="base" formatKey="general.format.id"/>'>
				<bean:write name="recurso" property="desRecurso"/>
			</option>
		</logic:equal>
		<logic:notEqual name="recurso" property="id" value="-1">
			<logic:notEqual name="recurso" property="categoria.id" value="<%=idCategoriaAnt%>">
				<optgroup label='<bean:write name="recurso" property="categoria.desCategoria"/>'>
				<bean:define id="catId" name="recurso" property="categoria.id"/>
				<% idCategoriaAnt = ""+catId+"";%>
			</logic:notEqual>
			<logic:equal name="recurso" property="categoria.id" value="<%=idCategoriaAnt%>">
				<bean:define id="recId" name="recurso" property="id"/>
				<% String idRecurso = ""+recId+"";%>				
					<option value='<bean:write name="recurso" property="id" bundle="base" formatKey="general.format.id"/>' 						
						<logic:equal name="includeIdRecursoSelected" value="<%=idRecurso%>">
							selected="selected"											
						</logic:equal>
					>
					<bean:write name="recurso" property="desRecurso"/>
				</option>
			</logic:equal>
			<logic:notEqual name="recurso" property="categoria.id" value="<%=idCategoriaAnt%>">	
				</optgroup>
			</logic:notEqual>
		</logic:notEqual>
	</logic:iterate>
</logic:notEmpty>
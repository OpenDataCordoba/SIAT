<%/*
Busqueda avanzada de recurso:
Parametros:
	adapterVO:       Adapter que posee la lista de Recursos a mostrar
	poseeParam:      Bandera que indica si debemos dispara el paramRecurso al seleccionar.
*/%>

<h1><bean:message bundle="def" key="def.recurso.filtro.title"/></h1>

<fieldset>
		<legend><bean:message bundle="def" key="def.recurso.filtro.legend"/></legend>

	<table class="tabladatos">
		<tr>
			<td>&nbsp;</td>
			<td class="normal">
				<ul class="vinieta"><li><bean:message bundle="def" key="def.recurso.filtro.description"/> </li></ul>
			</td>				
		</tr>
		<tr>
			<td>
				<label class="filtro"><bean:message bundle="def" key="def.recurso.filtro.label"/>: </label>
			</td>
			<td class="normal">
				<input type="text" name="recursoFilter" id="recursoFilter" size="15" onkeyup="fillin(this, 'cboRecursoSearch');" >
			</td>
		</tr>
		<tr>	
			<td>	
				<label><bean:message bundle="def" key="def.recurso.label"/>: </label>
			</td>
			<td class="normal">
				
				<!-- Retorna disparando paramRecurso -->
				<logic:equal name="poseeParam" value="true">
					<select class="select" id="cboRecursoSearch" size="10" style="width:90%" ondblclick="selectRecurso('cboRecursoSearch', 'cboRecurso'); submitForm('paramRecurso', '');" >
				</logic:equal>
				<!-- Retorna sin disparar param -->
				<logic:notEqual name="poseeParam" value="true">
					<select class="select" id="cboRecursoSearch" size="10" style="width:90%" ondblclick="selectRecurso('cboRecursoSearch', 'cboRecurso'); toggleSearchRecurso('blockBusqueda', 'blockSimple');" >			
				</logic:notEqual>	
					<logic:iterate id="recurso" name="adapterVO" property="listRecurso">
						<option value='<bean:write name="recurso" property="id" bundle="base" formatKey="general.format.id"/>'>
							<bean:write name="recurso" property="desRecurso"/>
						</option>
					</logic:iterate>
				</select>
			</td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td class="normal">
				<input type="button" class="boton" value='<bean:message bundle="def" key="def.recurso.filtro.button.cancelar"/>' onclick="toggleSearchRecurso('blockBusqueda', 'blockSimple');" />
				
				<!-- Retorna disparando paramRecurso -->
				<logic:equal name="poseeParam" value="true">
					<input type="button" class="boton" value='<bean:message bundle="def" key="def.recurso.filtro.button.seleccionar"/>' onclick="selectRecurso('cboRecursoSearch', 'cboRecurso'); submitForm('paramRecurso', '');" />
				</logic:equal>
				<!-- Retorna sin disparar param -->
				<logic:notEqual name="poseeParam" value="true">
					<input type="button" class="boton" value='<bean:message bundle="def" key="def.recurso.filtro.button.seleccionar"/>' onclick="selectRecurso('cboRecursoSearch', 'cboRecurso'); toggleSearchRecurso('blockBusqueda', 'blockSimple');" />			
				</logic:notEqual>	
			</td>
		</tr>
	</table>
</fieldset>	
	
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Formulario filter -->
<html:form styleId="filter" action="/${modulo}/AdministrarEnc${Bean}.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
	
	<h1><bean:message bundle="${modulo}" key="${modulo}.${bean}EditAdapter.title"/></h1>		

	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
	   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
	
	<!-- ${Bean} -->
	<fieldset>
		<legend><bean:message bundle="${modulo}" key="${modulo}.${bean}.title"/></legend>
		
		<table class="tabladatos">
			<!-- <#Campos#> -->
			<#Campos.TextCodigo#>
			<tr>
				<td><label><bean:message bundle="${modulo}" key="${modulo}.${bean}.cod${Bean}.label"/>: </label></td>
				<td class="normal">
					<logic:equal name="enc${Bean}AdapterVO" property="act" value="modificar">
						<bean:write name="enc${Bean}AdapterVO" property="${bean}.cod${Bean}"/>
				 	</logic:equal>
					<logic:equal name="enc${Bean}AdapterVO" property="act" value="agregar">
						<html:text name="enc${Bean}AdapterVO" property="${bean}.cod${Bean}" size="15" maxlength="20" />
				 	</logic:equal>	
				</td>
			</tr>
			<#Campos.TextCodigo#>
			<#Campos.TextDescripcion#>
			<tr>
				<td><label><bean:message bundle="${modulo}" key="${modulo}.${bean}.des${Bean}.label"/>: </label></td>
				<td class="normal"><html:text name="enc${Bean}AdapterVO" property="${bean}.des${Bean}" size="20" maxlength="100"/></td>			
			</tr>
			<#Campos.TextDescripcion#>
			<#Campos.Combo#>
			<tr>	
				<td><label><bean:message bundle="${modulo}" key="${modulo}.${bean_a_listar}.label"/>: </label></td>
				<td class="normal">
					<html:select name="enc${Bean}AdapterVO" property="${bean}.${bean_a_listar}.id" styleClass="select">
						<html:optionsCollection name="enc${Bean}AdapterVO" property="list${Bean_a_listar}" label="des${Bean_a_listar}" value="id" />
					</html:select>
				</td>					
			</tr>
			<#Campos.Combo#>
			<#Campos.ComboSiNo#>
			<tr>	
				<td><label><bean:message bundle="${modulo}" key="${modulo}.${bean}.${propiedad}.label"/>: </label></td>
				<td class="normal">
					<html:select name="enc${Bean}AdapterVO" property="${bean}.${propiedad}.id" styleClass="select">
						<html:optionsCollection name="enc${Bean}AdapterVO" property="listSiNo" label="value" value="id" />
					</html:select>
				</td>					
			</tr>
			<#Campos.ComboSiNo#>
			<#Campos.Text#>
			<tr>
				<td><label><bean:message bundle="${modulo}" key="${modulo}.${bean}.${propiedad}.label"/>: </label></td>
				<td class="normal"><html:text name="enc${Bean}AdapterVO" property="${bean}.${propiedad}" size="20" maxlength="100"/></td>			
			</tr>
			<#Campos.Text#>
			<#Campos.TextFecha#>
			<tr>
				<td><label><bean:message bundle="${modulo}" key="${modulo}.${bean}.${propiedadFecha}.label"/>: </label></td>
				<td class="normal">
					<html:text name="enc${Bean}AdapterVO" property="${bean}.${propiedadFecha}View" styleId="${propiedadFecha}View" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('${propiedadFecha}View');" id="a_${propiedadFecha}View">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>
			</tr>
			<#Campos.TextFecha#>
			<#Campos.TextArea#>
			<tr>
				<td><label><bean:message bundle="${modulo}" key="${modulo}.${bean}.${propiedad}.label"/>: </label></td>
				<td class="normal" colspan="3">
					<html:textarea name="enc${Bean}AdapterVO" property="${bean}.${propiedad}" cols="80" rows="15"/>
				</td>
			</tr>
			<#Campos.TextArea#>
			<#Campos.BuscarBeanRef#>
			<tr>
				<td><label><bean:message bundle="${modulo_ref}" key="${modulo_ref}.${bean_ref}.${propiedad_ref}.ref"/>: </label></td>
				<td class="normal" colspan="3">
					<html:text name="enc${Bean}AdapterVO" property="${bean}.${bean_ref}.${propiedad_ref}" size="20" disabled="true"/>
					<html:button property="btnBuscar${Bean_Ref}"  styleClass="boton" onclick="submitForm('buscar${Bean_Ref}', '');">
						<bean:message bundle="${modulo}" key="${modulo}.${bean}Adapter.button.buscar${Bean_Ref}"/>
					</html:button>
				</td>
			</tr>
			<#Campos.BuscarBeanRef#>			
			<!-- <#Campos#> -->
		</table>
	</fieldset>
	<!-- ${Bean} -->
	
	<table class="tablabotones" width="100%">
		<tr>				
			<td align="left">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
			<td align="right">
				<logic:equal name="enc${Bean}AdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="enc${Bean}AdapterVO" property="act" value="agregar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('agregar', '');">
						<bean:message bundle="base" key="abm.button.agregar"/>
					</html:button>
				</logic:equal>				
			</td>
			</tr>
	</table>
   	
	<input type="hidden" name="method" value=""/>
	<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
	<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

	<input type="hidden" name="selectedId" value=""/>
	<input type="hidden" name="isSubmittedForm" value="true"/>

	<!-- Inclusion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>	
</html:form>
<!-- Fin formulario -->
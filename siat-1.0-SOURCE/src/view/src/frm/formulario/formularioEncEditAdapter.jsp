<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Formulario filter -->
<html:form styleId="filter" action="/frm/AdministrarEncFormulario.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
	
	<h1><bean:message bundle="frm" key="frm.formularioAdapter.title"/></h1>		
	
	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
	
	<!-- Formulario -->
	<fieldset>
		<legend><bean:message bundle="frm" key="frm.formulario.title"/></legend>
		
		<table class="tabladatos">
		<tr>
			<td><label>(*)&nbsp;<bean:message bundle="frm" key="frm.formulario.codFormulario.label"/>: </label></td>
			<td class="normal">
<!-- 				<logic:equal name="encFormularioAdapterVO" property="act" value="modificar">
					<bean:write name="encFormularioAdapterVO" property="formulario.codFormulario"/>
			 	</logic:equal>
				<logic:equal name="encFormularioAdapterVO" property="act" value="agregar"></logic:equal>
-->				
					<html:text name="encFormularioAdapterVO" property="formulario.codFormulario" size="15" maxlength="20" />	
			</td>
		</tr>
		<tr>
			<td><label>(*)&nbsp;<bean:message bundle="frm" key="frm.formulario.desFormulario.label"/>: </label></td>
			<td class="normal"><html:text name="encFormularioAdapterVO" property="formulario.desFormulario" size="20" maxlength="100"/></td>			
		</tr>
		<tr>	
			<td><label>(*)&nbsp;<bean:message bundle="frm" key="frm.desImp.label"/>: </label></td>
			<td class="normal">
				<html:select name="encFormularioAdapterVO" property="formulario.desImp.id" styleClass="select">
					<html:optionsCollection name="encFormularioAdapterVO" property="listDesImp" label="desDesImp" value="id" />
				</html:select>
			</td>					
		</tr>
			<!-- <#Campos#> -->
		</table>
	</fieldset>
	<!-- Formulario -->
	
	<table class="tablabotones" width="100%">
		<tr>				
			<td align="left">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
			<td align="right">
				<logic:equal name="encFormularioAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="encFormularioAdapterVO" property="act" value="agregar">
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

<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    <%@include file="/base/submitForm.js"%>	 
</script>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/def/AdministrarRecursoArea.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
	
	<h1><bean:message bundle="def" key="def.recursoAdapter.title"/></h1>		
	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
				<input type="button" 
					class="boton" 
					onclick="submitForm('volver', '<bean:write name="recursoAreaAdapterVO" property="recursoArea.area.id" bundle="base" formatKey="general.format.id" />');" 
					value="<bean:message bundle="base" key="abm.button.volver"/>" />
			</td>
		</tr>
	</table>

	<!-- Area -->
	<fieldset>
		<legend><bean:message bundle="def" key="def.area.title"/></legend>
		<table class="tabladatos">
			<!-- Codigo -->
			<tr>
				<td><label><bean:message bundle="def" key="def.area.codArea.label"/>: </label></td>
				<td class="normal"><bean:write name="recursoAreaAdapterVO" property="recursoArea.area.codArea"/></td>
			</tr>
			<!-- Descricion -->
			<tr>
				<td><label><bean:message bundle="def" key="def.area.desArea.label"/>: </label></td>
				<td class="normal"><bean:write name="recursoAreaAdapterVO" property="recursoArea.area.desArea"/></td>
			</tr>
			<!-- Estado -->
			<tr>
				<td><label><bean:message bundle="base" key="base.estado.label"/>: </label></td>
				<td class="normal"><bean:write name="recursoAreaAdapterVO" property="recursoArea.area.estado.value"/></td>
			</tr>
		</table>
	</fieldset>	
	<!-- Area -->

	<!-- RecursoArea Nuevo -->
	<fieldset>
		<legend><bean:message bundle="def" key="def.area.title"/></legend>			
		<table class="tabladatos">
			<tr>
				<td><label><bean:message bundle="def" key="def.recurso.label"/>: </label></td>
				<td class="normal">
					<bean:write name="recursoAreaAdapterVO" property="recursoArea.recurso.desRecurso"/>
				</td>
			</tr>
			
			<tr>
				<td><label><bean:message bundle="def" key="def.recursoArea.perCreaEmi.label"/>: </label></td>
				<td class="normal">
					<bean:write name="recursoAreaAdapterVO" property="recursoArea.perCreaEmi.value"/>
				</td>
			</tr>
			
		</table>
	</fieldset>
	<!-- Fin RecursoArea -->

	<table class="tablabotones" width="100%">
		<tr>				
			<td align="left">
    			<input type="button" 
					class="boton" 
					onclick="submitForm('volver', '<bean:write name="recursoAreaAdapterVO" property="recursoArea.area.id" bundle="base" formatKey="general.format.id" />');" 
					value="<bean:message bundle="base" key="abm.button.volver"/>" />
			</td>
   	    	<td align="right" width="50%">
				<logic:equal name="recursoAreaAdapterVO" property="act" value="eliminar">
					<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
						<bean:message bundle="base" key="abm.button.eliminar"/>
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
<!-- Fin Tabla que contiene todos los formularios -->
	
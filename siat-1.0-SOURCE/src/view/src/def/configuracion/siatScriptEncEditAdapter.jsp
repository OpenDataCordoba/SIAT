<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Formulario filter -->
<html:form styleId="filter" action="/def/AdministrarEncSiatScript.do">

	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>
	
	<h1><bean:message bundle="def" key="def.siatScriptEditAdapter.title"/></h1>		

	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
	   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
	
	<!-- SiatScript -->
	<fieldset>
		<legend><bean:message bundle="def" key="def.siatScript.title"/></legend>
		
		<table class="tabladatos">
			<!-- <#Campos#> -->
			<tr>
				<td><label><bean:message bundle="def" key="def.siatScript.codSiatScript.label"/>: </label></td>
				<td class="normal">
					<logic:equal name="encSiatScriptAdapterVO" property="act" value="modificar">
						<bean:write name="encSiatScriptAdapterVO" property="siatScript.codigo"/>
				 	</logic:equal>
					<logic:equal name="encSiatScriptAdapterVO" property="act" value="agregar">
						<html:text name="encSiatScriptAdapterVO" property="siatScript.codigo" size="15" maxlength="20" />
				 	</logic:equal>	
				</td>				
			</tr>	
			<tr>
				<td><label><bean:message bundle="def" key="def.siatScript.desSiatScript.label"/>: </label></td>
				<td class="normal"><html:text name="encSiatScriptAdapterVO" property="siatScript.descripcion" size="25" maxlength="100"/></td>			
			</tr>		
			<tr>
				<td><label><bean:message bundle="def" key="def.siatScript.pathSiatScript.label"/>: </label></td>
				<td class="normal"><html:text name="encSiatScriptAdapterVO" property="siatScript.path" size="25" maxlength="100"/></td>
			</tr>			
			<!-- <#Campos#> -->
			
		</table>
	</fieldset>
	<!-- SiatScript -->
	
			<!--Script SH -->
		<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%">
	    	<tbody>
	    		<tr>
	    			<td><bean:message bundle="def" key="def.siatScript.script.ref"/>:</td>
	    			<td><html:textarea style="height:300px;width:600px" name="encSiatScriptAdapterVO" property="siatScript.scriptFile"></html:textarea></td>
	    		</tr>	    							
			</tbody>
		</table>	
	
	<table class="tablabotones" width="100%">
		<tr>				
			<td align="left">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
			<td align="right">
				<logic:equal name="encSiatScriptAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="encSiatScriptAdapterVO" property="act" value="agregar">
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
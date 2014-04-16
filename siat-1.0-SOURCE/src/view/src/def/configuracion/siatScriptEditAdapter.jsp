<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/def/AdministrarSiatScript.do">
	
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
			<!-- codSiatScript -->
			<tr>
				<td><label><bean:message bundle="def" key="def.siatScript.codSiatScript.label"/>: </label></td>
				<td class="normal">
					<logic:equal name="siatScriptAdapterVO" property="act" value="modificar">
						<bean:write name="siatScriptAdapterVO" property="siatScript.codigo"/>
				 	</logic:equal>
					<logic:equal name="siatScriptAdapterVO" property="act" value="agregar">
						<html:text name="siatScriptAdapterVO" property="siatScript.codigo" size="15" maxlength="20" />
				 	</logic:equal>	
				</td>
			</tr>

			<!-- desSiatScript -->
			<tr>
				<td><label><bean:message bundle="def" key="def.siatScript.desSiatScript.label"/>: </label></td>
				<td class="normal"><html:text name="siatScriptAdapterVO" property="siatScript.descripcion" size="25" maxlength="100"/></td>			
			</tr>		
			<tr>
				<td><label><bean:message bundle="def" key="def.siatScript.pathSiatScript.label"/>: </label></td>
				<td class="normal"><html:text name="siatScriptAdapterVO" property="siatScript.path" size="25" maxlength="100"/></td>
			</tr>
			<!-- <#Campos#> -->
		</table>
	</fieldset>	
	<!-- SiatScript -->
	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="50%">
				<logic:equal name="siatScriptAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="siatScriptAdapterVO" property="act" value="agregar">
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
<!-- Fin Tabla que contiene todos los formularios -->
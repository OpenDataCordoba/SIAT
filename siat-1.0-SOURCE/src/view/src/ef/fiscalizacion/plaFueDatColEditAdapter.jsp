<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/ef/AdministrarPlaFueDatCol.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="ef" key="ef.plaFueDatColEditAdapter.title"/></h1>	

	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
	   			<input type="button" class="boton" onclick="submitForm('volver', '<bean:write name="plaFueDatColAdapterVO" property="plaFueDatCol.plaFueDat.id" bundle="base" formatKey="general.format.id"/>');"
					value="<bean:message bundle="base" key="abm.button.volver"/>"/>				
			</td>
		</tr>
	</table>
	
	<!-- PlaFueDatCol -->
	<fieldset>
		<legend><bean:message bundle="ef" key="ef.plaFueDatCol.title"/></legend>
		
		<table class="tabladatos">
		<!-- colName -->
		<tr>
			<td><label>(*)&nbsp;<bean:message bundle="ef" key="ef.plaFueDatCol.colName.label"/>&nbsp;<bean:write name="plaFueDatColAdapterVO" property="plaFueDatCol.nroColumnaView"/>: </label></td>
			<td class="normal"><html:text name="plaFueDatColAdapterVO" property="plaFueDatCol.colName" size="20" maxlength="100"/></td>			
		</tr>
		<!-- orden -->
		<tr>
			<td><label>(*)&nbsp;<bean:message bundle="ef" key="ef.plaFueDatCol.orden.label"/>: </label></td>
			<td class="normal"><html:text name="plaFueDatColAdapterVO" property="plaFueDatCol.orden" size="20" maxlength="100"/></td>			
		</tr>
		
		<!-- oculta -->
		<tr>
			<td><label><bean:message bundle="ef" key="ef.plaFueDatCol.oculta.label"/>: </label></td>
			<td class="normal"><html:checkbox name="plaFueDatColAdapterVO" property="ocultaChecked"/></td>			
		</tr>
			
		<!-- Suma en total -->
		<tr>
			<td><label><bean:message bundle="ef" key="ef.plaFueDatCol.sumaEnTotal.label"/>: </label></td>
			<td class="normal"><html:checkbox name="plaFueDatColAdapterVO" property="sumaEnTotalChecked"/></td>			
		</tr>		
		
			<!-- <#Campos#> -->
		</table>
	</fieldset>	
	<!-- PlaFueDatCol -->
	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="50%">
				<logic:equal name="plaFueDatColAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="plaFueDatColAdapterVO" property="act" value="agregar">
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
<!-- plaFueDatColEditAdapter.jsp -->

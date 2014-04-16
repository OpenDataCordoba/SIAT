<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/rec/AdministrarUsoCdM.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="rec" key="rec.usoCdMEditAdapter.title"/></h1>	

	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
	
	<!-- UsoCdM -->
	<fieldset>
		<legend><bean:message bundle="rec" key="rec.usoCdM.title"/></legend>
		
		<table class="tabladatos">
			<!-- DesUsoCdM -->
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="rec" key="rec.usoCdM.desUsoCdM.label"/>: </label></td>
				<td class="normal"><html:text name="usoCdMAdapterVO" property="usoCdM.desUsoCdM" size="20" maxlength="100"/></td>			
			</tr>

			<!-- Factor -->
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="rec" key="rec.usoCdM.factor.label"/>: </label></td>
				<td class="normal"><html:text name="usoCdMAdapterVO" property="usoCdM.factorView" size="20" maxlength="10"/></td>			
			</tr>
			
			<!-- Usos Catastro -->
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="rec" key="rec.usoCdM.usosCatastro.label"/>: </label></td>
				<td class="normal"><html:text name="usoCdMAdapterVO" property="usoCdM.usosCatastro" size="20" maxlength="100"/></td>			
			</tr>

			<logic:equal name="usoCdMAdapterVO" property="act" value="modificar">
				<!-- Estado -->
				<tr>
					<td><label><bean:message bundle="base" key="base.estado.label"/>: </label></td>
					<td class="normal"><bean:write name="usoCdMAdapterVO" property="usoCdM.estado.value"/></td>
				</tr>
			</logic:equal>

		</table>
	</fieldset>	
	<!-- UsoCdM -->
	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="50%">
				<logic:equal name="usoCdMAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="usoCdMAdapterVO" property="act" value="agregar">
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

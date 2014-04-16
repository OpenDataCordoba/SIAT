<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/gde/AdministrarDesGen.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="gde" key="gde.desGenEditAdapter.title"/></h1>	
	<table class="tablabotones" width="100%">
		<tr>
			<td align="right"><html:button property="btnVolver"
				styleClass="boton" onclick="submitForm('volver', '');">
				<bean:message bundle="base" key="abm.button.volver" />
			</html:button></td>
		</tr>
	</table>
	<!-- DesGen -->
	<fieldset>
		<legend><bean:message bundle="gde" key="gde.desGen.title"/></legend>
		
		<table class="tabladatos">
			<!-- TextDescripcion -->
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.desGen.desDesGen.label"/>: </label></td>
				<td class="normal"><html:text name="desGenAdapterVO" property="desGen.desDesGen" size="20" maxlength="100"/></td>			
			</tr>
			<!-- Leyenda -->
			<tr>
				<td><label><bean:message bundle="gde" key="gde.desGen.leyendaDesGen.label"/>: </label></td>
				<td class="normal"><html:text name="desGenAdapterVO" property="desGen.leyendaDesGen" size="20" maxlength="100"/></td>			
			</tr>					
			<!-- Porcentaje -->
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="gde" key="gde.desGen.porDes.label"/>: </label></td>
				<td class="normal"><html:text name="desGenAdapterVO" property="desGen.porDesView" size="20" maxlength="100"/></td>			
			</tr>
			
			<!-- Inclucion de Caso -->
			<tr>
				<td><label><bean:message bundle="cas" key="cas.caso.label"/>: </label></td>
				<td colspan="3">
					<bean:define id="IncludedVO" name="desGenAdapterVO" property="desGen"/>
					<bean:define id="voName" value="desGen" />
					<%@ include file="/cas/caso/includeCaso.jsp" %>
				</td>
			</tr>
			<!-- Fin Inclucion de Caso -->
			
		</table>
	</fieldset>	
	<!-- DesGen -->
	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="50%">
				<logic:equal name="desGenAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="desGenAdapterVO" property="act" value="agregar">
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

<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/gde/AdministrarPlaEnvDeuPro.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="gde" key="gde.plaEnvDeuProEditAdapter.title"/></h1>	
	
	<!-- PlaEnvDeuPro -->
	<fieldset>
		<legend><bean:message bundle="gde" key="gde.plaEnvDeuPro.title"/></legend>
		
		<table class="tabladatos">
		<!-- nroPlanilla -->
		<tr>
			<td><label><bean:message bundle="gde" key="gde.plaEnvDeuPro.nroPlanilla.label"/>: </label></td>
			<td class="normal"><bean:write name="plaEnvDeuProAdapterVO" property="plaEnvDeuPro.nroPlanillaView" /></td>
						
		<!-- anioPlanilla -->
			<td><label><bean:message bundle="gde" key="gde.plaEnvDeuPro.anioPlanilla.label"/>: </label></td>
			<td class="normal"><bean:write name="plaEnvDeuProAdapterVO" property="plaEnvDeuPro.anioPlanillaView"/></td>			
		</tr>
		
		<!-- procurador -->
		<tr>
			<td><label><bean:message bundle="gde" key="gde.procurador.label"/>: </label></td>
			<td class="normal"><bean:write name="plaEnvDeuProAdapterVO" property="plaEnvDeuPro.procurador.descripcion"/></td>			
		
		<!-- estado -->
			<td><label><bean:message bundle="gde" key="gde.plaEnvDeuPro.EstPlaEnvDeuPr.label"/>: </label></td>
			<td class="normal"><bean:write name="plaEnvDeuProAdapterVO" property="plaEnvDeuPro.estPlaEnvDeuPr.desEstPlaEnvDeuPro"/></td>			
		</tr>
		
		<!-- fechaEnvio -->
		<tr>
			<td><label><bean:message bundle="gde" key="gde.plaEnvDeuPro.fechaEnvio.label"/>: </label></td>
			<td class="normal"><bean:write name="plaEnvDeuProAdapterVO" property="plaEnvDeuPro.fechaEnvioView"/></td>			
		
		<!-- fechaHabilitacion -->
			<td><label><bean:message bundle="gde" key="gde.plaEnvDeuPro.fechaHabilitacion.label"/>: </label></td>
			<td class="normal"><bean:write name="plaEnvDeuProAdapterVO" property="plaEnvDeuPro.fechaHabilitacionView"/></td>			
		</tr>
		<tr>
			<td><label><bean:message bundle="gde" key="gde.plaEnvDeuPro.observaciones.label"/>: </label></td>
			<td class="normal" colspan="3">
				<html:textarea name="plaEnvDeuProAdapterVO" property="plaEnvDeuPro.observaciones" cols="80" rows="15"/>
			</td>
		</tr>
			<!-- <#Campos#> -->
		</table>
	</fieldset>	
	<!-- PlaEnvDeuPro -->
	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="50%">
				<logic:equal name="plaEnvDeuProAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="plaEnvDeuProAdapterVO" property="act" value="agregar">
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

<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/gde/AdministrarMandatario.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="gde" key="gde.mandatarioEditAdapter.title"/></h1>	

	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
	   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
	
	<!-- Mandatario -->
	<fieldset>
		<legend><bean:message bundle="gde" key="gde.mandatario.label"/></legend>
		
		<table class="tabladatos">
			
			<!-- TextDescripcion -->
			<tr>
				<td><label>(*)<bean:message bundle="gde" key="gde.mandatario.descripcion.label"/>: </label></td>
				<td class="normal"><html:text name="mandatarioAdapterVO" property="mandatario.descripcion" size="20" maxlength="100"/></td>			
			</tr>
			<!-- Domicilio -->
			<tr>
				<td><label><bean:message bundle="gde" key="gde.mandatario.domicilio.label"/>: </label></td>
				<td class="normal"><html:text name="mandatarioAdapterVO" property="mandatario.domicilio" size="20" maxlength="100"/></td>			
			</tr>
			<!-- Telefono -->
			<tr>
				<td><label><bean:message bundle="gde" key="gde.mandatario.telefono.label"/>: </label></td>
				<td class="normal"><html:text name="mandatarioAdapterVO" property="mandatario.telefono" size="20" maxlength="100"/></td>			
			</tr>
			<!-- HorarioAtencion -->
			<tr>
				<td><label><bean:message bundle="gde" key="gde.mandatario.horarioAtencion.label"/>: </label></td>
				<td class="normal"><html:text name="mandatarioAdapterVO" property="mandatario.horarioAtencion" size="20" maxlength="100"/></td>		
			</tr>
			<!-- Observaciones -->
			<tr>
				<td><label><bean:message bundle="gde" key="gde.mandatario.observaciones.label"/>: </label></td>
				<td class="normal"><html:textarea name="mandatarioAdapterVO" property="mandatario.observaciones" rows="10" cols="30"/></td>			
			</tr>
			
		</table>
	</fieldset>	
	<!-- Mandatario -->
	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="100%">
				<logic:equal name="mandatarioAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="mandatarioAdapterVO" property="act" value="agregar">
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
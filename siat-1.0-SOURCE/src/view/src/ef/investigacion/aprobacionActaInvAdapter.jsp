<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/ef/AdministrarAprobActaInv.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="ef" key="ef.aprobacionActaInvAdapter.title"/></h1>	
	
	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
		
	<!-- OpeInv -->
	<bean:define id="opeInvVO" name="aprobacionActaInvAdapterVO" property="opeInvCon.opeInv"/>
	<%@include file="/ef/investigacion/includeOpeInvView.jsp" %>	
	<!-- OpeInv -->
	
	<!-- OpeInvCon -->
	<fieldset>
		<legend><bean:message bundle="ef" key="ef.opeInvConAdapter.contribuyente.label"/></legend>
		
		<table class="tabladatos">
		<!-- Persona -->
		<tr>
			<td><label><bean:message bundle="pad" key="pad.persona.label"/>: </label></td>
			<td class="normal" colspan="3">
				<bean:write name="aprobacionActaInvAdapterVO" property="opeInvCon.datosContribuyente"/>
			</td>
		</tr>
		<tr>
			<td><label><bean:message bundle="ef" key="ef.opeInvCon.contribuyente.cuit"/>: </td>
			<td class="normal"><bean:write name="aprobacionActaInvAdapterVO" property="opeInvCon.contribuyente.persona.cuit"/></td>
		</tr>
		<tr>
			<td><label><bean:message bundle="pad" key="pad.persona.domicilio.label"/>: </td>
			<td class="normal"><bean:write name="aprobacionActaInvAdapterVO" property="opeInvCon.domicilio.view"/></td>
		</tr>
		
		<tr>
			<td><label><bean:message bundle="pad" key="pad.cuenta.label"/>: </label></td>
			<td class="normal" colspan="3">
				<bean:write name="aprobacionActaInvAdapterVO" property="opeInvCon.opeInvConCueCuentaSelec.nroCuenta"/>
			</td>
		</tr>	
		
		<logic:iterate id="TipObjImpAtrDefinition" name="aprobacionActaInvAdapterVO" property="definition4Cuenta.listTipObjImpAtrDefinition" indexId="count">
			<bean:define id="AtrVal" name="TipObjImpAtrDefinition"/>
			<%@ include file="/def/atrDefinition4View.jsp" %>
		</logic:iterate>
			
			<!-- <#Campos#> -->
		</table>
	</fieldset>	
	
		
	<!-- Datos del acta -->
	<bean:define id="actaInv" name="aprobacionActaInvAdapterVO" property="opeInvCon.actaInv"/>
	<%@include file="/ef/investigacion/includeActaInvView.jsp" %>										
	<!-- FIN Datos del acta -->
	
	
	
	<!-- estado del contribuyente -->
		<fieldset>
			<legend><bean:message bundle="ef" key="ef.aprobacionActaInvAdapter.cambioEstadoContr.label"/></legend>
			<table class="tabladatos">
					<tr>
						<td><label><bean:message bundle="ef" key="ef.actaInv.estadoContr.label"/>: </label></td>
						<td class="normal" colspan="3">
							<html:select name="aprobacionActaInvAdapterVO" property="opeInvCon.estadoOpeInvCon.id">
								<html:optionsCollection name="aprobacionActaInvAdapterVO" property="listEstadoOpeInvCon" label="desEstadoOpeInvCon" value="id"/>
							</html:select>							
						</td>
					</tr>
			</table>
		</fieldset>		
	<!-- fin estado del contribuyente -->
		
		
		
	<!-- Estado del acta -->	
		<fieldset>
			<legend><bean:message bundle="ef" key="ef.aprobacionActaInvAdapter.cambioEstadoActa.label"/></legend>
			<table class="tabladatos">
					<tr>
						<td><label><bean:message bundle="ef" key="ef.actaInv.estadoActa.label"/>: </label></td>
						<td class="normal" colspan="3">
							<html:select name="aprobacionActaInvAdapterVO" property="opeInvCon.actaInv.estadoActa.id">
								<html:optionsCollection name="aprobacionActaInvAdapterVO" property="listEstadoActa" label="desEstadoActa" value="id"/>
							</html:select>							
						</td>
					</tr>
			</table>
		</fieldset>	
	<!-- FIN Estado del acta -->	
	
	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="50%">
					<bean:define id="cambiarEstadoActaEnabled" name="aprobacionActaInvAdapterVO" property="cambiarEstadoActaEnabled"/>
					<input type="button" <%=cambiarEstadoActaEnabled%> class="boton" 
						onClick="submitForm('guardar', '0');" 
						value="<bean:message bundle="ef" key="ef.aprobacionActaInvAdapter.button.cambiarEstadoActa"/>"/>   	    	
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
<!-- pageName: aprobacionActaInvAdapter.jsp -->
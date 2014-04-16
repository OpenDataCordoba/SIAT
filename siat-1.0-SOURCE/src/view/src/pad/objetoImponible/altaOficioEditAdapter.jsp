<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/pad/AdministrarAltaOficio.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="pad" key="pad.altaOficioEditAdapter.title"/></h1>	
	
	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
	
	<!-- AltaOficio -->
	<fieldset>
		<legend><bean:message bundle="pad" key="pad.altaOficio.title"/></legend>
		
		<table class="tabladatos">
			<!-- Text -->
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="pad" key="pad.altaOficio.nroActa.label"/>: </label></td>
				<td class="normal"><html:text name="altaOficioAdapterVO" property="altaOficio.nroActa" size="20" maxlength="100"/></td>			
			</tr>
			<!-- Fecha -->
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="pad" key="pad.altaOficio.fecha.label"/>: </label></td>
				<td class="normal">
					<html:text name="altaOficioAdapterVO" property="altaOficio.fechaView" styleId="fechaView" size="15" maxlength="10" styleClass="datos" />
					<a class="link_siat" onclick="return show_calendar('fechaView');" id="a_fechaView">
						<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/></a>
				</td>
			</tr>
	
			<!-- Inspector -->
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="pad" key="pad.inspector.label"/>: </label></td>
				<td class="normal">
					<html:text name="altaOficioAdapterVO" property="altaOficio.inspector.desInspector" disabled="true" size="20" maxlength="100"/>
					<html:button property="btnBuscarCuenta" styleClass="boton" onclick="submitForm('buscarInspector', '');">
						<bean:message bundle="pad" key="pad.altaOficioAdapterVO.button.buscarInspector"/>
					</html:button>			
				</td>				
			</tr>			
		</table>
	</fieldset>	
	<!-- FIN AltaOficio -->
	
	<!-- Contribuyente -->
	<fieldset>
		<legend><bean:message bundle="pad" key="pad.contribuyente.title"/></legend>		
			<table class="tabladatos">
			
				<logic:equal name="altaOficioAdapterVO" property="act" value="agregar">
				<tr>
					<td><label>(*)&nbsp;<bean:message bundle="pad" key="pad.contribuyente.label"/>: </label></td>
					<td class="normal">
						<html:text name="altaOficioAdapterVO" property="contribuyente.persona.represent" disabled="true" size="20" maxlength="100"/>
						<html:button property="btnBuscarCuenta" styleClass="boton" onclick="submitForm('buscarPersona', '');">
							<bean:message bundle="pad" key="pad.altaOficioAdapterVO.button.buscarPersona"/>
						</html:button>			
					</td>				
				</tr>
				<tr>
					<td><label><bean:message bundle="pad" key="pad.persona.cuit.label"/>: </label></td>
					<td class="normal">
						<bean:write name="altaOficioAdapterVO" property="contribuyente.persona.cuit"/>								
					</td>				
				</tr>
				</logic:equal>
				
				<logic:equal name="altaOficioAdapterVO" property="act" value="modificar">
				<tr>
					<td><label><bean:message bundle="pad" key="pad.contribuyente.label"/>: </label></td>
					<td class="normal">
						<bean:write name="altaOficioAdapterVO" property="contribuyente.persona.represent"/>			
					</td>				
				</tr>
				<tr>
					<td><label><bean:message bundle="pad" key="pad.persona.cuit.label"/>: </label></td>
					<td class="normal">
						<bean:write name="altaOficioAdapterVO" property="contribuyente.persona.cuit"/>						
					</td>				
				</tr>		
				</logic:equal>
					
				<tr>
					<td><label><bean:message bundle="pad" key="pad.altaOficio.contribuyente.nroIsib.label"/>: </label></td>
					<td class="normal">
						<html:text name="altaOficioAdapterVO" property="contribuyente.nroIsib"/>								
					</td>				
				</tr>
			</table>
	</fieldset>	
	<!-- FIN Contribuyente -->
	
	
	<!-- Domicilio -->
	<logic:equal name="altaOficioAdapterVO" property="act" value="agregar">
		<bean:define id="domicilioVO" name="altaOficioAdapterVO" property="contribuyente.persona.domicilio"/>
		<bean:define id="listSiNo" name="altaOficioAdapterVO" 	 property="listSiNo"/>				
		<%@ include file="/pad/ubicacion/includeDomicilioEdit.jsp" %>
	</logic:equal>
	
	<logic:equal name="altaOficioAdapterVO" property="act" value="modificar">
		<bean:define id="domicilioVO" name="altaOficioAdapterVO" property="contribuyente.persona.domicilio"/>
		<bean:define id="listSiNo" name="altaOficioAdapterVO" 	 property="listSiNo"/>				
		<%@ include file="/pad/ubicacion/includeDomicilioView.jsp" %>
	</logic:equal>

	<!-- Domicilio -->	


	
	<!-- ObjImp (Comercio) -->	
	<fieldset>
		<legend><bean:message bundle="pad" key="pad.altaOficio.comercio.title"/></legend>
		
		<logic:notEmpty name="altaOficioAdapterVO" property="tipObjImpDefinition.listTipObjImpAtrDefinition">
			<table class="tabladatos">		
																
				<logic:iterate id="TipObjImpAtrDefinition" name="altaOficioAdapterVO" property="tipObjImpDefinition.listTipObjImpAtrDefinition" indexId="count">
					<logic:equal name="TipObjImpAtrDefinition" property="esVisible" value="true">
						<bean:define id="AtrVal" name="TipObjImpAtrDefinition"/>
						
						<logic:equal name="AtrVal" property="modificarBussEnabled" value="true">					
							<%@ include file="/def/atrDefinition4Edit.jsp" %>
						</logic:equal>
						
						<!-- La cuenta, catastral, nroComercio y otras no se pueden modificar (Se valida en el service) -->
						<logic:equal name="AtrVal" property="modificarBussEnabled" value="false">
							<%@ include file="/def/atrDefinition4View.jsp" %>
						</logic:equal>
					</logic:equal>						
				</logic:iterate>
			</table>

		</logic:notEmpty>
	</fieldset>
	<!-- FIN ObjImp (Comercio) -->
	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="50%">
				<logic:equal name="altaOficioAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="altaOficioAdapterVO" property="act" value="agregar">
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

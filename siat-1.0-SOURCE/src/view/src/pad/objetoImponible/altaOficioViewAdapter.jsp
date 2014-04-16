<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/pad/AdministrarAltaOficio.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="pad" key="pad.altaOficioViewAdapter.title"/></h1>	
		
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
			<tr>
				<td><label><bean:message bundle="pad" key="pad.altaOficio.nroActa.label"/>: </label></td>
				<td class="normal"><bean:write name="altaOficioAdapterVO" property="altaOficio.nroActa"/></td>				
			</tr>
			<tr>
				<td><label><bean:message bundle="pad" key="pad.inspector.label"/>: </label></td>
				<td class="normal"><bean:write name="altaOficioAdapterVO" property="altaOficio.inspector.desInspector"/></td>
			</tr>
			<tr>
				<td><label><bean:message bundle="pad" key="pad.altaOficio.fecha.label"/>: </label></td>
				<td class="normal"><bean:write name="altaOficioAdapterVO" property="altaOficio.fechaView"/></td>				
			</tr>
				<!-- <#BeanWrites#> -->
			</table>
		</fieldset>	
		<!-- AltaOficio -->


	<!-- Contribuyente -->
	<fieldset>
		<legend><bean:message bundle="pad" key="pad.contribuyente.title"/></legend>
		<table class="tabladatos">
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
			<tr>
				<td><label><bean:message bundle="pad" key="pad.altaOficio.contribuyente.nroIsib.label"/>: </label></td>
				<td class="normal">
					<bean:write name="altaOficioAdapterVO" property="contribuyente.nroIsib"/>								
				</td>				
			</tr>							
		</table>
	</fieldset>	
	<!-- FIN Contribuyente -->
	
	<!-- ObjImp (Comercio) -->	
	<fieldset>
		<legend><bean:message bundle="pad" key="pad.altaOficio.comercio.title"/></legend>
		
		<logic:notEmpty name="altaOficioAdapterVO" property="tipObjImpDefinition.listTipObjImpAtrDefinition">
			<table class="tabladatos">
		
				<logic:iterate id="TipObjImpAtrDefinition" name="altaOficioAdapterVO" property="tipObjImpDefinition.listTipObjImpAtrDefinition" indexId="count">
				
					<bean:define id="AtrVal" name="TipObjImpAtrDefinition"/>
					
					<%@ include file="/def/atrDefinition4View.jsp" %>
					
				</logic:iterate>
			</table>

		</logic:notEmpty>
	</fieldset>
	<!-- FIN ObjImp (Comercio) -->

		<table class="tablabotones" width="100%">
	    	<tr>
  	    		<td align="left" width="50%">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
	   	    	<td align="right" width="50%">
					<logic:equal name="altaOficioAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="altaOficioAdapterVO" property="act" value="activar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('activar', '');">
							<bean:message bundle="base" key="abm.button.activar"/>
						</html:button>
					</logic:equal>
					<logic:equal name="altaOficioAdapterVO" property="act" value="desactivar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('desactivar', '');">
							<bean:message bundle="base" key="abm.button.desactivar"/>
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
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->

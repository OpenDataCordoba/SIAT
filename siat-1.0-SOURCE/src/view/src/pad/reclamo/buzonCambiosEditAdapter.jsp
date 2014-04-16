<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/pad/AdministrarBuzonCambios.do">
	
	<!-- Mensajes y/o Advertencias -->
	<%@ include file="/base/warning.jsp" %>
	<!-- Errors  -->
	<html:errors bundle="base"/>				
	
	<h1><bean:message bundle="pad" key="pad.buzonCambiosEditAdapter.title"/></h1>	
	
	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>
		
	<!-- BuzonCambios -->
	<fieldset>
		<legend><bean:message bundle="pad" key="pad.buzonCambios.title"/></legend>
		
		<table class="tabladatos">
			<tr>	
				<td><label><bean:message bundle="pad" key="pad.buzonCambios.tipoModificacion.label"/>: </label></td>
				<td class="normal" colspan="3">
					<html:select name="buzonCambiosAdapterVO" property="buzonCambios.tipoModificacion.id" onchange="submitForm('paramTipoModificacion', '');" styleClass="select">
						<html:optionsCollection name="buzonCambiosAdapterVO" property="listTipoModificacion" label="value" value="id" />
					</html:select>
				</td>
			</tr>
			<tr>	
				<td><label><bean:message bundle="pad" key="pad.buzonCambios.tipoPersona.label"/>: </label></td>
				<td class="normal"  colspan="3">
					<bean:write name="buzonCambiosAdapterVO" property="buzonCambios.tipoPersona.value"/>
				</td>
			</tr>
			
			<!-- Cuit Reportado -->
			<tr>
				<td>
					<label><bean:message bundle="pad" key="pad.buzonCambios.cuitReportado.label"/>: </label>
				</td>
				<td class="normal" colspan="3">
					<bean:write name="buzonCambiosAdapterVO" property="buzonCambios.cuit00"/>			
					-
					<bean:write name="buzonCambiosAdapterVO" property="buzonCambios.cuit01"/>			
					-
					<bean:write name="buzonCambiosAdapterVO" property="buzonCambios.cuit02"/>			
					-
					<bean:write name="buzonCambiosAdapterVO" property="buzonCambios.cuit03"/>			
				</td>
			</tr>
			
			<!-- Modificacion de Cuit -->
			<logic:equal name="buzonCambiosAdapterVO" property="buzonCambios.tipoModificacion.id" value="1">
				<!-- Persona Fisica -->
				<logic:equal name="buzonCambiosAdapterVO" property="buzonCambios.tipoPersona.id" value="1">
					<tr>
						<td><label>(*)&nbsp;<bean:message bundle="pad" key="pad.buzonCambios.tipoyNumeroDocumento.label"/>: </label></td>
						<td class="normal" colspan="3">
							<html:text name="buzonCambiosAdapterVO" property="buzonCambios.documento.numeroView" size="11" maxlength="11"/>						
						</td>
					</tr>
					<tr>
						<td><label><bean:message bundle="pad" key="pad.buzonCambios.nombres.label"/>: </label></td>
						<td class="normal"><bean:write name="buzonCambiosAdapterVO" property="buzonCambios.nombres" /></td>			
						
						<td><label><bean:message bundle="pad" key="pad.buzonCambios.apellido.label"/>: </label></td>
						<td class="normal"><bean:write name="buzonCambiosAdapterVO" property="buzonCambios.apellido"/></td>		
					</tr>
				</logic:equal>
				<!-- Persona Juridica -->
				<logic:equal name="buzonCambiosAdapterVO" property="buzonCambios.tipoPersona.id" value="2">
					<tr>
						<td><label>(*)&nbsp;<bean:message bundle="pad" key="pad.buzonCambios.tipoyNumeroDocumento.label"/>: </label></td>
						<td class="normal" colspan="3">
							Cuit &nbsp;
							<html:text name="buzonCambiosAdapterVO" property="buzonCambios.documento.numeroView" size="11" maxlength="11"/>						
						</td>
					</tr>
					<tr>
						<td><label><bean:message bundle="pad" key="pad.buzonCambios.razonSocial.label"/>: </label></td>
						<td class="normal" colspan="3"><bean:write name="buzonCambiosAdapterVO" property="buzonCambios.razonSocial"/></td>			
					</tr>
				</logic:equal>			
			</logic:equal>
			
			<!-- Modificacion de denominacion -->
			<logic:equal name="buzonCambiosAdapterVO" property="buzonCambios.tipoModificacion.id" value="2">
				<!-- Persona Fisica -->
				<logic:equal name="buzonCambiosAdapterVO" property="buzonCambios.tipoPersona.id" value="1">
					<tr>
						<td><label>(*)&nbsp;<bean:message bundle="pad" key="pad.buzonCambios.nombresCorrecto.label"/>: </label></td>
						<td class="normal"><html:text name="buzonCambiosAdapterVO" property="buzonCambios.nombres" size="20" maxlength="50"/></td>			
						
						<td><label>(*)&nbsp;<bean:message bundle="pad" key="pad.buzonCambios.apellidoCorrecto.label"/>: </label></td>
						<td class="normal"><html:text name="buzonCambiosAdapterVO" property="buzonCambios.apellido" size="20" maxlength="50"/></td>		
					</tr>
				</logic:equal>
				<!-- Persona Juridica -->
				<logic:equal name="buzonCambiosAdapterVO" property="buzonCambios.tipoPersona.id" value="2">
					<tr>
						<td><label>(*)&nbsp;<bean:message bundle="pad" key="pad.buzonCambios.razonSocialCorrecto.label"/>: </label></td>
						<td class="normal" colspan="3"><html:text name="buzonCambiosAdapterVO" property="buzonCambios.razonSocial" size="50" maxlength="50"/></td>			
					</tr>
				</logic:equal>			
			</logic:equal>
						
			<tr>
				<td><label>(*)&nbsp;<bean:message bundle="pad" key="pad.buzonCambios.contacto.label"/>: </label></td>
				<td class="normal" colspan="3"><html:text name="buzonCambiosAdapterVO" property="buzonCambios.contacto" size="50" maxlength="50"/></td>
			</tr>

			<tr>
				<td><label><bean:message bundle="pad" key="pad.buzonCambios.observaciones.label"/>: </label></td>
				<td class="normal" colspan="3">
					<html:textarea name="buzonCambiosAdapterVO" property="buzonCambios.observaciones" cols="80" rows="15"/>
				</td>
			</tr>
		</table>
	</fieldset>	
	<!-- BuzonCambios -->
	
	<table class="tablabotones" width="100%" >
	   	<tr>
  	   		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
   	    	<td align="right" width="50%">
				<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('agregar', '');">
					<bean:message bundle="pad" key="pad.button.enviar"/>
				</html:button>
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

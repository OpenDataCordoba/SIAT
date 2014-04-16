<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">			   
   	    
	function changeNombreCalle() {
		var form = document.getElementById('filter');
		form.elements["persona.domicilio.calle.id"].value = "";
	}   	    
</script>

	<!-- Tabla que contiene todos los formularios -->
	<html:form styleId="filter" action="/pad/AdministrarPersona.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="pad" key="pad.personaAdapter.title"/></h1>	
		
	<table class="tablabotones" width="100%">
		<tr>			
			<td align="right">
    			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
					<bean:message bundle="base" key="abm.button.volver"/>
				</html:button>
			</td>
		</tr>
	</table>		
	
	<!-- Agregar Persona -->
	<logic:equal  name="personaAdapterVO" property="act" value="agregar">
		<!-- Persona Fisisca -->
		<logic:equal  name="personaAdapterVO" property="persona.esPersonaFisica" value="true">
			<fieldset>
				<legend><bean:message bundle="pad" key="pad.persona.title"/></legend>
				<table class="tabladatos">
					<logic:equal  name="personaAdapterVO" property="act" value="agregar">				
						<tr>
							<td><label>(*)&nbsp;<bean:message bundle="pad" key="pad.persona.tipoPersona.label"/>: </label></td>
							<td class="normal"><html:radio name="personaAdapterVO" property="persona.tipoPersona" value="F" onchange="submitForm('paramTipoPersona', '');limpiaResultadoFiltro();"/>Persona F&iacute;sica</td>
							<td class="normal" colspan="2"><html:radio name="personaAdapterVO" property="persona.tipoPersona" value="J" onchange="submitForm('paramTipoPersona', '');limpiaResultadoFiltro();"/>Persona Jur&iacute;dica</td>
						</tr>
					</logic:equal>
					<tr>
						<td><label>(*)&nbsp;<bean:message bundle="pad" key="pad.persona.nombres.label"/>: </label></td>
						<td class="normal"><html:text name="personaAdapterVO" property="persona.nombres" size="20" maxlength="20"/></td>
						<td><label>(*)&nbsp;<bean:message bundle="pad" key="pad.persona.apellido.label"/>: </label></td>
						<td class="normal"><html:text name="personaAdapterVO" property="persona.apellido" size="20" maxlength="20"/></td>
					</tr>
					<tr>
						<td><label><bean:message bundle="pad" key="pad.persona.apellidoMaterno.label"/>: </label></td>
						<td class="normal"><html:text name="personaAdapterVO" property="persona.apellidoMaterno" size="20" maxlength="20"/></td>
						<td><label>(*)&nbsp;<bean:message bundle="pad" key="pad.persona.sexo.label"/>: </label></td>
						<td class="normal">
							<html:select name="personaAdapterVO" property="persona.sexo.id" styleClass="select">
								<html:optionsCollection name="personaAdapterVO" property="listSexo" label="value" value="id" />
							</html:select>
						</td>
					</tr>
					<tr>
						<td><label>(*)&nbsp;<bean:message bundle="pad" key="pad.tipoDocumento.label"/>: </label></td>
						<td class="normal">
							<html:select name="personaAdapterVO" property="persona.documento.tipoDocumento.id" styleClass="select">
								<html:optionsCollection name="personaAdapterVO" property="listTipoDocumento" label="abreviatura" value="id" />
							</html:select>
						</td>
						<td><label>(*)&nbsp;<bean:message bundle="pad" key="pad.documento.numero.ref"/>: </label></td>
						<td class="normal"><html:text name="personaAdapterVO" property="persona.documento.numeroView" size="20" maxlength="12"/></td>
					</tr>
					<tr>
						<td><label><bean:message bundle="pad" key="pad.persona.fechaNacimiento.label"/>: </label></td>					
						<td class="normal">
							<html:text name="personaAdapterVO" property="persona.fechaNacimientoView" styleId="fechaNacimientoView" size="15" maxlength="10" styleClass="datos" onchange="limpiaResultadoFiltro();"/>
							<a class="link_siat" onclick="return show_calendar('fechaNacimientoView');" id="a_fechaNacimientoView">
								<img border="0" src="<%= request.getContextPath()%>/images/calendario.gif"/>
							</a>
						</td>
						<td><label><bean:message bundle="pad" key="pad.persona.cuit.label"/>: </label></td>
						<td class="normal"><bean:write name="personaAdapterVO" property="persona.cuit"/></td>
					</tr>
				</table>
			</fieldset>	
		</logic:equal>	
		<!-- Fin Persona Fisica-->
		
		<!-- Fin Persona Juridica-->
		<logic:notEqual  name="personaAdapterVO" property="persona.esPersonaFisica" value="true">
			<fieldset>
				<legend><bean:message bundle="pad" key="pad.persona.title"/></legend>
				<table class="tabladatos">
					<logic:equal  name="personaAdapterVO" property="act" value="agregar">
						<tr>
							<td><label>(*)&nbsp;<bean:message bundle="pad" key="pad.persona.tipoPersona.label"/>: </label></td>
							<td class="normal"><html:radio name="personaAdapterVO" property="persona.tipoPersona" value="F" onchange="submitForm('paramTipoPersona', '');limpiaResultadoFiltro();"/>Persona F&iacute;sica</td>
							<td class="normal" colspan="2"><html:radio name="personaAdapterVO" property="persona.tipoPersona" value="J" onchange="submitForm('paramTipoPersona', '');limpiaResultadoFiltro();"/>Persona Jur&iacute;dica</td>
						</tr>
					</logic:equal>
					<tr>
						<td><label>(*)&nbsp;<bean:message bundle="pad" key="pad.persona.razonSocial.label"/>: </label></td>
						<td class="normal"><html:text name="personaAdapterVO" property="persona.razonSocial" size="36" maxlength="25"/></td>
						<td><label>(*)&nbsp;<bean:message bundle="pad" key="pad.persona.cuit.label"/>: </label></td>
						<td class="normal"><html:text name="personaAdapterVO" property="persona.cuit" size="12" maxlength="11"/></td>
					</tr>
				</table>
			</fieldset>	
		</logic:notEqual>
		<!-- Fin Persona Juridica-->

	</logic:equal>	
	<!-- Fin Agregar Persona -->
	
	
    <logic:notEqual name="personaAdapterVO" property="act" value="agregar">
		
	<!-- Persona -->
	<fieldset>
		<logic:equal name="personaAdapterVO" property="persona.esPersonaFisica" value="true">
			<legend><bean:message bundle="pad" key="pad.persona.title"/></legend>
		</logic:equal>
		<logic:notEqual name="personaAdapterVO" property="persona.esPersonaFisica" value="true">
			<legend><bean:message bundle="pad" key="pad.persona.juridica.title"/></legend>
		</logic:notEqual>
		
		<!-- Inclusion de los datos de la persona -->
		<bean:define id="personaVO" name="personaAdapterVO" property="persona"/>
		<%@ include file="/pad/persona/includePersona.jsp" %>

		<table class="tablabotones" width="100%">
			<tr>				
				<td align="right">
					<button type="button" name="btnBuzonCambios" class="boton" onclick="submitForm('buzonCambios', '<bean:write name="personaAdapterVO" property="persona.id" bundle="base" formatKey="general.format.id"/>');">
 						<bean:message bundle="pad" key="pad.button.buzonCambios"/>
					</button>
				</td>
			</tr>
		</table>
	</fieldset>
	</logic:notEqual>	
	<!-- Fin Persona -->
	
	<!-- Persona Fisisca -->
	<logic:equal  name="personaAdapterVO" property="persona.esPersonaFisica" value="true">
		<fieldset>
			<legend><bean:message bundle="pad" key="pad.persona.contacto.label"/></legend>
			<table class="tabladatos">
				<tr>
				<!--
					<td><label><bean:message bundle="pad" key="pad.persona.email.label"/>: </label></td>
					<td class="normal"><hhhtml:text name="personaAdapterVO" property="persona.email" size="20" maxlength="100"/>
					</td>
				-->
					<td><label><bean:message bundle="pad" key="pad.persona.telefono.label"/>: </label></td>
					<td class="normal">
						<html:text name="personaAdapterVO" property="persona.caracTelefono" size="3" maxlength="4"/>
						<html:text name="personaAdapterVO" property="persona.telefono" size="15" maxlength="100"/>
					</td>
					<td/>
					<td/>
				</tr>
			</table>
		</fieldset>	
	</logic:equal>	
	<!-- Fin Persona Fisica -->
		
	<!-- Domicilio -->
	<fieldset>
		<legend><bean:message bundle="pad" key="pad.domicilio.title"/></legend>
		
		<table class="tabladatos">			
			<tr>   <!-- Localidad -->
				<td><label>(*)&nbsp;<bean:message bundle="pad" key="pad.localidad.label"/>: </label></td>
				<td class="normal" colspan="3">										
					<html:text name="personaAdapterVO" property="persona.domicilio.localidad.descripcionPostal" size="20" maxlength="20" disabled="true" />				 	
				 	<html:button property="btnBucarLocalidad"  styleClass="boton" onclick="submitForm('buscarLocalidad', '');">
						<bean:message bundle="pad" key="pad.domicilioEditAdapter.button.buscarLocalidad"/>						
					</html:button>
				</td>
			</tr>			
			<tr> <!-- Calle -->
				<td><label>(*)&nbsp;<bean:message bundle="pad" key="pad.calle.label"/>: </label></td>
				<td class="normal" colspan="3">
					<html:text name="personaAdapterVO" property="persona.domicilio.calle.nombreCalle" size="20" maxlength="100" onchange="changeNombreCalle()"/>
					<logic:equal  name="personaAdapterVO" property="persona.domicilio.localidad.esRosario" value="true" >
						<html:button property="btnBuscarCalle"  styleClass="boton" onclick="submitForm('buscarCalle', '');">
							<bean:message bundle="pad" key="pad.domicilioEditAdapter.button.buscarCalle"/>
						</html:button>
					</logic:equal>
					<logic:notEqual  name="personaAdapterVO" property="persona.domicilio.localidad.esRosario" value="true" >
						<html:button property="btnValidarDomicilio"  styleClass="boton" disabled="true">
							<bean:message bundle="pad" key="pad.domicilioEditAdapter.button.buscarCalle"/>
						</html:button>
					</logic:notEqual>	
				</td>
			</tr>	
			<tr>	
				<!-- Numero -->
				<td><label>(*)&nbsp;<bean:message bundle="pad" key="pad.domicilio.numero.label"/>: </label></td>
				<td class="normal" colspan="3"><html:text name="personaAdapterVO" property="persona.domicilio.numeroView" size="20" maxlength="100"/></td>
			</tr>
			<tr> <!-- Letra -->
				<td><label><bean:message bundle="pad" key="pad.domicilio.letraCalle.label"/>: </label></td>
				<td class="normal"><html:text name="personaAdapterVO" property="persona.domicilio.letraCalle" size="20" maxlength="1"/></td>
				<!-- Bis -->
				<td><label>(*)&nbsp;<bean:message bundle="pad" key="pad.domicilio.bis.label"/>: </label></td>
				<td class="normal">
					<html:select name="personaAdapterVO" property="persona.domicilio.bis.id" styleClass="select">
						<html:optionsCollection name="personaAdapterVO" property="listSiNo" label="value" value="id" />
					</html:select>
				</td>
			</tr>
			<tr> <!-- Piso -->
				<td><label><bean:message bundle="pad" key="pad.domicilio.piso.label"/>: </label></td>
				<td class="normal"><html:text name="personaAdapterVO" property="persona.domicilio.piso" size="20" maxlength="100"/></td>
				<!-- Departamento -->
				<td><label><bean:message bundle="pad" key="pad.domicilio.depto.label"/>: </label></td>
				<td class="normal"><html:text name="personaAdapterVO" property="persona.domicilio.depto" size="20" maxlength="100"/></td>
			</tr>
			<tr> <!-- Monoblock -->
				<td><label><bean:message bundle="pad" key="pad.domicilio.monoblock.label"/>: </label></td>
				<td class="normal"><html:text name="personaAdapterVO" property="persona.domicilio.monoblock" size="20" maxlength="100"/></td>
			</tr>
			<tr>	
				<!-- RefGeografica -->
				<td><label><bean:message bundle="pad" key="pad.domicilio.refGeografica.label"/>: </label></td>
				<td class="normal" colspan="3"><html:text name="personaAdapterVO" property="persona.domicilio.refGeografica" size="50" maxlength="15"/></td>
			</tr>
			
			<tr>
				<td colspan="4" align="right">
					<logic:equal  name="personaAdapterVO" property="persona.domicilio.localidad.esRosario" value="true" >
						<html:button property="btnValidarDomicilio"  styleClass="boton" onclick="submitForm('validarDomicilio', '');">
							<bean:message bundle="pad" key="pad.domicilioEditAdapter.button.validarDomicilio"/>
						</html:button>
					</logic:equal>
					<logic:notEqual  name="personaAdapterVO" property="persona.domicilio.localidad.esRosario" value="true" >
						<html:button property="btnValidarDomicilio"  styleClass="boton" disabled="true">
							<bean:message bundle="pad" key="pad.domicilioEditAdapter.button.validarDomicilio"/>
						</html:button>
					</logic:notEqual>
				</td>
			</tr>
		</table>
	</fieldset>
	<!-- Domicilio -->

	<table class="tablabotones" width="100%">
    	<tr>
 	    		<td align="left" width="50%">
	   	    	<html:button property="btnVolver" styleClass="boton" onclick="submitForm('volver', '');">
	   	    		<bean:message bundle="base" key="abm.button.volver"/>
	   	    	</html:button>
   	    	</td>
  	    		<td align="right" width="50%">	   	    	
				<logic:equal name="personaAdapterVO" property="act" value="modificar">
					<html:button property="btnAceptar"  styleClass="boton" onclick="submitForm('modificar', '');">
						<bean:message bundle="base" key="abm.button.modificar"/>
					</html:button>
				</logic:equal>
				<logic:equal name="personaAdapterVO" property="act" value="agregar">
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
	<input type="hidden" name="persona.domicilio.calle.id" value=""/>
		
		
	<!-- Inclusion del Codigo Javascript del Calendar-->
	<div id="calendarDiv" style="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></div>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->

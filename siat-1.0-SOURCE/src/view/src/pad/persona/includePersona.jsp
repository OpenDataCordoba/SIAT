<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
			
<table class="tabladatos">

	<!-- Persona Fisisca-->
	<logic:equal  name="personaVO" property="esPersonaFisica" value="true">
		<tr>
			<td><label><bean:message bundle="pad" key="pad.persona.nombres.label"/>: </label></td>
			<td class="normal"><bean:write name="personaVO" property="nombres"/></td>
			<td><label><bean:message bundle="pad" key="pad.persona.apellido.label"/>: </label></td>
			<td class="normal"><bean:write name="personaVO" property="apellido"/></td>
		</tr>
		<tr>
			<td><label><bean:message bundle="pad" key="pad.persona.apellidoMaterno.label"/>: </label></td>
			<td class="normal"><bean:write name="personaVO" property="apellidoMaterno"/></td>
			<td><label><bean:message bundle="pad" key="pad.persona.sexo.label"/>: </label></td>
			<td class="normal"><bean:write name="personaVO" property="sexo.value"/></td>
		</tr>
		<tr>
			<td><label><bean:message bundle="pad" key="pad.tipoDocumento.label"/>: </label></td>
			<td class="normal"><bean:write name="personaVO" property="documento.tipoDocumento.abreviatura"/></td>
			<td><label><bean:message bundle="pad" key="pad.documento.numero.ref"/>: </label></td>
			<td class="normal"><bean:write name="personaVO" property="documento.numeroView"/></td>
		</tr>
		<tr>
			<td><label><bean:message bundle="pad" key="pad.persona.cuit.label"/>: </label></td>
			<td class="normal"><bean:write name="personaVO" property="cuit"/></td>
			<td><label><bean:message bundle="pad" key="pad.persona.letraCuit.label"/>: </label></td>
			<td class="normal"><bean:write name="personaVO" property="letraCuit.codigo"/></td>
		</tr>
		<tr>
			<td><label><bean:message bundle="pad" key="pad.persona.fechaNacimiento.label"/>: </label></td>
			<td class="normal"><bean:write name="personaVO" property="fechaNacimientoView"/></td>
		</tr>
	</logic:equal>	
	<!-- Fin Persona Fisicar-->
	
	<!-- Persona Juridica-->
	<logic:notEqual  name="personaVO" property="esPersonaFisica" value="true">
		<tr>
			<td><label><bean:message bundle="pad" key="pad.persona.razonSocial.label"/>: </label></td>
			<td class="normal"><bean:write name="personaVO" property="razonSocial"/></td>
			<td><label><bean:message bundle="pad" key="pad.persona.cuit.label"/>: </label></td>				
			<td class="normal"><bean:write name="personaVO" property="cuit"/></td>
		</tr>		
	</logic:notEqual>	
	<!-- Fin Persona Juridica -->	

	<!-- Domicilio Persona -->
	<tr>
		<td><label><bean:message bundle="pad" key="pad.localidad.label"/>: </label></td>
		<td class="normal"><bean:write name="personaVO" property="domicilio.localidad.descripcionPostal"/>&nbsp;</td>
	</tr>
	<tr>
		<td><label><bean:message bundle="pad" key="pad.calle.label"/>: </label></td>
		<td class="normal"><bean:write name="personaVO" property="domicilio.calle.nombreCalle"/></td>
		<td><label><bean:message bundle="pad" key="pad.domicilio.numero.label"/>: </label></td>
		<td class="normal"><bean:write name="personaVO" property="domicilio.numeroView"/></td>
	</tr>
	<tr>
		<td><label><bean:message bundle="pad" key="pad.domicilio.letraCalle.label"/>: </label></td>
		<td class="normal"><bean:write name="personaVO" property="domicilio.letraCalle"/></td>
		<td><label><bean:message bundle="pad" key="pad.domicilio.bis.label"/>: </label></td>
		<td class="normal"><bean:write name="personaVO" property="domicilio.bis.value"/></td>
	</tr>
	<tr>
		<td><label><bean:message bundle="pad" key="pad.domicilio.piso.label"/>: </label></td>
		<td class="normal"><bean:write name="personaVO" property="domicilio.piso"/></td>
		<td><label><bean:message bundle="pad" key="pad.domicilio.depto.label"/>:</label></td>
		<td class="normal"><bean:write name="personaVO" property="domicilio.depto"/></td>
	</tr>
	<tr>
		<td><label><bean:message bundle="pad" key="pad.domicilio.monoblock.label"/>: </label></td>
		<td class="normal"><bean:write name="personaVO" property="domicilio.monoblock"/></td>
		<td><label><bean:message bundle="pad" key="pad.domicilio.refGeografica.label"/>:</label></td>
		<td class="normal"><bean:write name="personaVO" property="domicilio.refGeografica"/></td>
	</tr>
	<!-- Fin Domicilio Persona -->
</table>


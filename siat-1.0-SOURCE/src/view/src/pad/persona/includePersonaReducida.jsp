<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

 <!-- Persona Fisisca-->
 <logic:equal  name="personaVO" property="esPersonaFisica" value="true">
    <tr>
       <td><label><bean:message bundle="pad" key="pad.persona.nombres.label"/>: </label></td>
       <td class="normal"><bean:write name="personaVO" property="nombres"/></td>
       <td><label><bean:message bundle="pad" key="pad.persona.apellido.label"/>: </label></td>
       <td class="normal"><bean:write name="personaVO" property="apellido"/></td>
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
       <td><label><bean:message bundle="pad" key="pad.persona.sexo.label"/>: </label></td>
	   <td class="normal"><bean:write name="personaVO" property="sexo.value"/></td>
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

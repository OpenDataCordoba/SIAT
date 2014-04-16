<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<!-- %@ include file="/base/warning.jsp" % -->
<!-- Errors  -->
<!-- html:errors bundle="base"/ -->
	  
<table class="tabladatos">
    <tr>
      <td><label><bean:message bundle="pro" key="pro.corrida.fechaInicio.label"/>: </label></td>
      <td class="normal" ><bean:write name="adpCorridaAdapterVO" property="corrida.fechaInicioView"/></td>
      <td><label><bean:message bundle="pro" key="pro.corrida.horaInicio.label"/>: </label></td>
      <td class="normal" ><bean:write name="adpCorridaAdapterVO" property="corrida.horaInicioView"/></td>
    </tr>

    <tr>
      <td><label><bean:message bundle="pro" key="pro.estadoCorrida.label"/>:</label></td>
      <td class="normal" colspan="3">
	<bean:write name="adpCorridaAdapterVO" property="corrida.estadoCorrida.desEstadoCorrida"/>
	<logic:notEmpty name="adpCorridaAdapterVO" property="corrida.mensajeEstado"> 
	  - <bean:write name="adpCorridaAdapterVO" property="corrida.mensajeEstado"/>
	</logic:notEmpty>
      </td>
    </tr>

    <tr>
      <td><label><bean:message bundle="pro" key="pro.corrida.observacion.label"/>:</label></td>
      <td class="normal" colspan="3"><bean:write name="adpCorridaAdapterVO" property="corrida.observacion"/></td>
    </tr>
</table>

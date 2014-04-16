<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<script type="text/javascript" language="javascript">
          <%@include file="/base/submitForm.js"%>  
</script>

<!-- Mensajes y/o Advertencias -->
<%@ include file="/base/warning.jsp" %>
<!-- Errors  -->
<html:errors bundle="base"/>
      
      
<html:form styleId="filter" action="/gde/BuscarPlaEnvDeuPro.do">


<fieldset>
  <p>Archivos de Envio a Judicial Masivos para el Procurador</p>

<logic:iterate id="ProcesoMasivoVO" name="procesoMasivoAdapterVO" property="listProcesosProcurador">
<table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%" >
  <caption>Envio Judicial: <bean:write name="ProcesoMasivoVO" property="represent" /></caption>
  <tbody>
      <tr>
    	<th width="1">&nbsp;</th> <!-- ver -->
	    <th align="left"><bean:message bundle="pro" key="pro.fileCorrida.nombre.label"/></th>
	    <th align="left"><bean:message bundle="pro" key="pro.fileCorrida.observacion.label"/></th>
      </tr>    
    
    <logic:iterate id="FileCorridaVO" name="ProcesoMasivoVO" property="listFileProcurador">
	<tr>
	  <td>
	    <a style="cursor: pointer; cursor: hand;" onclick="submitDownload('verArchivosEnvioRealizado', '<bean:write name="FileCorridaVO" property="fileName"/>');">
	      <img title="<bean:message bundle="gde" key="gde.procesoMasivoAdmProcesoAdapter.button.reporteEnvioRealizado"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/selec0.gif"/>
	    </a>
	  </td>
	  <td><bean:write name="FileCorridaVO" property="nombre" />&nbsp;</td>
	  <td><bean:write name="FileCorridaVO" property="observacion" />&nbsp;</td>
	</tr>
    </logic:iterate><!-- bucle de archivos por proceso -->
  </tbody>

</table>

</logic:iterate><!-- bucle de procesos masivos -->

<logic:empty  name="procesoMasivoAdapterVO" property="listProcesosProcurador">
      <table class="tramonline" border="0" cellpadding="0" cellspacing="1" width="100%" >
      <tr><td align="center"><bean:message bundle="base" key="base.noExistenRegitros"/></td></tr>
      </table>
</logic:empty>
    
</fieldset>

<table class="tablabotones" width="100%">
<tr>
   <td align="left" width="50%">
     <html:button property="btnVolver" styleClass="boton" onclick="document.location='/siat/gde/BuscarPlaEnvDeuPro.do?method=inicializar'">
       <bean:message bundle="base" key="abm.button.volver"/>
     </html:button>
   </td>
</tr>
</table>

		<input type="hidden" name="method" value=""/>
		<input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
		<input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

		<input type="hidden" name="fileParam" value=""/>
		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		<input type="hidden" id="locateFocus" name="locateFocus" value="<%= request.getParameter("locateFocus")%>"/>

</html:form>
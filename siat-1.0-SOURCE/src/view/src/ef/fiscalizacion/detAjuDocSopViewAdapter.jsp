<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>

<!-- Tabla que contiene todos los formularios -->
<html:form styleId="filter" action="/ef/AdministrarDetAjuDocSop.do">

		<!-- Mensajes y/o Advertencias -->
		<%@ include file="/base/warning.jsp" %>
		<!-- Errors  -->
		<html:errors bundle="base"/>
		
		<h1><bean:message bundle="ef" key="ef.detAjuDocSopViewAdapter.title"/></h1>	

		<table class="tablabotones" width="100%">
			<tr>			
				<td align="right">
		   			<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>
				</td>
			</tr>
		</table>
		
		<!-- DetAjuDocSop -->
		<fieldset>
			<legend><bean:message bundle="ef" key="ef.detAjuDocSop.label"/></legend>
			<table class="tabladatos">
			<tr>
				<td><label><bean:message bundle="ef" key="ef.detAjuDocSop.fechaGenerada.label"/>: </label></td>
				<td class="normal"><bean:write name="detAjuDocSopAdapterVO" property="detAjuDocSop.fechaGeneradaView"/></td>				
			</tr>
			<tr>
				<td><label><bean:message bundle="ef" key="ef.detAjuDocSop.fechaNotificada.label"/>: </label></td>
				<td class="normal"><bean:write name="detAjuDocSopAdapterVO" property="detAjuDocSop.fechaNotificadaView"/></td>				
			</tr>
			<tr>
				<td><label><bean:message bundle="ef" key="ef.docSop.desDocSop.label"/>: </label></td>
				<td class="normal"><bean:write name="detAjuDocSopAdapterVO" property="detAjuDocSop.docSop.desDocSop"/></td>
			</tr>
			<tr>
				<td><label><bean:message bundle="ef" key="ef.detAjuDocSop.notificadaPor.label"/>: </label></td>
				<td class="normal"><bean:write name="detAjuDocSopAdapterVO" property="detAjuDocSop.notificadaPor"/></td>				
			</tr>
			<tr>
				<td><label><bean:message bundle="ef" key="ef.detAjuDocSop.observacion.label"/>: </label></td>
				<td class="normal"><bean:write name="detAjuDocSopAdapterVO" property="detAjuDocSop.observacion"/></td>				
			</tr>
				<!-- Ubicación Plantilla -->
		    <tr>
			   <td><label><bean:message bundle="ef" key="ef.docSop.plantilla.label"/>: </label></td>
				<td class="normal"><bean:write name="detAjuDocSopAdapterVO" property="detAjuDocSop.docSop.plantilla"/>		
				  <a style="cursor: pointer; cursor: hand;" onclick="submitDownload('downloadFile', '<bean:write name="detAjuDocSopAdapterVO" property="detAjuDocSop.docSop.plantilla"/>');">
							<img title="<bean:message bundle="base" key="abm.button.ver"/>" border="0" src="<%=request.getContextPath()%>/images/iconos/excel0.gif"/>
				  </a>	
				</td>	
			</tr>
			
			</table>
		</fieldset>	
		<!-- DetAjuDocSop -->

		<table class="tablabotones" width="100%">
	    	<tr>
  	    		<td align="left">
		   	    	<html:button property="btnVolver"  styleClass="boton" onclick="submitForm('volver', '');">
						<bean:message bundle="base" key="abm.button.volver"/>
					</html:button>	   	    			
	   	    	</td>
	   	    	<td align="right">
					<logic:equal name="detAjuDocSopAdapterVO" property="act" value="eliminar">
						<html:button property="btnAccionBase"  styleClass="boton" onclick="submitForm('eliminar', '');">
							<bean:message bundle="base" key="abm.button.eliminar"/>
						</html:button>
					</logic:equal>					
	   	    	</td>
	   	    </tr>
	   	 </table>
	    <input type="hidden" name="name"  value="<bean:write name='detAjuDocSopAdapterVO' property='name'/>" id="name"/>
	   	<input type="hidden" name="report.reportFormat" value="1" id="reportFormat"/> 	
	   	 		
		<input type="hidden" name="method" value=""/>
        <input type="hidden" name="anonimo" value="<bean:write name="userSession" property="isAnonimoView"/>"/>
        <input type="hidden" name="urlReComenzar" value="<bean:write name="userSession" property="urlReComenzar"/>"/>

		<input type="hidden" name="selectedId" value=""/>
		<input type="hidden" name="fileParam" value=""/>
		<input type="hidden" name="isSubmittedForm" value="true"/>
		
	</html:form>
	<!-- Fin Tabla que contiene todos los formularios -->
<!-- detAjuDocSopViewAdapter.jsp -->
